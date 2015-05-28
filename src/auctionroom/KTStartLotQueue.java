package auctionroom;
import net.jini.core.lease.Lease;
import net.jini.space.JavaSpace;


public class KTStartLotQueue {
	
	public static void main(String args[]){
		JavaSpace js = SpaceUtils.getSpace("localhost");
		if(js == null) js = SpaceUtils.getSpace("waterloo");
		if(js == null){
			System.out.println("Unable to connect to a JavaSpace.");
			System.exit(1);
		}
		try{
			KTLotQueue queue = new KTLotQueue(0);
			js.write(queue, null, Lease.FOREVER);
			System.out.println("Queue made and written to space.");
		} catch(Exception e) {
			System.out.println("An error happened!");
		}
	}
}
