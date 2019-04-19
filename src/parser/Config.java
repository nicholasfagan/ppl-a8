package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Parses Command line arguments.
 * @author nfagan
 *
 */
public class Config {
	private InputStream in;
	private PrintStream out;
	private boolean color;
	public Config(String... args) {
		ArrayList<String> arguments = new ArrayList<String>(Arrays.asList(args));
		
		if(arguments.size() == 0) usage();
		
		//get color argument
		int color = arguments.indexOf("-c");
		if(color >= 0) {
			arguments.remove(color);
			this.setColor(true);
		} else {
			this.setColor(false);
		}
		
		
		if(arguments.size() == 0) usage();
		
		//try to open the input file
		File infile = new File(arguments.get(0));
		if(infile.exists()) {
			if(infile.isFile()) {
				if(infile.canRead()) {
					try {
						this.setIn(new FileInputStream(infile));
					} catch (FileNotFoundException e) {
						err("error opening "+ arguments.get(0));
					}
				}else {
					err(arguments.get(0) + " cannot be read.");
				}
			}else {
				err(arguments.get(0) + " is a directory.");
			}
		} else {
			err(arguments.get(0) + " does not exist.");
		}
		
		arguments.remove(0);
		
		//set up output printstream
		if(arguments.size() == 0) {
			setOut(System.out);
		} else {
				try {
					setOut(new PrintStream(new File(arguments.get(0))));
					arguments.remove(0);
				} catch (FileNotFoundException e) {
					err("could not open " + arguments.get(0) + " for writing.");
				}
				
		}
		
		if(arguments.size() > 0) {
			String msg = "do not understand";
			for(String a : arguments) msg+=" '"+a+"'";
			err(msg);
		}
		
		
		
	}
	private void err(String msg) {
		System.err.println("Error in arguments: " + msg);
	}
	public void usage() {
			System.err.println("usage: java -classpath bin parser.Parser [-c] <input file> [<output file>]\n"
			                 + "[-c]                           Colorize output with ANSI escape sequences.\n"
			                 + "<input file>                     Scheme-- file to read as input. Required.\n"
			                 + "[<output file>]           Write parse tree output to file. Default stdout.");
			System.err.flush();
			System.exit(1);
	}
	public InputStream getIn() {
		return in;
	}
	public void setIn(InputStream in) {
		this.in = in;
	}
	public PrintStream getOut() {
		return out;
	}
	public void setOut(PrintStream out) {
		this.out = out;
	}
	public boolean isColor() {
		return color;
	}
	public void setColor(boolean color) {
		this.color = color;
	}
}
