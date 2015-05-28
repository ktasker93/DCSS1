package auctionroom;
import java.rmi.RMISecurityManager;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;


public class SpaceUtils {
	
	/**
	 * 
	 * The SpaceUtils class provided by Gary Allen.
	 * Does not contain any changes from the original version.
	 * 
	 */

	public static JavaSpace getSpace(String hostname){
		JavaSpace js = null;
		try{
			LookupLocator l = new LookupLocator("jini://" + hostname);
			
			ServiceRegistrar sr = l.getRegistrar();
			
			Class c = Class.forName("net.jini.space.JavaSpace");
			Class[] classTemplate = {c};
			
			js = (JavaSpace) sr.lookup(new ServiceTemplate(null, classTemplate, null));
		
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		return js;
	}
	
	public static JavaSpace getSpace(){
		return getSpace("waterloo");
	}
	
	public static TransactionManager getManager(String hostname){
		if(System.getSecurityManager() == null){
			System.setSecurityManager(new RMISecurityManager());
		}
		
		TransactionManager tm = null;
		try{
			LookupLocator l = new LookupLocator("jini://" + hostname);
			
			ServiceRegistrar sr = l.getRegistrar();
			
			Class c = Class.forName("net.jini.core.transaction.server.TransactionManager");
			Class[] classTemplate = {c};
			
			tm = (TransactionManager) sr.lookup(new ServiceTemplate(null, classTemplate, null));
		} catch (Exception e){
			System.err.println("Error: " + e);
		}
		return tm;
	}
	
	public static TransactionManager getManager(){
		return getManager("waterloo");
	}	

}
