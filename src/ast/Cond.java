package ast;

import java.util.ArrayList;
import java.util.List;

import parser.ParseTree;

public class Cond extends Expression{
	
	List<CondBranch> branches;
	public Cond(Expression parent,ParseTree pt) {
		super(parent);
		branches = new ArrayList<CondBranch>();
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
		return branches.toArray();
	}

}
