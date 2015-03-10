package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import org.joda.time.DateTime;
import org.junit.Test;

import common.Task;
import common.TaskType;

import storage.FileStorage;

public class FileStorageTest {
	
	//@author A0112156U
	@Test
	public void testConstructorWithString() {
		FileStorage fileStorageTest = new FileStorage("test.xml");
		File file = new File("test.xml");
		assertEquals(fileStorageTest.getFile(), file );
		String text = fileStorageTest.getDocument().asXML();
		assertEquals(text, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>");
		file.delete();
	}
	
	//@author A0112156U
	@Test
	public void testConstructor() {
		FileStorage fileStorageTest = new FileStorage();
		File file = new File("store.xml");
		assertEquals(fileStorageTest.getFile(), file );
		String text = fileStorageTest.getDocument().asXML();
		assertEquals(text, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>");
	}
	
	//@author A0112156U
	@Test
	public void testInit() {
		String name = createRandomString();
		File file = new File(name);
		new FileStorage(name);
		assertTrue(file.exists());
		assertFalse(new File(createRandomString()).exists());
		file.delete();
	}
	
	//@author A0112156U
	@Test
	public void testLoad() throws IOException {
		FileStorage fileStorageTestBlank = new FileStorage("blank.xml");
		File file = new File("blank.xml");
		LinkedList<Task> tasks = fileStorageTestBlank.load();
		assertTrue(tasks.equals(new LinkedList<Task>()));
		file.delete();
	}
	
	//@author A0112156U
	@Test
	public void testStoreFloating() throws IOException {
		FileStorage fileStorageTest = new FileStorage("test.xml");
		File file = new File("test.xml");
		LinkedList<Task> tasks = new LinkedList<Task>();
		tasks.add(createRandomFloatingTask());
		fileStorageTest.store(tasks);
		assertTrue(file.exists());
		FileStorage fileStorageTest2 = new FileStorage("test.xml");
		LinkedList<Task> ctasks = fileStorageTest2.load();;
		for (int i = 0; i< tasks.size(); i++) {
			assertEquals(tasks.get(i).getTaskName(),ctasks.get(i).getTaskName());
		}
		file.delete();
	}
	
	//@author A0112156U
	@Test
	public void testStoreTimed() throws IOException {
		FileStorage fileStorageTest = new FileStorage("test.xml");
		File file = new File("test.xml");
		LinkedList <Task> tasks = new LinkedList<Task>();
		tasks.add(createRandomTimedTask());
		fileStorageTest.store(tasks);
		assertTrue(file.exists());
		FileStorage fileStorageTest2 = new FileStorage("test.xml");
		LinkedList<Task> ctasks = fileStorageTest2.load();
		for (int i = 0;i <tasks.size(); i++){
			assertEquals(tasks.get(i).getTaskName(),ctasks.get(i).getTaskName());
		}
		file.delete();
	}
	
	//@author A0112156U
	@Test
	public void testStoreDeadline() throws IOException {
		FileStorage fileStorageTest = new FileStorage("test.xml");
		File file = new File("test.xml");
		LinkedList <Task> tasks = new LinkedList<Task>();
		tasks.add(createRandomDeadlineTask());
		fileStorageTest.store(tasks);
		assertTrue(file.exists());
		FileStorage fileStorageTest2 = new FileStorage("test.xml");
		LinkedList<Task> ctasks = fileStorageTest2.load();
		for (int i = 0;i <tasks.size(); i++){
			assertEquals(tasks.get(i).getTaskName(),ctasks.get(i).getTaskName());
		}
		file.delete();
	}
		
	//@author A0112156U
	private String createRandomString() {
		char[] chars = "abcdefghijklmnopqrstuvwxyz01123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		return sb.toString();
	}
	
	//@author A0112156U
	private Task createRandomFloatingTask() {
		char[] chars = "abcdefghijklmnopqrstuvwxyz01123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		String name = sb.toString();
		return new Task(TaskType.FLOATING, name, false,"bob","soc");
	}
	
	//@author A0111513B
	private Task createRandomTimedTask() {
		//TODO
		char[] chars = "abcdefghijklmnopqrstuvwxyz01123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		String name = sb.toString();
		return new Task(TaskType.TIMED, name, new DateTime(2014,12,2,8,0),new DateTime(2014,12,2,23,59),false,"Bob","SOC");
	}
	
	//@author A0111513B
	private Task createRandomDeadlineTask() {
		//TODO
		char[] chars = "abcdefghijklmnopqrstuvwxyz01123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		String name = sb.toString();
		return new Task(TaskType.DEADLINE, name,new DateTime(2014,12,2,23,59), false,"Bob","SOC");
	}
}
