package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import app.CodeCollate;

public class ParseTest {
	private static final String PATH_VIEW = "/test/sample1/controller/View.java.in";
	private static final String PATH_CONTROLLER_B = "/test/sample1/controller/ControllerB.cpp.in";
	private static final String PATH_CONTROLLER_A = "/test/sample1/controller/ControllerA.cpp.in";
	private static final String EXTENSION = "cpp.in, java.in";
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testParseInput() {
		String[] input = new String[] {PATH_CONTROLLER_A, PATH_CONTROLLER_B, PATH_VIEW, EXTENSION};
		String[] roots = {};
		String[] extensions = {};
		CodeCollate collator = new CodeCollate(input);
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
		assertArrayEquals("Split args array", new String[]{PATH_CONTROLLER_A, PATH_CONTROLLER_B, PATH_VIEW}, roots);
		assertArrayEquals("Split args array", new String[]{"cpp.in","java.in"}, extensions);}

}
