package command;

import history.History;
import controller.Controller;
/**
 *  CommandUndo class is called by the controller in order to execute the undo command
 *  @param _history - stores the history of all the commands.
 *	@param _toBeRedone - stores the command that has to be undo-ed
 */
public class CommandUndo implements Command {

	private Command _toBeUndone;
	private History _history;
	
	private static final String FEEDBACK_INVALID_UNDO = "Cannot undo the undo";
	
	//@Author A0118899E
	public CommandUndo(Command toBeUndone) {
		_toBeUndone = toBeUndone;
		_history = Controller.getHistory();
	}
	
	//@Author A0118899E
	@Override
	public String execute() {
		try {
			_history.goBackwards();
			return _toBeUndone.undo();
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	//@Author A0118899E
	@Override
	public String undo() {
		return FEEDBACK_INVALID_UNDO;
	}
	
	//@Author A0118899E
	@Override
	public boolean isUndoable() {
		return false;
	}
}
