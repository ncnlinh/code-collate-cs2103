package test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import command.CommandAdd;
import controller.Controller;

//@author A0118899E
public class CommandAddTest {
	@Test
	public void testExecute() throws Exception {
		File file = new File ("test.xml");
		file.delete();
		Controller.init("test.xml");
		
		Controller.acceptUserCommand("add group meeting");
		CommandAdd command1 = (CommandAdd) Controller.getLatestCommand();
		assertEquals("Description", "Added group meeting to ToDoLog",command1.execute());
		
		Controller.acceptUserCommand("add lunch by monday at 2345");
		CommandAdd command2 = (CommandAdd) Controller.getLatestCommand();
		assertEquals("Description", "Added lunch to ToDoLog",command2.execute());
		
		Controller.acceptUserCommand("add Movie from monday at 1200 to 1500");
		CommandAdd command3 = (CommandAdd) Controller.getLatestCommand();
		assertEquals("Description", "Added Movie to ToDoLog",command3.execute());
		
		Controller.acceptUserCommand("add Picnic from tuesday at 1200 to 1500 at 'Gardens by the bay'");
		CommandAdd command4 = (CommandAdd) Controller.getLatestCommand();
		assertEquals("Description", "Added Picnic to ToDoLog",command4.execute());
		
		Controller.acceptUserCommand("add");
		CommandAdd command5 = (CommandAdd) Controller.getLatestCommand();
		assertEquals("Description", "Please enter the task details",command5.execute());
		
		Controller.acceptUserCommand("add          ");
		CommandAdd command6 = (CommandAdd) Controller.getLatestCommand();
		assertEquals("Description", "Please enter the task details",command6.execute());
		
		Controller.acceptUserCommand("add ");
		CommandAdd command7 = (CommandAdd) Controller.getLatestCommand();
		assertEquals("Description", "Please enter the task details",command7.execute());
		
		Controller.acceptUserCommand("ad");
		assertEquals("Description", "Invalid command.\nType in a command: add, delete, edit, done.",Controller.getFeedback());
		
			
	}
}