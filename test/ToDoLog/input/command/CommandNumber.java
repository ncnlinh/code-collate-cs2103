package command;

import java.util.LinkedList;

import logger.Log;
import common.Task;
import controller.Controller;

/**
 *  CommandNumber class is called by the controller in order to execute the Number command
 *  @param _index - index of the _task of which the details are required.
 *	@param _displayList - stores the list for every specific view
 *	@param _task - stores the task of whom the details are required
 */

public class CommandNumber implements Command {
	private int _index;
	private Task _task;
	private LinkedList<Task> _displayList;
	
	private static final int CORRECTION_INDEX = 1;
	
	private static final String FEEDBACK_VALID_TASK = "This is a valid task";
	private static final String FEEDBACK_INVALID_TASK = "This is not a valid index";
	private static final String FEEDBACK_INVALID_UNDO = "Cannot be undone!";
	
	//@Author A0118899E
	public CommandNumber(int index) {
		_index = index - CORRECTION_INDEX;
	}

	//@Author A0118899E
	@Override
	public String execute() {
		_displayList = Controller.getScheduleList();
		try {
			_task = _displayList.get(_index);
			Controller.setFocusTask(_task);
			return FEEDBACK_VALID_TASK;
		} catch (IndexOutOfBoundsException ioobe) {
			Log.info("Task index is out of bounds");
			_task = null ;
			return FEEDBACK_INVALID_TASK;
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
		return false ;
	}

	//@Author A0112156U
	public String tryExecute() {
		_displayList = Controller.getScheduleList();
		try {
			_task = _displayList.get(_index);
			Controller.setFocusTask(_task);
			return FEEDBACK_VALID_TASK;
		} catch (IndexOutOfBoundsException ioobe) {
			Log.info("Task index is out of bounds");
			_task = null ;
			return FEEDBACK_INVALID_TASK;
		}
	}

	//@Author A0118899E
	public Task getTask() {
		return _task;
	}

}
