package command;

import java.io.IOException;
import java.util.LinkedList;

import logger.Log;
import common.Task;
import controller.Controller;
import storage.DBStorage;
/**
 *  CommandDelete class is called by the controller in order to execute the Delete command
 *  @param _task - stores the task that is deleted from the log
 *  @param _storage - stores the DBstorage to get the scheduleList and FlexibleList
 *	@param _validity - to store if undo for delete is valid or not
 *	@param _index - stores the index of the task to be deleted
 */
public class CommandDelete implements Command {
	private Task _task;
	private DBStorage _storage;
	private int _index;
	private boolean _validity;

	private static final String FEEDBACK_INVALID_FORMAT = "Please specify the task to be deleted.";
	private static final String FEEDBACK_INVALID_INDEX = "Item number %1$s does not exist";
	private static final String FEEDBACK_VALID_DELETE = "Deleted %1$s from toDoLog";
	private static final String FEEDBACK_INVALID_STORAGE = "Cannot store the list to ToDoLog";
	private static final String FEEDBACK_VALID_UNDO = "Undone the delete command";
	
	//@Author A0118899E
	public CommandDelete(int index) {
		_index = index - 1;
	}
	
	//@Author A0118899E
	public CommandDelete() {
		_index = -1;
	}
	
	//@Author A0118899E
	public Task getDeletedTask() {
		return _task;
	}
	
	//@Author A0118899E
	@Override
	public String execute() {
		String feedback;
		LinkedList<Task> storageList;
		
		LinkedList<Task> _displayList= Controller.getScheduleList();
		_storage=Controller.getDBStorage();
		storageList=_storage.load();
		
		if (_index == -1) {
			_validity = false;
			return FEEDBACK_INVALID_FORMAT; 
		} else {
			assert (_index >= 0);
			try {
				_task = _displayList.get(_index);
				// set focus task to change UI's page
				Controller.setFocusTask(_task);
			} catch (IndexOutOfBoundsException ioobe ) {
				Log.info("Task index is out of bounds");
				_validity = false;
				Controller.setFocusTask(null);
				return String.format(FEEDBACK_INVALID_INDEX, _index+1);
			}
		}
		
		_displayList.remove(_index);
		storageList.remove(_task);
		feedback = String.format(FEEDBACK_VALID_DELETE, _task.getTaskName());
		_validity=true;
		try {
			_storage.store(storageList);
		} catch (IOException e) {
			Log.error("Storage I/O problem",e);
			feedback = FEEDBACK_INVALID_STORAGE;
			_validity=false;
			return feedback;
		}
		return feedback;
	}
	
	//@Author A0112156U
	public String tryExecute() { 
		LinkedList <Task> storageList;
		LinkedList <Task> _displayList= Controller.getScheduleList();
		_storage=Controller.getDBStorage();
		storageList=_storage.load();
		
		if (_index == -1) {
			_validity = false;
			return FEEDBACK_INVALID_FORMAT; 
		} else {
			assert (_index >= 0);
			try {
				_task = _displayList.get(_index);
				storageList.get(storageList.indexOf(_task));
				Controller.setFocusTask(_task);
			} catch (IndexOutOfBoundsException ioobe ) {
				Log.info("Task index is out of bounds");
				_validity = false;
				Controller.setFocusTask(_displayList.getLast());
				return String.format(FEEDBACK_INVALID_INDEX, _index+1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		_validity = true;
		return String.format(FEEDBACK_VALID_DELETE, _task.getTaskName());
	}
	
	//@Author A0118899E
	@Override
	public String undo() {
		String feedback;
		CommandAdd undoDelete = new CommandAdd(_task);
		// set focus task to change UI's page
		Controller.setFocusTask(_task);
		undoDelete.execute();
		feedback = FEEDBACK_VALID_UNDO;
		return feedback;
	}
	
	//@Author A0118899E
	@Override
	public boolean isUndoable(){
		assert isUndoable();
		return _validity;
	}
}
