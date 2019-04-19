package parser;

import scanner.Token;
import scanner.TokenType;
/**
 * This is the Recursive Descent Parser.
 * 
 * Each type of the Enum is a Non-Terminal in the grammar.txt.
 * 
 * The eval() for each non-terminal decides what to parse it as
 * using the next token from the iterator and the 
 * predict set of the grammar.
 * 
 * @author nfagan
 *
 */
public enum NonTerminal {
	Program,
	TopLevelForm,
	InnerTopLevelForm,
	Definition,
	InnerDefinition,
	ArgList,
	Statements,
	StatementsCU,
	StatementsSQ,
	StatementsRD,
	StatementsNoBrackets,
	OptionalStatements,
	Statement,
	BracketedStatement,
	Expressions,
	Expression,
	BracketedExpression,
	NonBracketedExpression,
	FunCall,
	Lambda,
	TickQuoteExpression,
	QuoteExpression,
	BeginExpression,
	LetExpression,
	InnerLetExpression,
	VarDefs,
	MoreVarDefs,
	VarDef,
	IfExpression,
	ElseExpression,
	CondExpression,
	CondBranches,
	MoreCondBranches,
	CondBranch
	;
	/**
	 * @param l
	 * @param r
	 * @return If l and r form a pair of matching open and close brackets.
	 */
	private boolean brack_match(Token l, Token r) {
		return l != null && r != null &&(
				(l.getType() == TokenType.OPENSQ && r.getType() == TokenType.CLOSEDSQ)
				||(l.getType() == TokenType.OPENCU && r.getType() == TokenType.CLOSEDCU)
				||(l.getType() == TokenType.OPENRD && r.getType() == TokenType.CLOSEDRD) 
			);
	}
	private void err(Token t) {
		System.err.println("SYNTAX ERROR ["+t.getLine()+":"+(t.getPos()+1)+"]: Unexpected token '" + t.getRep() + "'" );
		System.err.flush();
		System.exit(1);
	}
	private void eoferr() {
		System.err.println("SYNTAX ERROR: Unexpected EOF");
		System.err.flush();
		System.exit(1);
	}
	/**
	 * Evaluates the token iterator into a parsetree.
	 * 
	 * I have tried my best to annotate each case with the predict set
	 * that was used to make that decision.
	 * 
	 * @param ti The Token Iterator currently being used.
	 * @return The ParseTree decided by the grammar.
	 */
	public ParseTree eval(TokenIterator ti) {
		ParseTree pt = new ParseTree(this);
		switch(this) {
		case Program:
			//Program => ! ( use ! as epsilon)
			if(!ti.hasNext()) return pt;
			
			//Program => TopLevelForm Program
			pt.children = new ParseTree[] { TopLevelForm.eval(ti), Program.eval(ti) };
			return pt;
		
		case ArgList:
			if(!ti.hasNext()) eoferr();

			//ArgList => !
			if(ti.peek().getType() != TokenType.IDENTIFIER) return pt;
			//ArgList => identifier ArgList
			
			
			ParseTree id = new ParseTree();
			id.data = ti.next();
			pt.children = new ParseTree[] { id, ArgList.eval(ti)};
			
			return pt;
		
		case BeginExpression:
			
			if(!ti.hasNext()) eoferr();
			else if(ti.peek().getType() != TokenType.BEGIN) err(ti.peek());
			else {
				//BeginExpr => begin Stmts
				ParseTree begin = new ParseTree();
				begin.data = ti.next();
				pt.children = new ParseTree[] { begin, Statements.eval(ti) };
				return pt;
			}
		
		case BracketedExpression:
			
			if(!ti.hasNext()) eoferr();
			
			//go through predict set of BrackExpr
			Token next = ti.peek();
			switch(next.getType()) {
			case IDENTIFIER://BrackExpr => FunCall
				pt.children = new ParseTree[] { FunCall.eval(ti) };
				break;
			case LAMBDA://BrackExpr => Lambda
				pt.children = new ParseTree[] { Lambda.eval(ti) };
				break;
			case QUOTE: //BrackExpr => QuoteExpr
				pt.children = new ParseTree[] { QuoteExpression.eval(ti) };
				break;
			case BEGIN://BrackExpr => BeginExpr
				pt.children = new ParseTree[] { BeginExpression.eval(ti) };
				break;
			case LET://BrackExpr => LetExpr
				pt.children = new ParseTree[] { LetExpression.eval(ti) };
				break;
			case IF://BrackExpr => IfExpr
				pt.children = new ParseTree[] { IfExpression.eval(ti) };
				break;
			case COND://BrackExpr => CondExpr
				pt.children = new ParseTree[] { CondExpression.eval(ti)};
				break;
			default:
			}
			return pt;
			
		case BracketedStatement:
			
			if(!ti.hasNext()) eoferr();
			
			Token posdef = ti.peek();
			if(posdef.getType() == TokenType.DEFINE) 
				pt.children = new ParseTree[] { Definition.eval(ti) };
			else
				pt.children = new ParseTree[] { BracketedExpression.eval(ti) };
			return pt;
		
		case CondBranch:
			
			//CondBranch => ( Expr Stmts )
			
			if(!ti.hasNext()) eoferr();
			
			Token openbr = ti.next();
			if(openbr.getType() != TokenType.OPENCU 
					&& openbr.getType() != TokenType.OPENRD
					&& openbr.getType() != TokenType.OPENSQ) err(openbr);
			
			ParseTree expr = Expression.eval(ti);
			ParseTree stmts = Statements.eval(ti);
			
			if(!ti.hasNext()) eoferr();
			
			Token closebr = ti.next();
			
			
			if(!brack_match(openbr,closebr)) err(closebr);
			
			ParseTree open = new ParseTree();
			open.data = openbr;

			ParseTree close = new ParseTree();
			close.data = closebr;
			
			pt.children = new ParseTree[] { open, expr, stmts, close };
			return pt;
		
		case CondBranches:
			
			//CondBranches => CondBranch MoreCondBranches
			pt.children = new ParseTree[] { CondBranch.eval(ti), MoreCondBranches.eval(ti) };
			return pt;
		
		case CondExpression:
			//CondExpr => cond CondBranches
			if(!ti.hasNext()) eoferr();
			Token cond = ti.next();
			if(cond.getType() != TokenType.COND) err(cond);
			ParseTree condT = new ParseTree();
			condT.data = cond;
			pt.children = new ParseTree[] { condT, CondBranches.eval(ti) };
			return pt;
		
		case Definition:
			//Def => define InDef
			if(!ti.hasNext()) eoferr();
			Token def = ti.next();
			if(def.getType() != TokenType.DEFINE) err(def);
			ParseTree defT = new ParseTree();
			defT.data = def;
			pt.children = new ParseTree[] { defT, InnerDefinition.eval(ti) };
			return pt;
		
		case ElseExpression:
			
			if(!ti.hasNext()) eoferr();
			
			switch(ti.peek().getType()) {
			case OPENRD:case OPENSQ: case OPENCU:
			case IDENTIFIER: case NUMBER: case CHAR: case BOOL: case STRING: case TICK:
			//Else => Expression
			pt.children = new ParseTree[] { Expression.eval(ti)};
				return pt;
			//Else => !
			//(if not in rest of predict)
				default:
					return pt;
			}
		
		case Expression:
			
			if(!ti.hasNext()) eoferr();
			
			openbr = ti.peek();
			if(openbr.getType() != TokenType.OPENCU 
					&& openbr.getType() != TokenType.OPENRD
					&& openbr.getType() != TokenType.OPENSQ) {
			//Expr => NoBrackExpr
				pt.children = new ParseTree[] { NonBracketedExpression.eval(ti) };
			}else {
				openbr = ti.next();
			//Expr => ( BrackExpr )
			
				ParseTree brackexpr = BracketedExpression.eval(ti);
				
				if(!ti.hasNext()) eoferr();
				
				closebr = ti.next();
				
			if(!brack_match(openbr,closebr)) err(closebr);
				
				open = new ParseTree();
				open.data = openbr;

				close = new ParseTree();
				close.data = closebr;
				
				pt.children = new ParseTree[] { open, brackexpr, close };
			}
			return pt;
		
		case Expressions:
			
			if(!ti.hasNext()) eoferr();
			
			switch(ti.peek().getType()) {
			case OPENRD:case OPENSQ: case OPENCU:
			case IDENTIFIER: case NUMBER: case CHAR: case BOOL: case STRING: case TICK:
			//Exprs => Expr Exprs
			pt.children = new ParseTree[] { Expression.eval(ti), Expressions.eval(ti)};
				return pt;
			//Exprs => !
			//(if not in rest of predict)
				default:
					return pt;
			}
		
		case FunCall:
			
			if(!ti.hasNext()) eoferr();
			
			if(ti.peek().getType() != TokenType.IDENTIFIER) err(ti.next());
			id = new ParseTree();
			id.data = ti.next();
			pt.children = new ParseTree[] { id, Expressions.eval(ti) };
			return pt;
		
		case IfExpression:
			
			if(!ti.hasNext()) eoferr();
			
			if(ti.peek().getType() != TokenType.IF) err(ti.next());
			ParseTree ifex = new ParseTree();
			ifex.data = ti.next();
			pt.children = new ParseTree[] { ifex, Expression.eval(ti), Expression.eval(ti), ElseExpression.eval(ti) };
			return pt;

		case InnerDefinition:
			
			if(!ti.hasNext()) eoferr();
			
			//InDef => identifier Expr
			if(ti.peek().getType() == TokenType.IDENTIFIER) {
				id = new ParseTree();
				id.data = ti.next();
				pt.children = new ParseTree[] { id, Expression.eval(ti) };
			}else {
				//InDef => ( identifier ArgList ) Statements
				
				
				openbr = ti.next();
				if(openbr.getType() != TokenType.OPENCU 
						&& openbr.getType() != TokenType.OPENRD
						&& openbr.getType() != TokenType.OPENSQ) {
					err(openbr);
				}else {
				
					Token t = ti.next();
					if(t.getType() != TokenType.IDENTIFIER) err(t);
					id = new ParseTree();
					id.data = t;
					
					ParseTree arglist = ArgList.eval(ti);
					
					if(!ti.hasNext()) eoferr();
					
					closebr = ti.next();
					
			if(!brack_match(openbr,closebr)) err(closebr);
					
					open = new ParseTree();
					open.data = openbr;

					close = new ParseTree();
					close.data = closebr;
					
					pt.children = new ParseTree[] { open, id, arglist, close, Statements.eval(ti) };
				}
				
				
 			}
			return pt;
		
		case InnerLetExpression:
			
			if(!ti.hasNext()) eoferr();

			if(ti.peek().getType() == TokenType.IDENTIFIER) {
				//InLet => identifier ( VarDefs )
				id = new ParseTree();
				id.data = ti.next();
			} else id = null;
			
			openbr = ti.next();
			if(openbr.getType() != TokenType.OPENCU 
					&& openbr.getType() != TokenType.OPENRD
					&& openbr.getType() != TokenType.OPENSQ) {
				err(openbr);
			}else {
			//  ( VarDefs )
			
				ParseTree vardefs = VarDefs.eval(ti);
				
				if(!ti.hasNext()) eoferr();
				
				closebr = ti.next();
				
				if(!brack_match(openbr,closebr)) err(closebr);
				
				open = new ParseTree();
				open.data = openbr;

				close = new ParseTree();
				close.data = closebr;
				if(id != null) pt.children = new ParseTree[] { id, open, vardefs, close };
				else pt.children = new ParseTree[] {open, vardefs, close};
			}
			return pt;
		
		case InnerTopLevelForm:
			
			if(!ti.hasNext()) eoferr();
			
			next = ti.peek();
			switch(next.getType()) {
			case DEFINE: // InTopLF => Def
				pt.children = new ParseTree[] { Definition.eval(ti) };
				break;
			case IDENTIFIER://InTopLF => FunCall
				pt.children = new ParseTree[] { FunCall.eval(ti) };
				break;
			default:
				err(next);
				break;
			}
			return pt;
			
		case Lambda:
			if(!ti.hasNext()) eoferr();
			
			//Lambda => lambda ( ArgList ) Statements
			
			Token l = ti.next();
			if(l.getType() != TokenType.LAMBDA) err(l);
			ParseTree lambda = new ParseTree();
			lambda.data=l;
			
			openbr = ti.next();
			if(openbr.getType() != TokenType.OPENCU 
					&& openbr.getType() != TokenType.OPENRD
					&& openbr.getType() != TokenType.OPENSQ) {
				err(openbr);
			}else {
			//  ( ArgList ) Statements
			
				ParseTree arglist = ArgList.eval(ti);
				
				if(!ti.hasNext()) eoferr();
				
				closebr = ti.next();
				
			if(!brack_match(openbr,closebr)) err(closebr);
				
				open = new ParseTree();
				open.data = openbr;

				close = new ParseTree();
				close.data = closebr;
				
				stmts = Statements.eval(ti);
				
				
				pt.children = new ParseTree[] { lambda, open, arglist, close, stmts };
			}
			return pt;
		
		case LetExpression:
			if(!ti.hasNext()) eoferr();
			
			//LetExpr => let InLet Statements
			l = ti.next();
			ParseTree let = new ParseTree();
			let.data=l;
			
			pt.children = new ParseTree[] { let, InnerLetExpression.eval(ti), Statements.eval(ti) };
			return pt;
		
		case MoreCondBranches:
			if(!ti.hasNext()) eoferr();
			
			//MoreCondBranches => CondBranches
			switch(ti.peek().getType()) {
			case OPENSQ:case OPENRD:case OPENCU:
				pt.children = new ParseTree[] { CondBranches.eval(ti) };
				return pt;
			default://MoreCondBranches => !
				return pt;
			}
		
		case MoreVarDefs:

			if(!ti.hasNext()) eoferr();
			
			//MoreVarDefs => VarDefs
			switch(ti.peek().getType()) {
			case OPENSQ:case OPENRD:case OPENCU:
				pt.children = new ParseTree[] { VarDefs.eval(ti) };
				return pt;
			default://MoreVarDefs => !
				return pt;
			}
		
		case NonBracketedExpression:
			
			if(!ti.hasNext()) eoferr();
			
			
			switch(ti.peek().getType()) {
			case IDENTIFIER:case NUMBER:case CHAR:case BOOL:case STRING:
				ParseTree t = new ParseTree();
				t.data = ti.next();
				pt.children = new ParseTree[] {t};
				return pt;
			case TICK:
				pt.children = new ParseTree[] { TickQuoteExpression.eval(ti)};
				return pt;
			default:
				err(ti.peek());
			}
			
			break;
		
		case OptionalStatements:
			if(!ti.hasNext()) eoferr();
			
			switch(ti.peek().getType()) {
			case OPENRD:case OPENSQ: case OPENCU://OptStmts => Stmts
				pt.children = new ParseTree[]{ Statements.eval(ti) };
				return pt;
			default://OptStmts => !
				return pt;
			}
		
		case QuoteExpression:
			if(!ti.hasNext()) eoferr();
			
			Token q = ti.next();
			if(q.getType() != TokenType.QUOTE) err(q);
			ParseTree quote = new ParseTree();
			quote.data = q;
			pt.children = new ParseTree[] { quote, Expression.eval(ti) };
			return pt;
		
		case Statement:
			
			
			if(!ti.hasNext()) eoferr();
			
			openbr = ti.peek();
			if(openbr.getType() != TokenType.OPENCU 
					&& openbr.getType() != TokenType.OPENRD
					&& openbr.getType() != TokenType.OPENSQ) {
			//Stmt => NoBrackExpr
				pt.children = new ParseTree[] { NonBracketedExpression.eval(ti) };
			}else {
			//Stmt => ( BrackStmt )
				openbr = ti.next();
				ParseTree brackexpr = BracketedStatement.eval(ti);
				
				if(!ti.hasNext()) eoferr();
				
				closebr = ti.next();
				
			if(!brack_match(openbr,closebr)) err(closebr);
				
				open = new ParseTree();
				open.data = openbr;

				close = new ParseTree();
				close.data = closebr;
				
				pt.children = new ParseTree[] { open, brackexpr, close };
			}
			return pt;
			
		
		case Statements:
			
			if(!ti.hasNext()) eoferr();
			
			openbr = ti.peek();
			
			switch(openbr.getType()) {
			case OPENCU://Stmts => { StmtsCU
				openbr = ti.next();
				open = new ParseTree();
				open.data = openbr;
				pt.children = new ParseTree[] { open, StatementsCU.eval(ti) };
				return pt;
			case OPENRD://Stmts => ( StmtsRD
				openbr = ti.next();
				open = new ParseTree();
				open.data = openbr;
				pt.children = new ParseTree[] { open, StatementsRD.eval(ti) };
				return pt;
			case OPENSQ://Stmts => [ StmtsSQ
				openbr = ti.next();
				open = new ParseTree();
				open.data = openbr;
				pt.children = new ParseTree[] { open, StatementsSQ.eval(ti) };
				return pt;
				
			case IDENTIFIER:case NUMBER:case CHAR: case BOOL: case STRING: case TICK:
				//Statements => NonBracketedExpression OptionalStatements
				pt.children = new ParseTree[] { StatementsNoBrackets.eval(ti), OptionalStatements.eval(ti) };
				return pt;
			default:System.err.println(openbr);  err(openbr);
 			}
			return pt;
		
		case StatementsCU:
			
			if(!ti.hasNext()) eoferr();
			
			if(ti.peek().getType() == TokenType.DEFINE) {
			//StmtsAfterBrack => Def ) Stmts
				ParseTree define = Definition.eval(ti);
				closebr = ti.next();
				if(closebr.getType() != TokenType.CLOSEDCU) {
					err(closebr);
				}else {
					close = new ParseTree();
					close.data = closebr;
					pt.children = new ParseTree[] { define, close, Statements.eval(ti) };
					return pt;
				}
			}else {
			//StmtsAfterBrack => BrackExpr ) OptStmts
				ParseTree bexp = BracketedExpression.eval(ti);
				closebr = ti.next();
				if(closebr.getType() != TokenType.CLOSEDCU) {
					err(closebr);
				}else {
					close = new ParseTree();
					close.data = closebr;
					pt.children = new ParseTree[] { bexp, close, OptionalStatements.eval(ti) };
					return pt;
				}
			}
			
			break;
		case StatementsRD:
			
			if(!ti.hasNext()) eoferr();
			
			if(ti.peek().getType() == TokenType.DEFINE) {
			//StmtsAfterBrack => Def ) Stmts
				ParseTree define = Definition.eval(ti);
				closebr = ti.next();
				if(closebr.getType() != TokenType.CLOSEDRD) {
					err(closebr);
				}else {
					close = new ParseTree();
					close.data = closebr;
					pt.children = new ParseTree[] { define, close, Statements.eval(ti) };
					return pt;
				}
			}else {
			//StmtsAfterBrack => BrackExpr ) OptStmts
				ParseTree bexp = BracketedExpression.eval(ti);
				closebr = ti.next();
				if(closebr.getType() != TokenType.CLOSEDRD) {
					err(closebr);
				}else {
					close = new ParseTree();
					close.data = closebr;
					pt.children = new ParseTree[] { bexp, close, OptionalStatements.eval(ti) };
					return pt;
				}
			}
			
			break;
		case StatementsSQ:
			
			if(!ti.hasNext()) eoferr();
			
			if(ti.peek().getType() == TokenType.DEFINE) {
			//StmtsAfterBrack => Def ) Stmts
				ParseTree define = Definition.eval(ti);
				closebr = ti.next();
				if(closebr.getType() != TokenType.CLOSEDSQ) {
					err(closebr);
				}else {
					close = new ParseTree();
					close.data = closebr;
					pt.children = new ParseTree[] { define, close, Statements.eval(ti) };
					return pt;
				}
			}else {
			//StmtsAfterBrack => BrackExpr ) OptStmts
				ParseTree bexp = BracketedExpression.eval(ti);
				closebr = ti.next();
				if(closebr.getType() != TokenType.CLOSEDSQ) {
					err(closebr);
				}else {
					close = new ParseTree();
					close.data = closebr;
					pt.children = new ParseTree[] { bexp, close, OptionalStatements.eval(ti) };
					return pt;
				}
			}
			
			break;
		
		case StatementsNoBrackets:
			if(!ti.hasNext()) eoferr();
			//StmtsNobrack => NoBrackExpr OptStmts
			
			pt.children = new ParseTree[] { NonBracketedExpression.eval(ti), OptionalStatements.eval(ti) };
			return pt;
		
		case TickQuoteExpression:
			
			if(!ti.hasNext()) eoferr();
			//TickQuoteExpr => TICK Expr
			
			Token t = ti.next();
			if(t.getType() != TokenType.TICK) err(t);
			ParseTree tick = new ParseTree();
			tick.data = t;
			pt.children = new ParseTree[] { tick, Expression.eval(ti) };
			return pt;
		
		case TopLevelForm:
			
			
			if(!ti.hasNext()) eoferr();
			
			openbr = ti.next();
			if(openbr.getType() != TokenType.OPENCU 
					&& openbr.getType() != TokenType.OPENRD
					&& openbr.getType() != TokenType.OPENSQ) {
				err(openbr);
			}else {
			//TopLF => ( InTopLF )
			
				ParseTree itlf = InnerTopLevelForm.eval(ti);
				
				if(!ti.hasNext()) eoferr();
				
				closebr = ti.next();
				
			if(!brack_match(openbr,closebr)) err(closebr);
				
				open = new ParseTree();
				open.data = openbr;

				close = new ParseTree();
				close.data = closebr;
				
				pt.children = new ParseTree[] { open, itlf, close };
			}
			return pt;
		
		case VarDef:
			
			
			if(!ti.hasNext()) eoferr();
			
			openbr = ti.next();
			if(openbr.getType() != TokenType.OPENCU 
					&& openbr.getType() != TokenType.OPENRD
					&& openbr.getType() != TokenType.OPENSQ) {
				err(openbr);
			}else {
			//VarDef => ( identifier Expr )

				if(!ti.hasNext()) eoferr();
				
				t = ti.next();
				if(t.getType() != TokenType.IDENTIFIER) err(t);
				id = new ParseTree();
				id.data=  t;
			
				expr = Expression.eval(ti);
				
				if(!ti.hasNext()) eoferr();
				
				closebr = ti.next();
				
			if(!brack_match(openbr,closebr)) err(closebr);
				
				open = new ParseTree();
				open.data = openbr;

				close = new ParseTree();
				close.data = closebr;
				
				pt.children = new ParseTree[] { open, id, expr, close };
			}
			return pt;
		
		case VarDefs:
			
			//VarDefs => VarDef MoreVarDefs
			pt.children = new ParseTree[] { VarDef.eval(ti), MoreVarDefs.eval(ti) };
			return pt;
		
		default:
			break;
		
		}
		return null;
		
	}
}
