package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.HolderAnswerAndMore;
import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.LinkedList;
import java.util.List;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private int serialNumber;
	private Inventory inventory;
	private int currTick;
	private List<String> deletedGadgets;



	public Q() {
		super("Q");
		serialNumber=0;
		inventory = Inventory.getInstance();
		this.currTick=0;
		this.deletedGadgets = new LinkedList<>();
	}

	public Q(int serialNumber, String name)
	{
		super(name);
		this.serialNumber = serialNumber;
		inventory = Inventory.getInstance();
		this.currTick=0;
		this.deletedGadgets = new LinkedList<>();
	}

	@Override
	protected void initialize() {
		int serialNum = this.serialNumber;

		this.subscribeBroadcast(TerminateBroadcast.class, terminate->{
			terminate();

		});

		this.subscribeBroadcast(TickBroadcast.class, tick->
		{
			this.currTick++;
		});

		this.subscribeEvent(GadgetAvailableEvent.class, gadget->{

			String gadgetA =  gadget.getGadget();
			boolean gadgetIsAvail = inventory.getItem(gadgetA);
			HolderAnswerAndMore holderAnswerAndMore = new HolderAnswerAndMore();
			holderAnswerAndMore.setQtime(this.currTick);
			holderAnswerAndMore.setAns(gadgetIsAvail);
			complete(gadget , holderAnswerAndMore);

		});
		
	}

}
