package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast  implements Broadcast {

    private int timeServiceDuration;
    private int currTime;

    public TickBroadcast(int currTime, int timeServiceDuration)
    {
        this.timeServiceDuration=timeServiceDuration;
        this.currTime = currTime;
    }

    public int getTimeServiceDuration() {
        return timeServiceDuration;
    }


}
