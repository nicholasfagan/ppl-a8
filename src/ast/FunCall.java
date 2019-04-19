package ast;

import java.util.ArrayList;

import java.util.List;


public class FunCall extends Expression {
	Identifier id; // what function is it calling?
	List<Expression> args; // what arguuments are given?

	public FunCall(Expression parent,Identifier id) {
		super(parent);
		this.id = id;
		args = new ArrayList<Expression>();
	}
	public FunCall(Expression parent,Identifier id,List<Expression> args) {
		super(parent);
		this.id = id;
		this.args=args;
		
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
		Object[] a = args.toArray();
		Object[] res = new Object[a.length+1];
		res[0]=id;
		for(int i = 0; i < a.length; i++) res[i+1] = a[i];
		return res;
	}

}
