package eOSB;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jidesoft.plaf.LookAndFeelFactory;

import eOSB.game.controller.Handler;


/**
 * Main entry point for eOSB.
 * @author Caine Jette
 *
 */
public class EOSB {
  
  /**
   * @param args command line arguments
   */
	public static void main( String[] args ) {
		try	{
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
			LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
		}
		catch ( UnsupportedLookAndFeelException e ) {
		  // do nothing
		}
		catch ( ClassNotFoundException e ) {
		  // do nothing
		}
		catch ( InstantiationException e ) { 
		  // do nothing
		}
		catch ( IllegalAccessException e ) { 
		  // do nothing
		}

		final Handler handler = new Handler();
	}
}
