package parser;

import java.util.ArrayList;
import java.util.Iterator;

import scanner.Token;
/**
 * An iterator of tokens from an arraylist.
 * 
 * I needed a peek() and didnt want to implement Peekable,
 * but its there.
 * @author nfagan
 *
 */
public class TokenIterator implements Iterator<Token> {
	private int pos;
	private ArrayList<Token> tkarr;
	public TokenIterator(ArrayList<Token> tkarr) {
		this.tkarr = tkarr;
		this.pos = 0;
	}
	public Token peek() {
		return hasNext() ? tkarr.get(pos) : null; 
	}
	@Override
	public boolean hasNext() {
		return pos < tkarr.size();
	}
	@Override
	public Token next() {
		Token t = peek();
		if( t != null) pos++;
		return t;
	}

}
