package app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CodeCollate {
	private static Logger log = Logger.getLogger(CodeCollate.class.getSimpleName());
	private String[] _roots;
	private String[] _extensions;
	private List<String> _files;
	
	public static void main(String[] args) {
		CodeCollate collator = new CodeCollate(args);
		collator.collateAll();
	}
	
	private void collateAll() {
		acknowledgeCodeFiles(_roots, _extensions);
		
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
				if (rootPath.endsWith("/")) {
					addFilesIn(rootPath + subPaths[i], extensions);
				} else {
					addFilesIn(rootPath + "/" + subPaths[i], extensions);
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
			 System.out.println("Did not collate code");
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
				return true;
			} else {
				System.out.println("There is a file called \"collated\" prevents creation of folder");
				log.log(Level.WARNING, "There is a file called \"collated\" prevents creation of folder");
				return outputFolder.mkdir();
			}
		} else {
			return outputFolder.mkdir();
		}
	}

	private void openFiles() {
		
	}

	private boolean parseInput(String[] args){
		if (args.length >= 2) {
			if (!(parseRoots(args) && parseExtensions(args[args.length-1]))) {
				return false;
			}
			return true;
		} else {
			System.out.println("Missing arguments");
			return false;
		}
	}

	private boolean parseExtensions(String extensionArg) {
		if (!isValidExtensionArgument(extensionArg)) {
			System.out.println("Arguments contain no extensions");
			return false;
		} else {
			_extensions = extensionArg.split(", ");
			return true;
		}
	}

	private boolean isValidExtensionArgument(String extensionArg) {
		if (extensionArg.contains("/")) {
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
		    	System.out.println("Arguments contain invalid roots");
				return false;
		    }
		}
		return true;
	}
}
