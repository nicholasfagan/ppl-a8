package ast;

public class Number implements Expression {

	String value;
	public Number(String n) {
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
