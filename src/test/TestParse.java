package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import app.CodeCollate;

public class TestParse {
	private static final String PATH_VIEW = "./test/sample1/view/";
	private static final String PATH_CONTROLLER_B = "./test/sample1/controller/ControllerB.cpp";
	private static final String PATH_CONTROLLER_A = "./test/sample1/controller/ControllerA.cpp";
	private static final String EXTENSION = "cpp, java";
	private static final String PATH_UNKNOWN = "/a/b/c/d/e/f/g/";
	
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
		assertTrue("Split args array and get roots collection", roots.length == 3);
		assertTrue("Split args array and get roots collection", 
				Arrays.asList(roots).contains(PATH_VIEW));
		assertTrue("Split args array and get roots collection", 
				Arrays.asList(roots).contains(PATH_CONTROLLER_A));
		assertTrue("Split args array and get roots collection", 
				Arrays.asList(roots).contains(PATH_CONTROLLER_B));
		assertTrue("Split args array, parse and get extensions collection",
				extensions.length == 2);
		assertTrue("Split args array, parse and get extensions collection", 
				Arrays.asList(extensions).contains("cpp"));
		assertTrue("Split args array, parse and get extensions collection", 
				Arrays.asList(extensions).contains("java"));
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
	
	@Test
	public void testBadInputMissingExtensions() {
		input = new String[] {PATH_CONTROLLER_A, PATH_CONTROLLER_B};
		collator = new CodeCollate(input);
		assertTrue("Console warning message",
				outContent.toString().contains("Arguments contain no extensions"));
	}
	
	@Test
	public void testBadInputInvalidRoot() {
		input = new String[] {PATH_CONTROLLER_A, PATH_UNKNOWN, EXTENSION};
		collator = new CodeCollate(input);
		assertTrue("Console warning message",
				outContent.toString().contains("Arguments contain invalid roots"));
	}
	
	

}
