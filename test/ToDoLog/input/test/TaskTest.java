package test;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Task;
import common.TaskType;

import parser.TaskParser;

//@author A0111608R
public class TaskTest {

	@Test
	public void testParseName() throws Exception {
		Task task = new Task ("group meeting");
		assertEquals("output should be group meeting", "group meeting" , task.getTaskName());
		try {
			new Task ("group meeting");
		} catch (Exception e) {
			assertEquals("output should be invalid input", "Invalid command. Missing task name.\nTask name must be inside quotation marks." ,
					e.getMessage());
		}
		try {
			new Task ("group meeting");
		} catch (Exception e) {
			assertEquals("output should be invalid input", "Invalid command. Missing task name.\nTask name must be inside quotation marks." , 
					e.getMessage());
		}
	}

	@Test
	public void testParseType() throws Exception {
		Task task = new Task ("group meeting");
		assertEquals("output should be FLOATING", TaskType.FLOATING , task.getTaskType());
		Task task2 = new Task ("group meeting from friday to saturday");
		assertEquals("output should be TIMED", TaskType.TIMED , task2.getTaskType());
		Task task3 = new Task ("group meeting by friday");
		assertEquals("output should be DEADLINE", TaskType.DEADLINE , task3.getTaskType());
		Task task4 = new Task ("group meeting every friday");
		assertEquals("output should be RECURRING", TaskType.RECURRING , task4.getTaskType());
	}

//	@Test
//	public void testParseDay() throws Exception {
//		Task task = new Task ("group meeting from fri at 2359 to sat");
//		assertEquals("output should be Friday", "Friday" , task.getStartDay());
//		assertEquals("output should be Saturday", "Saturday" , task.getEndDay());
//
//		Task task2 = new Task ("group meeting from thur");
//		assertEquals("output should be Thursday", "Thursday" , task2.getStartDay());
//		assertEquals("output should be Thursday", "Thursday" , task2.getEndDay());
//
//		Task task3 = new Task ("group meeting to sunday");
//		assertEquals("output should be Today", "Today" , task3.getStartDay());
//		assertEquals("output should be Sunday", "Sunday" , task3.getEndDay());
//
//		Task task4 = new Task ("group meeting from tue");
//		assertEquals("output should be Tuesday", "Tuesday" , task4.getStartDay());
//		assertEquals("output should be Tuesday", "Tuesday" , task4.getEndDay());
//
//		Task task5 = new Task ("group meeting by weDnesDAY");
//		assertEquals("output should be Today", "Today" , task5.getStartDay());
//		assertEquals("output should be Wednesday", "Wednesday" , task5.getEndDay());
//	}

	@Test
	public void testTime() throws Exception {
		Task task = new Task ("group meeting from fri at 2359 to sat at 1345");
		assertEquals("output should be 2359", 2359 , task.getStartTime());
		assertEquals("output should be 1345", 1345 , task.getEndTime());

		Task task2 = new Task ("group meeting from fri at 2459 to sat at 0000");
		assertEquals("output should be 0000", 0000 , task2.getStartTime());
		assertEquals("output should be 0000", 0000 , task2.getEndTime());

		Task task3 = new Task ("group meeting from fri at 1100 to sat at 1650");
		assertEquals("output should be 1100", 1100 , task3.getStartTime());
		assertEquals("output should be 1650", 1650 , task3.getEndTime());


		Task task4=new Task ("group meeting from fri at 11h00 to sat at 16h50");
		assertEquals("output should not be 1100", 0 , task4.getStartTime());
		assertEquals("output should not be 1650", 2359 , task4.getEndTime());
		
		Task task5 = new Task ("group meeting to sat at 1650");
		assertEquals("output should be 0000", 0000 , task5.getStartTime());
		assertEquals("output should be 1650", 1650 , task5.getEndTime());
	}

	@Test
	public void testDateMonthYear() throws Exception {
		Task task = new Task ("group meeting from 090314 at 1500 to 100514 at 1650");
		assertEquals("output should be 9", 9, task.getStartDate());
		assertEquals("output should be 03", 3, task.getStartMonth());
		assertEquals("output should be 2014", 2014, task.getStartYear());
		assertEquals("output should be 10", 10, task.getEndDate());
		assertEquals("output should be 5", 5, task.getEndMonth());
		assertEquals("output should be 2014", 2014, task.getEndYear());
		
	}

	@Test
	public void testVenueAndPerson() throws Exception {
		Task task = new Task ("group meeting with Linh at school cafe at 1500" );
		assertEquals ("output should be Linh", "Linh" , task.getTaskPerson());
		assertEquals ("output should be school cafe", "school cafe" , task.getTaskVenue());

	}

	@Test
	public void testGeneralCases() throws Exception {
		Task task = new Task ("group meeting with ben and linh at computing on 140414 at 1600");
		assertEquals ("output should be group meeting", "group meeting", task.getTaskName());
		assertEquals ("output should be ben and linh", "ben and linh", task.getTaskPerson());
		assertEquals ("output should be computing", "computing", task.getTaskVenue());
		assertEquals ("output should be 14", 14, task.getStartDate());
		assertEquals ("output should be 4", 4, task.getStartMonth());
		assertEquals ("output should be 14", 2014, task.getStartYear());

		Task task2 = new Task ("camping with schoolmates in school from saturday at 1500 to sunday at 1600" );
		assertEquals ("output should be schoolmates", "schoolmates" , task2.getTaskPerson());
		assertEquals ("output should be school", "school" , task2.getTaskVenue());
		assertEquals ("output should be 1500", 1500 , task2.getStartTime());
		assertEquals ("output should be 1500", 1600 , task2.getEndTime());

		Task task3 = new Task ("engin club meeting with engin club in club room on tuesday at 1500 to wed at 0800");
		assertEquals ("output should be engin club meeting", "engin club meeting", task3.getTaskName());
		assertEquals ("output should be engin club", "engin club" , task3.getTaskPerson());
		assertEquals ("output should be club room", "club room" , task3.getTaskVenue());
		assertEquals ("output should be 1500", 1500 , task3.getStartTime());
		assertEquals ("output should be 0800", 800 , task3.getEndTime());

		Task task4 = new Task ("engin club meeting with engin club in club room on 290314 at 1500 to 300314 at 0800");
		assertEquals ("output should be engin club meeting", "engin club meeting", task4.getTaskName());
		assertEquals ("output should be engin club", "engin club" , task4.getTaskPerson());
		assertEquals ("output should be club room", "club room" , task4.getTaskVenue());
		assertEquals ("output should be 29", 29 , task4.getStartDate());
		assertEquals ("output should be 30", 30 , task4.getEndDate());
		assertEquals ("output should be 03", 3 , task4.getStartMonth());
		assertEquals ("output should be 03", 3 , task4.getEndMonth());
		assertEquals ("output should be 2014", 2014 , task4.getStartYear());
		assertEquals ("output should be 2014", 2014 , task4.getEndYear());
		assertEquals ("output should be 1500", 1500 , task4.getStartTime());
		assertEquals ("output should be 0800", 800 , task4.getEndTime());
		try{
			Task task5 = new Task ("engin club meeting with engin club in club room on 331312 at 1500 to 331312 at 0800");
			fail();
			assertEquals ("output should be engin club meeting", "engin club meeting", task5.getTaskName());
			assertEquals ("output should be engin club", "engin club" , task5.getTaskPerson());
			assertEquals ("output should be club room", "club room" , task5.getTaskVenue());
		} catch (Exception e) {
			assertEquals("output should be Invalid Date Format", "Invalid Date Format", e.getMessage());
		}
	}
	
	@Test
	public void testValidKeyWord() {
		String s1 = "deadline task with \'dad at soc\' at \'dad with soc\'";
		String[] arr1 = s1.split(" ");

		assertEquals(true, TaskParser.isValidKeyWord(arr1, "with", 2));
		assertEquals(false, TaskParser.isValidKeyWord(arr1, "at", 4));
		assertEquals(false, TaskParser.isValidKeyWord(arr1, "with", 8));
		assertEquals(true, TaskParser.isValidKeyWord(arr1, "at", 6));
	}

	@Test
	public void testFull() {
		//To test deadline task
		Task task;
		try {
			task = new Task ("homework 1 by 101114 at 1900");
			assertEquals ("output should be homework 1", "homework 1", task.getTaskName());
			assertEquals ("output should be 1900", 1900, task.getEndTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("homework 2 by tue at -1");
			assertEquals ("output should be homework 2", "homework 2", task.getTaskName());
			assertEquals ("output should be 2359", 2359, task.getEndTime());
			assertEquals ("nearest date of tuesday", 18, task.getEndDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("homework 3 by wed at 2430");
			assertEquals ("output should be homework 3", "homework 3", task.getTaskName());
			assertEquals ("output should be 2359", 2359, task.getEndTime());
			assertEquals ("nearest date of Wednesday", 19, task.getEndDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//To test floating task
		try {
			task = new Task ("group meeting 1 at soc with team");
			assertEquals ("output should be group meeting 1", 
					"group meeting 1", task.getTaskName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("\'group meeting 2 at soc with team\'");
			assertEquals ("output should be group meeting 2 at soc with team", 
					"group meeting 2 at soc with team", task.getTaskName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//To test floating task
		try {
			task = new Task ("group meeting 1 at soc with team");
			assertEquals ("output should be group meeting 1", 
					"group meeting 1", task.getTaskName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//To test for timed task
		try {
			task = new Task ("exam 1 from thur at 1900 to 2000");
			assertEquals ("output should be exam 1", "exam 1", task.getTaskName());
			assertEquals ("output should be 13", 13, task.getStartDate());
			assertEquals ("output should be 13", 1900, task.getStartTime());
			assertEquals ("output should be 13", 13, task.getEndDate());
			assertEquals ("output should be 2000", 2000, task.getEndTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("exam 2 from thur at 2000 to fri at 2100");
			assertEquals ("output should be exam 2", "exam 2", task.getTaskName());
			assertEquals ("output should be 13", 13, task.getStartDate());
			assertEquals ("output should be 2000", 2000, task.getStartTime());
			assertEquals ("output should be 14", 14, task.getEndDate());
			assertEquals ("output should be 2100", 2100, task.getEndTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("exam 3 from friday to sat at 1800");
			assertEquals ("output should be exam 3", "exam 3", task.getTaskName());
			assertEquals ("output should be 14", 14, task.getStartDate());
			assertEquals ("output should be 0000", 0000, task.getStartTime());
			assertEquals ("output should be 15", 15, task.getEndDate());
			assertEquals ("output should be 1800", 1800, task.getEndTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("exam 4 on thursday at 1700 to sunday");
			assertEquals ("output should be exam 4", "exam 4", task.getTaskName());
			assertEquals ("output should be 13", 13, task.getStartDate());
			assertEquals ("output should be 1700", 1700, task.getStartTime());
			assertEquals ("output should be 16", 16, task.getEndDate());
			assertEquals ("output should be 2359", 2359, task.getEndTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("exam 5 from thursday at -1 to friday at 1000");
			assertEquals ("output should be exam 5", "exam 5", task.getTaskName());
			assertEquals ("output should be 13", 13, task.getStartDate());
			assertEquals ("output should be 0000", 0000, task.getStartTime());
			assertEquals ("output should be 14", 14, task.getEndDate());
			assertEquals ("output should be 1000", 1000, task.getEndTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("exam 6 from thursday at 2430 to friday at 1000");
			assertEquals ("output should be exam 6", "exam 6", task.getTaskName());
			assertEquals ("output should be 13", 13, task.getStartDate());
			assertEquals ("output should be 0000", 0000, task.getStartTime());
			assertEquals ("output should be 14", 14, task.getEndDate());
			assertEquals ("output should be 1000", 1000, task.getEndTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("exam 7 from thursday at 1100 to friday at -1");
			assertEquals ("output should be exam 7", "exam 7", task.getTaskName());
			assertEquals ("output should be 13", 13, task.getStartDate());
			assertEquals ("output should be 1100", 1100, task.getStartTime());
			assertEquals ("output should be 14", 14, task.getEndDate());
			assertEquals ("output should be 2359", 2359, task.getEndTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("exam 8 from thursday at 1100 to friday at 2430");
			assertEquals ("output should be exam 8", "exam 8", task.getTaskName());
			assertEquals ("output should be 13", 13, task.getStartDate());
			assertEquals ("output should be 1100", 1100, task.getStartTime());
			assertEquals ("output should be 14", 14, task.getEndDate());
			assertEquals ("output should be 2359", 2359, task.getEndTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("exam 9 from 121114 at 1600 to 131114 at 1700");
			assertEquals ("output should be exam 9", "exam 9", task.getTaskName());
			assertEquals ("output should be 12", 12, task.getStartDate());
			assertEquals ("output should be 1600", 1600, task.getStartTime());
			assertEquals ("output should be 13", 13, task.getEndDate());
			assertEquals ("output should be 1700", 1700, task.getEndTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("exam 10 from 121114 at -1 to 131114 at 1700");
			assertEquals ("output should be exam 10", "exam 10", task.getTaskName());
			assertEquals ("output should be 12", 12, task.getStartDate());
			assertEquals ("output should be 0000", 0000, task.getStartTime());
			assertEquals ("output should be 13", 13, task.getEndDate());
			assertEquals ("output should be 1700", 1700, task.getEndTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("exam 11 from 121114 at 2430 to 131114 at 1700");
			assertEquals ("output should be exam 11", "exam 11", task.getTaskName());
			assertEquals ("output should be 12", 12, task.getStartDate());
			assertEquals ("output should be 0000", 0000, task.getStartTime());
			assertEquals ("output should be 13", 13, task.getEndDate());
			assertEquals ("output should be 1700", 1700, task.getEndTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("exam 12 from 121114 at 0900 to 131114 at -1");
			assertEquals ("output should be exam 12", "exam 12", task.getTaskName());
			assertEquals ("output should be 12", 12, task.getStartDate());
			assertEquals ("output should be 0900", 900, task.getStartTime());
			assertEquals ("output should be 13", 13, task.getEndDate());
			assertEquals ("output should be 2359", 2359, task.getEndTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("exam 13 from 121114 at 0900 to 131114 at 2430");
			assertEquals ("output should be exam 13", "exam 13", task.getTaskName());
			assertEquals ("output should be 12", 12, task.getStartDate());
			assertEquals ("output should be 0900", 900, task.getStartTime());
			assertEquals ("output should be 13", 13, task.getEndDate());
			assertEquals ("output should be 2359", 2359, task.getEndTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("exam 14 from 131114 to 141114");
			assertEquals ("output should be exam 14", "exam 14", task.getTaskName());
			assertEquals ("output should be 13", 13, task.getStartDate());
			assertEquals ("output should be 0000", 0000, task.getStartTime());
			assertEquals ("output should be 14", 14, task.getEndDate());
			assertEquals ("output should be 2359", 2359, task.getEndTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//To test for person
		try {
			task = new Task ("group work 1 with teammate at soc");
			assertEquals ("output should be teammate", "teammate", task.getTaskPerson());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			task = new Task ("group work 1 with \'teammate at soc\'");
			assertEquals ("output should be teammate ay soc", "teammate at soc", task.getTaskPerson());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//To test for venue
				try {
					task = new Task ("picnic 1 with friends at gardens by the bay");
					assertEquals ("output should be gardens", "gardens", task.getTaskVenue());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					task = new Task ("picnic 1 with friends at \'gardens by the bay\'");
					assertEquals ("output should be gardens by the bay", "gardens by the bay", task.getTaskVenue());
				} catch (Exception e) {
					e.printStackTrace();
				}
	}
}