package ast;

public class ASTChar extends Expression {
	String value;

	public ASTChar(Expression parent, String val) {
		super(parent);
		this.value=val;
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
