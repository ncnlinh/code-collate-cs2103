package app;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CodeCollate {
	private static Logger log = Logger.getLogger(CodeCollate.class.getSimpleName());
	private String[] _roots;
	private String[] _extensions;
	
	public static void main(String[] args) {
		System.out.println("Hello world!");
	}
	
	public CodeCollate(String[] args){
		 parseInput(args);
		 initialiseEnvironment();
		 openFiles();
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

	private void parseInput(String[] args){
		parseRoots(args);
		parseExtensions(args[args.length-1]);
	}

	private void parseExtensions(String extensionArg) {
		_extensions = extensionArg.split(", ");
	}

	private void parseRoots(String[] args) {
		_roots = Arrays.copyOfRange(args, 0, args.length-1);
	}
}
