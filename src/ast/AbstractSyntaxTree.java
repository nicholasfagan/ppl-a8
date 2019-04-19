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
		scope = new Scope(null,ess,a);
		for(int i = 0; i < pts.size(); i++) {
			List<Expression> es = Expression.eval(scope,pts.get(i));
			ess.addAll(es);
			for(Expression e : es) if(e instanceof Assignment)
				a.add(((Assignment)e).id.name);
		}
		try {
			resolve_names(scope);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	static void resolve_names(Expression e) throws Exception {
		if (e != null) {
			if(e instanceof Identifier ) {
				((Identifier)e).resolve_hops();
		//		throw new Exception();
			}
			else if(e instanceof Scope) {
				for( Expression i : ((Scope)e).children)
					resolve_names(i);
			}
			else if (e instanceof Function) { 
				for( Expression i : ((Function)e).children)
					resolve_names(i);
			}
			else if (e instanceof Sequence) { 
				for( Expression i : ((Sequence)e).children)
					resolve_names(i);
			}
			else if (e instanceof Assignment) { 
					resolve_names(((Assignment)e).expression);
			}
			else if (e instanceof Cond) {
				for(CondBranch b : ((Cond)e).branches) {
					resolve_names(b.condition);
					resolve_names(b.expressions);
				}
			}
			else if (e instanceof If) {
				resolve_names(((If)e).condition);
				resolve_names(((If)e).thenExpr);
				resolve_names(((If)e).elseExpr);
			}
			else if (e instanceof FunCall) {
				resolve_names(((FunCall)e).id);
				for(Expression i : ((FunCall)e).args)
					resolve_names(i);
			}
		}
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
