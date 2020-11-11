package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {

        JsonParser parser = new JsonParser();
        try {
//////////////////////////////////////////////////////////////createInventory//////////////////////////////////////////////////////////////////////////////////////////////////
            Object objParser = parser.parse(new FileReader(args[0]));
            JsonObject jsonObject = (JsonObject) objParser;
            JsonArray initializeInventory = (JsonArray) jsonObject.get("inventory");
            int size = initializeInventory.size();
            String[] gadgetsArray = new String[size];
            for (int j = 0; j < gadgetsArray.length; j++) {
                gadgetsArray[j] = initializeInventory.get(j).getAsString();

            }

            Inventory inventory = Inventory.getInstance();
            inventory.load(gadgetsArray);

//////////////////////////////////////////////////////////////createSquad////////////////////////////////////////////////////////////////////////////////////////
            JsonArray initializeSqud = (JsonArray) jsonObject.get("squad");
            int sizeSquad = initializeSqud.size();
            Agent[] agentsArray = new Agent[sizeSquad];
            int i = 0;
            for (JsonElement sq : initializeSqud) {
                JsonObject sqInfo = sq.getAsJsonObject();
                agentsArray[i] = new Agent(sqInfo.get("name").getAsString(), sqInfo.get("serialNumber").getAsString());
                i++;
            }

            Squad squad = Squad.getInstance();
            squad.load(agentsArray);
///////////////////////////////////////////////////////////////////////////createIntelligence//////////////////////////////////////////////////////////////////////////////////////////////
            JsonObject initializeServices = (JsonObject) jsonObject.get("services");
            JsonArray intelligence = (JsonArray) initializeServices.get("intelligence");
            JsonPrimitive jsonM = (JsonPrimitive) initializeServices.get("M");
            JsonPrimitive jsonMoneypenny = (JsonPrimitive) initializeServices.get("Moneypenny");
            JsonPrimitive jsontime = (JsonPrimitive) initializeServices.get("time");

            ArrayList<MissionInfo> missionsInfo;
            List<String> agentsSerial;
            MissionInfo missionInfoToInitialize;
            ArrayList<Thread> MThreadList = new ArrayList<>();
            ArrayList<Thread> moneyPennyThreadList = new ArrayList<>();
            ArrayList<Thread> intelligenceThreadList = new ArrayList<>();

            for (JsonElement mis : intelligence) {
                missionsInfo = new ArrayList<>();
                JsonObject missionInfo = mis.getAsJsonObject();
                JsonArray missions = (JsonArray) missionInfo.get("missions");
                for (JsonElement details : missions) {
                    JsonObject mission = details.getAsJsonObject();
                    agentsSerial = new ArrayList<>();
                    JsonArray agents = (JsonArray) mission.get("serialAgentsNumbers");
                    for (JsonElement a : agents) {
                        agentsSerial.add(a.getAsString());

                    }
                    missionInfoToInitialize = new MissionInfo();
                    missionInfoToInitialize.setGadget(mission.get("gadget").getAsString());
                    missionInfoToInitialize.setTimeExpired(mission.get("timeExpired").getAsInt());
                    missionInfoToInitialize.setDuration(mission.get("duration").getAsInt());
                    missionInfoToInitialize.setMissionName(mission.get("name").getAsString());
                    missionInfoToInitialize.setTimeIssued(mission.get("timeIssued").getAsInt());
                    missionInfoToInitialize.setSerialAgentsNumbers(agentsSerial);
                    missionsInfo.add(missionInfoToInitialize);
                }
                Thread t = new Thread(new Intelligence(missionsInfo));
                intelligenceThreadList.add(t);

            }


///////////////////////////////////////////////////////////////////////////createM//////////////////////////////////////////////////////////////////////////////////////////////
            boolean moreThenOne;
            if (jsonMoneypenny.getAsInt() > 1) {
                moreThenOne = true;
            } else {
                moreThenOne = false;
            }
            boolean oneMoneyPennyAlotOfM;
            if (jsonM.getAsInt() > 1 && moreThenOne == true)
                oneMoneyPennyAlotOfM = true;
            else
                oneMoneyPennyAlotOfM = false;

            for (int j = 0; j < jsonM.getAsInt(); j++) {
                Thread t = new Thread(new M(j + 1, "missionInfoToInitialize" + (j + 1), oneMoneyPennyAlotOfM));
                MThreadList.add(t);
            }
///////////////////////////////////////////////////////////////////////////createMoneyPenny/////////////////////////////////////////////////////////////////////////////////////////////
            int numOfResive;

            if (jsonMoneypenny.getAsInt() %2 ==0) {
              numOfResive  = (jsonMoneypenny.getAsInt() / 2);
           }
            else
            {
                numOfResive = (jsonMoneypenny.getAsInt() / 2)+1;
            }

            for (int j = 0; j < jsonMoneypenny.getAsInt(); j++) {
                Thread moneypenny;
                if (((j+1) % 2) == 0) {
                    moneypenny = new Thread(new Moneypenny(j + 1, "moneyPenny" + (j + 1), false, moreThenOne, numOfResive));

                } else {
                    moneypenny = new Thread(new Moneypenny(j + 1, "moneyPenny" + (j + 1), true, moreThenOne, numOfResive));

                }
                moneyPennyThreadList.add(moneypenny);

            }

///////////////////////////////////////////////////////////////////////////all thread are start//////////////////////////////////////////////////////////////////////////////////////////////
            Thread qThread = new Thread(new Q());
            qThread.start();
            for (Thread t : intelligenceThreadList)
                t.start();
            for (Thread t : MThreadList)
                t.start();
            for (Thread t : moneyPennyThreadList)
                t.start();
            Thread timeService = new Thread(new TimeService(jsontime.getAsInt()));
            timeService.start();

///////////////////////////////////////////////////////////////////////////join threads//////////////////////////////////////////////////////////////////////////////////////////////
            try {
                qThread.join();
                for (Thread t : intelligenceThreadList)
                    t.join();
                for (Thread t : MThreadList)
                    t.join();
                for (Thread t : moneyPennyThreadList)
                    t.join();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }


            Inventory.getInstance().printToFile(args[1]); // inventory output
            Diary.getInstance().printToFile(args[2]); // diary reports output

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            System.exit(0);
        }

    }
}
