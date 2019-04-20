package ast;

public class ASTString extends Expression {
	String value;
	public ASTString(Expression parent,String rep) {
		super(parent);
		value = rep;
	}
	public String toString() {
		return this.string_rep();
	}
	public String attributes() {
		return value;
	}
	public Object[] children() {
		return new Object[0];
	}

}
