package auctionroom;
import net.jini.core.lease.*;

import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;

/**
 * 
 * A class containing a few utilities for lots.
 * Originally intended to contain more code, however during development
 * it wasn't particularly needed.
 * 
 * Primarily used to set lease times throughout the project and
 * 'delete lots' from the JavaSpace.
 *
 */
public class LotUtils {
	
	public static final int FIVE_MINUTES = 1000*60*5;
	public static final int ONE_MINUTE = 1000*60;
	public static final int FIVE_SECONDS = 1000*5;
	public static final int HALF_SECOND = (int)0.5*1000;
	
	public static void deleteLot(String lotN, JavaSpace js, TransactionManager txmgr){
		//'delete' a lot from the space by setting it's isExpired variable to true.
		//write it back to space after it has been changed.
		Transaction.Created trc = null;
		try{
			trc = TransactionFactory.create(txmgr, FIVE_SECONDS);
		} catch(Exception e){
			System.out.println("Error creating transaction." + e);
		}
		
		
		Transaction txn = trc.transaction;
		
		try{
			try{
			KTLotQueue queueTemplate = new KTLotQueue();
			KTLot lotTemplate = new KTLot();
			lotTemplate.lotName = lotN;
			KTLot delLot = (KTLot)js.takeIfExists(lotTemplate, txn, HALF_SECOND);
			System.out.println("Deleting " + delLot.lotName + " which is lot " + delLot.lotCounter);
			delLot.isExpired = true;
			js.write(delLot, txn, Lease.FOREVER);
			txn.commit();
			System.out.println("Transaction successful!");
		} catch(Exception e){
			e.printStackTrace();
			txn.abort();
			System.out.println("Transaction cancelled.");
		}
		} catch (Exception e){
			System.out.println("Unhandled exception in LotUtils.deleteLot()!" + e);
		}
	}
	

}
