
public class Snode {
	
	//depth getter
	public int getDepth() {
		return depth;
	}

	//Node as defined in class has a state, parent node, operator, depth, and cost
	public State state;
	public Snode parent;
	public Operator operator;
	public int depth;
	public int cost;
	
	//Node Constructor
	public Snode(State aState, Snode aParent, Operator anOperator, int aDepth, int aCost) {
		state = aState;
		parent = aParent;
		operator = anOperator;
		depth = aDepth;
		cost = aCost;
	}
	
	
	//Root Node Constructor 
	public Snode(State initState) {
		state = initState;
		parent = null;
		operator = null;
		depth = 0;
		cost = 0;
	}

}
