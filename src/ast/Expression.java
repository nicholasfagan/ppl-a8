package ast;


import java.util.ArrayList;
import java.util.List;

import parser.NonTerminal;
import parser.ParseTree;
import scanner.Token;
import scanner.TokenType;

public class Expression {
	Expression parent;
	Expression (Expression parent) {
		this.parent=parent;
	}
	String attributes() {return null;};
	Object[] children() {return null;};
	static final String vline = "\u2502";   // '|'   (but taller)
	static final String hline = "\u2500";   // '-'   (but wider)
	static final String vhsplit = "\u251C"; // '|-'  (but one char)
	static final String vhend = "\u2514";   // 'L'   (but centered)
	
	static final String VLine = vline + "  ";
	static final String Split = vhsplit + hline + " ";
	static final String End   = vhend + hline + " ";
	static final String Space = "   ";
	public String string_rep() {
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
	static List<Expression> eval(Expression parent,ParseTree pt) throws Exception {
		List<Expression> res = new ArrayList<Expression>();
		if(pt != null && pt.getData() instanceof NonTerminal) {
			NonTerminal nt = (NonTerminal)(pt.getData());
			switch(nt) {
			case BeginExpression:
				Sequence seq = new Sequence(parent,pt.getChildren()[1]);
				res.add(seq);
				return res;
			case CondExpression:
				Cond c = new Cond(parent,pt);
				res.add(c);
				return res;
			case Definition:
				res.add( new Assignment(parent,pt));
				return res;
			case FunCall:
				List<Expression> params = new ArrayList<Expression>();
				params = getExpr(parent,pt.getChildren()[1],params);
				res.add( new FunCall(parent,new Identifier(parent,((Token)pt.getChildren()[0].getData()).getRep(),-1), params));
				return res;
			case IfExpression:
				res.add(new If(parent,pt));
				return res;
			case Lambda:
				Function f = new Function(parent,pt.getChildren()[2],pt.getChildren()[4]);
				res.add(f);
				return res;
			case LetExpression:
				// can be a named let 
				//   (which should be an assignment of a function followed by a funcall)
				// or a scope.
				
				if(pt.getChildren()[1].getChildren()[0].getData() instanceof Token 
						&&
						((Token)(pt.getChildren()[1].getChildren()[0].getData())).getType() == TokenType.IDENTIFIER) {
					//Its a named let
					Assignment a = new Assignment(parent,pt);
					res.add(a);
					//named let has to call itself. grab the funcall (assignment will have made it last child).
					FunCall fc = (FunCall)((Function)a.expression).children.remove(((Function)a.expression).children.size()-1);
					res.add(fc);
					
					return res;
				} else {
					//its a scope.
					List<Expression> stmts = new ArrayList<Expression>();
					List<ParseTree> x = new ArrayList<ParseTree>();
					x = getStmts(pt.getChildren()[2],x);
					
					List<String> vars = new ArrayList<String>();
					List<ParseTree> vardefs = new ArrayList<ParseTree>();
					vardefs = getVarDefs(pt.getChildren()[1].getChildren()[1],vardefs);
					
					Scope s = new Scope(parent,stmts,vars);
					
					for(ParseTree p : vardefs) {
						String vid = ((Token)p.getChildren()[1].getData()).getRep();
						Assignment a = new Assignment(s,vid,eval(s,p.getChildren()[2]).get(0));
						vars.add(vid);
						stmts.add(a);
					}
					
					
					for(ParseTree p : x) stmts.addAll(Expression.eval(s, p));
					res.add(s);
					
					return res;
				}
			case Expression:
				if(pt.getChildren().length > 1) {
					//bracketed
					return Expression.eval(parent,pt.getChildren()[1].getChildren()[0]);
				}else {
					//non-bracketed
					return Expression.eval(parent,pt.getChildren()[0].getChildren()[0]);
				}
			case TickQuoteExpression:
			case QuoteExpression:
				res.add(new Quote(parent,pt.getChildren()[1]));
				return res;
			case Statement:
				break;
			case Statements:
				List<ParseTree> stmts = new ArrayList<ParseTree>();
				stmts = getStmts(pt,stmts);
				for(ParseTree t : stmts) {
					res.addAll(Expression.eval(parent,t));
				}
				return res;
			case VarDef:
				break;
			default:
				break;
			}
		} else if (pt.getData() instanceof Token) {
			Token t = (Token)pt.getData();
			switch(t.getType()) {
			case BOOL:
				res.add(new ASTBool(parent,t.getRep()));
				return res;
			case CHAR:
				res.add(new ASTChar(parent,t.getRep()));
				return res;
			case IDENTIFIER:
				res.add(new Identifier(parent,t.getRep()));
				return res;
			case NUMBER:
				res.add(new Number(parent,t.getRep()));
				return res;
			case STRING:
				res.add(new ASTString(parent,t.getRep()));
				return res;
			default:
				break;
				
			}
		}
		throw new Exception("Dunno:" + pt);
	}
	static strictfp List<Expression> getExpr(Expression parent,ParseTree pt, List<Expression> l) throws Exception{
		if(pt == null || pt.getChildren() == null || pt.getChildren().length == 0) {
			return l;
		} else {
			List<Expression> e = Expression.eval(parent, pt.getChildren()[0]);
			if(e != null) l.addAll(e);
			if(pt.getChildren()[1].getChildren() == null || pt.getChildren()[1].getChildren().length == 0)
				return l;
			else 
				return getExpr(parent,pt.getChildren()[1],l);
		}
		
	}
	static List<ParseTree> getVarDefs(ParseTree t, List<ParseTree> l) {
		if(t == null || t.getChildren() == null || t.getChildren().length == 0) {
			return l;
		} else {
			l.add(t.getChildren()[0]);
			if(t.getChildren()[1].getChildren() == null || t.getChildren()[1].getChildren().length == 0) {
				return l;
			}
			else
				return getVarDefs(t.getChildren()[1].getChildren()[0],l);
		}
	}
	static List<ParseTree> getStmts(ParseTree t, List<ParseTree> l) {
		if(t == null || t.getChildren() == null || t.getChildren().length == 0) {
			return l;
		} else {
			if(! ((t.getChildren()[0].getData()) instanceof Token )) {
				l.add(t.getChildren()[0].getChildren()[0].getChildren()[0]);
				if(t.getChildren()[0].getChildren()[1] != null && t.getChildren()[0].getChildren()[1].getChildren() != null && t.getChildren()[0].getChildren()[1].getChildren().length != 0)
					l = getStmts(t.getChildren()[0].getChildren()[1].getChildren()[0],l);
				if(t.getChildren()[1].getChildren() != null && t.getChildren()[1].getChildren().length != 0) {
					List<ParseTree> a = getStmts(t.getChildren()[1].getChildren()[0],l);
					return a;
				} else return l;
			} else {
				l.add(t.getChildren()[1].getChildren()[0].getChildren()[0]);
				if(t.getChildren()[1].getChildren().length > 2 && t.getChildren()[1].getChildren()[2] != null && t.getChildren()[1].getChildren()[2].getChildren() != null && t.getChildren()[1].getChildren()[2].getChildren().length != 0) {
					List<ParseTree> a =  getStmts(t.getChildren()[1].getChildren()[2].getChildren()[0],l);
					return a;
				}else return l;
			}
			
		}
	}
	static List<ParseTree> getCondBranches(ParseTree t, List<ParseTree> l) {
		if(t == null || t.getChildren() == null || t.getChildren().length == 0) {
			return l;
		} else {
			l.add(t.getChildren()[0]);
			if(t.getChildren()[1].getChildren() != null && t.getChildren()[1].getChildren().length != 0)
				return getCondBranches(t.getChildren()[1].getChildren()[0],l);
			else return l;
		}
	}

}
