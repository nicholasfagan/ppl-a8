package ast;

import java.util.ArrayList;
import java.util.List;

import parser.ParseTree;

public class Cond extends Expression{
	
	List<CondBranch> branches;
	public Cond(Expression parent,ParseTree pt) throws Exception {
		super(parent);
		branches = new ArrayList<CondBranch>();
		List<ParseTree> cb = new ArrayList<ParseTree>();
		cb = getCondBranches(pt.getChildren()[1],cb);
		for(ParseTree t : cb) {
			branches.add(new CondBranch(this,t));
		}
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
