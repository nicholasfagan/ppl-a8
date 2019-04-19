package ast;

import java.util.ArrayList;
import java.util.List;

import parser.NonTerminal;
import parser.ParseTree;

public class Scope implements Expression {

	List<Expression> children; //all the expressions in the scope
	List<String> attributes; //list of IDENTIFIERS for things declared in this scope.
	
	public Scope(List<Expression> children, List<String> attributes) {
		this.children = children;
		this.attributes = attributes;
	}
	public void addId(String id) {
		attributes.add(id);
	}
	public String toString() {
		return this.string_rep();
	}

	@Override
	public String attributes() {
		String res = "";
		for (int i = 0; i < attributes.size()-1; i++) res += attributes.get(i) + " ";
		if(attributes.size() > 0) res += attributes.get(attributes.size()-1);
		return (res == "" ? " " : res);
	}
	@Override
	public Object[] children() {
		return this.children.toArray();
	}

}
