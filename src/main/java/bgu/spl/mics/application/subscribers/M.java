package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.HolderAnswerAndMore;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.ArrayList;
import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
    private int serialNumber;
    private int currTick;
    private Diary diary;
    private int intelligeicTimeTick;
    private int missionDuraton;
    private List<String> agentsSerials;
    private String gadgetName;
    private String missionName;
    private List<String> agentsNames;
    private int serialOfMoneyPenny;
    private boolean oneMoneyPennyAlotOfM;

    public M(String name) {
        super(name);
        serialNumber = 0;
        this.currTick = 0;
        diary = Diary.getInstance();
    }

    public M() {
        super("M");
        serialNumber = 0;
        this.currTick = 0;
        diary = Diary.getInstance();
    }

    public M(int ser, String name, boolean alotOfMOneMoneyPenny) {
        super(name);
        serialNumber = ser;
        currTick = 0;
        diary = Diary.getInstance();
        agentsNames = new ArrayList<>();
        gadgetName = "";
        agentsSerials = new ArrayList<>();
        serialOfMoneyPenny = 0;
        missionDuraton = 0;
        intelligeicTimeTick = 0;
        missionName = "";
        oneMoneyPennyAlotOfM = alotOfMOneMoneyPenny;

    }
    protected void initialize() {

        int serialNum = this.serialNumber;

        subscribeBroadcast(TerminateBroadcast.class, t -> {
            terminate();
        });

        subscribeBroadcast(TickBroadcast.class, tick -> {
           this.currTick++;
        });


        subscribeEvent(MissionReceivedEvent.class, missionEvent -> {
            this.diary.incrementTotal();
            this.missionDuraton = missionEvent.getMissionInfo().getDuration();
            this.agentsSerials = missionEvent.getMissionInfo().getSerialAgentsNumbers();
            this.gadgetName = missionEvent.getMissionInfo().getGadget();
            this.missionName = missionEvent.getMissionInfo().getMissionName();
            this.intelligeicTimeTick = missionEvent.getMissionInfo().getTimeIssued();
            AgentsAvailableEvent agentsAvailableEvent = new AgentsAvailableEvent(agentsSerials, missionDuraton);
            Future<HolderAnswerAndMore> futureHolder = getSimplePublisher().sendEvent(agentsAvailableEvent);
            if (futureHolder != null) {
                HolderAnswerAndMore agentResult = futureHolder.get();
                if (agentResult != null) {
                    this.agentsNames = agentResult.getAgentsNames();
                    this.serialOfMoneyPenny = agentResult.getMoneyPennySerialNum();
                    if (agentResult.answer() != true || this.currTick > missionEvent.getMissionInfo().getTimeExpired()) {
                        ReleaseAgentsEvent releaseAgentsEvent = new ReleaseAgentsEvent(this.agentsSerials);
                        Future<Boolean> futureRel = this.getSimplePublisher().sendEvent(releaseAgentsEvent);
                        if (futureRel != null) {
                            complete(missionEvent, false);
                            return;

                        } else complete(releaseAgentsEvent, null);
                    } else {
                        GadgetAvailableEvent gadgetAvailableEvent = new GadgetAvailableEvent(gadgetName);
                        Future<HolderAnswerAndMore> futureGad = this.getSimplePublisher().sendEvent(gadgetAvailableEvent);
                        if (futureGad == null || futureGad.get() == null) {
                            ReleaseAgentsEvent releaseAgentsEvent = new ReleaseAgentsEvent(this.agentsSerials);
                            Future<Boolean> futeueRelease = getSimplePublisher().sendEvent(releaseAgentsEvent);
                            if (futeueRelease != null) {
                                complete(missionEvent, false);
                                return;

                            } else {
                                complete(releaseAgentsEvent, null);
                                return;
                            }


                        }

                        HolderAnswerAndMore gadgetResult = futureGad.get();

                        if (futureGad != null || futureGad.get() != null) {
                            if (gadgetResult.answer() != true) {
                                ReleaseAgentsEvent releaseAgentsEvent = new ReleaseAgentsEvent(this.agentsSerials);
                                Future<Boolean> futureRel = getSimplePublisher().sendEvent(releaseAgentsEvent);
                                if (futureRel != null) {
                                    complete(missionEvent, false);
                                    return;

                                } else complete(releaseAgentsEvent, null);

                            } else {
                                if (missionEvent.getMissionInfo().getTimeExpired() < gadgetResult.getQtime()) {
                                    ReleaseAgentsEvent releaseAgentsEvent = new ReleaseAgentsEvent(this.agentsSerials);
                                    Future<Boolean> futureRel = getSimplePublisher().sendEvent(releaseAgentsEvent);
                                    if (futureRel != null) {
                                        complete(missionEvent, false);
                                        return;

                                    } else complete(releaseAgentsEvent, null);
                                } else {
                                    Future<Boolean> futureSend = getSimplePublisher().sendEvent(new SendAgentsEvent(this.agentsSerials, this.missionDuraton));
                                    if (futureSend.get() != null) {
                                        if (futureSend.get() == true) {
                                            Report reportingMission = new Report();
                                            reportingMission.setMissionName(this.missionName);
                                            reportingMission.setTimeIssued(this.intelligeicTimeTick);
                                            reportingMission.setMoneypenny(this.serialOfMoneyPenny);
                                            reportingMission.setAgentsSerialNumbers(this.agentsSerials);
                                            reportingMission.setM(this.serialNumber);
                                            reportingMission.setAgentsNames(this.agentsNames);
                                            reportingMission.setGadgetName(this.gadgetName);
                                            if (gadgetAvailableEvent.getqTime() < missionEvent.getMissionInfo().getTimeIssued())
                                                reportingMission.setQTime(missionEvent.getMissionInfo().getTimeIssued());
                                            else
                                                reportingMission.setQTime(gadgetAvailableEvent.getqTime());
                                            reportingMission.setTimeCreated(this.currTick);
                                            this.diary.addReport(reportingMission);//adding reportForMission to Diary
                                            complete(missionEvent, true);
                                            reportingMission.setTimeCreated(this.currTick);

                                            diary.addReport(reportingMission);
                                            complete(missionEvent, true);
                                        }
                                    }

                                }
                            }
                        } else {
                            ReleaseAgentsEvent releaseAgentsEvent = new ReleaseAgentsEvent(this.agentsSerials);
                            Future<Boolean> futeueRelease = getSimplePublisher().sendEvent(releaseAgentsEvent);
                            if (futeueRelease != null) {
                                complete(missionEvent, false);
                                return;

                            } else complete(releaseAgentsEvent, null);
                        }

                    }

                }
                else {
                    ReleaseAgentsEvent releaseAgentsEvent = new ReleaseAgentsEvent(this.agentsSerials);
                    Future<Boolean> futeueRelease = getSimplePublisher().sendEvent(releaseAgentsEvent);
                    if (futeueRelease != null) {
                        complete(missionEvent, false);
                        return;

                    } else complete(releaseAgentsEvent, null);
                }
            }
        });

    }

}
