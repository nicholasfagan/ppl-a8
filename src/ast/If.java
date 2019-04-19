package ast;

import parser.ParseTree;

public class If implements Expression{

	Expression condition;
	Expression thenExpr;
	Expression elseExpr; //optional
	public If(ParseTree pt) {
		// TODO Auto-generated constructor stub
	}
	public String toString() {
		return this.string_rep();
	}
	@Override
	public String attributes() {
		return "";
	}
	@Override
	public Object[] children() {
		if(elseExpr == null)
			return new Object[] {condition,thenExpr};
		else return new Object[] {condition,thenExpr,elseExpr};
	}


}
