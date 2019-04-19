package ast;

import parser.Config;
import parser.ParseTree;
import parser.Parser;

public class ASTBuilder {
	public static void main(String[] args) {
		Config c = new Config(args);
		c.getOut().println(build(Parser.parse(c.getIn())));
	}
	public static AbstractSyntaxTree build(ParseTree pt) {
		return new AbstractSyntaxTree(pt);
		
	}
}
