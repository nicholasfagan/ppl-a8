package scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Reades the input file and turns it into tokens,
 * using greedy algorithm.
 * @author nfagan
 *
 */
public class SchemeScanner {
	public static ArrayList<Token> scan(InputStream in) {
		Scanner s = new Scanner(in);
		ArrayList<Token> tokens = new ArrayList<Token>();
		try {
			int line_number = 1;
			while(s.hasNextLine()) {
				Tokenizer tk = new Tokenizer(s.nextLine(), line_number++);
				while(tk.hasNext()) {
					Token t = tk.next();
					tokens.add(t);
				}
			}
		} catch (NoSuchElementException e) {
			s.close();
			throw new NoSuchElementException("LEXICAL ERROR: "  + e.getMessage());
		}
		s.close();
		return tokens;
		
	}
	public static void main(String[] args) {
		if(args.length < 1) {
			System.err.println("usage: SchemeScanner <in file> [<out file>]");
			System.err.flush();
			System.exit(1);
		}
		
		ArrayList<Token> tokens = new ArrayList<Token>();
		File f = new File(args[0]);
		try (InputStream in = new FileInputStream(f)) {
			tokens = scan(in);
		} catch (FileNotFoundException e1) {
			System.err.printf("Error: file not found: '%s'\n",args[0]);
			System.err.flush();
			System.exit(1);
		} catch (NoSuchElementException e2) {
			System.err.println("LEXICAL ERROR: "  + e2.getMessage());
			System.err.flush();
			System.exit(1);
		} catch (IOException e3) {
			System.err.println("LEXICAL ERROR: "  + e3.getMessage());
			System.err.flush();
			System.exit(1);
		}
		
		try {
			PrintStream out;
			if(args.length == 1) out = System.out;
			else out = new PrintStream(new File(args[1]));
			for(Token t : tokens)out.println(t.toString());
			out.close();
		} catch (FileNotFoundException e) {
			System.err.printf("Error: file not found: '%s'\n",args[1]);
			System.err.flush();
			System.exit(1);
		}
	}

}
