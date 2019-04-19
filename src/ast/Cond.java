package ast;

import java.util.ArrayList;
import java.util.List;

import parser.ParseTree;

public class Cond implements Expression{
	
	List<CondBranch> branches;
	public Cond(ParseTree pt) {
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
