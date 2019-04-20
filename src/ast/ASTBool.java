package ast;

public class ASTBool extends Expression {
	String value;

	public ASTBool(Expression parent,String val) {
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
