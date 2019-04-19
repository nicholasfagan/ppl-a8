package ast;


import java.util.ArrayList;
import java.util.List;

import parser.NonTerminal;
import parser.ParseTree;
import scanner.Token;
import scanner.TokenType;

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
			if(e == null) {
				res += Split + "null\n";
				break;
			};
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
			} else {
				res += End + "null\n"; 
			}
		}
		
		
		return res;
		
	}
	static List<Expression> eval(ParseTree pt) {
		List<Expression> res = new ArrayList<Expression>();
		if(pt != null && pt.getData() instanceof NonTerminal) {
			NonTerminal nt = (NonTerminal)(pt.getData());
			switch(nt) {
			case BeginExpression:
				break;
			case CondExpression:
				break;
			case Definition:
				
				res.add( new Assignment(pt));
				return res;
			case FunCall:
				List<Expression> params = new ArrayList<Expression>();
				params = getExpr(pt.getChildren()[1],params);
				
				res.add( new FunCall(new Identifier(((Token)pt.getChildren()[0].getData()).getRep(),-1), params));
				return res;
			case IfExpression:
				res.add(new If(pt));
				return res;
			case Lambda:
				break;
			case LetExpression:
				// can be a named let 
				//   (which should be an assignment of a function followed by a funcall)
				// or a scope.
				if(pt.getChildren()[1].getChildren()[0].getData() instanceof Token 
						&& ((Token)pt.getChildren()[1].getChildren()[0].getData()).getType() == TokenType.IDENTIFIER) {
					//Its a named let
					Assignment a = new Assignment(pt);
					res.add(a);
					FunCall fc = (FunCall)((Function)a.expression).children.remove(((Function)a.expression).children.size()-1);
					res.add(fc);
					
					return res;
				} else {
					//its a scope.
					//return new Scope(pt);
					
				}
				break;
			case Expression:
				if(pt.getChildren().length > 1) {
					//bracketed
					return Expression.eval(pt.getChildren()[1].getChildren()[0]);
				}else {
					//non-bracketed
					return Expression.eval(pt.getChildren()[0].getChildren()[0]);
				}
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
		} else if (pt.getData() instanceof Token) {
			Token t = (Token)pt.getData();
			switch(t.getType()) {
			case BOOL:
				break;
			case CHAR:
				break;
			case IDENTIFIER:
				res.add(new Identifier(t.getRep(),-1));
				return res;
			case NUMBER:
				res.add(new Number(t.getRep()));
				return res;
			case STRING:
				break;
			default:
				break;
				
			}
		}
		return null;
	}
	static strictfp List<Expression> getExpr(ParseTree pt, List<Expression> l){
		if(pt == null || pt.getChildren() == null || pt.getChildren().length == 0) {
			return l;
		} else {
			l.addAll(Expression.eval(pt.getChildren()[0]));
			return getExpr(pt.getChildren()[1],l);
		}
		
	}

}
