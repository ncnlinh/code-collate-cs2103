package command;

import java.io.IOException;
import java.util.LinkedList;

import logger.Log;
import common.Task;
import controller.Controller;
import storage.DBStorage;
/**
 *  CommandDeleteAll class is called by the controller in order to execute the Delete all command
 *  @param _storage - stores the DBstorage to get the storageList
 *	@param _validity - to store if undo for deleteAll is valid or not
 *	@param _storageList - stores the complete list of tasks
 */
public class CommandDeleteAll implements Command {
	private LinkedList<Task> _storageList;
	private DBStorage _storage;
	private boolean _validity;
	
	private static final String FEEDBACK_INVALID_STORAGE = "Cannot store the list to ToDoLog";
	private static final String FEEDBACK_VALID_DELETE_ALL = "Deleted all tasks";
	private static final String FEEDBACK_VALID_UNDO = "Undone the delete command";

	//@Author A0118899E
	@Override
	public String execute() {
		String feedback;
		_storage = Controller.getDBStorage();
		_storageList = _storage.load();
		try {
			_storage.store(new LinkedList<Task>());
		} catch (IOException e) {
			Log.error("Storage I/O problem",e);
			feedback = FEEDBACK_INVALID_STORAGE;
			_validity=false;
			return feedback;
		}
		// set focus task to change UI's page
		Controller.setFocusTask(null);
		feedback = FEEDBACK_VALID_DELETE_ALL;
		_validity=true;
		return feedback;
	}
	
	//@Author A0112156U
	public String tryExecute() {
		String feedback;
		_storage = Controller.getDBStorage();
		_storageList = _storage.load();
		// set focus task to change UI's page
		Controller.setFocusTask( null );
		feedback = FEEDBACK_VALID_DELETE_ALL;
		_validity = true;
		return feedback;
	}

	//@Author A0118899E
	@Override
	public String undo() {
		String feedback;
		try {
			_storage.store(_storageList);
		} catch (IOException e) {
			Log.error("Storage I/O problem",e);
			feedback = FEEDBACK_INVALID_STORAGE;
			return feedback;
		}
		feedback = FEEDBACK_VALID_UNDO;
		return feedback;
	}
	
	//@Author A0118899E
	@Override
	public boolean isUndoable() {
		assert isUndoable();
		return _validity;
	}

}
