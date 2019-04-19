package scanner;
import java.util.NoSuchElementException;

/**
 * A Peekable Iterator of Tokens.
 *  Uses regular expressions. (It Cheats)
 * @author nfagan
 */
public class Tokenizer {
	private String s;
	private int pos;
	private int line;
	
	Token peeked;
	
	/**
	 * Initialized a new Tokenizer for the current line.
	 * @param input The line of input
	 * @param line The line number
	 */
	public Tokenizer(String input, int line) {
		this.line=line;
		this.s=input;
		this.pos=0;
		this.peeked=null;
	}
	
	public boolean hasNext() {
		if(peeked == null) peeked = next();
		return peeked != null;
	}
	
	public Token next() {
		if(peeked != null) {
			Token tmp = peeked;
			peeked = null;
			return tmp;
		}
		
		//skip past whitespace
		while(pos < s.length() && is_whitespace(pos))
			pos++;
		
		//test for end of line
		if(pos == s.length()) return null;
	
		int next = next_break(pos);
		
		Token t = new Token(s.substring(pos,next),pos,line);
		if(t == null || t.getType() == TokenType.ERR) {
			//invalid? better let them know
			throw new NoSuchElementException("'"+s.substring(pos,next)+"' ["+pos+":"+(line+1)+"]");
		} else if (t.getType() == TokenType.COMMENT) {
			//comment, just skip past it.
			pos = s.length();
			return null;
		} else {
			//its good :)
			pos=next;
			return t;
		}
	} 
	private boolean is_whitespace(int i) {
		return " \t\r\n".contains(""+s.charAt(i));
	}
	private int next_break(int i) {
		if(is_break(i)) return i+1;
		if(s.charAt(i)=='"') {
			return s.substring(i+1).indexOf("\"")+pos+2;
		}
		else while(i < s.length() && !is_break(i)) i++;
		return i;
	}
	private boolean is_break(int i) {
		return " (){}[]';".contains("" + s.charAt(i));
	}
}
