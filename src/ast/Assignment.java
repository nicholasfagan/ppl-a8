package ast;

import parser.NonTerminal;
import parser.ParseTree;
import scanner.Token;

public class Assignment implements Expression {
	Identifier id;
	Expression expression;
	public Assignment(ParseTree pt) {
		if(pt.getData() != null && pt.getData() instanceof NonTerminal) {
			switch((NonTerminal)pt.getData()) {
			//if this is a definition
			case Definition:
				String id_name = ((Token)pt.getChildren()[1].getChildren()[1].getData()).getRep();
				id = new Identifier(id_name,0);
				//if its defining a function
				if(pt.getChildren()[1].getChildren()[2].getChildren().length != 0) //(if there are no args)
					expression = new Function(pt.getChildren()[1].getChildren()[2], pt.getChildren()[1].getChildren()[4]);
				break;
			case LetExpression:
				id_name = ((Token)pt.getChildren()[1].getChildren()[0].getData()).getRep();
				id = new Identifier(id_name,0);
				expression= new Function(pt.getChildren()[1].getChildren()[0], pt.getChildren()[2]);
				
				break;
			default:
				break;		
				
			}
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
