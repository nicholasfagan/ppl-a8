package ast;

import java.util.ArrayList;
import java.util.List;
import parser.ParseTree;

public class AbstractSyntaxTree  {
	Scope scope;
	public AbstractSyntaxTree(ParseTree pt) {
		List<ParseTree> pts = new ArrayList<ParseTree>();
		pts = readProgram(pt, pts);
		List<Expression> ess = new ArrayList<Expression>();
		List<String> a = new ArrayList<String>();
		for(int i = 0; i < pts.size(); i++) {
			List<Expression> es = Expression.eval(pts.get(i));
			ess.addAll(es);
			for(Expression e : es) if(e instanceof Assignment)
				a.add(((Assignment)e).id.name);
		}
		scope = new Scope(ess,a);
	}
	static List<ParseTree> readProgram(ParseTree pt, List<ParseTree> l) {
		if(pt == null || pt.getChildren() == null || pt.getChildren().length == 0) {
			return l;
		}else {
			l.add(pt.getChildren()[0].getChildren()[1].getChildren()[0]);
			return readProgram(pt.getChildren()[1],l);
		}
	}
	public String toString() {
		return scope.toString();
	}
}
