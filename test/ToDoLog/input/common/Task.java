package common;

//import java.util.LinkedList;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import parser.TaskParser;

public class Task {

	//Key Variables
	
	private static final int NOT_DEADLINE = Integer.MIN_VALUE;
	private String _taskName;
	private TaskType _taskType;
	private String _taskStartDay;
	private String _taskEndDay;
	private String _taskPerson;
	private String _taskVenue;
	private DateTime _taskStart;
	private DateTime _taskEnd;
	
	private boolean _taskStatus;
	
	//@Author A0112156U
	public Task(String parameter) throws Exception{
		_taskName = TaskParser.parseTaskName(parameter);
		_taskType = TaskParser.parseTaskType(parameter);
		_taskPerson = TaskParser.parseTaskPerson(parameter);
		_taskVenue = TaskParser.parseTaskVenue(parameter);
		_taskStart = TaskParser.parseTaskStart(parameter);
		_taskEnd = TaskParser.parseTaskEnd(_taskStart,parameter);
	}

	public Task(TaskType floating, String name, boolean status, String person, String venue) {
		_taskType = floating;
		_taskName = name;
		_taskStatus = status;
		_taskPerson = person;
		_taskVenue = venue;
	}

	public Task(TaskType deadline, String name, DateTime end, boolean status, String person, String venue) {
		_taskType = deadline;
		_taskName = name;
		_taskEnd = end;
		_taskStatus = status;
		_taskPerson = person;
		_taskVenue = venue;
	}

	public Task(TaskType timed, String name, DateTime start, DateTime end,
			boolean status, String person, String venue) {
		_taskType = timed;
		_taskName = name;
		_taskStart = start;
		_taskEnd = end;
		_taskStatus = status;
		_taskPerson = person;
		_taskVenue = venue;
	}

	public void setTaskName(String name) {
		_taskName = name;
	}
	
	public String getTaskName() {
		return _taskName;
	}
	
	public TaskType getTaskType() {
		return _taskType;
	}
	
	public String getStartDay() {
		return _taskStartDay;
	}
	
	public String getEndDay() {
		return _taskEndDay;
	}

	public int getStartTime() {
		return _taskStart.hourOfDay().get()*100
				+ _taskStart.minuteOfHour().get();
	}

	public int getEndTime() {
		return _taskEnd.hourOfDay().get()*100
				+ _taskEnd.minuteOfHour().get();
	}
	
	public int getStartDate() {
		return _taskStart.dayOfMonth().get();
	}
	
	public int getStartMonth() {
		return _taskStart.monthOfYear().get();
	}
	
	public int getStartYear() {
		return _taskStart.year().get();
	}
	
	public int getEndDate() {
		return _taskEnd.dayOfMonth().get();
	}
	
	public int getEndMonth() {
		return _taskEnd.monthOfYear().get();
	}
	
	public int getEndYear() {
		return _taskEnd.year().get();
	}
	
	public String getTaskVenue() {
		return _taskVenue;
	}
	
	public String getTaskPerson() {
		return _taskPerson;
	}

	public boolean getTaskStatus() {
		return _taskStatus;
	}
	public DateTime getStart() {
		//return _taskStart.toString(ISODateTimeFormat.dateTime());
		return _taskStart;
	}
	public DateTime getEnd() {
		//return _taskEnd.toString(ISODateTimeFormat.dateTime());
		return _taskEnd;
	}

	public void toggleTaskStatus() {
		if (_taskStatus) {
			_taskStatus = false;
		} else {
			_taskStatus = true;
		}
		
	}

	public void setStartDate(String editInfo) throws Exception{
		int year = TaskParser.parseYear(editInfo);
		int month = TaskParser.parseMonth(editInfo);
		int day = TaskParser.parseDayOfMonth(editInfo);
		int time = getStartTime();
		int hour = time/100;
		int min = time%100;
		_taskStart = new DateTime(year,month,day,hour,min);
	}
	
	public void setEndDate(String editInfo) throws Exception {
		int year = TaskParser.parseYear(editInfo);
		int month = TaskParser.parseMonth(editInfo);
		int day = TaskParser.parseDayOfMonth(editInfo);
		int time = getEndTime();
		int hour = time/100;
		int min = time%100;
		_taskEnd = new DateTime(year,month,day,hour,min);
	}
	
	public void setStartTime(String editInfo) throws Exception {
		int year = getStartYear();
		int month = getStartMonth();
		int day = getStartDate();
		int time = Integer.parseInt(editInfo);
		int hour = time/100;
		int min = time%100;
		_taskStart = new DateTime(year,month,day,hour,min);
	}
	
	public void setEndTime(String editInfo) throws Exception {
		int year = getEndYear();
		int month = getEndMonth();
		int day = getEndDate();
		int time =  Integer.parseInt(editInfo);
		int hour = time/100;
		int min = time%100;
		_taskEnd = new DateTime(year,month,day,hour,min);
	}
	
	public void setVenue(String _toBeEdited){
		_taskVenue = _toBeEdited;
	}
	
	public void setPerson(String _toBeEdited){
		_taskPerson = _toBeEdited;
	}

	// for sorting in CommandAdd
	public DateTime getEndDateTime() {
		return _taskEnd;
	}
	
	public DateTime getStartDateTime() {
		return _taskStart;
	}

	public String getStartTimeStr() {
		LocalTime time = new LocalTime(getStartTime()/100,getStartTime()%100);
		return (DateTimeFormat.forPattern("HH:mm")).print(time);
	}
	public String getEndTimeStr() {
		LocalTime time = new LocalTime(getEndTime()/100,getEndTime()%100);
		return (DateTimeFormat.forPattern("HH:mm")).print(time);
	}


	public Task copy() {
		// TODO Auto-generated method stub
		return new Task(_taskType, _taskName, new DateTime(_taskStart), new DateTime(_taskEnd),
				_taskStatus, _taskPerson, _taskVenue); 
	}

	public int duePeriod() {
		if (getTaskType() == TaskType.DEADLINE) { 
			if (getEnd().isBeforeNow()) {
				return -1;
			} else {
				return (Days.daysBetween(LocalDate.now(),new LocalDate(getEnd()))).getDays();
			}
		} else if (getTaskType()== TaskType.TIMED){
			if (getEnd().isBeforeNow()) {
				return -1;
			} else {
				return (Days.daysBetween(LocalDate.now(),new LocalDate(getStart()))).getDays();
			}
		}
	assert (getTaskType()== TaskType.FLOATING);
	return NOT_DEADLINE;
	}
	
	
}
