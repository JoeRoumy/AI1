import java.util.concurrent.Callable;

public abstract class SearchProblem {
	
	public Operator[] operators;
	public State initState;
	
	abstract  Snode[] expand(Snode node, Grid grid);
	abstract boolean goalTest(State s);
	abstract int pathCost(Operator o);
}
