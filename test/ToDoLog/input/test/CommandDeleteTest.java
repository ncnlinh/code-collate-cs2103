package test;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import command.Command;
import command.CommandDelete;
import common.Task;
import controller.Controller;

//@author A0118899E
public class CommandDeleteTest {
	@Before
	public void before() throws Exception{
		CommandAddTest addTest = new CommandAddTest();
		addTest.testExecute();
	}
	@Test
	public void testExecute() throws Exception{
		Controller.init("test.xml");
		
		LinkedList <Task> displayList = Controller.getScheduleList();
		Controller.acceptUserCommand("delete -1");
		CommandDelete command1 = (CommandDelete) Controller.getLatestCommand();
		assertEquals("Description", "Item number -1 does not exist",command1.execute());
		
		Controller.acceptUserCommand("delete 100000000");
		CommandDelete command2 = (CommandDelete) Controller.getLatestCommand();
		assertEquals("Description", "Item number 100000000 does not exist",command2.execute());
		
		Controller.acceptUserCommand("delete");
		CommandDelete command3 = (CommandDelete) Controller.getLatestCommand();
		assertEquals("Description", "Please specify the task to be deleted.",command3.execute());
		
		Controller.acceptUserCommand("delete         ");
		CommandDelete command4 = (CommandDelete) Controller.getLatestCommand();
		assertEquals("Description", "Please specify the task to be deleted.",command4.execute());
		
		Controller.acceptUserCommand("del");
		assertEquals("Description", "Invalid command.\nType in a command: add, delete, edit, done.",Controller.getFeedback());
		
		Controller.acceptUserCommand("delete 3");
		CommandDelete command6 = (CommandDelete) Controller.getLatestCommand();
		String deletedName1 = displayList.get(2).getTaskName();
		assertEquals("Description", "Deleted "+ deletedName1+" from toDoLog",command6.execute());
		
		Controller.acceptUserCommand("delete 1 ");
		Command command7 = Controller.getLatestCommand();
		String deletedName2 = displayList.get(0).getTaskName();
		assertEquals("Description", "Deleted "+ deletedName2+" from toDoLog",command7.execute());
			
	}
}