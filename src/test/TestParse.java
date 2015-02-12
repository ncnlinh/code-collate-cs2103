package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;

import org.junit.BeforeClass;
import org.junit.Test;

import app.CodeCollate;

public class TestParse {
	private static final String PATH_VIEW = "/test/sample1/controller/View.java.in";
	private static final String PATH_CONTROLLER_B = "/test/sample1/controller/ControllerB.cpp.in";
	private static final String PATH_CONTROLLER_A = "/test/sample1/controller/ControllerA.cpp.in";
	private static final String EXTENSION = "cpp.in, java.in";
	
	private static String[] input;
	private static CodeCollate collator;
	@BeforeClass
	public static void setUpBeforeClass() {
		input = new String[] {PATH_CONTROLLER_A, PATH_CONTROLLER_B, PATH_VIEW, EXTENSION};	
		collator = new CodeCollate(input);
	}

	@Test
	public void testParseInput() {
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
	
	
	
	
	

}
