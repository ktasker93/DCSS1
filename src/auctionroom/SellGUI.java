package auctionroom;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
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
public class SellGUI extends javax.swing.JFrame {
	private JLabel labelCurrentSellerID;

	private String sellerID;
	public static final int FIVE_MINUTES = 1000*60*5;
	public static final int ONE_MINUTE = 1000*60;
	public static final int FIVE_SECONDS = 1000*5;
	private JavaSpace js;
	private TransactionManager txmgr;
	
//	public KTLot lotTemplate = new KTLot();
//	public KTLotQueue queueTemplate = new KTLotQueue();
	
	private JTextField lotDescription;
	private JLabel labelLotStartingPrice;
	private JLabel labelLotDuration;
	private JTextField lotDuration;
	private JTextField infoTimeRemaining;
	private JLabel labelTimeRemaining;
	private JTextField infoBidCount;
	private JLabel labelTotalBidders;
	private JTextField infoCurrentBidAmount;
	private JLabel labelCurrentPrice;
	private JTextField infoHighBidder;
	private JLabel labelHighBidder;
	private JLabel labelCurrentStatus;
	private JButton buttonCancelLot;
	private JButton buttonEnterLot;
	private JLabel labelIsPrivate;
	private JCheckBox lotIsPrivate;
	private JTextField lotStartPrice;
	private JLabel labelLotEntry;
	private JTextField lotNameEntry;
	private JLabel labelLotName;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SellGUI inst = new SellGUI();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public SellGUI() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {

			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			setTitle("Place a Lot");
			sellerID = (String)JOptionPane.showInputDialog("Enter your seller ID.",null);				
			//an invalid input was used. enforce the user chooses an appropriate username.
			if(sellerID == null || sellerID.isEmpty() || sellerID.length() > 50){
				while(sellerID == null || sellerID.isEmpty() || sellerID.length() > 50){
				sellerID = (String)JOptionPane.showInputDialog("Enter a valid seller ID. (Alphanumeric string of 50 characters or less.)",null);				
				}
			}
			
			//initialise javaspace and transactionmanager connections.
			//NOTE: code to connect to a local server (localhost) remains in the submitted version - removing it would serve no real purpose as this code was constantly used in development.
			//it will have been mentioned in the report.
			
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
			txmgr = null;
			
			try{
				KTLotQueue queueTemplate = new KTLotQueue();
				System.out.println("Testing if a KTLotQueue exists...");
				KTLotQueue queueStatus = (KTLotQueue)js.read(queueTemplate, null, FIVE_SECONDS);
				if(queueStatus != null){
					System.out.println("KTLotQueue found.");
					System.out.println(queueStatus.lotCounter);
				} else {
					System.out.println("KTLotQueue not found. Please run KTStartLotQueue.");
				}
			} catch(Exception e){
				System.out.println("Exception caught, javaspace connection error.");
			}
			
			//initialise the GUI elements (swing)
			
			{
				labelCurrentSellerID = new JLabel();
				getContentPane().add(labelCurrentSellerID);
				labelCurrentSellerID.setText("Currently logged in as: " + sellerID);
				labelCurrentSellerID.setBounds(12, 12, 411, 16);
			}
			{
				labelLotName = new JLabel();
				getContentPane().add(labelLotName);
				labelLotName.setText("Lot Name");
				labelLotName.setBounds(12, 40, 119, 16);
			}
			{
				lotNameEntry = new JTextField();
				getContentPane().add(lotNameEntry);
				lotNameEntry.setText("lotID1");
				lotNameEntry.setBounds(131, 37, 160, 23);
			}
			{
				lotDescription = new JTextField();
				getContentPane().add(lotDescription);
				lotDescription.setText("Hello world! This is a test lot.");
				lotDescription.setBounds(131, 59, 160, 23);
			}
			{
				labelLotEntry = new JLabel();
				getContentPane().add(labelLotEntry);
				labelLotEntry.setText("Lot Description");
				labelLotEntry.setBounds(12, 63, 80, 16);
			}
			{
				labelLotStartingPrice = new JLabel();
				getContentPane().add(labelLotStartingPrice);
				labelLotStartingPrice.setText("Starting Price");
				labelLotStartingPrice.setBounds(12, 85, 119, 16);
			}
			{
				lotStartPrice = new JTextField();
				getContentPane().add(lotStartPrice);
				lotStartPrice.setText("15.00");
				lotStartPrice.setBounds(131, 81, 160, 23);
			}
			{
				labelLotDuration = new JLabel();
				getContentPane().add(labelLotDuration);
				labelLotDuration.setText("Lot Duration (mins)");
				labelLotDuration.setBounds(12, 107, 119, 16);
			}
			{
				lotDuration = new JTextField();
				getContentPane().add(lotDuration);
				lotDuration.setText("2");
				lotDuration.setBounds(131, 103, 160, 23);
			}
			{
				lotIsPrivate = new JCheckBox();
				getContentPane().add(lotIsPrivate);
				lotIsPrivate.setText("Only those who know your lotID will be able to bid.");
				lotIsPrivate.setBounds(128, 128, 322, 17);
			}
			{
				labelIsPrivate = new JLabel();
				getContentPane().add(labelIsPrivate);
				labelIsPrivate.setText("Private Lot?");
				labelIsPrivate.setBounds(12, 129, 119, 16);
			}
			{
				buttonEnterLot = new JButton();
				getContentPane().add(buttonEnterLot);
				buttonEnterLot.setText("Submit");
				buttonEnterLot.setBounds(423, 36, 84, 23);
				buttonEnterLot.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						System.out.println("Source: buttonEnterLot");
						//Takes details of all information entered in fields. If valid, sends this information to the javaspace as a new KTLot object.
						try{
						String lotID = lotNameEntry.getText().toString();
						Double startPrice = Double.valueOf(lotStartPrice.getText());
						Boolean isPrivate = lotIsPrivate.isSelected();
						String isPrivateStr = "";
						if(isPrivate == true) //initalise the isPrivate variable into a string readable by the user.
							isPrivateStr = " (Private Lot)";
						System.out.println(isPrivate);
						Long duration = Long.valueOf(lotDuration.getText());
						Long leaseTime = toMiliS(duration);
						String info = lotDescription.getText().toString();
						String sellID = sellerID;

						Transaction.Created trc = null;
						
						try{
							trc = TransactionFactory.create(txmgr, Lease.FOREVER);
						} catch(Exception e) {
							System.out.println("Transaction creation failed. " + e);
						}
						
						Transaction txn = trc.transaction;
						KTLotQueue queueTemplate = new KTLotQueue();

						try{
							try{			
								KTLotQueue queueStatus = (KTLotQueue)js.take(queueTemplate, txn, LotUtils.FIVE_SECONDS);
								int lotCounter = queueStatus.lotCounter;
								KTLot newLot = new KTLot(lotID, lotCounter, startPrice, info, isPrivate, sellID, duration);	
								System.out.println("newLot sellerID " + newLot.sellerID);
								System.out.println(lotID + lotCounter + startPrice + info + isPrivate + sellerID + duration);
								js.write(newLot, txn, Lease.FOREVER);
								queueStatus.incLotCount();
								js.write(queueStatus, txn, Lease.FOREVER);
								txn.commit();
								JOptionPane.showMessageDialog(null,"Now monitoring your lot " + lotID + ". This lot is number " + (lotCounter + 1) + " to be submitted today.");
								labelCurrentStatus.setText("Current information for your lot: " + lotID + isPrivateStr);
							} catch(Exception e) {
								txn.abort();
								e.printStackTrace();
								System.out.println("An error occurred! Lost contact with the JavaSpace. Try restarting application.");
							}
						} catch (Exception e){
							e.printStackTrace();
							System.out.println("outer try/catch submitting lot");
							
						}
						} catch(Exception e){
							System.out.println("Error intialising variables");
							JOptionPane.showMessageDialog(null, "Please ensure all fields are not empty.");
							//e.printStackTrace();
						}
						
								
					}
				});
			}
			{
				buttonCancelLot = new JButton();
				getContentPane().add(buttonCancelLot);
				buttonCancelLot.setText("Cancel");
				buttonCancelLot.setBounds(423, 154, 84, 23);
				buttonCancelLot.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						//Below are the changes 1 and 2 made stated in the changelog.
						try{
							System.out.println("source: cancel button");
							System.out.println("sellerID: " + sellerID);
							
						KTLot lotTemplate = new KTLot();
						lotTemplate.lotName = lotNameEntry.getText().toString();
						String lotID = lotTemplate.lotName;
						KTLot lotNameTest = (KTLot)js.readIfExists(lotTemplate,null,LotUtils.HALF_SECOND);
						//System.out.println(lotNameTest.lotName + " " + lotNameTest.sellerID + "hibid " + lotNameTest.hiBidder);
						if(lotNameTest == null){ 
							System.out.println("lot did not exist");
							//enforce that the lot actually exists (check if readIfExists was successful.)
							JOptionPane.showMessageDialog(null, "Error: This lot does not exist.");
						}
						else if(!lotNameTest.sellerID.toString().equals(sellerID)) {
							//enforce that the current sellerID is equal to the sellerID stored in the KTLot object.
							//if the sellerID in the lot is not equal to the current sellerID
							System.out.println("this was not the real lot owner");
							JOptionPane.showMessageDialog(null, "Error: You are not the owner of this lot.");
						}
						else if((lotNameTest.hiBidder.equals("") && lotNameTest.bidCount == 0) && lotNameTest.sellerID.toString().equals(sellerID)){ 
							//check whether anybody placed any bids on the lot
														System.out.println("deleted, 0 bids");
							JOptionPane.showMessageDialog(null, "Lot cancelled succesfully. There were no bids placed on your item.");
							LotUtils.deleteLot(lotID,js,txmgr);
						}
						else if((!(lotNameTest.hiBidder.equals("")) && lotNameTest.bidCount > 0) && lotNameTest.sellerID.toString().equals(sellerID)){ 
							//double check that somebody did place a bid on the lot, show a message containing all relevant information
							System.out.println("deleted, winning bidder");
							JOptionPane.showMessageDialog(null, "Lot cancelled succesfully.\nYour auction ended after " + lotNameTest.bidCount + " bids,\n and user \"" + lotNameTest.hiBidder + "\" won the lot with a bid of £" + lotNameTest.currentPrice + ".");
							LotUtils.deleteLot(lotID,js,txmgr);
						}
						} catch(Exception e) {
							System.out.println("Caught exception: " + e);
							e.printStackTrace();
						}
						


					}
				});
			}
			{
				labelCurrentStatus = new JLabel();
				getContentPane().add(labelCurrentStatus);
				labelCurrentStatus.setText("Current information for your lot: ");
				labelCurrentStatus.setBounds(12, 157, 411, 16);
			}
			{
				labelHighBidder = new JLabel();
				getContentPane().add(labelHighBidder);
				labelHighBidder.setText("Highest Bidder");
				labelHighBidder.setBounds(12, 179, 119, 16);
			}
			{
				infoHighBidder = new JTextField();
				getContentPane().add(infoHighBidder);
				infoHighBidder.setText("...");
				infoHighBidder.setBounds(131, 176, 160, 23);
				infoHighBidder.setEditable(false);
			}
			{
				labelCurrentPrice = new JLabel();
				getContentPane().add(labelCurrentPrice);
				labelCurrentPrice.setText("Current Bid");
				labelCurrentPrice.setBounds(12, 201, 119, 16);
			}
			{
				infoCurrentBidAmount = new JTextField();
				getContentPane().add(infoCurrentBidAmount);
				infoCurrentBidAmount.setText("...");
				infoCurrentBidAmount.setBounds(131, 196, 160, 23);
				infoCurrentBidAmount.setEditable(false);
			}
			{
				labelTotalBidders = new JLabel();
				getContentPane().add(labelTotalBidders);
				labelTotalBidders.setText("Number of Bids");
				labelTotalBidders.setBounds(12, 223, 119, 16);
			}
			{
				infoBidCount = new JTextField();
				getContentPane().add(infoBidCount);
				infoBidCount.setText("...");
				infoBidCount.setBounds(131, 218, 160, 23);
				infoBidCount.setEditable(false);
			}
			{
				labelTimeRemaining = new JLabel();
				getContentPane().add(labelTimeRemaining);
				labelTimeRemaining.setText("Time Remaining");
				labelTimeRemaining.setBounds(12, 245, 119, 16);
			}
			{
				infoTimeRemaining = new JTextField();
				getContentPane().add(infoTimeRemaining);
				infoTimeRemaining.setText("...");
				infoTimeRemaining.setBounds(131, 240, 160, 23);
				infoTimeRemaining.setEditable(false);
			}
			pack();

			this.setSize(534, 321);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	public Long toMiliS(Long i){
		/*
		 * Converts a long value to miliseconds.
		 */
		return i * 1000 * 60;
	}

}

