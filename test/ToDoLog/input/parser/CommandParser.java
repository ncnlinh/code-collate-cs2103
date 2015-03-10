package parser;

import history.History;
import logger.Log;
import command.Command;
import command.CommandAdd;
import command.CommandDelete;
import command.CommandDeleteAll;
import command.CommandDeleteDone;
import command.CommandEdit;
import command.CommandHelp;
import command.CommandLoad;
import command.CommandMarkAsDone;
import command.CommandNumber;
import command.CommandRedo;
import command.CommandSearch;
import command.CommandUndo;
import command.CommandView;
import common.Task;
import controller.Controller;

public class CommandParser {
	private static final String FEEDBACK_TYPE = "Type in a command: add, delete, edit, done.";
	//private static final String HELP_TEXT_ADD = "To add, enter:\n - add \"[task name]\" (from [date] @ [time] to "
	//		+ "[date] @ [time]).\n - add \"[task name]\" by [date] @ [time] if you want to create a\ndeadline.";
	private static final String HELP_TEXT_DELETE = "To delete, enter:\n - delete [task number].";
	//private static final String HELP_TEXT_DONE = "To mark/unmark a task as done, enter:\n - done [task number].";
	//private static final String HELP_TEXT_EDIT = "To edit task name, enter:\n - edit [task number] \"[new name]\"";

	//@author A0112156U
	public static Command createCommand(String userCommand) throws Exception{
		userCommand = userCommand.trim();
		String firstWord = getFirstWord(userCommand);
		boolean isNumber = false;
		int taskNumber = 0;
		try {
			taskNumber = Integer.parseInt(firstWord);
			isNumber = true;
		} catch (NumberFormatException nfe) {
			Log.trace("This will not be a CommandNumber");
			isNumber = false;
		}
		if (!isNumber) {
			if (firstWord.equalsIgnoreCase("add")) {
				return parseCommandAdd(userCommand);
			} else if (firstWord.equalsIgnoreCase("delete")) {
				return parseCommandDelete(userCommand);
			} else if (firstWord.equalsIgnoreCase("done")) {
				return parseCommandMarkAsDone(userCommand);
			} else if (firstWord.equalsIgnoreCase("edit")) {
				return parseCommandEdit(userCommand);
			} else if (firstWord.equalsIgnoreCase("search")) {
				return parseCommandSearch(userCommand);
			} else if (firstWord.equalsIgnoreCase("view")) {
				return parseCommandView(userCommand);
			} else if (firstWord.equalsIgnoreCase("undo")) {
				return parseCommandUndo();
			} else if (firstWord.equalsIgnoreCase("redo")) {
				return parseCommandRedo();
			} else if (firstWord.equalsIgnoreCase("load")) {
				return parseCommandLoad(userCommand);
			} else if (firstWord.equalsIgnoreCase("help")) {
				return parseCommandHelp();
			} else {
				throw new Exception("Invalid command.\n"+FEEDBACK_TYPE);
			}
		} else {
			return parseCommandNumber(taskNumber);
		}
	}
	
	//@author A0112156U
	private static Command parseCommandNumber(int taskNumber) {
		CommandNumber command = new CommandNumber(taskNumber);
		return command;
	}

	//@author A0112156U
	private static Command parseCommandHelp() {
		CommandHelp command = new CommandHelp();
		return command;
	}
	
	//@author A0112156U
	private static Command parseCommandLoad(String userCommand)
			throws Exception {
		String restOfTheString = getTheRestOfTheString(userCommand);
		CommandLoad command = new CommandLoad(restOfTheString);
		return command;
	}

	//@author A0112156U
	private static Command parseCommandRedo() throws Exception {
		History history = Controller.getHistory();
		Command toBeUndone = history.getForwards();
		CommandRedo command = new CommandRedo(toBeUndone);
		return command;
	}

	//@author A0112156U
	private static Command parseCommandUndo() throws Exception {
		History history = Controller.getHistory();
		Command toBeUndone = history.getBackwards();
		CommandUndo command = new CommandUndo(toBeUndone);
		return command;
	}
	
	//@author A0112156U
	private static Command parseCommandView(String userCommand) throws Exception {
		String restOfTheString = getTheRestOfTheString(userCommand);
		if (restOfTheString == null) {
			return new CommandView("this week");
		}
		CommandView command = new CommandView(restOfTheString);
		return command;
	}

	//@author A0112156U
	private static Command parseCommandSearch(String userCommand) throws Exception {
		String restOfTheString = getTheRestOfTheString(userCommand);
		CommandSearch command = new CommandSearch(restOfTheString);
		return command;
	}

	//@author A0112156U
	private static Command parseCommandEdit(String userCommand) throws Exception {
		String restOfTheString = getTheRestOfTheString(userCommand);
		if (restOfTheString == null) {
			return new CommandEdit();
		}
		restOfTheString = restOfTheString.trim();
		int index = Integer.valueOf(getFirstWord(restOfTheString));
		restOfTheString = getTheRestOfTheString(restOfTheString);
		if (restOfTheString == null) {
			return new CommandEdit(index);
		}
		String editType = getFirstWord(restOfTheString);
		restOfTheString = getTheRestOfTheString(restOfTheString);
		if (restOfTheString == null) {
			return new CommandEdit(index,editType);
		}
		assert (restOfTheString != null);
		if (editType.equalsIgnoreCase("start") || editType.equalsIgnoreCase("end")) {
			editType = editType.concat(" ").concat(getFirstWord(restOfTheString));
			restOfTheString = getTheRestOfTheString(restOfTheString);
		} else if (editType.equalsIgnoreCase("task")) {
			editType = getFirstWord(restOfTheString);
			restOfTheString = getTheRestOfTheString(restOfTheString);
		}
		CommandEdit command = new CommandEdit(index, restOfTheString, editType);
		return command;
	}

	//@author A0112156U
	private static Command parseCommandMarkAsDone(String userCommand)
			throws Exception {
		String restOfTheString = getTheRestOfTheString(userCommand);
		if (restOfTheString == null) {
			return new CommandMarkAsDone();
		}
		assert (restOfTheString != null);
		restOfTheString = restOfTheString.trim();
		int index = Integer.valueOf(restOfTheString);
		CommandMarkAsDone command = new CommandMarkAsDone(index);
		return command;
	}
	
	//@author A0112156U
	private static Command parseCommandDelete(String userCommand)
			throws Exception {
		String restOfTheString = getTheRestOfTheString(userCommand);
		if (restOfTheString == null) {
			return new CommandDelete();
		}
		assert (restOfTheString != null);
		restOfTheString = restOfTheString.trim();
		if (isInteger(restOfTheString)) {
			int index = Integer.valueOf(restOfTheString);
			CommandDelete command = new CommandDelete(index);
			return command; 
		} else {
			if (restOfTheString.equalsIgnoreCase("all")) {
				CommandDeleteAll command = new CommandDeleteAll();
				return command;
			}else if (restOfTheString.equalsIgnoreCase("done")){
				CommandDeleteDone command= new CommandDeleteDone();
				return command;
			}
			else {
				throw new Exception(HELP_TEXT_DELETE);
			}
		}
	}

	//@author A0112156U
	private static Command parseCommandAdd(String userCommand) throws Exception {
		Task task = createTask(userCommand);
		CommandAdd command = new CommandAdd(task);
		return command;
	}

	//@author A0112156U
	public static Task createTask(String userInput) throws Exception{
		String restOfTheString = getTheRestOfTheString(userInput);
		if (restOfTheString == null) {
			return null;
		}
		assert (restOfTheString != null);
		Task task = new Task(restOfTheString);
		return task;
	}

	//@author A0111608R
	public static String getTheRestOfTheString(String userCommand) throws Exception {
		try {
			String[] result = userCommand.split(" ", 2);
			String restOfTheWord = result[1];
			return restOfTheWord;
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			Log.trace("Rest Of The String is null",aioobe);
			return null;
		}
	}
	
	//@author A0111608R
	public static String getFirstWord(String userCommand) {
		String[] result = userCommand.split(" ", 2);
		String firstWord = result[0];
		return firstWord;
	}
	
	//@author A0112156U
	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			Log.trace(s+" is not a Integer",e);
			return false; 
		}
		return true;
	}
}
