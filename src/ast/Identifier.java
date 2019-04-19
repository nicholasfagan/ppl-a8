package ast;

public class Identifier implements Expression {
	String name;
	int hops; //number of hops to the scope with this defined, or -1 if UNDEFINIED.

	public Identifier(String name, int hops) {
		this.name=name;
		this.hops=hops;
		// TODO Auto-generated constructor stub
	}
	public String toString() {
		return this.string_rep();
	}
	@Override
	public String attributes() {
		// TODO Auto-generated method stub
		return name + "; " + hops;
	}
	@Override
	public Object[] children() {
		// TODO Auto-generated method stub
		return new Object[0];
	}

}
