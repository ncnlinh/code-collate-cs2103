package command;

import gui.HelpFrame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

import logger.Log;

/**
 *  CommandHelp class is called by the controller in order to execute the help command
 *  @param inFile - to read the helper.txt file.
 */
public class CommandHelp implements Command {
	
	private static final String FILEPATH_HELPTEXT = "resources/text/helper.txt";

	private Scanner inFile;
	
	private static final String FEEDBACK_HELP = "Help is here!";
	private static final String FEEDBACK_UNDO = "Help cannot be undone";
	
	//@Author A0118899E
	@Override
	public String execute() {
		String inFileText = "";
		try {
			URL helpTextUrl = this.getClass().getClassLoader().getResource(FILEPATH_HELPTEXT);
			inFile = new Scanner(new BufferedReader(new InputStreamReader(helpTextUrl.openStream())));
			while(inFile.hasNextLine() != false ) {
				String line = inFile.nextLine();
				inFileText = inFileText + line + "\n"; 
			} 	
		} catch (FileNotFoundException e) {
			Log.error("helper.txt not found");
		} catch (IOException e1){
			Log.error("error reading helper.txt");
		}
		if (inFileText != null ) {
			HelpFrame generateHelpText = new HelpFrame(inFileText);
			generateHelpText.execute();
		}
		return FEEDBACK_HELP;
	}
	
	//@Author A0118899E
	@Override
	public String undo() {
		return FEEDBACK_UNDO ;
	}
	
	//@Author A0118899E
	@Override
	public boolean isUndoable() {
		return false;
	}
}
