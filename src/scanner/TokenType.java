package scanner;
/**
 * Basic token types given from A3.
 * @author nfagan
 *
 */
public enum TokenType {

	//invalid types
	COMMENT,
	ERR,
	
	//Bracket Types
	OPENRD,CLOSEDRD,
	OPENSQ,CLOSEDSQ,
	OPENCU,CLOSEDCU,
	
	
	//Value Types
	BOOL,
	NUMBER,
	CHAR,
	STRING,
	IDENTIFIER,
	
	//Keyword Types
	LAMBDA,
	DEFINE,
	LET,
	COND,
	IF,
	BEGIN,
	QUOTE, TICK;
	public static TokenType fromString(String s) {
		for ( TokenType t : TokenType.values()) {
			if(t.toString().equals(s)) return t;
		}
		return null;
	}
	
}
