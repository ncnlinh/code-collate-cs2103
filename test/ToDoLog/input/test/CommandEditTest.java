package test;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import command.Command;
import common.Task;
import common.TaskType;
import controller.Controller;

//@author A0118899E
public class CommandEditTest {
	@Before
	public void before() throws Exception{
		CommandAddTest addTest = new CommandAddTest();
		addTest.testExecute();
	}
	@Test
	public void testExecute() throws Exception{
		Controller.init("test.xml");
		LinkedList <Task> displayList = Controller.getScheduleList();
		
		Controller.acceptUserCommand("edit -1");
		Command command1 = Controller.getLatestCommand();
		assertEquals("Description", "Item number -1 does not exist",command1.execute());
		
		Controller.acceptUserCommand("edit 100000000");
		Command command2 =  Controller.getLatestCommand();
		assertEquals("Description", "Item number 100000000 does not exist",command2.execute());
		
		Controller.acceptUserCommand("edit");
		Command command3 =  Controller.getLatestCommand();
		assertEquals("Description", "Please specify task to be edited and the details.",command3.execute());
		
		Controller.acceptUserCommand("edit         ");
		Command command4 = Controller.getLatestCommand();
		assertEquals("Description", "Please specify task to be edited and the details.",command4.execute());
		
		Controller.acceptUserCommand("ed");
		assertEquals("Description", "Invalid command.\nType in a command: add, delete, edit, done.",Controller.getFeedback());
		
		Controller.acceptUserCommand("edit 3 name Have fun");
		Command command6 = Controller.getLatestCommand();
		assertEquals("Description", "Edited name of the task.",command6.execute());
		
		Controller.acceptUserCommand("edit 3 venue SOC");
		Command command7 = Controller.getLatestCommand();
		assertEquals("Description", "Edited venue of the task.",command7.execute());
		
		Controller.acceptUserCommand("edit 3 venue");
		Command command8 = Controller.getLatestCommand();
		assertEquals("Description", "Please specify the details.",command8.execute());
		
		Controller.acceptUserCommand("edit 3 start date");
		Command command9 = Controller.getLatestCommand();
		assertEquals("Description", "Please specify the details.",command9.execute());
		
		Controller.acceptUserCommand("edit 3 start day 121214");
		Command command10 = Controller.getLatestCommand();
		assertEquals("Description", "Incorrect input for edit",command10.execute());
		
		Controller.acceptUserCommand("edit 3 start date 121214");
		Command command11 = Controller.getLatestCommand();
		if(displayList.get(2).getTaskType() == TaskType.TIMED){
			assertEquals("Description", "Edited start date of the task.",command11.execute());
		} else if (displayList.get(2).getTaskType() == TaskType.DEADLINE || displayList.get(3).getTaskType() == TaskType.FLOATING) { 
			assertEquals("Description", "Incorrect input for edit",command11.execute());
		}
		
		Controller.acceptUserCommand("edit 4 venue SOC");
		Command command12 = Controller.getLatestCommand();
		if(displayList.get(0).getTaskType() == TaskType.TIMED || displayList.get(0).getTaskType() == TaskType.DEADLINE ){
			assertEquals("Description", "Edited venue of the task.",command12.execute());
		} else if (displayList.get(3).getTaskType() == TaskType.FLOATING) { 
			assertEquals("Description", "Incorrect input for edit",command12.execute());
		}
		
		Controller.acceptUserCommand("edit 2 end date 121214");
		Command command13 = Controller.getLatestCommand();
		if(displayList.get(0).getTaskType() == TaskType.TIMED || displayList.get(0).getTaskType() == TaskType.DEADLINE ){
			assertEquals("Description", "Edited end date of the task.",command13.execute());
		} else if (displayList.get(3).getTaskType() == TaskType.FLOATING) { 
			assertEquals("Description", "Incorrect input for edit",command13.execute());
		}
		
		Controller.acceptUserCommand("edit 4 end date 121214");
		Command command14 = Controller.getLatestCommand();
		if(displayList.get(0).getTaskType() == TaskType.TIMED || displayList.get(0).getTaskType() == TaskType.DEADLINE ){
			assertEquals("Description", "Edited end date of the task.",command14.execute());
		} else if (displayList.get(3).getTaskType() == TaskType.FLOATING) { 
			assertEquals("Description", "Incorrect input for edit",command14.execute());
		}
		
		Controller.acceptUserCommand("edit 4 person Linh");
		Command command15 = Controller.getLatestCommand();
		if(displayList.get(0).getTaskType() == TaskType.TIMED || displayList.get(0).getTaskType() == TaskType.DEADLINE ){
			assertEquals("Description", "Edited person of the task.",command15.execute());
		} else if (displayList.get(3).getTaskType() == TaskType.FLOATING) { 
			assertEquals("Description", "Incorrect input for edit",command15.execute());
		}
		
		Controller.acceptUserCommand("edit 2 person Linh");
		Command command16 = Controller.getLatestCommand();
		if(displayList.get(0).getTaskType() == TaskType.TIMED || displayList.get(0).getTaskType() == TaskType.DEADLINE ){
			assertEquals("Description", "Edited person of the task.",command16.execute());
		} else if (displayList.get(3).getTaskType() == TaskType.FLOATING) { 
			assertEquals("Description", "Incorrect input for edit",command16.execute());
		}
		
		Controller.acceptUserCommand("edit 2 place R5 lounge");
		Command command17 = Controller.getLatestCommand();
		if(displayList.get(0).getTaskType() == TaskType.TIMED || displayList.get(0).getTaskType() == TaskType.DEADLINE ){
			assertEquals("Description", "Edited venue of the task.",command17.execute());
		} else if (displayList.get(3).getTaskType() == TaskType.FLOATING) { 
			assertEquals("Description", "Incorrect input for edit",command17.execute());
		}
		
		Controller.acceptUserCommand("edit 4 place R5 lounge");
		Command command18 = Controller.getLatestCommand();
		if(displayList.get(0).getTaskType() == TaskType.TIMED || displayList.get(0).getTaskType() == TaskType.DEADLINE ){
			assertEquals("Description", "Edited venue of the task.",command18.execute());
		} else if (displayList.get(3).getTaskType() == TaskType.FLOATING) { 
			assertEquals("Description", "Incorrect input for edit",command18.execute());
		}
	}
}