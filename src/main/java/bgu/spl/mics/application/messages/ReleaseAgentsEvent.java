package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class ReleaseAgentsEvent implements Event {

    private List<String> agentSerial;//requested

    public ReleaseAgentsEvent(List<String> agentSerial)
    {
        this.agentSerial = agentSerial;
    }

    public List<String> getAgentSerial() {
        return agentSerial;
    }


}
