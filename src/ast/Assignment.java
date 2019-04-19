package ast;

import parser.NonTerminal;
import parser.ParseTree;
import scanner.Token;

public class Assignment implements Expression {
	Identifier id;
	Expression expression;
	public Assignment(ParseTree pt) {
		
		//if this is a definition
		if(pt.getData() != null && pt.getData() instanceof NonTerminal && ((NonTerminal)pt.getData()) == NonTerminal.Definition) {
			String id_name = ((Token)pt.getChildren()[1].getChildren()[1].getData()).getRep();
			id = new Identifier(id_name,0);
			//if its defining a function
			expression = new Function(pt.getChildren()[1].getChildren()[2], pt.getChildren()[1].getChildren()[4]);
		}
		
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
		return new Object[] {id, expression};
	}
	

}
