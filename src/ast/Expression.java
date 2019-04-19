package ast;

import java.util.ArrayList;
import java.util.List;

import parser.NonTerminal;
import parser.ParseTree;

public interface Expression {
	String attributes();
	Object[] children();
	static final String vline = "\u2502";   // '|'   (but taller)
	static final String hline = "\u2500";   // '-'   (but wider)
	static final String vhsplit = "\u251C"; // '|-'  (but one char)
	static final String vhend = "\u2514";   // 'L'   (but centered)
	
	static final String VLine = vline + "  ";
	static final String Split = vhsplit + hline + " ";
	static final String End   = vhend + hline + " ";
	static final String Space = "   ";
	public default String string_rep() {
		String res = "";
		if(attributes()!= "")
			res += this.getClass().getSimpleName() + " [" + this.attributes() + "]\n";
		else res += this.getClass().getSimpleName() + "\n";
		for(int i = 0; i < children().length-1; i++) {	
			Object e = children()[i];
			if(e == null) break;
			String[] lines = e.toString().split("\n");
			res += Split + lines[0] + "\n";
			for(int j = 1; j < lines.length; j++)
				res += VLine + lines[j] + "\n";
			
		}
		if(children().length > 0) {
			Object e = children()[children().length-1];
		
			if(e != null) {
				String[] lines = e.toString().split("\n");
				res += End + lines[0] + "\n";
				for(int j = 1; j < lines.length; j++)
					res += Space + lines[j] + "\n";
			}
		}
		
		
		return res;
		
	}
	static Expression eval(ParseTree pt) {
		if(pt != null && pt.getData() instanceof NonTerminal) {
			NonTerminal nt = (NonTerminal)(pt.getData());
			switch(nt) {
			case BeginExpression:
				break;
			case CondExpression:
				break;
			case Definition:
				List<String> a = new ArrayList<String>();
				List<Expression> e = new ArrayList<Expression>();

				return new Scope(e,a);
			case FunCall:
				break;
			case IfExpression:
				break;
			case Lambda:
				break;
			case LetExpression:
				break;
			case Program:
				break;
			case QuoteExpression:
				break;
			case Statement:
				break;
			case Statements:
				break;
			case TickQuoteExpression:
				break;
			case VarDef:
				break;
			default:
				break;
			}
		}
		return null;
	}

}
