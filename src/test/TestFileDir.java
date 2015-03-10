package test;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import app.CodeCollate;

public class TestFileDir {
	private static final String PATH_VIEW = "test/sample1/controller";
	private static final String PATH_CONTROLLER_B = "test/sample1/controller/ControllerB.cpp";
	private static final String PATH_CONTROLLER_A = "test/sample1/controller/ControllerA.cpp";
	private static final String EXTENSION = "cpp, java";
	
	private static String[] input;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@BeforeClass
	public static void setUpBeforeClass() {
		input = new String[] {PATH_CONTROLLER_A, PATH_CONTROLLER_B, PATH_VIEW, EXTENSION};	
	}
	@Test
	public void testNoFolderExists() {
		File outputFolder = new File("collated");
		if (outputFolder.exists()) {
			purgeDirectory(outputFolder);
			outputFolder.delete();
		}
		new CodeCollate(input);
		assertTrue("\"collated\" created when no file or folder of same name exists before",
				outputFolder.exists());
		assertTrue("\"collated\" is a folder", outputFolder.isDirectory());
	}
	
	@Test
	public void testHaveFolderExists() {
		File outputFolder = new File("collated");
		if (outputFolder.exists()) {
			purgeDirectory(outputFolder);
			outputFolder.delete();
		}
		outputFolder.mkdir();
		new CodeCollate(input);
		assertTrue("\"collated\" created when folder of same name exists before",
				outputFolder.exists());
		assertTrue("\"collated\" is a folder", outputFolder.isDirectory());
	}
	
	void purgeDirectory(File dir) {
	    for (File file: dir.listFiles()) {
	        if (file.isDirectory()) purgeDirectory(file);
	        file.delete();
	    }
	}
	
	@Test
	public void testHaveFileOfSameNameExists() {
		File outputFolder = new File("collated");
		if (outputFolder.exists()) {
			purgeDirectory(outputFolder);
			outputFolder.delete();
		}
		try {
			outputFolder.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new CodeCollate(input);
		assertTrue("\"collated\" exists when file of same name exists before",
				outputFolder.exists());
		assertTrue("\"collated\" is a file", !outputFolder.isDirectory());
		assertTrue("Console warning message",
				outContent.toString().contains("There is a file called \"collated\" prevents creation of folder\n"));
	}
}
