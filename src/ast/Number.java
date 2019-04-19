package ast;

public class Number extends Expression {

	String value;
	public Number(Expression parent, String n) {
		super(parent);
		value=n;
	}
	public String toString() {
		return this.string_rep();
	}
	@Override
	public String attributes() {
		return value;
	}
	@Override
	public Object[] children() {
		return new Object[0];
	}
	
	

}
