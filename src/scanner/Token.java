package scanner;
/**
 * Represents a token.
 * 
 * Contains its type and actual string representation,
 * as well as its line number and position in the line.
 * @author nfagan
 *
 */
public class Token {


	private String rep;
	private TokenType type;
	private int pos;
	private int line;
	public String toString() {
		return String.format("%s [%d:%d] %s", getType(),this.line,this.pos+1,getRep());
	}
	/**
	 * Create a new Token from the serialized form.
	 * (Used when reading the scanner output.)
	 * @param serialized
	 */
	public Token(String serialized) {
		String[] parts = serialized.split(" ",3);
		if(parts.length != 3) return;
		TokenType ts = TokenType.fromString(parts[0]);
		String lns[] = parts[1].substring(1,parts[1].length()-1).split(":",2);
		this.setType(ts);
		this.setLine(Integer.parseInt(lns[0]));
		this.setPos(Integer.parseInt(lns[1])-1);
		this.setRep(parts[2]);		
		return;
	}
	
	/**
	 * Try to create a new Token from a substring of user input.
	 * @param input
	 * @param pos
	 * @param line
	 */
	public Token(String input, int pos, int line) {
		this.setRep(input);
		this.setPos(pos);
		this.setLine(line);
		this.setType(null);
		//empty string
		if(input.length() == 0) {
			this.setType(TokenType.ERR);
			return;
		}
		// First try single char tokens
		switch(input) {
			case "(":this.setType(TokenType.OPENRD);break;
			case ")":this.setType(TokenType.CLOSEDRD);break;
			case "[":this.setType(TokenType.OPENSQ);break;
			case "]":this.setType(TokenType.CLOSEDSQ);break;
			case "{":this.setType(TokenType.OPENCU);break;
			case "}":this.setType(TokenType.CLOSEDCU);break;
			case "'":this.setType(TokenType.TICK);break;
		}
		if(this.getType() != null) return;
		//then constant size
		if(input.matches("#[tf]")) {
			this.setType(TokenType.BOOL);
		}else if(input.matches("#\\\\\\\\(.|space|newline|tab|[0-3][0-7]{2})")) {
			this.setType(TokenType.CHAR);
		}
		//then variable length.
		else if (input.matches("[-+]?[0-9]+|0x[0-9a-fA-F]+|0b[01]+")
				|| input.matches("[-+]?[0-9]+\\.[0-9]+([eE][-+]?[0-9]+)?|[-+]?[0-9]+[eE][-+]?[0-9]+")) {
			this.setType(TokenType.NUMBER);
		} else if (input.matches("\"([^\\\"]|\\\\([tn]|[0-3][0-7]{2}))*\"")) {
			this.setType(TokenType.STRING);
		}
		//comment?
		else if(input.matches(";.*")) {
			this.setType(TokenType.COMMENT);
		} 
		//KeyWords or identifier?
		else {
			switch(input) {
			case "lambda":this.setType(TokenType.LAMBDA);break;
			case "define":this.setType(TokenType.DEFINE);break;
			case "let":this.setType(TokenType.LET);break;
			case "cond":this.setType(TokenType.COND);break;
			case "if":this.setType(TokenType.IF);break;
			case "begin":this.setType(TokenType.BEGIN);break;
			case "quote":this.setType(TokenType.QUOTE);break;
			default:
				//must be identifier?
				if(input.matches("[^\\]\\[\\(\\)\\{\\};0-9'\"# ][^\\]\\[\\(\\)\\{\\};'\"# ]*")) {
					this.setType(TokenType.IDENTIFIER);
				}else {
					this.setType(TokenType.ERR);
				}
			}
		}
			
		
	}
	public TokenType getType() {
		return type;
	}
	public void setType(TokenType type) {
		this.type = type;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public String getRep() {
		return rep;
	}
	public void setRep(String rep) {
		this.rep = rep;
	}
	
}
