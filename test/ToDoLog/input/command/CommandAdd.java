package command;

import java.io.IOException;
import java.util.LinkedList;

import logger.Log;
import common.Task;
import common.TaskType;
import controller.Controller;
import storage.DBStorage;
	/**
	 *  CommandAdd class is called by the controller in order to execute the Add command
	 *  @param _task - stores the task to be added to the log
	 *  @param _storage - stores the DBstorage to get the scheduleList
	 *	@param _validity - to store if undo for add is valid or not
	 */
public class CommandAdd implements Command {
	private Task _task;
	private DBStorage _storage;
	private boolean _validity;
	
	private static final String FEEDBACK_INVALID_DETAILS = "Please enter the task details";
	private static final String FEEDBACK_INVALID_STORAGE = "Cannot store the list to ToDoLog";
	private static final String FEEDBACK_VALID_INPUT = "Added %1$s to ToDoLog";
	private static final String FEEDBACK_VALID_UNDO = "Undone adding %1$s";
	private static final String FEEDBACK_INVALID_UNDO = "Cannot undo adding %1$s";
	
	//@Author A0118899E
	public CommandAdd(Task task) {
		_task = task;
	}
	
	//@Author A0118899E
	public Task getAddedTask() {
		return _task;
	}
	
	//@Author A0118899E
	@Override
	public String execute() {
		
		String feedback;
		LinkedList <Task> storageList = new LinkedList<Task>();
		
		_storage= Controller.getDBStorage();
		storageList=_storage.load();
		
		if (_task == null) {
			_validity = false;
			return FEEDBACK_INVALID_DETAILS;
		}
		
		sortByDate(storageList);
		
		try {
			_storage.store(storageList);
		} catch (IOException e) {
			Log.error("Storage I/O problem",e);
			feedback = FEEDBACK_INVALID_STORAGE;
			_validity = false;
			return feedback;
		}
		feedback = String.format (FEEDBACK_VALID_INPUT, _task.getTaskName());
		_validity=true;
		return feedback;
	}
	
	//@Author A0118899E
	public void sortByDate(LinkedList<Task> toSortList) {
		
	    if (_task.getTaskType() == TaskType.FLOATING) {
	    	toSortList.add(_task);
	    	// set focus task to change UI's page
	    	Controller.setFocusTask(_task); 
	    } else {
	    	assert (_task.getTaskType() == TaskType.TIMED || _task.getTaskType() == TaskType.DEADLINE);
	    	boolean isAdded = false;
    		for (int i=0; i<toSortList.size(); i++) {
    			Task current = toSortList.get(i);
    			if (current.getTaskType() == TaskType.FLOATING) {
    				toSortList.add(i,_task);
    				// set focus task to change UI's page
    				Controller.setFocusTask(_task); 
    				isAdded = true;
    				break;
    			} else {
    				assert (current.getTaskType() == TaskType.TIMED || current.getTaskType() == TaskType.DEADLINE);
	    			if (current.getEndDateTime().compareTo(_task.getEndDateTime()) >0) {
	    				toSortList.add(i,_task);
	    				// set focus task to change UI's page
	    				Controller.setFocusTask(_task); 
	    				isAdded=true;
	    				break;
	    			}
    			}	
    		}
    		
    		if (!isAdded) {
    			toSortList.add(_task);
    		}
	    }
	}
	
	//@Author A0112156U
	public String tryExecute() {
		String feedback;
		LinkedList <Task> storageList;
		_storage= Controller.getDBStorage();
		storageList = _storage.load();
		
		if (_task == null) {
			_validity = false;
			return FEEDBACK_INVALID_DETAILS;
		}
		
		sortByDate(storageList);
		feedback = String.format(FEEDBACK_VALID_INPUT, _task.getTaskName());
		_validity=true;
		return feedback;
		
	}
	
	//@Author A0118899E
	@Override
	public String undo() {
		String feedback;
		LinkedList <Task> storageList;
		_storage = Controller.getDBStorage();
		storageList = _storage.load();
		int index = storageList.indexOf(_task);
		Task removedTask = storageList.remove(index);
		if (index == storageList.size()) {
			if (index == 0) {
				// set focus task to change UI's page
				Controller.setFocusTask(null); 
			} else {
				assert (index > 0);
				Controller.setFocusTask(storageList.get(index-1));
			}
		} else {
			assert (index != storageList.size());
			if (storageList.size() == 0) {
				// set focus task to change UI's page
				Controller.setFocusTask(null); 
			} else {
				assert (storageList.size() > 0);
				Controller.setFocusTask(storageList.get(index));
			}
		}
		
		try {
			_storage.store(storageList);
		} catch (IOException e) {
			Log.error("Storage I/O problem",e);
			feedback = FEEDBACK_INVALID_STORAGE;
			return feedback;
		}
		if (removedTask != null) {
			feedback = String.format(FEEDBACK_VALID_UNDO, _task.getTaskName());
		} 
		else {
			assert (removedTask == null);
			feedback = String.format(FEEDBACK_INVALID_UNDO, _task.getTaskName());
		}
		
		return feedback;
	}
	
	//@Author A0118899E
	@Override
	public boolean isUndoable(){
		assert isUndoable();
		return _validity;
	}

}
