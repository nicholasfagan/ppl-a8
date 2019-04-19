package ast;

import parser.ParseTree;

public class CondBranch implements Expression{
	Expression condition;
	Sequence expressions;
	public CondBranch(ParseTree pt) {

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
