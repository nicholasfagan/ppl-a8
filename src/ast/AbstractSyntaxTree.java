package ast;

import parser.ParseTree;

public class AbstractSyntaxTree  {
	Scope[] scopes;
	public AbstractSyntaxTree(ParseTree pt) {
		System.out.println(pt);
		if(pt != null && pt.getChildren() != null)
			scopes = new Scope[pt.getChildren().length-1];
		else
			scopes = new Scope[0];
		for(int i = 0; i < pt.getChildren().length-1; i++) {
			scopes[i] = (Scope) Expression.eval(pt.getChildren()[i].getChildren()[1].getChildren()[0]);
		}
	}
	public String toString() {
		String res = "";
		if (scopes != null) for (Scope s : scopes) if(s != null) res += s.toString();
		return res;
	}
}
