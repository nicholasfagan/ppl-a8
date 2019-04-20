package ast;

import parser.ParseTree;

public class Quote extends Expression {
	Expression e;
	public Quote(Expression parent, ParseTree expr) throws Exception {
		super(parent);
		e = Expression.eval(this,expr).get(0);
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
		return new Object[] {e};
	}

}
