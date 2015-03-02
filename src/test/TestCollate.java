package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import app.CodeCollate;

public class TestCollate {
	private static final String PATH_VIEW = "./test/sample1/view/";
	private static final String PATH_CONTROLLER_B = "./test/sample1/controller/ControllerB.cpp";
	private static final String PATH_CONTROLLER_A = "./test/sample1/controller/ControllerA.cpp";
	private static final String FILE_PATH_VIEW = "./test/sample1/view/View.java";
	private static final String FILE_PATH_CONTROLLER_B = "./test/sample1/controller/ControllerB.cpp";
	private static final String FILE_PATH_CONTROLLER_A = "./test/sample1/controller/ControllerA.cpp";
	private static final String EXTENSION = "cpp, java";
	private static final String PATH_UNKNOWN = "/a/b/c/d/e/f/g/";
	private static final String SAMPLE_AUTHOR_1 = "A1111111K";
	private static final String SAMPLE_AUTHOR_2 = "A23447500K";
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
	public void testCodeFileRecorded() {
		input = new String[] {PATH_CONTROLLER_A, PATH_CONTROLLER_B, PATH_VIEW, EXTENSION};	
		collator = new CodeCollate(input);
		Object[] files = {};
		try {
			Method collateAllMethod = CodeCollate.class.getDeclaredMethod("collateAll");
			collateAllMethod.setAccessible(true);
			collateAllMethod.invoke(collator);
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		try {
			Field filesField = CodeCollate.class.getDeclaredField("_files");
			filesField.setAccessible(true);
			List<?> list = (List<?>) filesField.get(collator);
			files = list.toArray();
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		assertTrue("Correct files collection", files.length == 3);
		assertTrue("Correct files collection", Arrays.asList(files).contains(FILE_PATH_VIEW));
		assertTrue("Correct files collection", Arrays.asList(files).contains(FILE_PATH_CONTROLLER_B));
		assertTrue("Correct files collection", Arrays.asList(files).contains(FILE_PATH_CONTROLLER_A));
	
	}

}
