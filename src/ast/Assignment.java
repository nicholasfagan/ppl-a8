package ast;

import java.util.ArrayList;
import java.util.List;

import parser.NonTerminal;
import parser.ParseTree;
import scanner.Token;

public class Assignment extends Expression {
	Identifier id;
	Expression expression;
	public Assignment(Expression parent, String vid, Expression expression) {
		super(parent);
		this.id=new Identifier(this,vid,0);
		this.expression=expression;
	}
	public Assignment(Expression parent, ParseTree pt) {
		super(parent);
		if(pt.getData() != null && pt.getData() instanceof NonTerminal) {
			switch((NonTerminal)pt.getData()) {
			//if this is a definition
			case Definition:
				String id_name = ((Token)pt.getChildren()[1].getChildren()[1].getData()).getRep();
				id = new Identifier(this,id_name,0);
				
				//if its defining a function
				if(pt.getChildren()[1].getChildren()[2].getChildren().length != 0) //(if there are no args)
					expression = new Function(this, pt.getChildren()[1].getChildren()[2], pt.getChildren()[1].getChildren()[4]);
				break;
			case LetExpression:
				// named let
				id_name = ((Token)pt.getChildren()[1].getChildren()[0].getData()).getRep();
				id = new Identifier(this,id_name,0);
				Function f = new Function(this, pt.getChildren()[1].getChildren()[0], pt.getChildren()[2]);
				expression = f;
				//get any declarations (the func name)
				for(Expression e : f.children) {
					if(e instanceof Assignment) {
						f.attributes.add(((Assignment)e).id.name);
					}
				}
				//get the args and default values;
				List<ParseTree> vardefs = new ArrayList<ParseTree>();
				vardefs = getVarDefs(pt.getChildren()[1].getChildren()[2],vardefs);
				FunCall fc = new FunCall(parent,this.id);
				f.children.add(fc);
				for(ParseTree vd : vardefs) {
					fc.args.addAll(Expression.eval(fc,vd.getChildren()[2]));
					f.arguments.add(((Token)vd.getChildren()[1].getData()).getRep());
				}
				
				break;
			default:
				break;		
				
			}
		}
		
		// TODO Auto-generated constructor stub
	}
	/*public List<ParseTree> getVarDefs(ParseTree t, List<ParseTree> l) {
		if(t == null || t.getChildren() == null || t.getChildren().length == 0) {
			return l;
		} else {
			l.add(t.getChildren()[0]);
			if(t.getChildren()[1].getChildren() == null)
				return l;
			else 
				return getVarDefs(t.getChildren()[1].getChildren()[0],l);
		}
	}*/
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
