package parser;

import logger.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

//import command.Command;
//import command.CommandAdd;
//import command.CommandDelete;
//import command.CommandDeleteAll;
//import command.CommandDeleteDone;
//import command.CommandEdit;
//import command.CommandHelp;
//import command.CommandLoad;
//import command.CommandMarkAsDone;
//import command.CommandNumber;
//import command.CommandRedo;
//import command.CommandSearch;
//import command.CommandUndo;
//import command.CommandView;
//import common.Task;
import common.TaskType;
//import controller.Controller;
//import storage.History;

public class TaskParser {

	//private static final String INVALID_MESSAGE = "Invalid Input!";

	//MISC
	private static final String EMPTY_STRING = "";
	private static final String SINGLE_SPACE = " ";
	private static final String QUOTATION_MARK = "'";

	//KEYWORDS
	private static final String KEYWORD_DAY_STARTING = "from";
	private static final String KEYWORD_DAY_STARTING_2 = "on";
	private static final String KEYWORD_DAY_ENDING = "to";
	private static final String KEYWORD_DEADLINE = "by";
	private static final String KEYWORD_RECURRING = "every";
	private static final String KEYWORD_WITH = "with";
	private static final String KEYWORD_AT = "at";
	private static final String KEYWORD_IN = "in";

	//DAY KEYWORDS
	private static final String DAY_KEYWORD_TODAY = "Today";
	private static final String DAY_KEYWORD_TDY = "Tdy";
	private static final String DAY_KEYWORD_TOMORROW = "Tomorrow";
	private static final String DAY_KEYWORD_TMR = "tmr";
	private static final String DAY_KEYWORD_MONDAY = "Monday";
	private static final String DAY_KEYWORD_MON = "mon";
	private static final String DAY_KEYWORD_TUESDAY = "Tuesday";
	private static final String DAY_KEYWORD_TUES = "tues";
	private static final String DAY_KEYWORD_TUE = "tue";
	private static final String DAY_KEYWORD_WEDNESDAY = "Wednesday";
	private static final String DAY_KEYWORD_WED = "wed";
	private static final String DAY_KEYWORD_THURSDAY = "Thursday";
	private static final String DAY_KEYWORD_THURS = "thurs";
	private static final String DAY_KEYWORD_THUR = "thur";
	private static final String DAY_KEYWORD_THU = "thu";
	private static final String DAY_KEYWORD_FRIDAY = "Friday";
	private static final String DAY_KEYWORD_FRI = "fri";
	private static final String DAY_KEYWORD_SATURDAY = "Saturday";
	private static final String DAY_KEYWORD_SAT = "sat";
	private static final String DAY_KEYWORD_SUNDAY = "Sunday";
	private static final String DAY_KEYWORD_SUN = "sun";
	
	private static final int TODAY = 0;
	private static final int TOMORROW = -1;

	//@author A0111608R
	private static String[] generateArray(String parameter){
		parameter = parameter.trim();
		String[] array = parameter.split(SINGLE_SPACE);
		return array;
	}

	//@author A0111608R
	public static int parseTaskEndTime(String parameter) throws Exception {
		String [] messageArray = generateArray(parameter);

		for (int i = 0; i<=messageArray.length-1; i++) {
			if ((isValidKeyWordEnding(messageArray[i], messageArray, i))) {
				for (int j=i+1; j<=messageArray.length-1; j++) {
					if (isValidKeyWordAt(messageArray[j], messageArray, j)
							&& isInteger(messageArray[j+1])) {
						try {
							int endTime = Integer.parseInt(messageArray[j+1]);
							if (endTime >= 0000 && endTime <= 2359) {
								return endTime;
							} else {
								return 2359;
							}
						} catch (NumberFormatException nfe) {
							throw new Exception("Invalid Time Format");
						} 
					}
				}
			} else if ((isValidKeyWordDeadline(messageArray[i], messageArray, i))) {
				for (int j=i+1; j<=messageArray.length-1; j++) {
					if (isValidKeyWordAt(messageArray[j], messageArray, j)
							&& isInteger(messageArray[j+1])) {
						try {
							int endTime = Integer.parseInt(messageArray[j+1]);
							if (endTime >= 0000 && endTime <= 2359) {
								return endTime;
							} else {
								return 2359;
							}
						} catch (NumberFormatException nfe) {
							throw new Exception("Invalid Time Format");
						} 
					}
				}
			} else if ((isValidKeyWordStart(messageArray[i], messageArray, i))
					|| (isValidKeyWordStart_2(messageArray[i], messageArray, i))) {
				for (int j=i+1; j<=messageArray.length-1; j++) {
					if ((isValidKeyWordEnding(messageArray[j], messageArray, j))
							&& isInteger(messageArray[j+1])
							&& messageArray[j+1].length() == 4) {
						try {
							int endTime = Integer.parseInt(messageArray[j+1]);
							if (endTime >= 0000 && endTime <= 2359) {
								return endTime;
							} else {
								return 2359;
							}
						} catch (NumberFormatException nfe) {
							throw new Exception("Invalid Time Format");
						} 
					}
				}
			}
		}
		return 2359;
	}

	//@author A0111608R
	public static int parseTaskStartTime(String parameter) throws Exception  {
		String [] messageArray = generateArray(parameter);

		for (int i = 0; i<=messageArray.length-1; i++) {
			if ((isValidKeyWordStart(messageArray[i], messageArray, i))
					|| (isValidKeyWordStart_2(messageArray[i], messageArray, i))) {
				for (int j=i+1; j<=messageArray.length-1; j++) {
					if (isValidKeyWordAt(messageArray[j], messageArray, j)
							&& isInteger(messageArray[j+1])) {
						try {
							int startTime = Integer.parseInt(messageArray[j+1]);
							if (startTime >= 0000 && startTime <= 2359) {
								return startTime;
							} else {
								return 0000;
							}
						} catch (NumberFormatException nfe) {
							throw new Exception("Invalid Time Format");
						} 
					} else if (isValidKeyWordEnding(messageArray[j], messageArray, j)) {
						return 0000;
					}
				}
			}
		}
		return 0000;
	}

	//@author A0112156U
	public static boolean checkDateFormat(String dateInString){
		try{
			parseYear(dateInString);
			parseMonth(dateInString);
			parseDayOfMonth(dateInString);
		}
		catch(Exception e){
			Log.trace(dateInString +"is not valid date format");
			return false;
		}
		return true;
	}

	//@author A0112156U
	public static int parseYear(String dateInString) throws Exception {
		int _year = DateTime.now().year().get();
		try {
			_year = Integer.parseInt(dateInString);
		} catch (NumberFormatException nfe) {
			Log.trace(dateInString+ " is not a date : "+nfe.getMessage());
			return _year;
		}
		_year = _year %100;
		if (_year<65) {
			_year = _year + 2000 ;
		} else {
			assert (_year >= 65);
			_year = _year + 1900;
		}
		return _year;
	}

	//@author A0111608R
	public static int parseDayOfMonth(String dateInString) throws Exception {
		int month = parseMonth(dateInString);
		int date = Integer.parseInt(dateInString);
		date = date/10000;
		if (month == 2 && date > 0 && date <= 28) {
			return date;
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			if (date > 0 && date <= 30){
				return date;
			}
		} else if (date > 0 &&date <= 31){
			return date;
		} else {
			throw new Exception("Invalid Date Format");
		}
		return date;

	}

	//@author A0111608R
	public static int parseMonth(String dateInString) throws Exception {
		int _month = 1;
		_month = Integer.parseInt(dateInString);
		_month = _month/100;
		_month = _month % 100;
		if (_month > 0 && _month <= 12) {
			return _month;
		} else {
			throw new Exception("Invalid Date Format");
		}
	}

	//@author A0111608R
	public static int parseDayOfWeek(String parameter) {
		String day = parameter;

		if (day.equalsIgnoreCase(DAY_KEYWORD_TODAY) || day.equalsIgnoreCase(DAY_KEYWORD_TDY)) {
			return TODAY;
		} else if (day.equalsIgnoreCase(DAY_KEYWORD_TOMORROW) || day.equalsIgnoreCase(DAY_KEYWORD_TMR)) {
			return TOMORROW;
		} else if (day.equalsIgnoreCase(DAY_KEYWORD_MONDAY) || day.equalsIgnoreCase(DAY_KEYWORD_MON)) {
			return DateTimeConstants.MONDAY;
		} else if (day.equalsIgnoreCase(DAY_KEYWORD_TUESDAY) || day.equalsIgnoreCase(DAY_KEYWORD_TUE) || 
				day.equalsIgnoreCase(DAY_KEYWORD_TUES)) {
			return DateTimeConstants.TUESDAY;
		} else if (day.equalsIgnoreCase(DAY_KEYWORD_WEDNESDAY) || day.equalsIgnoreCase(DAY_KEYWORD_WED)) {
			return DateTimeConstants.WEDNESDAY;
		} else if (day.equalsIgnoreCase(DAY_KEYWORD_THURSDAY) || day.equalsIgnoreCase(DAY_KEYWORD_THURS) || 
				day.equalsIgnoreCase(DAY_KEYWORD_THUR) || day.equalsIgnoreCase(DAY_KEYWORD_THU)) {
			return DateTimeConstants.THURSDAY;
		} else if (day.equalsIgnoreCase(DAY_KEYWORD_FRIDAY) || day.equalsIgnoreCase(DAY_KEYWORD_FRI)) {
			return DateTimeConstants.FRIDAY;
		} else if (day.equalsIgnoreCase(DAY_KEYWORD_SATURDAY) || day.equalsIgnoreCase(DAY_KEYWORD_SAT)) {
			return DateTimeConstants.SATURDAY;
		} else if (day.equalsIgnoreCase(DAY_KEYWORD_SUNDAY) || day.equalsIgnoreCase(DAY_KEYWORD_SUN)) {
			return DateTimeConstants.SUNDAY;
		} else {
			return TODAY;
		}
	}

	//@author A0111608R
	public static TaskType parseTaskType (String parameter) {
		String [] messageArray = generateArray(parameter);
		if (messageArray.length != 0) {
			for (int i=0; i<=messageArray.length-1; i++) {
				if (isTimedTaskKeyWord(messageArray[i], messageArray, i)) {
					return (TaskType.TIMED);
				}
			}

			for (int i=0; i<=messageArray.length-1; i++) {
				if (isDeadlineTaskKeyWord(messageArray[i], messageArray, i)) {
					return (TaskType.DEADLINE);
				} 
			}		

			for (int i=0; i<=messageArray.length-1; i++) {
				if (isRecurringTaskKeyWord(messageArray[i], messageArray, i)) {
					return (TaskType.RECURRING);
				} 		
			}
		}
		return (TaskType.FLOATING);
	}

	//@author A0111608R
	private static boolean isRecurringTaskKeyWord(String string,
			String[] messageArray, int i) {
		return string.equalsIgnoreCase(KEYWORD_RECURRING)
				&& isValidKeyWord(messageArray, KEYWORD_RECURRING, i);
	}

	//@author A0111608R
	private static boolean isDeadlineTaskKeyWord(String string,
			String[] messageArray, int i) {
		return string.equalsIgnoreCase(KEYWORD_DEADLINE)
				&& isValidKeyWord(messageArray, KEYWORD_DEADLINE, i);
	}

	//@author A0111608R
	private static boolean isTimedTaskKeyWord(String string, String[] messageArray, int i) {
		return (string.equalsIgnoreCase(KEYWORD_DAY_STARTING)
					&& isValidKeyWord(messageArray, KEYWORD_DAY_STARTING, i))
				|| (string.equalsIgnoreCase(KEYWORD_DAY_STARTING_2)
					&& isValidKeyWord(messageArray, KEYWORD_DAY_STARTING_2, i))
				|| string.equalsIgnoreCase(KEYWORD_DAY_ENDING)
					&& isValidKeyWord(messageArray, KEYWORD_DAY_ENDING, i);
	}

	//@author A0111608R
	public static String parseTaskName(String parameter) throws Exception {
		String [] messageArray = generateArray(parameter);
		String taskName = EMPTY_STRING;

		if (!parameter.contains(QUOTATION_MARK)) {			
			for (int i=0; i<=messageArray.length-1; i++) {
				if (isKeyWord(messageArray[i])) {
					break;
				} else {
					taskName = taskName + messageArray[i] + SINGLE_SPACE;
				}
			}
		} else {
			for (int i=0; i<=messageArray.length-1; i++) {
				if(isValidKeyWordStart(messageArray[i], messageArray, i)) {
					break;
				} else if (isValidKeyWordStart_2(messageArray[i], messageArray, i)) {
					break;
				} else if (isValidKeyWordEnding(messageArray[i], messageArray, i)) {
					break;
				} else if (isValidKeyWordDeadline(messageArray[i], messageArray, i)) {
					break;
				} else if (isValidKeyWordRecurring(messageArray[i], messageArray, i)) {
					break;
				} else if (isValidKeyWordWith(messageArray[i], messageArray, i)) {
					break;
				} else if (isValidKeyWordAt(messageArray[i], messageArray, i)) {
					break;
				} else if (isValidKeyWordIn(messageArray[i], messageArray, i)) {
					break;
				} else {
					taskName = taskName + messageArray[i] + SINGLE_SPACE;
				} 
			}
		}

		taskName = taskName.replaceAll(QUOTATION_MARK, EMPTY_STRING);
		return taskName.trim();
	}

	//@author A0111608R
	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			Log.trace(s+" is not integer",e);
			return false; 
		}
		return true;
	}

	//@author A0111608R
	public static String parseTaskPerson(String parameter) {
		String [] messageArray = generateArray(parameter);
		String taskPerson = EMPTY_STRING;

		if (!parameter.contains(QUOTATION_MARK)) {
			for (int i=0; i+1<=messageArray.length-1; i++) {
				for (int j=i+1; j<=messageArray.length-1; j++) {
					if (isPersonKeyword(messageArray[i])
							&& !isKeyWord(messageArray[j])) {
						taskPerson = taskPerson + messageArray[j] + SINGLE_SPACE;
					} else {
						break;
					}
				}
			}
		} else {
			for (int i=0; i+1<=messageArray.length-1; i++) {
				if((isValidKeyWordWith(messageArray[i], messageArray, i))) {
					for (int j=i+1; j<=messageArray.length-1; j++) {
						if(isValidKeyWordAt(messageArray[j], messageArray, j)) {
							break;
						} else if (isValidKeyWordStart(messageArray[j], messageArray, j)) {
							break;
						} else if (isValidKeyWordStart_2(messageArray[j], messageArray, j)) {
							break;
						} else if (isValidKeyWordEnding(messageArray[j], messageArray, j)) {
							break;
						} else if (isValidKeyWordDeadline(messageArray[j], messageArray, j)) {
							break;
						} else if (isValidKeyWordRecurring(messageArray[j], messageArray, j)) {
							break;
						} else if (isValidKeyWordEnding(messageArray[j], messageArray, j)) {
							break;
						} else if (isValidKeyWordIn(messageArray[j], messageArray, j)) {
							break;
						} else { 
							taskPerson = taskPerson + messageArray[j] + SINGLE_SPACE;
						}
					}
				}
			}
		}

		taskPerson = taskPerson.replaceAll(QUOTATION_MARK, EMPTY_STRING);

		return taskPerson.trim();
	}
	
	//@author A0111608R
	private static boolean isValidKeyWordWith(String string, String[] messageArray,
			int j) {
		return messageArray[j].equalsIgnoreCase(KEYWORD_WITH)
		&& isValidKeyWord(messageArray, KEYWORD_WITH, j);
	}
	
	//@author A0111608R
	private static boolean isValidKeyWordIn(String string, String[] messageArray,
			int j) {
		return messageArray[j].equalsIgnoreCase(KEYWORD_IN)
		&& isValidKeyWord(messageArray, KEYWORD_IN, j);
	}

	//@author A0111608R
	private static boolean isValidKeyWordRecurring(String string,
			String[] messageArray, int j) {
		return messageArray[j].equalsIgnoreCase(KEYWORD_RECURRING)
		&& isValidKeyWord(messageArray, KEYWORD_RECURRING, j);
	}

	//@author A0111608R
	private static boolean isValidKeyWordDeadline(String string,
			String[] messageArray, int j) {
		return messageArray[j].equalsIgnoreCase(KEYWORD_DEADLINE)
		&& isValidKeyWord(messageArray, KEYWORD_DEADLINE, j);
	}

	//@author A0111608R
	private static boolean isValidKeyWordEnding(String string,
			String[] messageArray, int j) {
		return string.equalsIgnoreCase(KEYWORD_DAY_ENDING)
		&& isValidKeyWord(messageArray, KEYWORD_DAY_ENDING, j);
	}

	//@author A0111608R
	private static boolean isValidKeyWordStart_2(String string,
			String[] messageArray, int j) {
		return string.equalsIgnoreCase(KEYWORD_DAY_STARTING_2)
				&& isValidKeyWord(messageArray, KEYWORD_DAY_STARTING_2, j);
	}

	//@author A0111608R
	private static boolean isValidKeyWordStart(String string,
			String[] messageArray, int j) {
		return string.equalsIgnoreCase(KEYWORD_DAY_STARTING)
				&& isValidKeyWord(messageArray, KEYWORD_DAY_STARTING, j);
	}

	//@author A0111608R
	private static boolean isValidKeyWordAt(String string, String[] messageArray,
			int j) {
		return string.equalsIgnoreCase(KEYWORD_AT)
				&& isValidKeyWord(messageArray, KEYWORD_AT, j);
	}

	//@author A0111608R
	private static boolean isPersonKeyword(String string) {
		return string.equalsIgnoreCase(KEYWORD_WITH);

	}

	//@author A0111608R
	public static String parseTaskVenue(String parameter) {
		String [] messageArray = generateArray(parameter);
		String taskVenue = EMPTY_STRING;

		if (!parameter.contains(QUOTATION_MARK)) {
			for (int i=0; i+1<=messageArray.length-1; i++) {
				for (int j=i+1; j<=messageArray.length-1; j++) {
					if ( isVenueKeyword(messageArray[i])
							&& (!isKeyWord(messageArray[j]))
							&& !isInteger(messageArray[j])) {
						taskVenue = taskVenue + messageArray[j] + SINGLE_SPACE;
					} else {
						break;
					}
				}
			}
		} else {
			for (int i=0; i+1<=messageArray.length-1; i++) {
				if(isVenue(messageArray[i], messageArray, i)
						|| isVenue_2(messageArray[i], messageArray, i)) {
					for (int j=i+1; j<=messageArray.length-1; j++) {
						if(isValidKeyWordWith(messageArray[j], messageArray, j)) {
							break;
						} else if (isValidKeyWordStart (messageArray[j], messageArray, j)) {
							break;
						} else if (isValidKeyWordStart_2(messageArray[j], messageArray, j)) {
							break;
						} else if (isValidKeyWordEnding(messageArray[j], messageArray, j)) {
							break;
						} else if (isValidKeyWordDeadline(messageArray[j], messageArray, j)) {
							break;
						} else if (isValidKeyWordRecurring(messageArray[j], messageArray, j)) {
							break;
						} else if (isValidKeyWordEnding(messageArray[j], messageArray, j)) {
							break;
						} else if (isValidKeyWordAt(messageArray[j], messageArray, j)) {
							break;
						} 

						else { 
							taskVenue = taskVenue + messageArray[j] + SINGLE_SPACE;
						}
					}
				} 
			}
		}

		taskVenue = taskVenue.replaceAll(QUOTATION_MARK, EMPTY_STRING);

		return taskVenue.trim();
	}

	//@author A0111608R
	private static boolean isVenue_2(String string, String[] messageArray, int i) {
		return string.equalsIgnoreCase(KEYWORD_IN)
				&& isValidKeyWord(messageArray, KEYWORD_AT, i);
	}

	//@author A0111608R
	private static boolean isVenue(String string, String[] messageArray, int i) {
		return string.equalsIgnoreCase(KEYWORD_AT)
				&& isValidKeyWord(messageArray, KEYWORD_AT, i)
				&& !isInteger(messageArray[i+1]);
	}

	//@author A0111608R
	private static boolean isKeyWord(String string) {
		return string.equalsIgnoreCase(KEYWORD_DAY_STARTING)
				|| string.equalsIgnoreCase(KEYWORD_DAY_STARTING_2)
				|| string.equalsIgnoreCase(KEYWORD_DAY_ENDING)
				|| string.equalsIgnoreCase(KEYWORD_DEADLINE)
				|| string.equalsIgnoreCase(KEYWORD_RECURRING)
				|| string.equalsIgnoreCase(KEYWORD_WITH)
				|| string.equalsIgnoreCase(KEYWORD_AT)
				|| string.equalsIgnoreCase(KEYWORD_IN);
	}

	//@author A0111608R
	private static boolean isVenueKeyword(String string) {
		return (string.equalsIgnoreCase(KEYWORD_AT) 
				|| string.equalsIgnoreCase(KEYWORD_IN));
	}

	//@author A0111608R
	public static boolean isValidKeyWord(String [] array, String keyWord, int index) {
		int count = 0;

		for (int i=0; i<=index; i++) {
			String currentWord = array[i];
			if (currentWord.equalsIgnoreCase(keyWord)) {
			} else if (currentWord.indexOf(QUOTATION_MARK) != -1 
					&& currentWord.lastIndexOf(QUOTATION_MARK) != -1
					&& currentWord.indexOf(QUOTATION_MARK) 
					!= currentWord.lastIndexOf(QUOTATION_MARK)) {
				count = count + 2;
			} else if (currentWord.indexOf(QUOTATION_MARK) != -1 
					&& currentWord.indexOf(QUOTATION_MARK) 
					== currentWord.lastIndexOf(QUOTATION_MARK)) {
				count = count + 1;
			}
		}

		if (count % 2 == 0) {
			return true;
		} else {
			assert count %2 == 1 : count;
			return false;
		}
	}

	//@author A0112156U
	public static DateTime parseTaskStart(String parameter) throws Exception {
		String[] messageArray = generateArray(parameter);
		int year = 1, month = 1, day = 1;
		boolean hasKeyword = false;

		for (int i = 0; i+1<=messageArray.length-1; i++) {
			if ((isValidKeyWordStart(messageArray[i], messageArray, i))
					|| ((isValidKeyWordStart_2(messageArray[i], messageArray, i)))) {
				hasKeyword = true;
				if (isInteger(messageArray[i+1])) {
					year = parseYear(messageArray[i+1]);
					month = parseMonth(messageArray[i+1]);
					day = parseDayOfMonth(messageArray[i+1]);	
				} else {
					int dayOfWeek = parseDayOfWeek(messageArray[i+1]);
					DateTime today = new DateTime();
					if (dayOfWeek == TODAY) {
						year = today.year().get();
						month = today.monthOfYear().get();
						day = today.dayOfMonth().get();
					} else if (dayOfWeek == TOMORROW) {
						DateTime tomorrow = today.plusDays(1);
						year = tomorrow.year().get();
						month = tomorrow.monthOfYear().get();
						day = tomorrow.dayOfMonth().get();
					} else {
						int nextDayDistance = dayOfWeek - today.dayOfWeek().get();
						if (nextDayDistance < 0) {
							nextDayDistance+=7;
						}
						DateTime nextDay = today.plusDays(nextDayDistance);
						year = nextDay.year().get();
						month = nextDay.monthOfYear().get();
						day = nextDay.dayOfMonth().get();
					}
				}
			}
			if (isValidKeyWordDeadline (messageArray[i], messageArray, i)) {
				hasKeyword = true;
				DateTime taskStart = new DateTime(0);
				return taskStart;
			}
		}

		if (!hasKeyword) {
			hasKeyword = true;
			DateTime today = new DateTime();
			year = today.year().get();
			month = today.monthOfYear().get();
			day = today.dayOfMonth().get();
		}

		int time = parseTaskStartTime(parameter);
		int hour = time/100;
		int min = time%100;

		return new DateTime(year,month,day,hour,min);
	}

	//@author A0112156U
	public static DateTime parseTaskEnd(DateTime taskStart, String parameter) throws Exception {
		String[] messageArray = generateArray(parameter);
		int year = 1, month = 1, day = 1;
		boolean hasKeyword = false;
		//Parse date
		for (int i = 0; i+1<=messageArray.length-1; i++) {
			if ((isValidKeyWordEnding(messageArray[i], messageArray, i))
					|| (isValidKeyWordDeadline(messageArray[i], messageArray, i)) ) {
				hasKeyword = true;
				if (isInteger(messageArray[i+1])) {
					if (messageArray[i+1].length() == 6) { 
						year = parseYear(messageArray[i+1]);
						month = parseMonth(messageArray[i+1]);
						day = parseDayOfMonth(messageArray[i+1]);	
					} else if (messageArray[i+1].length() == 4) {
						year = taskStart.getYear();
						month = taskStart.getMonthOfYear();
						day = taskStart.getDayOfMonth();	 
					}
				} else {
					int dayOfWeek = parseDayOfWeek(messageArray[i+1]);
					DateTime today = new DateTime();
					if (dayOfWeek == TODAY) {
						year = today.year().get();
						month = today.monthOfYear().get();
						day = today.dayOfMonth().get();
					} else if (dayOfWeek == TOMORROW) {
						DateTime tomorrow = today.plusDays(1);
						year = tomorrow.year().get();
						month = tomorrow.monthOfYear().get();
						day = tomorrow.dayOfMonth().get();
					} else {
						if (!taskStart.isEqual(new DateTime(0))) {
							int nextDayDistance = dayOfWeek - taskStart.dayOfWeek().get();
							if (nextDayDistance < 0) {
								nextDayDistance+=7;
							}
							DateTime nextDay = taskStart.plusDays(nextDayDistance);
							year = nextDay.year().get();
							month = nextDay.monthOfYear().get();
							day = nextDay.dayOfMonth().get();
						} else { 
							int nextDayDistance = dayOfWeek - today.dayOfWeek().get();
							if (nextDayDistance < 0) {
								nextDayDistance+=7;
							}
							DateTime nextDay = today.plusDays(nextDayDistance);
							year = nextDay.year().get();
							month = nextDay.monthOfYear().get();
							day = nextDay.dayOfMonth().get();
						}

					}
				}
			}
		}
		if (!hasKeyword) {
			year = taskStart.year().get();
			month = taskStart.monthOfYear().get();
			day = taskStart.dayOfMonth().get();
			int hour = 23;
			int min = 59;
			return new DateTime(year,month,day,hour,min);
		}
		//parse time
		int time = parseTaskEndTime(parameter);
		int hour = time/100;
		int min = time%100;
		DateTime taskEnd = new DateTime(year,month,day,hour,min);
		if (taskEnd.compareTo(taskStart)<0) {
			throw new Exception("End time cannot be earlier than Start time");
		}
		return taskEnd;
	}

	//@author A0112156U
	public static DateTime parseTaskEnd(String parameter) throws Exception {
		DateTime taskStart = new DateTime(0);
		return parseTaskEnd(taskStart, parameter);
	}
}
