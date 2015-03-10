package controller;

import java.util.LinkedList;

import org.joda.time.format.DateTimeFormat;

import command.Command;
import command.CommandAdd;
import command.CommandDelete;
import command.CommandDeleteAll;
import command.CommandDeleteDone;
import command.CommandEdit;
import command.CommandMarkAsDone;
import command.CommandNumber;
import common.Task;
import common.TaskType;

public class ControllerFeedbackHelper {

	//@Author A0112156U
	public static LinkedList<String> getHelperTextsForCmdNumber(Command command) {
		((CommandNumber) command).tryExecute();
		Task task = ((CommandNumber) command).getTask();
		LinkedList<String> details = ControllerFeedbackHelper.createHelperTexts("number",task);
		return details;
	}

	public static LinkedList<String> getHelperTextsForCmdMarkAsDone(
			Command command) {
		((CommandMarkAsDone) command).tryExecute();
		Task task = ((CommandMarkAsDone) command).getMarkedTask();
		LinkedList<String> details = ControllerFeedbackHelper.createHelperTexts("done",task);
		return details;
	}

	public static LinkedList<String> getHelperTextsForCmdDeleteAll(
			Command command) {
		((CommandDeleteAll) command).tryExecute();
		LinkedList<String> details = ControllerFeedbackHelper.createHelperTexts("deleteall",null);
		return details;
	}

	public static LinkedList<String> getHelperTextsForCmdDeleteDone(
			Command command) {
		((CommandDeleteDone) command).tryExecute();
		LinkedList<String> details = ControllerFeedbackHelper.createHelperTexts("deletedone",null);
		return details;
	}	
	
	public static LinkedList<String> getHelperTextsForCmdDelete(Command command) {
		((CommandDelete) command).tryExecute();
		Task task = ((CommandDelete) command).getDeletedTask();
		LinkedList<String> details = ControllerFeedbackHelper.createHelperTexts("delete",task);
		return details;
	}

	public static LinkedList<String> getHelperTextsForCmdEdit(Command command) {
		((CommandEdit) command).tryExecute();
		Task task = ((CommandEdit) command).getCurrentTask();
		LinkedList<String> currentTaskDetails = ControllerFeedbackHelper.createHelperTexts("edit",task);
		task = ((CommandEdit) command).getEditedTask();
		LinkedList<String> newTaskDetails = ControllerFeedbackHelper.createHelperTexts("edit",task);
		LinkedList<String> details = new LinkedList<String>(currentTaskDetails);
		details.addAll(newTaskDetails);
		return details;
	}

	public static LinkedList<String> getHelperTextsForCmdAdd(Command command) {
		Task task = ((CommandAdd) command).getAddedTask();
		LinkedList<String> details = ControllerFeedbackHelper.createHelperTexts("add",task);
		return details;
	}

	
	private static LinkedList<String> createHelperTexts(String commandType, Task task) {
		LinkedList<String> helperTexts = new LinkedList<String>();
		helperTexts.add(commandType);
		if (task == null) { 
			return helperTexts; 
		}
		switch (task.getTaskType()) {
			case TIMED:
				helperTexts.addAll(createTimedTaskHelper(task));
				break;
			case DEADLINE:
				helperTexts.addAll(createDeadlineHelper(task));
				break;
			default:
				assert (task.getTaskType () == TaskType.FLOATING);
				helperTexts.addAll(createFloatingTaskHelper(task));
				break;
		}
		return helperTexts;
	}

	private static LinkedList<String> createFloatingTaskHelper(Task task) {
		LinkedList<String> helperTexts = new LinkedList<String>();
		helperTexts.add(task.getTaskType().toString());
		helperTexts.add(task.getTaskName().toString());
		helperTexts.add(task.getTaskPerson().toString());
		helperTexts.add(task.getTaskVenue().toString());
		return helperTexts;
	}

	private static LinkedList<String> createDeadlineHelper(Task task) {
		LinkedList<String> helperTexts = new LinkedList<String>();
		helperTexts.add(task.getTaskType().toString());
		helperTexts.add(task.getTaskName().toString());
		helperTexts.add((DateTimeFormat.forPattern("dd/MM/yyyy")).print(task.getEnd()));
		helperTexts.add((DateTimeFormat.forPattern("HH:mm")).print(task.getEnd()));
		helperTexts.add(task.getTaskPerson().toString());
		helperTexts.add(task.getTaskVenue().toString());
		return helperTexts;
	}

	private static LinkedList<String> createTimedTaskHelper(Task task) {
		LinkedList<String> helperTexts = new LinkedList<String>();
		helperTexts.add(task.getTaskType().toString());
		helperTexts.add(task.getTaskName().toString());
		helperTexts.add((DateTimeFormat.forPattern("dd/MM/yyyy")).print(task.getStart()));
		helperTexts.add((DateTimeFormat.forPattern("HH:mm")).print(task.getStart()));
		helperTexts.add((DateTimeFormat.forPattern("dd/MM/yyyy")).print(task.getEnd()));
		helperTexts.add((DateTimeFormat.forPattern("HH:mm")).print(task.getEnd()));
		helperTexts.add(task.getTaskPerson().toString());
		helperTexts.add(task.getTaskVenue().toString());
		return helperTexts;
	}

	

}
