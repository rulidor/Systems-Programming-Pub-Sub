package bgu.spl.mics.application.passiveObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

    private Map<String, Agent> agents;
    private static Squad instanceSquad;
    private Object lock;

    private Squad() {

        agents = new HashMap<>();
        lock = new Object();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Squad getInstance() {
        if (instanceSquad == null) {
            instanceSquad = new Squad();
        }
        return instanceSquad;
    }

    /**
     * Initializes the squad. This method adds all the agents to the squad.
     * <p>
     *
     * @param agents Data structure containing all data necessary for initialization
     *               of the squad.
     */
    public void load(Agent[] agents) {

        	for (Agent a : agents)
			{
				this.agents.put(a.getSerialNumber(),a);
			}

    }

    /**
     * Releases agents.
     */
    public synchronized void releaseAgents(List<String> serials) {

            for (String ser : serials) {
                if (agents.containsKey(ser)) {
                    agents.get(ser).release();
                }
            }
            notifyAll();

    }

    /**
     * simulates executing a mission by calling sleep.
     *
     * @param time time ticks to sleep
     */
    public synchronized void sendAgents(List<String> serials, int time) {
		if(!serials.isEmpty()) {
            try {
                Thread.sleep(100*time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            releaseAgents(serials);
        }
	}

    /**
     * acquires an agent, i.e. holds the agent until the caller is done with it
     *
     * @param serials the serial numbers of the agents
     * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
     */
    public synchronized boolean getAgents(List<String> serials) {

            for (String ser : serials) {
                if (!agents.containsKey(ser)) return false;
            }

            for (String ser : serials) {
                while (!agents.get(ser).isAvailable()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                agents.get(ser).acquire();

            }
            return true;

    }

    /**
     * gets the agents names
     *
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials) {

            List<String> ans = new ArrayList<>();
            for (String ser : serials) {
                if (agents.containsKey(ser)) {
                    ans.add(agents.get(ser).getName());
                }
            }
            return ans;

    }

}
