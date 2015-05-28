/**
 * The LotListGUI class.
 * Provides a list of all current lots in the space.
 */

package auctionroom;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.export.Exporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.tcp.TcpServerEndpoint;
import net.jini.space.JavaSpace;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class LotListGUI extends javax.swing.JFrame implements RemoteEventListener {
	private JList lotsList;
	private JScrollPane lotScrollPane;
	private JButton refreshButton;
	private JList lotList;
	
	public KTLot lotTemplate = new KTLot();
	public KTLotQueue queueTemplate = new KTLotQueue();
	
	private final static int FIVE_SECONDS = 5*1000;
	private final static double HALF_SECOND = 0.5 * 1000;
	
	private JavaSpace js;
	private TransactionManager txmgr;
	private RemoteEventListener theStub;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LotListGUI inst = new LotListGUI();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public LotListGUI() {
		super();
		initGUI();
	}
		
	private void initGUI() {

		 
	
		try {
			
			
			
			setTitle("All available lots");
			
			//attempt to find javaspace and transactionmanager
			js = SpaceUtils.getSpace();
			if(js == null){
				System.out.println("JavaSpace not found on hostname waterloo. Attempting localhost connection.");
				js = SpaceUtils.getSpace("localhost");
				if(js == null){
					System.out.println("Unable to find JavaSpace. Application will run in offline mode.");
				} else {
					System.out.println("JavaSpace hostname: localhost");
				}
			} else {
				System.out.println("JavaSpace hostname: waterloo");
			}
			
			txmgr = SpaceUtils.getManager();
			if(txmgr == null){
				System.out.println("TransactionManager not found on hostname waterloo. Attempting localhost connection.");
				txmgr = SpaceUtils.getManager("localhost");
				if(txmgr == null){
					System.out.println("Unable to find TransactionManager. Application will run in offline mode.");
				} else {
					System.out.println("TransactionManager hostname: localhost");
				}
			} else {
				System.out.println("TransactionManager hostname: waterloo");
			}
			
			Exporter exporter = new BasicJeriExporter(TcpServerEndpoint.getInstance(0), new BasicILFactory(),false,true);
			System.out.println("exporter created");
			
			try{
				theStub = (RemoteEventListener) exporter.export(this);
				KTLot lotTemplate = new KTLot();
				js.notify(lotTemplate, null, this.theStub, Lease.FOREVER, null);
			} catch (Exception e){
				System.out.println("something went wrong setting up notify. " + e);
			}
			
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			{
				lotScrollPane = new JScrollPane();
				getContentPane().add(lotScrollPane);
				lotScrollPane.setBounds(12, 12, 688, 209);
				{
					ListModel lotListModel = 
							new DefaultComboBoxModel(
									new String[] { "No results found." });
					lotList = new JList();
					lotScrollPane.setViewportView(lotList);
					lotList.setBounds(89, -31, 656, 209);
					lotList.setModel(lotListModel);
				}
			}
			{
				refreshButton = new JButton();
				getContentPane().add(refreshButton);
				refreshButton.setText("Refresh List");
				refreshButton.setBounds(618, 227, 84, 23);
				refreshButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						System.out.println("Source: refreshButton");
						updateList();
					} //close actionevent
				});
			}

			pack();
			this.setSize(729, 300);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	public void updateList(){
		
		Transaction.Created trc = null;

		try{
			trc = TransactionFactory.create(txmgr, LotUtils.FIVE_SECONDS);
		} catch (Exception e){
			System.out.println ("Transaction creation failed. " + e);
		}
		
		Transaction txn = trc.transaction;
		
		
		DefaultListModel lotListModel = new DefaultListModel();
		//ArrayList<KTLot> lotArray = new ArrayList<KTLot>();
		try{
			try {
				System.out.println("Trying to read KTLotQueue.");
				KTLotQueue queueStatus = (KTLotQueue)js.read(queueTemplate, txn, LotUtils.HALF_SECOND);
				int j = 0;
				int k = 0;
				for(int i = 0; i < queueStatus.lotCounter; i++){
				
					lotTemplate.lotCounter = new Integer(i);
					KTLot currentLot = (KTLot)js.readIfExists(lotTemplate, txn,LotUtils.HALF_SECOND);
					//System.out.println("name: " + currentLot.lotName);
					
					if(currentLot != null){
						if(currentLot.isExpired == true){
							System.out.println(currentLot.lotName + " is expired. Not showing this item.");
							lotListModel.addElement("[This Lot has either expired or the owner has removed it.]");
							//this line may be removed to hide any trace of the item from the lot list.
						} else if (currentLot.isPrivate == true){
							System.out.println(currentLot.lotName + " is private. Not showing this item.");
							lotListModel.addElement("[Private Lot, contact " + currentLot.sellerID + " for details.]");
							//this line may be removed to hide any trace of the item from the lot list.
						} else if (currentLot.privateBid == true){
							System.out.println("The highest bidder wishes to remain anonymous.");
							System.out.println("Adding a lot to the list.");
							String lotStatus = "Lot name: " + currentLot.lotName + " | Highest bidder: [Anonymous Bidder]" + " | Current Bid: £[Redacted]" + " | Merchant: " + currentLot.sellerID + " | Description: " + currentLot.description;
							lotListModel.addElement(lotStatus);
						} else {
							System.out.println("Adding a lot to the list.");
							String lotStatus = "Lot name: " + currentLot.lotName + " | Highest bidder: " + currentLot.hiBidder + " | Current Bid: £" + currentLot.currentPrice + " | Merchant: " + currentLot.sellerID + " | Description: " + currentLot.description;
							lotListModel.addElement(lotStatus);
						}
					}
				}
			txn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			txn.abort();
		}
		} catch (Exception e){
			e.printStackTrace();
		}
		if(lotListModel.getSize() == 0) lotListModel.add(0,"There are currently no lots available to view!");

		lotList.setModel(lotListModel);
		try{
			//lotScrollPane.scrollRectToVisible(null);
		} catch (Exception e){
			//TODO investigate why the scroll down method causes exceptions.
		}
	}

	@Override
	public void notify(RemoteEvent arg0) throws UnknownEventException,
			RemoteException {
		//KTLot lotTemplate = new KTLot();
		//update the list whenever a new/edited KTLot object enters space.
		//this includes bidding on existing lots and lot owners cancelling lots.
		
		updateList();
		
	}

}
