package ast;

import scanner.Token;

public class Identifier {
	String name;
	int hops; //number of hops to the scope with this defined, or -1 if UNDEFINIED.

	public Identifier(String name, int hops) {
		this.name=name;
		this.hops=hops;
		// TODO Auto-generated constructor stub
	}
	public String toString() {
		return this.getClass().getSimpleName() + " [" + name + "; " + hops + "]";
	}

}
