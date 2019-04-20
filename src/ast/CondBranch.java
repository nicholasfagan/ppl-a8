package ast;

import parser.ParseTree;

public class CondBranch extends Expression{
	Expression condition;
	Sequence expressions;
	public CondBranch(Expression parent,ParseTree pt) throws Exception {
		super(parent);
		condition = Expression.eval(this, pt.getChildren()[1]).get(0);
		expressions = new Sequence(this,pt.getChildren()[2]);
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
		return new Object[] {condition,expressions};
	}

}
