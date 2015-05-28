/**
 * The KTLot class.
 * Contains all details of new lots.
 * KTLot objects represent lots that users may create and bid on.
 */

package auctionroom;
import net.jini.core.entry.Entry;

public class KTLot implements Entry {
	
	public String lotName;
	public Double currentPrice; //initialised as the lowest/starting price by seller. changed every time a bidder offers a successful new bid (higher than the previous currentPrice.)
	
	public Integer lotCounter; //used to store this object's location in the linked list data structure
	public String description;
	public Integer bidCount; //lot may not be 'sold' if bidCount is 0.
	public String hiBidder; //records the last bidder to add a successful bid.
	public Boolean isPrivate; //a private lot is only available to those who know the lotName, will not show up on the LotListGUI.
	public Long originTime; //time at which the bid was created
	public Long duration; //time the bid will last until expiring (minutes)
	public Long expirationTime; //time at which the bid will expire and decide based on the winning bet
	public String sellerID; //seller who submitted this lot
	public Boolean isExpired;
	public Boolean privateBid;
	
	public KTLot(){
		//no arg constructor/template for space
	}
	
	public KTLot(String name, int counter, Double price, String info, Boolean p, String sID, Long d){
		//create object, initialise variables
		lotName = name;
		currentPrice = price;
		lotCounter = new Integer(counter);
		sellerID = sID;
		System.out.println("KTLot SellerID = " + sID);
		duration = d;
		description = info;
		hiBidder = "";
		bidCount = 0;
		isPrivate = p;
		originTime = System.currentTimeMillis();
		expirationTime = originTime + toMilis(duration);
		isExpired = false;
		privateBid = false;
	}
	
	public void addBid(){
		bidCount++;
	}
	
	public void updatePrice(double price){
		currentPrice = price;
	}
	
	public long toMilis(long t){ //converts an integer to miliseconds.
		return t * 60 * 60 * 1000;
	}
	
}
