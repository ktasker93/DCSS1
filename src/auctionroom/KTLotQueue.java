package auctionroom;
import net.jini.core.entry.Entry;

public class KTLotQueue implements Entry {
	
	public Integer lotCounter;
	public String auctionRoomName; //TODO implement functionality to connect to multiple auction rooms
	
	public KTLotQueue(){
		//no arg constructor/template
	}
	
	public KTLotQueue(int n){
		lotCounter = new Integer(n);
	}
	
	public void incLotCount(){
		lotCounter = new Integer(lotCounter.intValue()+1);
	}
}
