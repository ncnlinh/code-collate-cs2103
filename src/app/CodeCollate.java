package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeCollate {
	private String[] _roots;
	private String[] _extensions;
	
	public static void main(String[] args) {
		System.out.println("Hello world!");
	}
	
	public CodeCollate(String[] args){
		 parseInput(args);
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
