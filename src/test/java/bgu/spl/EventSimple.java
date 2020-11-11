package bgu.spl;

import bgu.spl.mics.Event;

import javax.lang.model.element.ElementVisitor;

public class EventSimple implements Event<String> {

    private String value;

    public EventSimple(){};

    public void setValue(String newVal)
    {
        value=newVal;
    }

    public String getValue()
    {
        return value;
    }
}
