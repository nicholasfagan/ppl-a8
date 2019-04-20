package ast;

import java.util.ArrayList;
import java.util.List;

import parser.ParseTree;

public class Sequence extends Expression {
	List<Expression> children; //all the expressions in the scope
	public Sequence(Expression parent,ParseTree pt) throws Exception {
		super(parent);
		children = new ArrayList<Expression>();
		children.addAll(Expression.eval(this,pt));
		
	
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
