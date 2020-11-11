package bgu.spl.mics.application.publishers;
import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;


/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {

	private int duration;
	private int currTick;
	private boolean isTerminated;

	public TimeService(String name, int duration)
	{
		super(name);
		this.duration = duration;
		this.currTick = 0;
		this.isTerminated = false;
	}

	public TimeService(int duration) {
		super("TimeService");
		this.duration = duration;
		this.currTick = 0;
		this.isTerminated = false;
	}

	@Override
	protected void initialize() {
		this.currTick = 0;
		this.isTerminated = false;

	}

	@Override
	public void run() {
		initialize();
		while (!isTerminated) {
			Broadcast tick = new TickBroadcast(currTick, duration);//broadcast currTick
			this.getSimplePublisher().sendBroadcast(tick);
			try {
				Thread.sleep(100); //sleep for 100 ms
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currTick++;
			if (currTick == duration) { // termination for all Subscribers should be executed
				isTerminated = true;
				this.getSimplePublisher().sendBroadcast(new TerminateBroadcast());
			}
		}
	}

}
