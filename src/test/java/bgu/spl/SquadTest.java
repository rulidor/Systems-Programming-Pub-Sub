package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {

    Squad squad;
    Agent[] agents;
    Agent[] agents2;
    Agent[] agents3;
    Agent[] agents4;
    Agent[] agents5;
    List<String> serials;
    List<String> serials2;
    List<String> serials3;
    List<String> serials4;
    List<String> serials5;
    @BeforeAll
    public void setUp()
    {
        squad=squad.getInstance();
        Agent a1=new Agent();
        a1.setName("James Bond");
        a1.setSerialNumber("007");
        Agent a2=new Agent();
        a2.setName("Alec Trevelyan");
        a2.setSerialNumber("006");
        agents=new Agent[]{a1,a2};
        serials= new ArrayList<>(Arrays.asList(a1.getSerialNumber(), a2.getSerialNumber()));
        Agent a3=new Agent();
        a3.setName("Ethan Hunt");
        a3.setSerialNumber("001");
        Agent a4=new Agent();
        a4.setName("Superman");
        a4.setSerialNumber("002");
        agents2=new Agent[]{a3,a4};
        serials2=new ArrayList<>(Arrays.asList(a3.getSerialNumber(),a4.getSerialNumber()));
        Agent a5=new Agent();
        Agent a6=new Agent();
        a5.setName("IronMan");
        a5.setSerialNumber("003");
        a6.setName("Captain America");
        a6.setSerialNumber("004");
        agents3=new Agent[]{a5,a6};
        serials3=new ArrayList<>(Arrays.asList(a5.getSerialNumber(),a6.getSerialNumber()));
        Agent a7=new Agent();
        Agent a8=new Agent();
        a7.setName("Captain Marvel");
        a7.setSerialNumber("008");
        a8.setName("The Hulk");
        a8.setSerialNumber("009");
        agents4=new Agent[]{a7,a8};
        serials4=new ArrayList<>(Arrays.asList(a7.getSerialNumber(),a8.getSerialNumber()));
        Agent a9=new Agent();
        Agent a10=new Agent();
        a9.setName("X-Man");
        a9.setSerialNumber("009");
        a10.setName("Black Panther");
        a10.setSerialNumber("010");
        agents5=new Agent[]{a9,a10};
        serials5=new ArrayList<>(Arrays.asList(a9.getSerialNumber(),a10.getSerialNumber()));

    }



    @Test
    public void testLoad()
    {
        squad.load(agents);
        List<String> names=squad.getAgentsNames(serials);
        assertEquals("James Bond",names.get(0));
        assertEquals("Alec Trevelyan",names.get(1));
        List<String> serial2=new ArrayList<>();
        serial2.add("004");
        names=squad.getAgentsNames(serial2);
        assertEquals(0,names.size());
    }

    @Test
    public void testGetAgentsName()
    {
        squad.load(agents2);
        List<String> names=squad.getAgentsNames(serials2);
        assertNotEquals(-1,names.indexOf("Ethan Hunt"));
        assertNotEquals(-1,names.indexOf("Superman"));
        assertEquals(-1, names.indexOf("SpiderMan"));
    }

    @Test
    public void testGetAgents()
    {
        squad.load(agents3);
        boolean checkAgents=squad.getAgents(serials3);
        assertTrue(checkAgents,"agents should be in map");
        List<String> agent=new ArrayList<>(Arrays.asList("Thanos"));
        checkAgents=squad.getAgents(agent);
        assertFalse(checkAgents,"agent should not be in map");
    }

    @Test
    public void testReleaseAgents()
    {
        squad.load(agents4);
        squad.releaseAgents(serials4);
        assertTrue(agents4[0].isAvailable(),"should return true");
        assertTrue(agents4[1].isAvailable(),"should return true");

    }

    @Test
    public void testSendAgents()
    {
        squad.load(agents5);
        squad.sendAgents(serials5,5);
        assertTrue(agents5[0].isAvailable(),"should return true");
        assertTrue(agents5[1].isAvailable(),"should return true");

    }
    @Test
    public void test(){
        //TODO: change this test and add more tests :)
        fail("Not a good test");
    }
}
