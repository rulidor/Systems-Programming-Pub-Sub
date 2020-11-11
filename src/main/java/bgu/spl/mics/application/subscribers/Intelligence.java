package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private int currTick;
	private List<MissionInfo> missions;

	public Intelligence (String name, List<MissionInfo> missions)
	{
		super(name);
		this.currTick=0;
		this.missions = missions;
	}
	public Intelligence(List<MissionInfo> missions) {
		super("Intelligence");
		this.currTick=0;
		this.missions = missions;
	}

	@Override
	protected void initialize() {
		this.subscribeBroadcast(TerminateBroadcast.class, terminate->
		{
			terminate();
		});

		this.subscribeBroadcast(TickBroadcast.class, tick->
		{
			this.currTick++;

			//executes missions if needed:
			if (this.missions == null) return;
			for( MissionInfo mission : this.missions)
			{
				if (mission.getTimeIssued() == this.currTick)
				{

					MissionReceivedEvent missionReceivedEvent = new MissionReceivedEvent(mission);
					this.getSimplePublisher().sendEvent(missionReceivedEvent);
				}
			}
		});


	}
}
