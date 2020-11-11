package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.LinkedList;
import java.util.List;

public class AgentsAvailableEvent implements Event {

    private List<String> agentSerial;//requested

    //received from MonneyPenny:
    private List<String> rcvdNames;
    private int monneyPennySerial;
    private int time;//amount of time to send agents


    public AgentsAvailableEvent(List<String> agentSerial, int time){
        this.agentSerial = agentSerial;
        this.time=time;
        rcvdNames=new LinkedList<>();
        monneyPennySerial = 0;
    }

    public List<String> getRcvdNames() {
        return rcvdNames;
    }

    public void setRcvdNames(List<String> rcvdNames) {
        this.rcvdNames = rcvdNames;
    }

    public int getTime() {
        return time;
    }

    public List<String> getAgentSerial() {
        return agentSerial;
    }


    public int getMonneyPennySerial() {
        return monneyPennySerial;
    }

    public void setMonneyPennySerial(int monneyPennySerial) {
        this.monneyPennySerial = monneyPennySerial;
    }
}
