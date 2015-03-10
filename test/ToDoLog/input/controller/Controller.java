package controller;

import history.History;
import history.InputHistory;

import java.util.LinkedList;

import logger.Log;
import command.Command;
import command.CommandAdd;
import command.CommandDelete;
import command.CommandDeleteAll;
import command.CommandEdit;
//import command.CommandHelp;
import command.CommandMarkAsDone;
import command.CommandNumber;
//import command.CommandRedo;
import command.CommandSearch;
//import command.CommandUndo;
import command.CommandView;
import common.Task;
import common.TaskType;
import parser.CommandParser;
import storage.DBStorage;

// remember to write unit test as you code

public class Controller {

	private static DBStorage _dbStorage;

	private static History _history;
	private static InputHistory _input;
//	private static String _textDisplay;
	private static LinkedList<Task> _displayList;
	private static Task _focusTask;
	private static String _feedback;
	private static String _viewOrSearchType;
	private static CommandView _currentViewMode;
	private static CommandSearch _currentSearchCriterion;

	private static Command latestCommand;
	private static final String FEEDBACK_START = "To start, type a command HELP \n"
			+ "Or enter a command: add, delete, edit, done, view, search.\n";
	
	//@Author A0112156U
	public static void setStorage(DBStorage DBstorage) {
		_dbStorage = DBstorage;
	}
	
	public static DBStorage getDBStorage() {
		return _dbStorage;
	}
	public static LinkedList<Task> getAllTasksInViewList() {
		LinkedList<Task> displayTasks = new LinkedList<Task>();
		for (Task task : _displayList) {
			if (task.getTaskType() != TaskType.FLOATING) {
				displayTasks.add(task);
			}
		}
		return displayTasks;
	}
	public static LinkedList<Task> getScheduleList() {
		return _displayList;
	}
	public static LinkedList<Task> getFlexibleList() {
		LinkedList<Task> floatingTasks = new LinkedList<Task>();
		for (Task task : _dbStorage.load()) {
			if (task.getTaskType() == TaskType.FLOATING) {
				floatingTasks.add(task);
			}
		}
		return floatingTasks;
	}
	public static Task getFocusTask() {
		return _focusTask;
	}
	public static void setFocusTask(Task focusTask) {
		_focusTask = focusTask;
	}
	public static void setHistory(History history) {
		_history = history;
	}
	public static History getHistory() {
		return _history;
	}
	
	public static void setInputHistory (InputHistory input) {
		_input = input;
	}
	public static InputHistory getInput() {
		return _input;
	}
	
	public static void acceptUserCommand(String userCommand) {
		try {
			_input.addInput(userCommand);
			Command command = CommandParser.createCommand(userCommand);
			setLatestCommand(command);
			_feedback = command.execute();
			if (command instanceof CommandSearch) {
				setSearchResult(command);
			} else if (command instanceof CommandView) {
				setNewViewMode((CommandView) command);
			} else {
				setViewMode(_currentViewMode);
			}
			if (command.isUndoable()){
				_history.addCommand(command);
				Log.info("Command created and added to history storage");
			}
			Log.info("Command executed and feedback return");
		} catch (Exception e) {
			_feedback = e.getMessage();
			setLatestCommand(null);
			Log.info("Command not created and executed with "+ e.getClass().getName() + ":"+e.getMessage());
		}
	}

	private static void setViewMode(CommandView command) {
		command.execute();
		_displayList =  command.getReturnList();
		setViewOrSearchType(command.getViewType()+" events and deadlines:");
	}

	private static void setNewViewMode(CommandView command) {
		setFocusTask(null);
		if (command.getViewType() != null) {
			_currentViewMode =  command;
			setViewMode(command);
		}
	}

	private static void setSearchResult(Command command) {
		_displayList = ((CommandSearch) command).getReturnList();
		_currentSearchCriterion = (CommandSearch) command;
		_displayList = _currentSearchCriterion.getReturnList();
		setViewOrSearchType("Search results for \""+((CommandSearch) command).getSearchKey()+"\"");
	}
	
	public static LinkedList<String> getCommandEntryHelperDetailsFromInput(String userCurrentInput) {
		try {			
			Command command = CommandParser.createCommand(userCurrentInput);
			if (command instanceof CommandAdd) {
				return ControllerFeedbackHelper.getHelperTextsForCmdAdd(command);
			} else if (command instanceof CommandEdit) {
				return ControllerFeedbackHelper.getHelperTextsForCmdEdit(command);
			} else if (command instanceof CommandDelete) {
				return ControllerFeedbackHelper.getHelperTextsForCmdDelete(command);
			} else if (command instanceof CommandDeleteAll) {
				return ControllerFeedbackHelper.getHelperTextsForCmdDeleteAll(command);
			} else if (command instanceof CommandDeleteAll) {
				return ControllerFeedbackHelper.getHelperTextsForCmdDeleteDone(command);
			} else if (command instanceof CommandMarkAsDone) {
				return ControllerFeedbackHelper.getHelperTextsForCmdMarkAsDone(command);
			} else if (command instanceof CommandNumber) {
				return ControllerFeedbackHelper.getHelperTextsForCmdNumber(command);
			} else {
				return new LinkedList<String>();
			}
		} catch (Exception e) {
			Log.debug("Command is not complete");
			return new LinkedList<String>();
		}
	}


	public static void init() {
		_dbStorage = new DBStorage();
		//_textDisplay = createNewDisplay();
		_history = new History();
		_input = new InputHistory();
		_feedback = FEEDBACK_START;
		_currentViewMode = new CommandView("this week");
		setViewMode(_currentViewMode);
		Log.info("Initialize controller");
	}
	public static void init(String fileName) {
		_dbStorage = new DBStorage(fileName);
		_history = new History();
		_input = new InputHistory();
		_feedback = FEEDBACK_START;
		_currentViewMode = new CommandView("this week");
		setViewMode(_currentViewMode);
	}


	public static String getFeedback() {
		return _feedback;
	}

	public static void resetDisplayListToAll() {
		_displayList = _dbStorage.load();
		
	}
	
	public static int getNumberOfScheduledTasks() {
		int numberOfScheduledTasks = 0;
		for (Task task : _displayList) {
			if (task.getTaskType() != TaskType.FLOATING) {
				assert (task.getTaskType() == TaskType.TIMED || task.getTaskType() == TaskType.DEADLINE);
				numberOfScheduledTasks++;
			}
		}
		return numberOfScheduledTasks;
	}

	public static String getViewOrSearchType() {
		return _viewOrSearchType;
	}
	private static void setViewOrSearchType(String text) {
		_viewOrSearchType = text;
	}

	public static Command getLatestCommand() {
		return latestCommand;
	}

	public static void setLatestCommand(Command latestCommand) {
		Controller.latestCommand = latestCommand;
	}
}
