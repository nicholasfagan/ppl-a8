package parser;
import scanner.SchemeScanner;
import scanner.Token;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Main driver for the program.
 * @author nfagan
 *
 */
public class Parser {

	public static void main(String[] args) {
		Config config = new Config(args);
		
		//run the tokenizer.
		//If this fails, it will show its own error and halt the program.
		ArrayList<Token> tkarr = SchemeScanner.scan(config.getIn());
		
		//create an iterator of all the tokens.
		TokenIterator tki = new TokenIterator(tkarr);
		
		//Evaluate the token iterator into a parsetree.
		//If this fails, it will show its own error and halt the program.
		ParseTree pt = NonTerminal.Program.eval(tki);
		
		//print color if they want.
		if(config.isColor())
			config.getOut().println(pt.color_toString());
		else 
			config.getOut().println(pt);
	}
	public static ParseTree parse(InputStream in) {
		return NonTerminal.Program.eval(new TokenIterator(SchemeScanner.scan(in)));
	}
}
