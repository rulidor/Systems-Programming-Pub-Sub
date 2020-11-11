package bgu.spl;

import bgu.spl.mics.Broadcast;

public class BroadcastSimple implements Broadcast {

    private String value;
    public String getValue(){return value;}
    public void setValue(String val){this.value=val;}
}
