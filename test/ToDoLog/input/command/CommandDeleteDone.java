package command;

import java.io.IOException;
import java.util.LinkedList;

import logger.Log;
import common.Task;
import controller.Controller;
import storage.DBStorage;

/**
 *  CommandDeleteDone class is called by the controller in order to execute the Delete done command
 *  @param _storage - stores the DBstorage to get the storageList
 *	@param _validity - to store if undo for deleteDone is valid or not
 *	@param _storageList - stores the complete list of tasks
 *	@param _undoList - stores the list before performing the command for undo
 */

public class CommandDeleteDone implements Command {
	private LinkedList<Task> _storageList;
	private DBStorage _storage;
	private boolean _validity;
	private LinkedList <Task> _undoList;

	private static final String FEEDBACK_INVALID_STORAGE = "Cannot store the list to ToDoLog";
	private static final String FEEDBACK_VALID_DELETE_DONE = "Deleted completed tasks";
	private static final String FEEDBACK_VALID_UNDO = "Undone the delete done command";

	//@author A0118899E
	@Override
	public String execute() {
		String feedback;
		_storage = Controller.getDBStorage();
		_storageList = _storage.load();
		_undoList = new LinkedList <Task> (_storage.load());

		for ( int i=_storageList.size()-1; i >= 0; i-- ) {
			if (_storageList.get(i).getTaskStatus() == true ) {
				_storageList.remove(i);
			}
		}
		try {
			_storage.store(_storageList);
		} catch (IOException e) {
			Log.error("Storage I/O problem",e);
			feedback = FEEDBACK_INVALID_STORAGE;
			_validity=false;
			return feedback;
		}
		// set focus task to change UI's page
		Controller.setFocusTask(Controller.getScheduleList().getLast());
		Controller.setFocusTask(_storageList.getLast());
		feedback = FEEDBACK_VALID_DELETE_DONE;
		_validity = true;
		return feedback;
	}

	//@author A0112156U
	public String tryExecute() {
		String feedback;
		_storage = Controller.getDBStorage();
		_storageList = _storage.load();
		feedback = FEEDBACK_VALID_DELETE_DONE;
		_validity = true;
		return feedback;
	}

	//@author A0118899E
	@Override
	public String undo() {
		String feedback;
		try {
			_storage.store(_undoList);
		} catch (IOException e) {
			Log.error("Storage I/O problem",e);
			feedback = FEEDBACK_INVALID_STORAGE;
			return feedback;
		}
		feedback = FEEDBACK_VALID_UNDO;
		return feedback;
	}

	//@author A0118899E
	@Override
	public boolean isUndoable() {
		assert isUndoable();
		return _validity;
	}
}
