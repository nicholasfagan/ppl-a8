package ast;

import parser.ParseTree;

public class If extends Expression{
	Expression condition;
	Expression thenExpr;
	Expression elseExpr; //optional
	public If(Expression parent,ParseTree pt) throws Exception {
		super(parent);
		condition = Expression.eval(this,pt.getChildren()[1]).get(0);
		thenExpr = Expression.eval(this,pt.getChildren()[2]).get(0);
		if(pt.getChildren()[3].getChildren().length > 0) {
			elseExpr = Expression.eval(this,pt.getChildren()[3].getChildren()[0].getChildren()[1].getChildren()[0]).get(0);
		} else elseExpr = null;
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
