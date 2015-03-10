package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
	private static final String PATH_CONTROLLER_DIR = "./test/sample1/controller";
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
	public void testCodeFilesAcknowledged() {
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
	
		input = new String[] {PATH_CONTROLLER_DIR, EXTENSION};	
		collator = new CodeCollate(input);
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
		assertTrue("Correct files collection", files.length == 2);
		assertTrue("Correct files collection", Arrays.asList(files).contains(FILE_PATH_CONTROLLER_B));
		assertTrue("Correct files collection", Arrays.asList(files).contains(FILE_PATH_CONTROLLER_A));
	
		
	}

	@Test
	public void testCreateCollatedFiles() {
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
		List<File> collatedFiles = new ArrayList<File>();
		collatedFiles.add(new File("collated/" + SAMPLE_AUTHOR_1 +".col"));
		collatedFiles.add(new File("collated/" + SAMPLE_AUTHOR_2 +".col"));
		for (int i = 0; i < collatedFiles.size(); i++) {
			File collatedFile = collatedFiles.get(i);
			assertTrue("File #"+(i+1)+" exists", collatedFile.exists());
			assertTrue("File #"+(i+1)+" is a file", collatedFile.isFile());
		}
	}
	
	private static final String TODOLOG_PATH_INPUT = "./test/ToDoLog/input";
	private static final String TODOLOG_PATH_EXTENSIONS = "java";
	private static final String TODOLOG_PATH_OUTPUT_EXPECTED = "./test/ToDoLog/output/collated";
	private static final String[] TODOLOG_AUTHORS = {"a0111513b","a0111608r","a0112156u-reused","a0112156u","a0118899e"};
	@Test
	public void testCodeCollated() throws IOException {
		CodeCollate.main(new String[] {TODOLOG_PATH_INPUT,TODOLOG_PATH_EXTENSIONS});
		for (int i = 0; i < TODOLOG_AUTHORS.length; i++) {
			File collatedFile = new File("collated/"+TODOLOG_AUTHORS[i]+".col");
			assertTrue("File #"+(i+1)+" exists", collatedFile.exists());
			assertTrue("File #"+(i+1)+" is a file", collatedFile.isFile());
		}
//		File expected = new File (TODOLOG_PATH_OUTPUT_EXPECTED);
//		for (int i = 0; i < TODOLOG_AUTHORS.length; i++) {
//			File expectedFile = new File("collated/"+TODOLOG_AUTHORS[i]+".col");
//			File actualFile = new File(TODOLOG_PATH_OUTPUT_EXPECTED+"/"+TODOLOG_AUTHORS[i]+".col");
//			assertTrue("number of lines of "+TODOLOG_AUTHORS[i],Math.abs(countLines(expectedFile)-countLines(actualFile))<countLines(expectedFile)*0.1);
//		}
	}
//	public static int countLines(File file) throws IOException {
//	    InputStream is = new BufferedInputStream(new FileInputStream(file));
//	    try {
//	        byte[] c = new byte[1024];
//	        int count = 0;
//	        int readChars = 0;
//	        boolean empty = true;
//	        while ((readChars = is.read(c)) != -1) {
//	            empty = false;
//	            for (int i = 0; i < readChars; ++i) {
//	                if (c[i] == '\n') {
//	                    ++count;
//	                }
//	            }
//	        }
//	        return (count == 0 && !empty) ? 1 : count;
//	    } finally {
//	        is.close();
//	    }
//	}
}
