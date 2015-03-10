package command;

import controller.Controller;

/**
 *  CommandLoad class is called by the controller in order to execute the load command
 *  @param _newFileName - stores the new file name to be loaded
 *  @param _storage - store the old file name for undo
 */

public class CommandLoad implements Command {
	
	private String _newFileName;
	private String _oldFileName;
	
	private static final String FEEDBACK_LOADED_FILE_NAME = "Loaded/created \" %1$s \"";
	
	//@Author A0118899E
	public CommandLoad(String fileName) {
		_oldFileName = Controller.getDBStorage().getFileStorage().getFile().getName();
		_newFileName = fileName;
	}
	
	//@Author A0118899E
	@Override
	public String execute() {
		Controller.init(_newFileName);
		 // set focus task to change UI's page
		Controller.setFocusTask( null );
		return String.format(FEEDBACK_LOADED_FILE_NAME, _newFileName); 
	}

	//@Author A0118899E
	@Override
	public String undo() {
		Controller.init(_oldFileName);
		// set focus task to change UI's page
		Controller.setFocusTask( null ); 
		return String.format(FEEDBACK_LOADED_FILE_NAME, _oldFileName); 
	}

	//@Author A0118899E
	@Override
	public boolean isUndoable() {
		assert isUndoable();
		return true;
	}
}
