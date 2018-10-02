public class Snode {

	public int getDepth() {
		return depth;
	}


	public State state;
	public Snode parent;
	public Operator operator;
	public int depth;
	public int cost;
	
	//To initialize tree node
	public Snode(State aState, Snode aParent, Operator anOperator, int aDepth, int aCost) {
		state = aState;
		parent = aParent;
		operator = anOperator;
		depth = aDepth;
		cost = aCost;
	}
	
	
	//To initialize tree root
	public Snode(State initState) {
		state = initState;
		parent = null;
		operator = null;
		depth = 0;
		cost = 0;
	}

}
