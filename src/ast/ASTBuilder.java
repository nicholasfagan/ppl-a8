package ast;

import parser.ParseTree;
import parser.Parser;

public class ASTBuilder {
	public static void main(String[] args) throws Exception {
		Config c = new Config(args);
		c.getOut().println(build(Parser.parse(c.getIn())));
	}
	public static AbstractSyntaxTree build(ParseTree pt) throws Exception {
		return new AbstractSyntaxTree(pt);
		
	}
}
