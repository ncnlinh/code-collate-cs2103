package gui;

import javax.swing.JOptionPane;

import common.Task;
import common.TaskType;

//@Author A0118899E
public class ShowMessageDialog {
	private static Task _task;
	
	private static final String FRAME_HEADER = "Reminder";
	private static final String REMINDER_MESSAGE_DEADLINE = "You have %1$s due tomorrow at %2$s";
	private static final String REMINDER_MESSAGE_TIMED = "You have %1$s from%2$s at %3$s to%4$s at %5$s"; 
	private static final String REMINDER_NO_MESSAGE = "Sorry for the wrong reminder";
	
	public ShowMessageDialog( Task task) {
		_task = task;
	}
	
	public void execute() {
		String message = generateMessage();
	    JOptionPane.showMessageDialog( null, message, FRAME_HEADER, JOptionPane.WARNING_MESSAGE); 
	  }
	
	public String generateMessage() {
		String name = _task.getTaskName();
		String message;
		if (_task.getTaskType() == TaskType.DEADLINE) {
			String dueDate = _task.getEndTimeStr();
			message = String.format(REMINDER_MESSAGE_DEADLINE, name, dueDate);
		} else if (_task.getTaskType() == TaskType.TIMED) {
			String startTime = _task.getStartTimeStr();
			String endTime = _task.getEndTimeStr();
			int endDate = _task.getEndDate();
			int endMonth =_task.getEndMonth();
			int endYear = _task.getEndYear();
			String endDay = " " + endDate + "/" + endMonth + "/" + endYear;
			int startDate = _task.getStartDate();
			int startMonth = _task.getStartMonth();
			int startYear = _task.getStartYear();
			String startDay =" " + startDate + "/" + startMonth + "/" + startYear;
			message = String.format(REMINDER_MESSAGE_TIMED, name, startDay, startTime, endDay, endTime);
		} else {
			message = REMINDER_NO_MESSAGE;
		}
		return message;
	}
}
