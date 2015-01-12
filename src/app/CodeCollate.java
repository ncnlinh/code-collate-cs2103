package app;

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
		parseExtensions(args[args.length]);
	}

	private void parseExtensions(String extensionArg) {
		//TODO
	}

	private void parseRoots(String[] args) {
		//TODO
		for (int i = 0; i< args.length-1; i++) { 
		}
	}
}
