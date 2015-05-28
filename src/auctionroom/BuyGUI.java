/**
 * The BuyGUI class.
 * Creates a GUI to place bids in the lots in the JavaSpace.
 */

package auctionroom;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
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
public class BuyGUI extends javax.swing.JFrame implements RemoteEventListener {
	private JLabel labelCurrentlyViewing;
	private JLabel labelCurrentHighBid;
	private JLabel labelNumberOfBids;
	private JTextField currentLotHighBid;
	private JLabel currentBidIsPrivate;
	private JLabel labelLotInfo;
	private JTextField lotInfoField;
	private JCheckBox isPrivateButton;
	private JButton placeBidButton;
	private JTextField currentLotNewBid;
	private JTextField currentLotBidCount;
	private JButton lotSearchButton;
	private JTextField currentLot;
	private JLabel labelCurrentBuyerID;
	private JLabel labelEnterBid;
	
	private String buyerID;
	private JavaSpace js;
	private TransactionManager txmgr;
	private KTLot viewingLot;
	
	private RemoteEventListener theStub;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				BuyGUI inst = new BuyGUI();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public BuyGUI() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			setTitle("Place a bid");
			buyerID = (String)JOptionPane.showInputDialog("Enter your buyer ID.",null);				
			//an invalid input was used. enforce the user chooses an appropriate username.
			if(buyerID == null || buyerID.isEmpty() || buyerID.length() > 50){
				while(buyerID == null || buyerID.isEmpty() || buyerID.length() > 50){
				buyerID = (String)JOptionPane.showInputDialog("Enter a valid buyer ID. (Alphanumeric string of 50 characters or less.)",null);				
				}
			}
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
				lotTemplate.isExpired = true;
				lotTemplate.hiBidder = buyerID;
				js.notify(lotTemplate, null, this.theStub, Lease.FOREVER, null);
			} catch (Exception e){
				System.out.println("something went wrong setting up notify. " + e);
			}
			

			{
				labelCurrentlyViewing = new JLabel();
				getContentPane().add(labelCurrentlyViewing);
				labelCurrentlyViewing.setText("Currently Viewing Lot");
				labelCurrentlyViewing.setBounds(12, 34, 119, 16);
			}
			{
				labelCurrentHighBid = new JLabel();
				getContentPane().add(labelCurrentHighBid);
				labelCurrentHighBid.setText("High Bid");
				labelCurrentHighBid.setBounds(12, 56, 119, 16);
				
			}
			{
				labelNumberOfBids = new JLabel();
				getContentPane().add(labelNumberOfBids);
				labelNumberOfBids.setText("Bid Count");
				labelNumberOfBids.setBounds(12, 78, 119, 16);
			}
			{
				labelEnterBid = new JLabel();
				getContentPane().add(labelEnterBid);
				labelEnterBid.setText("Enter Amount");
				labelEnterBid.setBounds(12, 123, 119, 16);
			}
			{
				labelCurrentBuyerID = new JLabel();
				getContentPane().add(labelCurrentBuyerID);
				labelCurrentBuyerID.setText("Currently logged in as: " + buyerID);
				labelCurrentBuyerID.setBounds(12, 12, 285, 16);
			}
			{
				currentLot = new JTextField();
				getContentPane().add(currentLot);
				currentLot.setText("Enter a lotID.");
				currentLot.setBounds(137, 31, 85, 23);
			}
			{
				lotSearchButton = new JButton();
				getContentPane().add(lotSearchButton);
				lotSearchButton.setText("Search");
				lotSearchButton.setBounds(228, 31, 83, 23);
				lotSearchButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						System.out.println("Source: lotSearchButton. ");
						//fetches details from the selected lot.
						try{
							String lotName = currentLot.getText().toString();
							KTLot lotTemplate = new KTLot();
							lotTemplate.lotName = lotName;
							KTLot lotInfo = (KTLot)js.readIfExists(lotTemplate, null, LotUtils.HALF_SECOND);
							if(currentLot != null){
							currentLot.setText(lotInfo.lotName);			
						
							if((buyerID.equals(lotInfo.hiBidder) && lotInfo.privateBid == true) || lotInfo.privateBid == false){
								currentLotHighBid.setText(lotInfo.currentPrice.toString());
							} else {
								currentLotHighBid.setText("The bidder wished to place a private bid.");
							}
							
							if(lotInfo.bidCount == null)
								currentLotBidCount.setText("0");
							else
								currentLotBidCount.setText(lotInfo.bidCount.toString());
						
							if(lotInfo.isExpired == true){
								currentLotNewBid.setEditable(false);
								currentLotNewBid.setText("This lot has expired.");
							} else {
								currentLotNewBid.setEditable(true);
								currentLotNewBid.setText("");
							}		
							lotInfoField.setText(lotInfo.description);
							viewingLot = lotInfo;
							}
							} catch (Exception e){
								JOptionPane.showMessageDialog(null, "No lot with that name found.");
								//e.printStackTrace();
								System.out.println();
							}
					}
								
				});
					
			}
			{
				currentLotHighBid = new JTextField();
				getContentPane().add(currentLotHighBid);
				currentLotHighBid.setText("NA");
				currentLotHighBid.setBounds(137, 53, 248, 23);
				currentLotHighBid.setEditable(false);
			}
			{
				currentLotBidCount = new JTextField();
				getContentPane().add(currentLotBidCount);
				currentLotBidCount.setText("NA");
				currentLotBidCount.setBounds(137, 75, 85, 23);
				currentLotBidCount.setEditable(false);
			}
			{
				currentLotNewBid = new JTextField();
				getContentPane().add(currentLotNewBid);
				currentLotNewBid.setText("Enter an amount to bid");
				currentLotNewBid.setBounds(137, 120, 128, 23);
			}
			{
				placeBidButton = new JButton();
				getContentPane().add(placeBidButton);
				placeBidButton.setText("Place offer");
				placeBidButton.setBounds(136, 166, 148, 23);
				placeBidButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						
						Transaction.Created trc = null;
						
						try{
							trc = TransactionFactory.create(txmgr,  LotUtils.FIVE_SECONDS);
						} catch (Exception e){
							System.out.println("Error creating transaction "+ e);
						}
						
						Transaction txn = trc.transaction;
						
					
						try{
							viewingLot = (KTLot)js.readIfExists(viewingLot, txn, LotUtils.HALF_SECOND);
							double a = viewingLot.currentPrice;
							if(a < Double.parseDouble(currentLotNewBid.getText())){
						// handle new bid
						try{
							js.take(viewingLot, txn, LotUtils.HALF_SECOND);
							viewingLot.hiBidder = buyerID;
							viewingLot.privateBid = isPrivateButton.isSelected();
							viewingLot.bidCount++;
							viewingLot.currentPrice = Double.parseDouble(currentLotNewBid.getText());
							js.write(viewingLot, txn, Lease.FOREVER);
							JOptionPane.showMessageDialog(null, "Your bid was accepted. You are now the highest bidder!");
							 txn.commit();
						} catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(null, "Please ensure all fields are filled correctly.");
						} catch (Exception e){
							System.out.println("An error occurred. Somebody may have undercut your bid.");
							txn.abort();
						}
							
					} else {
						JOptionPane.showMessageDialog(null, "Your bid was not accepted as there is already a higher bid present.");
					}
					}catch(Exception e){
						
					}
						if(currentLot.getText().toString() != null){
							currentLot.setText(viewingLot.lotName);			
						
							if((buyerID.equals(viewingLot.hiBidder) && viewingLot.privateBid == true) || viewingLot.privateBid == false){
								currentLotHighBid.setText(viewingLot.currentPrice.toString());
							} else {
								currentLotHighBid.setText("The bidder wished to place a private bid.");
							}
							
							if(viewingLot.bidCount == null)
								currentLotBidCount.setText("0");
							else
								currentLotBidCount.setText(viewingLot.bidCount.toString());
						
							if(viewingLot.isExpired == true){
								currentLotNewBid.setEditable(false);
								currentLotNewBid.setText("This lot has expired.");
							} else {
								currentLotNewBid.setEditable(true);
								currentLotNewBid.setText("");
							}		
							lotInfoField.setText(viewingLot.description);
							}

					}
				});
			}
			{
				currentBidIsPrivate = new JLabel();
				getContentPane().add(currentBidIsPrivate);
				currentBidIsPrivate.setText("Private bid?");
				currentBidIsPrivate.setBounds(12, 145, 119, 16);
			}
			{
				isPrivateButton = new JCheckBox();
				getContentPane().add(isPrivateButton);
				isPrivateButton.setText("Your buyer ID will not appear in the Lots list.");
				isPrivateButton.setBounds(133, 146, 343, 17);
			}
			{
				labelLotInfo = new JLabel();
				getContentPane().add(labelLotInfo);
				labelLotInfo.setText("Description");
				labelLotInfo.setBounds(12, 102, 119, 16);
			}
			{
				lotInfoField = new JTextField();
				getContentPane().add(lotInfoField);
				lotInfoField.setText("NA");
				lotInfoField.setEditable(false);
				lotInfoField.setBounds(137, 98, 367, 23);
			}
			pack();
			this.setSize(532, 300);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}

	@Override
	public void notify(RemoteEvent arg0) throws UnknownEventException,
			RemoteException {
		
		KTLot lotTemplate = new KTLot();
		lotTemplate.hiBidder = buyerID;
		lotTemplate.isExpired = true;
		System.out.println("notify caught - a bid was won");
		try{
			KTLot notifiedLot = (KTLot)js.readIfExists(lotTemplate, null, LotUtils.FIVE_SECONDS);
			JOptionPane.showMessageDialog(null, "Congratulations " + notifiedLot.hiBidder + "!\nThe owner of the lot " + notifiedLot.lotName + " has just closed their lot and you were the highest bidder.\nPlease arrange payment of £" + notifiedLot.currentPrice.toString() + " to " + notifiedLot.sellerID + " as soon as possible.");
		} catch(Exception e){
			System.out.println("something went wrong in the notify method " + e);
			e.printStackTrace();
		}
		
	}

}


