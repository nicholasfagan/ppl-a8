package ast;

import parser.ParseTree;

public class CondBranch extends Expression{
	Expression condition;
	Sequence expressions;
	public CondBranch(Expression parent,ParseTree pt) {
		super(parent);

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
		return new Object[] {condition,expressions};
	}

}
