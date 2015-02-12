package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import app.CodeCollate;

public class TestParse {
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
	
	@Test
	public void testParseInput() {
		input = new String[] {PATH_CONTROLLER_A, PATH_CONTROLLER_B, PATH_VIEW, EXTENSION};	
		collator = new CodeCollate(input);
		String[] roots = {};
		String[] extensions = {};
		try {
			Field rootsField = CodeCollate.class.getDeclaredField("_roots");
			rootsField.setAccessible(true);
			roots = (String[]) rootsField.get(collator);
			Field extensionsField = CodeCollate.class.getDeclaredField("_extensions");
			extensionsField.setAccessible(true);
			extensions = (String[]) extensionsField.get(collator);
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		assertArrayEquals("Split args array and get roots collection", new String[]{PATH_CONTROLLER_A, PATH_CONTROLLER_B, PATH_VIEW}, roots);
		assertArrayEquals("Split args array, parse and get extensions collection", new String[]{"cpp.in","java.in"}, extensions);
	}
	
	@Test
	public void testBadInputOnlyPath() {
		input = new String[] {PATH_VIEW};	
		collator = new CodeCollate(input);
		assertTrue("Console warning message",
				outContent.toString().contains("Missing arguments"));
	}
	
	@Test
	public void testBadInputOnlyExtension() {
		input = new String[] {EXTENSION};	
		collator = new CodeCollate(input);
		assertTrue("Console warning message",
				outContent.toString().contains("Missing arguments"));
	}
	
	@Test
	public void testBadInputNoArguments() {
		input = new String[] {};	
		collator = new CodeCollate(input);
		assertTrue("Console warning message",
				outContent.toString().contains("Missing arguments"));
	}
	
	
	

}
