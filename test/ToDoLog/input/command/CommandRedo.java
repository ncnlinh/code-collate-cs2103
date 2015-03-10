package command;

import history.History;
import controller.Controller;
/**
 *  CommandRedo class is called by the controller in order to execute the redo command
 *  @param _history - stores the history of all the commands.
 *	@param _toBeRedone - stores the command that has to be redo-ed
 */
public class CommandRedo implements Command {

	private Command _toBeRedone;
	private History _history;
	
	private static final String FEEDBACK_INVALID_UNDO = "Redo cannot be undone";
	
	//@Author A0118899E
	public CommandRedo(Command toBeRedone) {
		_toBeRedone = toBeRedone;
		_history = Controller.getHistory();
	}
	
	//@Author A0118899E
	@Override
	public String execute() {
		try {
			_history.goForwards();
			return _toBeRedone.execute();
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
