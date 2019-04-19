package parser;

import scanner.Token;

/**
 * Internal Representation for the parsed tree.
 * 
 * Creates the Printable Tree Structure output.
 * @author nfagan
 *
 */
public class ParseTree {
	Object data;
	ParseTree[] children;
	public ParseTree(Object data) {
		this.data = data;
	}
	public ParseTree() {}
	
	private String vline = "\u2502";   // '|'   (but taller)
	private String hline = "\u2500";   // '-'   (but wider)
	private String vhsplit = "\u251C"; // '|-'  (but one char)
	private String vhend = "\u2514";   // 'L'   (but centered)
	
	private String VLine = vline + "  ";
	private String Split = vhsplit + hline + " ";
	private String End   = vhend + hline + " ";
	private String Space = "   ";
	
	private static final String ANSI_BOLD = "\u001B[1m";
	private static final String ANSI_UNBOLD = "\u001B[21m";
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_BLUE = "\u001B[34m";
			
	
	/**
	 * Recursively print the ParseTree, with coloring. 	
	 * @return
	 */
	public String color_toString() {
		String res= "";
		if(data instanceof Token) {
			switch(((Token)data).getType()) {
			case IDENTIFIER:case NUMBER:case CHAR:case BOOL:case STRING:
				res += ANSI_BOLD + ((Token)data ).getType() + " (" + ((Token)data).getRep() + ")" + ANSI_UNBOLD + ANSI_RESET +"\n";
				break;
			default:
				res += ANSI_BLUE + ((Token)data ).getType() + ANSI_RESET;
			}
		}else {
			res +=  ANSI_GREEN + data.toString() + "\n" + ANSI_RESET;
		}
		if (children != null && children.length > 0) {
			for(int i = 0; i < children.length; i++) {
				String cstr = children[i].color_toString();
				String[] lines = cstr.split("\n");
				res += ANSI_RED + (i == children.length-1 ? End : Split ) +ANSI_RESET+ lines[0] + "\n";
				for(int j = 1; j < lines.length; j++) {
					res +=  (i == children.length - 1 ? Space : ANSI_RED +VLine+ANSI_RESET) +lines[j] + "\n"; 
				}
			}
		}
		else if(! (data instanceof Token)) {
			res += ANSI_RED + End + ANSI_RESET;
		}
		return res;
		
	}
	public ParseTree[] getChildren() {
		return this.children;
		
	}
	public Object getData() {
		return this.data;
		
	}
	
	/**
	 * Recursively print the ParseTree 
	 */
	public String toString() {
		String res= "";
		if(data instanceof Token) {
			switch(((Token)data).getType()) {
			case IDENTIFIER:case NUMBER:case CHAR:case BOOL:case STRING:
				res += ((Token)data ).getType() + " (" + ((Token)data).getRep() + ")\n";
				break;
			default:
				res += ((Token)data ).getType() + "\n";
			}
		}else {
			res +=  data.toString() + "\n";
		}
		//Prepend all the children lines with out tree structure.
		if (children != null && children.length > 0) {
			for(int i = 0; i < children.length; i++) {
				String cstr = children[i].toString();
				String[] lines = cstr.split("\n");
				res += (i == children.length-1 ? End : Split ) + lines[0] + "\n";
				for(int j = 1; j < lines.length; j++) {
					res += (i == children.length - 1 ? Space : VLine) +lines[j] + "\n"; 
				}
			}
		}
		else if(! (data instanceof Token)) {
			res += End;
		}
		return res;
	}
}
