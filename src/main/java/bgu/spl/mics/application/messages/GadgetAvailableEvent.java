package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class GadgetAvailableEvent implements Event {

    private String gadget; //requested

    private int qSerial;
    private int qTime;

    public GadgetAvailableEvent(String gadget)
    {
        this.gadget=gadget;
        this.qSerial=0;
        this.qTime=0;
    }

    public String getGadget() {
        return gadget;
    }

    public void setGadget(String gadget) {
        this.gadget = gadget;
    }

    public int getqTime() {
        return qTime;
    }

}
