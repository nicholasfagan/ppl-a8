package ast;

import java.util.ArrayList;
import java.util.List;

import parser.ParseTree;
import scanner.Token;

public class Function extends Expression {
	List<Expression> children; //all the expressions in the scope
	List<String> attributes; //list of IDENTIFIERS for things declared in this scope.
	List<String> arguments;
	public Function(Expression parent,ParseTree args, ParseTree body) {
		super(parent);
		children = new ArrayList<Expression>();
		attributes = new ArrayList<String>();
		arguments = new ArrayList<String>();
		arguments = getArgs(args,arguments);
		children = getStmts(body,children);
		for(Expression e : children) {
			if(e instanceof Assignment) {
				attributes.add( ((Assignment)e).id.name );
			}
		}
	}
	private List<Expression> getStmts(ParseTree pt, List<Expression> l) {
		if(pt == null || pt.getChildren() == null || pt.getChildren().length == 0) {
			return l;
		} else {
			ParseTree p = pt.getChildren()[1].getChildren()[0].getChildren()[0];
			l.addAll(Expression.eval(this, p));
			return getStmts(pt.getChildren()[1].getChildren()[2],l);
		}
		
	}
	private List<String> getArgs(ParseTree arglist, List<String> a) {
		if(arglist == null || arglist.getChildren() == null || arglist.getChildren().length == 0) {
			return a;
		}else {
			String arg = ((Token)arglist.getChildren()[0].getData()).getRep();
			a.add(arg);
			return getArgs(arglist.getChildren()[1],a);
		}
	}
	
	public String toString() {
		return this.string_rep();
	}
	@Override
	public String attributes() {
		String res = "";
		for(int i = 0; i < arguments.size() - 1; i++) res += arguments.get(i) + " ";
		if(arguments.size() > 0) res += arguments.get(arguments.size()-1) + ";";
		for (int i = 0; i < attributes.size(); i++) res += " " + attributes.get(i) ;
		return res;
	}
	@Override
	public Object[] children() {
		return this.children.toArray();
	}


}
