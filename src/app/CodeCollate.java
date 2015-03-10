package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CodeCollate {
	private static final String MESSAGE_ERROR_IO = "Failed to read code file %s";
	private static final String MESSAGE_INVALID_ROOTS = "Arguments contain invalid roots";
	private static final String MESSAGE_NO_EXTENSIONS = "Arguments contain no extensions";
	private static final String MESSAGE_MISSING_ARGUMENTS = "Missing arguments";
	private static final String MESSAGE_FILE_PREVENT_FOLDER_CREATION = "There is a file called \"collated\" prevents creation of folder";
	private static final String MESSAGE_DID_NOT_COLLATE_CODE = "Did not collate code";
	private static final String SYMBOL_ONE_SPACE = " ";
	private static final String SYMBOL_COMMA_AND_SPACE = ", ";
	private static final String SYMBOL_SLASH = "/";
	private static final String SYMBOL_DOUBLE_SLASH = "//";
	private static final String AUTHOR = "author";
	private static final String TAG_END_SEGMENT_ORIGIN = "\t// End of segment: %s";
	private static final String TAG_START_SEGMENT_ORIGIN = "\t/**\n\t/ * origin: %s\n\t */";
	private static final String TAG_START_FILE_AUTHOR = "//@author %s";
	private static final String EXTENSION_COL = ".col";
	private static final String PATH_COLLATED_FOLDER_RELATIVE = "collated/";
	private static Logger log = Logger.getLogger(CodeCollate.class.getSimpleName());
	private String[] _roots;
	private String[] _extensions;
	private List<String> _files;
	private enum CodeLineType {
		AUTHOR_COMMENT,
		COMMENT,
		REGULAR,
		BLANK
	}
	public static void main(String[] args) {
		CodeCollate collator = new CodeCollate(args);
		collator.collateAll();
	}
	
	private void collateAll() {
		acknowledgeCodeFiles(_roots, _extensions);
		collateFiles(_files);
		
	}
	void purgeDirectory(File dir) {
	    for (File file: dir.listFiles()) {
	        if (file.isDirectory()) purgeDirectory(file);
	        file.delete();
	    }
	}
	
	private void collateFiles(List<String> files) {
		for (String filePath: files){
			try {	
				readCode(filePath);
			} catch (IOException e) {
				throw new RuntimeException(String.format(MESSAGE_ERROR_IO, filePath));
			}
		}
	}

	private void readCode(String filePath) throws IOException {
		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(filePath));
		String line = reader.readLine();
		String currentAuthor = null;
		while (line != null) {
			CodeLineType lineType = identifyLine(line);
			switch (lineType) {
				case BLANK: // Fallthrough
				case COMMENT: // Fallthrough
				case REGULAR:
					addToAuthor(line,currentAuthor);
					break;
				case AUTHOR_COMMENT:
					if (currentAuthor != null) {
						insertEndSegment(currentAuthor, filePath);
					}
					String[] authorLine = line.split(SYMBOL_ONE_SPACE);
					String author = authorLine[authorLine.length-1];
					currentAuthor = author;
					insertStartSegment(currentAuthor, filePath);
					break;
			}
			line = reader.readLine();
		}
		if (currentAuthor!=null) {
			insertEndSegment(currentAuthor, filePath);
		}
		reader.close();
		
	}

	private void insertStartSegment(String author, String filePath) throws IOException {
		File collatedFile = new File(PATH_COLLATED_FOLDER_RELATIVE + author + EXTENSION_COL);
		if (!collatedFile.exists()) {
			collatedFile.createNewFile();
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(collatedFile,true)));
			pw.println(String.format(TAG_START_FILE_AUTHOR, author));
			pw.println();
			pw.println();
			pw.println();
			pw.close();
			
		}
		String origin = new File(filePath).getCanonicalPath();
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(collatedFile,true)));
		pw.println(String.format(TAG_START_SEGMENT_ORIGIN, origin));
		pw.println();
		pw.close();
		
	}

	private void insertEndSegment(String author, String filePath) throws IOException {
		File collatedFile = new File(PATH_COLLATED_FOLDER_RELATIVE+author+EXTENSION_COL);
		String origin = new File(filePath).getCanonicalPath();
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(collatedFile,true)));
		pw.println();
		pw.println(String.format(TAG_END_SEGMENT_ORIGIN, origin));
		pw.println();
		pw.println();
		pw.println();
		pw.println();
		pw.println();
		pw.close();
		
	}

	private void addToAuthor(String line, String author) throws IOException {
		if (author!=null) {
			File collatedFile = new File(PATH_COLLATED_FOLDER_RELATIVE+author+EXTENSION_COL);
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(collatedFile,true)));
			pw.println(line);
			pw.close();
		}
		
	}

	private CodeLineType identifyLine(String line) {
		if (line.trim().isEmpty()) {
			return CodeLineType.BLANK;
		}
		if (line.trim().startsWith(SYMBOL_DOUBLE_SLASH)) {
			if (line.toLowerCase().contains(AUTHOR)) {
				return CodeLineType.AUTHOR_COMMENT;
			}
			return CodeLineType.COMMENT;
		} 
		return CodeLineType.REGULAR;
	}

	private void acknowledgeCodeFiles(String[] roots, String[] extensions) {
		_files = new ArrayList<String>();
		for (int i = 0; i < roots.length; i++) {
			addFilesIn(roots[i],extensions);
		}
		
	}

	private void addFilesIn(String rootPath, String[] extensions) {
		File root = new File(rootPath);
		if (root.isDirectory()){
			String[] subPaths = (root.list());
			for (int i = 0; i < subPaths.length; i++) {
				if (rootPath.endsWith(SYMBOL_SLASH)) {
					addFilesIn(rootPath + subPaths[i], extensions);
				} else {
					addFilesIn(rootPath + SYMBOL_SLASH + subPaths[i], extensions);
				}
			}
		} else {
			String fileName = root.getName();
			String fileExtension = fileName.substring(fileName.lastIndexOf('.')+1);
			if (Arrays.asList(extensions).contains(fileExtension)) {
				_files.add(rootPath);
			}
		}
		
			
		
	}

	public CodeCollate(String[] args){
		 if (parseInput(args)){
			 this.initialiseEnvironment();
		 } else {
			 System.out.println(MESSAGE_DID_NOT_COLLATE_CODE);
		 }
	}
	
	
	private void initialiseEnvironment() {
		if (!createOutputFolder()) {
			log.log(Level.WARNING, "Cannot create \"collated\" folder, try changing the destination folder.");
		};
		
	}

	private boolean createOutputFolder() {
		File outputFolder = new File("collated");
		if (outputFolder.exists()) {
			if (outputFolder.isDirectory()) {
				log.log(Level.INFO,"Folder already created");
				purgeDirectory(outputFolder);
				return true;
			} else {
				System.out.println(MESSAGE_FILE_PREVENT_FOLDER_CREATION);
				log.log(Level.WARNING, MESSAGE_FILE_PREVENT_FOLDER_CREATION);
				return outputFolder.mkdir();
			}
		} else {
			return outputFolder.mkdir();
		}
	}


	private boolean parseInput(String[] args){
		if (args.length >= 2) {
			if (!(parseRoots(args) && parseExtensions(args[args.length-1]))) {
				return false;
			}
			return true;
		} else {
			System.out.println(MESSAGE_MISSING_ARGUMENTS);
			return false;
		}
	}

	private boolean parseExtensions(String extensionArg) {
		if (!isValidExtensionArgument(extensionArg)) {
			System.out.println(MESSAGE_NO_EXTENSIONS);
			return false;
		} else {
			_extensions = extensionArg.split(SYMBOL_COMMA_AND_SPACE);
			return true;
		}
	}

	private boolean isValidExtensionArgument(String extensionArg) {
		if (extensionArg.contains(SYMBOL_SLASH)) {
			return false;
		}
		return true;
	}
	private boolean parseRoots(String[] args) {
		_roots = Arrays.copyOfRange(args, 0, args.length-1);
		for (String root: _roots) {
			File f = new File(root);
		    if (!f.isDirectory())
		    	f = f.getParentFile();
		    if (!f.exists()){
		    	System.out.println(MESSAGE_INVALID_ROOTS);
				return false;
		    }
		}
		return true;
	}
}
