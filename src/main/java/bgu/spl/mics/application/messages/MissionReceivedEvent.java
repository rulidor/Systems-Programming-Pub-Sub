package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionReceivedEvent implements Event {

    private MissionInfo missionInfo;

    private int mSerial;

    public MissionReceivedEvent(MissionInfo missionInfo)
    {
        this.missionInfo = missionInfo;
        this.mSerial=0;
    }

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }

    public void setmSerial(int mSerial) {
        this.mSerial = mSerial;
    }
}
