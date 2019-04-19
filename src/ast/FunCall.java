package ast;

import java.util.ArrayList;
import java.util.List;

import parser.ParseTree;

public class FunCall implements Expression {
	Identifier id; // what function is it calling?
	List<Expression> args; // what arguuments are given?

	public FunCall(ParseTree pt) {
		args = new ArrayList<Expression>();
		// TODO Auto-generated constructor stub
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
