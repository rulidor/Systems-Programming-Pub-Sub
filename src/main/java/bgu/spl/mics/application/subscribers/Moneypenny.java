package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.HolderAnswerAndMore;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.ArrayList;
import java.util.List;


/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
    private int numOfPenyRecivedAgentAvilable;
    private int serialNumber;
    private Squad squad;
    private int currTick;
    private int timeServiceDuration;
    private List<String> agentSerialNumbers;
    private boolean takeAvilabeAgentEeventCare;
    private boolean moreThenOneFromMe;

    public Moneypenny(String name) {
        super(name);
        serialNumber = 0;
        squad = Squad.getInstance();
        this.currTick = 0;
        this.timeServiceDuration = 0;
    }

    public Moneypenny() {
        super("Moneypenny");
        serialNumber = 0;
        squad = Squad.getInstance();
        this.currTick = 0;
        this.timeServiceDuration = 0;
    }

    public Moneypenny(int serialNumber, String name, boolean bol, boolean moreThenOneFromMe, int numOfRecAgentAvilable) {
        super(name);
        this.serialNumber = serialNumber;
        squad = Squad.getInstance();
        this.currTick = 0;
        this.timeServiceDuration = 0;
        agentSerialNumbers = new ArrayList<>();
        this.takeAvilabeAgentEeventCare = bol;
        this.moreThenOneFromMe = moreThenOneFromMe;
        this.numOfPenyRecivedAgentAvilable = numOfRecAgentAvilable;
    }

    @Override
    protected void initialize() {


        this.subscribeBroadcast(TickBroadcast.class, tick ->
        {
            this.currTick++;
            this.timeServiceDuration = tick.getTimeServiceDuration();
        });

        if (moreThenOneFromMe) {

            if (this.takeAvilabeAgentEeventCare) // only  moneypenny that build with true filed will receives AgentsAvailableEvent- to prevent deadlock
            {
                this.subscribeBroadcast(TerminateBroadcast.class, terminate -> {

                    squad.releaseAgents(this.agentSerialNumbers);
                    timeToFinishBroadcast timeToFinishBroadcast = new timeToFinishBroadcast();
                    this.getSimplePublisher().sendBroadcast(timeToFinishBroadcast);
                    terminate();
                });


                subscribeEvent(AgentsAvailableEvent.class, agentAvailableEvent -> {
                    synchronized (this) {
                        List<String> agentsSerial = agentAvailableEvent.getAgentSerial();
                        boolean isAvailble = squad.getAgents(agentsSerial);
                        List<String> agentsName = squad.getAgentsNames(agentsSerial);
                        this.agentSerialNumbers = agentsSerial;
                        HolderAnswerAndMore holderAnswerAndMore = new HolderAnswerAndMore();
                        holderAnswerAndMore.setAgentsNames(agentsName);
                        holderAnswerAndMore.setMoneyPennySerialNum(this.serialNumber);
                        holderAnswerAndMore.setAns(isAvailble);
                        complete(agentAvailableEvent, holderAnswerAndMore);
                        notifyAll();
                    }
                });
            } else // all other moneypenny will subscribe to these events
            {
                subscribeBroadcast(timeToFinishBroadcast.class, timeToFinish -> {

                    numOfPenyRecivedAgentAvilable -= 1;
                    if (numOfPenyRecivedAgentAvilable <= 0)
                        terminate();

                });

                subscribeEvent(ReleaseAgentsEvent.class, releaseAgents -> {
                    List<String> agentsSerial = releaseAgents.getAgentSerial();
                    squad.releaseAgents(agentsSerial);
                    complete(releaseAgents, true);
                });

                subscribeEvent(SendAgentsEvent.class, sendAgents -> {
                    squad.sendAgents(sendAgents.getAgentSerial(), sendAgents.getTime());
                    complete(sendAgents, true);
                });
            }


        }
        else {
            subscribeEvent(AgentsAvailableEvent.class, agentAvailableEvent -> {
                List<String> agentsSerial = agentAvailableEvent.getAgentSerial();
                boolean isAvailble = squad.getAgents(agentsSerial);
                List<String> agentsName = squad.getAgentsNames(agentsSerial);
                this.agentSerialNumbers = agentsSerial;
                HolderAnswerAndMore holderAnswerAndMore = new HolderAnswerAndMore();
                holderAnswerAndMore.setAgentsNames(agentsName);
                holderAnswerAndMore.setMoneyPennySerialNum(this.serialNumber);
                holderAnswerAndMore.setAns(isAvailble);
                complete(agentAvailableEvent, holderAnswerAndMore);
                squad.sendAgents(agentAvailableEvent.getAgentSerial(), agentAvailableEvent.getTime());


            });

            subscribeEvent(ReleaseAgentsEvent.class, releaseAgents -> {

                List<String> agentsSerial = releaseAgents.getAgentSerial();
                squad.releaseAgents(agentsSerial);
                complete(releaseAgents, true);

            });

            subscribeEvent(SendAgentsEvent.class, sendAgents -> {
                squad.sendAgents(sendAgents.getAgentSerial(), sendAgents.getTime());
                complete(sendAgents, true);
            });

        }
    }


}
