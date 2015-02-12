package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import app.CodeCollate;

public class TestFileDir {
	private static final String PATH_VIEW = "/test/sample1/controller/View.java.in";
	private static final String PATH_CONTROLLER_B = "/test/sample1/controller/ControllerB.cpp.in";
	private static final String PATH_CONTROLLER_A = "/test/sample1/controller/ControllerA.cpp.in";
	private static final String EXTENSION = "cpp.in, java.in";
	
	private static String[] input;
	private static CodeCollate collator;
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
			outputFolder.delete();
		}
		collator = new CodeCollate(input);
		assertTrue("Output folder \"collated\" created when no folder exists before",
				outputFolder.exists() && outputFolder.isDirectory());
	}
	
	@Test
	public void testHaveFolderExists() {
		File outputFolder = new File("collated");
		if (outputFolder.exists()) {
			outputFolder.delete();
		}
		outputFolder.mkdir();
		collator = new CodeCollate(input);
		assertTrue("Output folder \"collated\" created when have a folder there before",
				outputFolder.exists() && outputFolder.isDirectory());
	}
	
	@Test
	public void testHaveFileOfSameNameExists() {
		File outputFolder = new File("collated");
		if (outputFolder.exists()) {
			outputFolder.delete();
		}
		try {
			outputFolder.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		collator = new CodeCollate(input);
		assertTrue("Output folder \"collated\" not created when there is a file of same name there before",
				outputFolder.exists() && !outputFolder.isDirectory());
		assertTrue("Console warning message",
				outContent.toString().contains("There is a file called \"collated\" prevents creation of folder\n"));
	}
}
