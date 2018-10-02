import java.util.concurrent.Callable;

public class SearchProblem {

	public Operator[] operators;
	public State initState;
	public State[] stateSpace;
	public Callable goalTest;
	public Callable pathCost;
	
}
