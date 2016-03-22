package eOSB.game.data;

/**
 * A class used to store the various PDFs used throughout eOSB.
 * @author Caine Jette
 *
 */
public class PathStore {

	/** the NOSB competition rules */
	public static final String COMPETITION_RULES = "eOSB/game/data/pdfs/rules.pdf";

	/** the eOSB EULA agreement terms */
	public static final String EULA_TXT = "eOSB/game/data/pdfs/eula.txt";
	
	/** the eOSB EULA agreement terms */
	public static final String EULA_PDF = "eOSB/game/data/pdfs/eula.pdf";
		
	/** the eOSB user manual */
	public static final String USER_MANUAL = "eOSB/game/data/pdfs/user_manual.pdf";
	
	public static final String ROUND_PREAMBLE = "eOSB/game/data/moderator_preamble.txt";
	
	public static final String TCQ_PREAMBLE = "eOSB/game/data/tcq_preamble.txt";
	
	public static final String INFO = "eOSB/game/data/questions/info.txt";
	
	public static final String BASE = "eOSB/game/data/questions/";
	
	public static String MakeTcqPath(String round, boolean isA, boolean isSolutions) {
		return "eOSB/game/data/questions/tcq_{0}_{1}{2}.pdf".replace("{0}", round).replace("{1}", isA ? "a" : "b").replace("{2}", isSolutions ? "_solutions" : "");
	}

}
