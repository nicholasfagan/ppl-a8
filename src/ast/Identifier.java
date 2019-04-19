package ast;

public class Identifier extends Expression {
	String name;
	int hops; //number of hops to the scope with this defined, or -1 if UNDEFINIED.

	public Identifier(Expression parent,String name, int hops) {
		super(parent);
		this.name=name;
		this.hops=hops;
	}
	public Identifier(Expression parent, String name) {
		super(parent);
		this.name=name;
		this.hops = -1;
		
	}
	public void resolve_hops() {
		int i = 0;
		Expression e = this.parent;
		while(e != null) {
			if(e instanceof Function) {
				if(((Function)e).attributes.contains(name)
						|| ((Function)e).arguments.contains(name)) {
					this.hops = i;
					break;
				}
				i++;
			}
			else if(e instanceof Scope) {
				if(((Scope)e).attributes.contains(name)) {
					this.hops = i;
					break;
				}
				i++;
			}
			e = e.parent;
		}
		if(e ==  null) {
			this.hops = -1;
		}
	}
	public String toString() {
		return this.string_rep();
	}
	@Override
	public String attributes() {
		// TODO Auto-generated method stub
		return name + "; " + (hops == -1 ? "UNDEFINED" : hops);
	}
	@Override
	public Object[] children() {
		// TODO Auto-generated method stub
		return new Object[0];
	}

}
