package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

    /**
     * Retrieves the single instance of this class.
     */


    private static MessageBrokerImpl instanceMessageBroker;
    private ConcurrentHashMap<Subscriber, ConcurrentLinkedQueue<Message>> msgForSubs;
    private ConcurrentHashMap<Class<? extends Event>, ConcurrentLinkedQueue<Subscriber>> typeOfEventForSubs;
    private ConcurrentHashMap<Class<? extends Broadcast>, ConcurrentLinkedQueue<Subscriber>> typeOfBroadcast;
    private ConcurrentHashMap<Event<?>, Future> msgFtr;

    private MessageBrokerImpl() {
        msgForSubs = new ConcurrentHashMap();
        typeOfEventForSubs = new ConcurrentHashMap<>();
        typeOfBroadcast = new ConcurrentHashMap<>();
        msgFtr = new ConcurrentHashMap<>();
    }

    public synchronized static MessageBroker getInstance() {
        if (instanceMessageBroker == null) {
            instanceMessageBroker = new MessageBrokerImpl();
        }
        return instanceMessageBroker;
    }

    @Override
    public synchronized  <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {

            if (!typeOfEventForSubs.containsKey(type)) {
                typeOfEventForSubs.put(type, new ConcurrentLinkedQueue<>());
            }
            if (!typeOfEventForSubs.get(type).contains(m))
                typeOfEventForSubs.get(type).add(m);

    }


    @Override
    public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {

            if (!typeOfBroadcast.containsKey(type)) {
                typeOfBroadcast.put(type, new ConcurrentLinkedQueue<>());
            }
            if (!typeOfBroadcast.get(type).contains(m))
                typeOfBroadcast.get(type).add(m);

    }


    @Override
    public <T> void complete(Event<T> e, T result) {
                if (e != null && result != null){
                    if (msgFtr == null||msgFtr.get(e)==null) return;
            msgFtr.get(e).resolve(result);
            msgFtr.remove(e);
                }


    }

    @Override
    public synchronized void sendBroadcast(Broadcast b) {

            if (typeOfBroadcast.get(b.getClass())==null) return;
            for (Subscriber s : typeOfBroadcast.get(b.getClass()))
                if (msgForSubs.containsKey(s)) {
                    msgForSubs.get(s).add(b);
                }
            notifyAll();


    }


    @Override
    public  synchronized <T> Future<T> sendEvent(Event<T> e) {

            if (typeOfEventForSubs.get(e.getClass())==null || typeOfEventForSubs.get(e.getClass()).isEmpty()) {
                return null;}
            if (msgForSubs.get(typeOfEventForSubs.get(e.getClass()).peek()) == null){
                return null;}
            msgForSubs.get(typeOfEventForSubs.get(e.getClass()).peek()).add(e);
            if (typeOfEventForSubs.get(e.getClass()).peek() == null) {
               return null;}
            typeOfEventForSubs.get(e.getClass()).add(typeOfEventForSubs.get(e.getClass()).poll());
            msgFtr.put(e, new Future<T>());
            notifyAll();
            return msgFtr.get(e);

    }

    @Override
    public  void register(Subscriber m) {
        if (!msgForSubs.containsKey(m)) {
            ConcurrentLinkedQueue<Message> msgQueueSubs = new ConcurrentLinkedQueue<>();
            msgForSubs.put(m, msgQueueSubs);
        }

    }

    @Override
    public synchronized void unregister(Subscriber m) {
        if (m==null)  { notifyAll(); return;}
        if (!msgForSubs.containsKey(m)){ notifyAll(); return;}


        Queue<Message> toDelete = msgForSubs.get(m);
        while (!toDelete.isEmpty()){
            Message message = toDelete.remove();
            Future future = msgFtr.get(message);
            if (future == null) continue;
            future.resolve(null);
        }
        for(Object messageType : typeOfEventForSubs.keySet())
        {
            if(typeOfEventForSubs.get(messageType).contains(m))
                typeOfEventForSubs.get(messageType).remove(m);
        }
        for(Object messageType : typeOfBroadcast.keySet())
        {
            if(typeOfBroadcast.get(messageType).contains(m))
                typeOfBroadcast.get(messageType).remove(m);
        }

        msgForSubs.remove(m);
        notifyAll();

    }

    @Override
    public synchronized Message awaitMessage(Subscriber m) throws InterruptedException {
            if(!msgForSubs.containsKey(m))
                throw new IllegalStateException("subscriber not found");
            else
                {
                        try {
                            while (msgForSubs.get(m).isEmpty())
                                wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            e.printStackTrace();
                        }
                        return msgForSubs.get(m).remove();
                }
            }


    }
