package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class SendAgentsEvent implements Event {

    private List<String> agentSerial;//requested
    private int time;//amount of time to send agents


    public SendAgentsEvent(List<String> agentSerial, int time)
    {
        this.agentSerial=agentSerial;
        this.time=time;

    }

    public List<String> getAgentSerial() {
        return agentSerial;
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
