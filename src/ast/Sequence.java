package ast;

import java.util.ArrayList;
import java.util.List;

import parser.ParseTree;

public class Sequence implements Expression {

	List<Expression> children; //all the expressions in the scope
	public Sequence(ParseTree pt) {
		children = new ArrayList<Expression>();
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
		return this.children.toArray();
	}
}
