package bgu.spl.mics;

import bgu.spl.BroadcastSimple;
import bgu.spl.EventSimple;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MessageBrokerTest {
    MessageBroker messageBroker;
    Event<String> event;
    M subM1;
    M subM2;
    Intelligence pub;
    Broadcast broadcast;
    @BeforeEach
    public void setUp(){
        messageBroker=MessageBrokerImpl.getInstance();
        event=new EventSimple();
        subM1=new M();
        subM2=new M();
        broadcast=new BroadcastSimple();
    }

    @Test
    public void testRegister()
    {
        messageBroker.register(subM1);
        try {
            messageBroker.awaitMessage(subM1);
        }
        catch (Exception e)
        {
            fail("should not throw exception");
        }
    }

    @Test
    public void testSubscribeEvent() throws InterruptedException {
        messageBroker.register(subM2);
    //    messageBroker.subscribeEvent(bgu.spl.EventSimple.class,subM2);
        messageBroker.awaitMessage(subM2);
        pub.getSimplePublisher().sendEvent(event);
        Future<String> future = messageBroker.sendEvent(event);
        assertTrue(future != null,"the subscriber should have received the event");
    }

    @Test
    public void testComplete() throws InterruptedException {
        messageBroker.register(subM1);
       // messageBroker.subscribeEvent(bgu.spl.EventSimple.class, subM1);
        messageBroker.awaitMessage(subM1);
        pub.getSimplePublisher().sendEvent(event);
        Future<String> future = messageBroker.sendEvent(event);
        messageBroker.complete(event, "the event is solved");
        assertTrue(future.get(100, TimeUnit.MILLISECONDS).equals("the event is solved"));

    }

    @Test
    public void testUnregister() {
        messageBroker.register(subM1);
      //  messageBroker.subscribeEvent(bgu.spl.EventSimple.class, subM1);
        messageBroker.unregister(subM1);
        try {
            messageBroker.awaitMessage(subM1);
            fail("should throw exception");
        }
        catch (Exception e)
        {

        }
    }

    @Test
    public void testSubscribeBroadcast()  {
        messageBroker.register(subM2);
        messageBroker.subscribeBroadcast(BroadcastSimple.class,subM2);
        try {
            messageBroker.awaitMessage(subM2);
            pub.getSimplePublisher().sendBroadcast(broadcast);
        }catch (Exception e)
        {
            fail("should not throw exception");
        }



    }

    @Test
    public void test(){
        //TODO: change this test and add more tests :)
        fail("Not a good test");
    }
}
