package test;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import command.Command;
import common.Task;
import controller.Controller;

//@author A0118899E
public class CommandMarkAsDoneTest {
	@Before
	public void before() throws Exception{
		CommandAddTest addTest = new CommandAddTest();
		addTest.testExecute();
	}
	@Test
	public void testExecute() throws Exception{
		Controller.init("test.xml");
		LinkedList <Task> displayList = Controller.getScheduleList();
		
		Controller.acceptUserCommand("done -1");
		Command command1 = Controller.getLatestCommand();
		assertEquals("Description", "Invalid task number. Cannot mark.", command1.execute());
		
		Controller.acceptUserCommand("done 100000000");
		Command command2 =  Controller.getLatestCommand();
		assertEquals("Description", "Invalid task number. Cannot mark.", command2.execute());
		
		Controller.acceptUserCommand("done");
		Command command3 =  Controller.getLatestCommand();
		assertEquals("Description", "Invalid task number. Cannot mark.", command3.execute());
		
		Controller.acceptUserCommand("done         ");
		Command command4 = Controller.getLatestCommand();
		assertEquals("Description", "Invalid task number. Cannot mark.", command4.execute());
		
		Controller.acceptUserCommand("do");
		assertEquals("Description", "Invalid command.\nType in a command: add, delete, edit, done.", 
				Controller.getFeedback());
		
		Controller.acceptUserCommand("done 3");
		Command command6 = Controller.getLatestCommand();
		String taskName = displayList.get(2).getTaskName();
		if (displayList.get(2).getTaskStatus() == true) {
			assertEquals("Description", "" + taskName + " is mark as completed", command6.execute());
		} else {
			assertEquals("Description", "" + taskName + " is mark as not completed",command6.execute());
		}
		
		
		
	}
}