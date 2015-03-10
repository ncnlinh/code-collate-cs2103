package command;

import java.util.LinkedList;

import org.joda.time.DateTime;

import common.Task;
import common.TaskType;
import controller.Controller;
import parser.TaskParser;
import storage.DBStorage;

/**
 *  CommandView class is called by the controller in order to execute the view command
 *  @param _toView - stores the user command of what is to be viewed
 *	@param _viewType - stores the command for feedbacks
 *	@param _storage - stores the DBstorage to get the storageList
 *	@param _returnList - stores the list which has all the tasks that contain the search key
 *  @param monthInIntegers - stores the month in numbers. 
 */
public class CommandView implements Command {

	private String _toView;
	private String _viewType;
	private DBStorage _storage;
	private static LinkedList<Task> _returnList;
	private int monthInIntegers;
	
	private static final int START_MIN_HOUR = 0;
	private static final int END_TIME_MINUTE = 59;
	private static final int END_TIME_HOUR = 23;
	private static final int THIS_WEEK_END = 7;
	private static final int NEXT_WEEK_START = 8;
	private static final int NEXT_WEEK_END = 15;
	
	private static final String POSSESSIVE_CASE = "'s";
	
	private static final String FEEDBACK_VALID = "Displaying tasks for %1$s";
	private static final String FEEDBACK_INVALID_COMMAND = "invalid command";
	private static final String FEEDBACK_INVALID_UNDO = "View cannot be undone";
	
	private static String DAY_KEYWORD_TODAY = "Today";
	private static String DAY_KEYWORD_THIS_DAY = "This day ";
	private static String DAY_KEYWORD_TOMORROW = "Tomorrow";
	private static String DAY_KEYWORD_TMR = "tmr";
	private static String DAY_KEYWORD_NEXT_DAY="Next day";
	private static String DAY_KEYWORD_THIS_WEEK="This week";
	private static String DAY_KEYWORD_NEXT_WEEK="Next week";
	
	private static final String KEYWORD_PAST = "Past";
	private static final String KEYWORD_PENDING = "pending";
	private static final String KEYWORD_OVERDUE = "overdue";
	private static final String KEYWORD_ALL = "all";
	
	private static final String KEYWORD_SUNDAY2 = "sun";
	private static final String KEYWORD_SUNDAY1 = "sunday";
	private static final String KEYWORD_SATURDAY2 = "sat";
	private static final String KEYWORD_SATURDAY1 = "saturday";
	private static final String KEYWORD_FRIDAY2 = "fri";
	private static final String KEYWORD_FRIDAY1 = "Friday";
	private static final String KEYWORD_THURSDAY2 = "thurs";
	private static final String KEYWORD_THURSDAY1 = "thursday";
	private static final String KEYWORD_WEDNESDAY2 = "wed";
	private static final String KEYWORD_WEDNESDAY1 = "wednesday";
	private static final String KEYWORD_TUESDAY2 = "tues";
	private static final String KEYWORD_TUESDAY1 = "tuesday";
	private static final String KEYWORD_MONDAY2 = "mon";
	private static final String KEYWORD_MONDAY1 = "monday";
	
	private static final String KEYWORD_DECEMBER2 = "dec";
	private static final String KEYWORD_DECEMBER1 = "december";
	private static final String KEYWORD_NOVEMBER2 = "nov";
	private static final String KEYWORD_NOVEMBER1 = "november";
	private static final String KEYWORD_OCTOBER2 = "oct";
	private static final String KEYWORD_OCTOBER1 = "october";
	private static final String KEYWORD_SEPTEMBER2 = "sept";
	private static final String KEYWORD_SEPTEMBER1 = "september";
	private static final String KEYWORD_AUGUST2 = "aug";
	private static final String KEYWORD_AUGUST1 = "august";
	private static final String KEYWORD_JULY2 = "jul";
	private static final String KEYWORD_JULY1 = "july";
	private static final String KEYWORD_JUNE2 = "jun";
	private static final String KEYWORD_JUNE1 = "june";
	private static final String KEYWORD_MAY = "may";
	private static final String KEYWORD_APRIL2 = "apr";
	private static final String KEYWORD_APRIL1 = "april";
	private static final String KEYWORD_MARCH2 = "mar";
	private static final String KEYWORD_MARCH1 = "march";
	private static final String KEYWORD_FEBRUARY2 = "feb";
	private static final String KEYWORD_FEBRUARY1 = "february";
	private static final String KEYWORD_JANUARY2 = "jan";
	private static final String KEYWORD_JANUARY1 = "january";
	
	//@Author A0118899E
	public CommandView(String toView ) {
		_toView = toView;
		_storage = Controller.getDBStorage();
	}
	
	//@Author A0118899E
	public String getViewType() {
		return _viewType;
	}
	
	//@Author A0118899E
	public LinkedList<Task> getReturnList() {
		return _returnList;
	}
	
	//@Author A0118899E
	@Override
	public String execute() {
		String feedback;
		int year,month,day;
		DateTime startDay = new DateTime();
		DateTime endDay = new DateTime();
		year = startDay.getYear();
		month = startDay.getMonthOfYear();
		day = startDay.getDayOfMonth();
		if (_toView.equalsIgnoreCase(DAY_KEYWORD_TODAY) || _toView.equalsIgnoreCase(DAY_KEYWORD_THIS_DAY)) { 
			feedback = viewToday(year, month, day);
		} else if (_toView.equalsIgnoreCase(DAY_KEYWORD_TOMORROW)
				|| _toView.equalsIgnoreCase(DAY_KEYWORD_TMR)
				|| _toView.equalsIgnoreCase(DAY_KEYWORD_NEXT_DAY)) {
			feedback = viewTomorrow(startDay);
		} else if (isWeekDay()!= null) {
			feedback = viewWeekDay(startDay);
		} else if (TaskParser.checkDateFormat(_toView)) {
			feedback = viewDate();
		} else if (_toView.equalsIgnoreCase(DAY_KEYWORD_THIS_WEEK)) {
			feedback = viewThisWeek(startDay);
		} else if (_toView.equalsIgnoreCase(DAY_KEYWORD_NEXT_WEEK)) {
			feedback = viewNextWeek(startDay, endDay);
		} else if (checkMonth() != null ) {
			feedback = viewMonth(startDay);
		} else if (_toView.equalsIgnoreCase(KEYWORD_ALL)) {
			feedback = viewAll();
		} else if (_toView.equalsIgnoreCase(KEYWORD_OVERDUE)||_toView.equalsIgnoreCase(KEYWORD_PENDING)){
			feedback = viewOverdue(startDay);	
		} else {
			feedback=FEEDBACK_INVALID_COMMAND;
		}
		return feedback;
	}

	//@Author A0118899E
	private String viewOverdue(DateTime startDay) {
		String feedback;
		DateTime endDay;
		endDay = startDay.minusMinutes(1);
		viewOverDueTasks(endDay);
		feedback = KEYWORD_OVERDUE;
		_viewType = KEYWORD_PAST;
		feedback = String.format(FEEDBACK_VALID, _toView);
		return feedback;
	}

	//@Author A0118899E
	private String viewAll() {
		String feedback;
		formViewList(new DateTime(0), new DateTime(9999, 12, 31, 23, 59));
		_viewType = KEYWORD_ALL;
		feedback = String.format(FEEDBACK_VALID, _toView);
		return feedback;
	}

	//@Author A0118899E
	private String viewMonth(DateTime startDay) {
		String feedback;
		DateTime endDay;
		DateTime startOfThisMonth=startDay.dayOfMonth().withMinimumValue().withTimeAtStartOfDay();
		int currentMonth = startOfThisMonth.getMonthOfYear();
		if (monthInIntegers >= currentMonth) {
			startDay = startOfThisMonth.plusMonths(monthInIntegers - currentMonth)
					.dayOfMonth().withMinimumValue();
			endDay = startOfThisMonth.plusMonths(monthInIntegers - currentMonth)
					.dayOfMonth().withMaximumValue();	
		} else {
			startDay = startOfThisMonth.minusMonths(currentMonth - monthInIntegers)
					  .dayOfMonth().withMinimumValue();
			endDay = startOfThisMonth.minusMonths(currentMonth - monthInIntegers)
					 .dayOfMonth().withMaximumValue();	
		}
		startDay.withHourOfDay(START_MIN_HOUR);
		startDay.withMinuteOfHour(START_MIN_HOUR);
		endDay.withHourOfDay(END_TIME_HOUR);
		endDay.withMinuteOfHour(END_TIME_MINUTE);

		formViewList(startDay,endDay);
		_viewType = checkMonth() + POSSESSIVE_CASE;
		feedback = String.format(FEEDBACK_VALID, _toView);
		return feedback;
	}

	//@Author A0118899E
	private String viewNextWeek(DateTime startDay, DateTime endDay) {
		String feedback;
		startDay = startDay.plusDays(NEXT_WEEK_START);
		endDay = endDay.plusDays(NEXT_WEEK_END);
		startDay.withHourOfDay(START_MIN_HOUR);
		startDay.withMinuteOfHour(START_MIN_HOUR);
		endDay.withHourOfDay(END_TIME_HOUR);
		endDay.withMinuteOfHour(END_TIME_MINUTE);
		formViewList(startDay,endDay);
		_viewType = DAY_KEYWORD_NEXT_WEEK;
		feedback = String.format(FEEDBACK_VALID, _toView);
		return feedback;
	}

	//@Author A0118899E
	private String viewThisWeek(DateTime startDay) {
		String feedback;
		DateTime endDay;
		endDay = startDay.plusDays(THIS_WEEK_END);
		startDay.withHourOfDay(START_MIN_HOUR);
		startDay.withMinuteOfHour(START_MIN_HOUR);
		endDay.withHourOfDay(END_TIME_HOUR);
		endDay.withMinuteOfHour(END_TIME_MINUTE);
		formViewList(startDay,endDay);
		_viewType = DAY_KEYWORD_THIS_WEEK + POSSESSIVE_CASE;
		feedback = String.format(FEEDBACK_VALID, _toView);
		return feedback;
	}

	//@Author A0118899E
	private String viewDate() {
		String feedback;
		int year;
		int month;
		int day;
		DateTime startDay;
		DateTime endDay;
		int ddmmyy = Integer.parseInt(_toView);
		year = ddmmyy % 100 + 2000;
		month = (ddmmyy / 100) % 100;
		day = (ddmmyy / 10000);
		startDay = new DateTime(year, month, day, START_MIN_HOUR, START_MIN_HOUR);
		endDay = new DateTime(year, month, day, END_TIME_HOUR, END_TIME_MINUTE);
		formViewList(startDay,endDay);
		_viewType = _toView + POSSESSIVE_CASE;
		feedback = String.format(FEEDBACK_VALID, _toView);
		return feedback;
	}
	
	//@Author A0118899E
	private String viewWeekDay(DateTime startDay) {
		String feedback;
		int year;
		int month;
		int day;
		DateTime endDay;
		int currentWeekDay = startDay.getDayOfWeek();
		int givenWeekDay = TaskParser.parseDayOfWeek(_toView);
		if (givenWeekDay >= currentWeekDay) {
			startDay = startDay.plusDays(givenWeekDay - currentWeekDay);	
		} else {
			startDay = startDay.plusDays(givenWeekDay - currentWeekDay + THIS_WEEK_END);
		}
		year = startDay.getYear();
		month = startDay.getMonthOfYear();
		day = startDay.getDayOfMonth();
		startDay = new DateTime(year, month, day, START_MIN_HOUR, START_MIN_HOUR);
		endDay = new DateTime(year, month, day, END_TIME_HOUR, END_TIME_MINUTE);
		formViewList(startDay,endDay);
		_viewType = isWeekDay() + POSSESSIVE_CASE;
		feedback = String.format(FEEDBACK_VALID, _toView);
		return feedback;
	}

	//@Author A0118899E
	private String viewTomorrow(DateTime startDay) {
		String feedback;
		int year;
		int month;
		int day;
		DateTime endDay;
		startDay = startDay.plusDays(1);
		year = startDay.getYear();
		month = startDay.getMonthOfYear();
		day = startDay.getDayOfMonth();
		startDay = new DateTime(year, month, day, START_MIN_HOUR, START_MIN_HOUR);
		endDay = new DateTime(year, month, day, END_TIME_HOUR, END_TIME_MINUTE);
		formViewList(startDay,endDay);
		_viewType = DAY_KEYWORD_TOMORROW + POSSESSIVE_CASE;
		feedback = String.format(FEEDBACK_VALID, _toView);
		return feedback;
	}

	//@Author A0118899E
	private String viewToday(int year, int month, int day) {
		String feedback;
		DateTime startDay;
		DateTime endDay;
		startDay = new DateTime(year, month, day, START_MIN_HOUR, START_MIN_HOUR );
		endDay = new DateTime(year, month, day, END_TIME_HOUR, END_TIME_MINUTE);
		formViewList(startDay, endDay);
		_viewType = DAY_KEYWORD_TODAY + POSSESSIVE_CASE;
		feedback = String.format(FEEDBACK_VALID, _toView);
		return feedback;
	}

	//@Author A0118899E
	public String isWeekDay(){
		if (_toView.equalsIgnoreCase(KEYWORD_MONDAY1) || _toView.equalsIgnoreCase(KEYWORD_MONDAY2)) {
			return KEYWORD_MONDAY1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_TUESDAY1) || _toView.equalsIgnoreCase(KEYWORD_TUESDAY2)) {
			return KEYWORD_TUESDAY1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_WEDNESDAY1) || _toView.equalsIgnoreCase(KEYWORD_WEDNESDAY2)) {
			return KEYWORD_WEDNESDAY1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_THURSDAY1) || _toView.equalsIgnoreCase(KEYWORD_THURSDAY2)) {
			return KEYWORD_THURSDAY1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_FRIDAY1) || _toView.equalsIgnoreCase(KEYWORD_FRIDAY2)) {
			return KEYWORD_FRIDAY1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_SATURDAY1) || _toView.equalsIgnoreCase(KEYWORD_SATURDAY2)) {
			return KEYWORD_SATURDAY1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_SUNDAY1) || _toView.equalsIgnoreCase(KEYWORD_SUNDAY2)) {
			return KEYWORD_SUNDAY1;
		} else {
			return null;
		}
	}
	
	//@Author A0118899E
	public String checkMonth() {
		if (_toView.equalsIgnoreCase(KEYWORD_JANUARY1) || _toView.equalsIgnoreCase(KEYWORD_JANUARY2)) {
			monthInIntegers = 1;
			return KEYWORD_JANUARY1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_FEBRUARY1) || _toView.equalsIgnoreCase(KEYWORD_FEBRUARY2)) {
			monthInIntegers = 2;
			return KEYWORD_FEBRUARY1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_MARCH1) || _toView.equalsIgnoreCase(KEYWORD_MARCH2)) {
			monthInIntegers = 3;
			return KEYWORD_MARCH1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_APRIL1) || _toView.equalsIgnoreCase(KEYWORD_APRIL2)) {
			monthInIntegers = 4;
			return KEYWORD_APRIL1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_MAY)) {
			monthInIntegers = 5;
			return KEYWORD_MAY;
		} else if (_toView.equalsIgnoreCase(KEYWORD_JUNE1) || _toView.equalsIgnoreCase(KEYWORD_JUNE2)){
			monthInIntegers = 6;
			return KEYWORD_JUNE1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_JULY1) || _toView.equalsIgnoreCase(KEYWORD_JULY2)) {
			monthInIntegers = 7;
			return KEYWORD_JULY1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_AUGUST1) || _toView.equalsIgnoreCase(KEYWORD_AUGUST2)) {
			monthInIntegers = 8;
			return KEYWORD_AUGUST1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_SEPTEMBER1) || _toView.equalsIgnoreCase(KEYWORD_SEPTEMBER2)) {
			monthInIntegers = 9;
			return KEYWORD_SEPTEMBER1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_OCTOBER1) || _toView.equalsIgnoreCase(KEYWORD_OCTOBER2)) {
			monthInIntegers = 10;
			return KEYWORD_OCTOBER1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_NOVEMBER1)||_toView.equalsIgnoreCase(KEYWORD_NOVEMBER2)) {
			monthInIntegers = 11;
			return KEYWORD_NOVEMBER1;
		} else if (_toView.equalsIgnoreCase(KEYWORD_DECEMBER1)||_toView.equalsIgnoreCase(KEYWORD_DECEMBER2)) {
			monthInIntegers = 12;
			return KEYWORD_DECEMBER1;
		} else {
			return null ;
		}
	}
	
	//@Author A0118899E
	public void formViewList(DateTime startDay, DateTime endDay){
		
		_storage = Controller.getDBStorage();
		LinkedList<Task> viewList=new LinkedList<Task>();

		for (int i = 0; i < _storage.load().size(); i++) {
			if (_storage.load().get(i).getTaskType() == TaskType.TIMED) {
				
				if (((_storage.load().get(i).getStart().isAfter(startDay))
						|| (_storage.load().get(i).getStart().isEqual(startDay)))
						&& ((_storage.load().get(i).getStart().isBefore(endDay))
							|| (_storage.load().get(i).getStart().isEqual(endDay)))){
					viewList.add(_storage.load().get(i));
				} else if (((_storage.load().get(i).getEnd().isAfter(startDay))
						|| (_storage.load().get(i).getEnd().isEqual(startDay)))
						&& ((_storage.load().get(i).getEnd().isBefore(endDay))
								|| (_storage.load().get(i).getEnd().isEqual(endDay)))){
					viewList.add(_storage.load().get(i));	
				}
			} else if (_storage.load().get(i).getTaskType() == TaskType.DEADLINE){
				if (_storage.load().get(i).getEnd()!=null){
					if (((_storage.load().get(i).getEnd().isAfter(startDay))
							|| (_storage.load().get(i).getEnd().isEqual(startDay)))
							&& ((_storage.load().get(i).getEnd().isBefore(endDay))
									|| (_storage.load().get(i).getEnd().isEqual(endDay)))){
						viewList.add(_storage.load().get(i));	
					}
				}
			}	
		}
		setReturnList(viewList);	
	}
	
	//@Author A0118899E
	public void viewOverDueTasks(DateTime endDay){
		LinkedList <Task> viewList = new LinkedList<Task>();
		for(int i = 0; i < _storage.load().size(); i++ ){
			if (_storage.load().get(i).getTaskType() == TaskType.FLOATING) {
				break;
			}
			if (((_storage.load().get(i).getEnd().isBefore(endDay))
					||(_storage.load().get(i).getEnd().isEqual(endDay)))
					&&(_storage.load().get(i).getTaskStatus() == false)) {
				viewList.add(_storage.load().get(i));
			}
		}
		setReturnList(viewList);
	}

	//@Author A0118899E
	private void setReturnList(LinkedList <Task> list) {
		LinkedList <Task> returnList = new LinkedList <Task>(list);
		for (Task task : _storage.load()) {
			if (task.getTaskType() == TaskType.FLOATING) {
				returnList.add(task);
			}
		}
		_returnList = returnList;
	}

	//@Author A0118899E
	@Override
	public String undo() {
		return FEEDBACK_INVALID_UNDO;
	}

	//@Author A0118899E
	@Override
	public boolean isUndoable() {
		return false;
	}
}