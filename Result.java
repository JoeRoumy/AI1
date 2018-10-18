import java.util.ArrayList;

public class Result {

	public ArrayList<Snode> solution;
	public int totalcost;
	public int expandedNodes;

	public Result(ArrayList<Snode> solution, int totalcost, int expandedNodes) {
		this.expandedNodes = expandedNodes;
		this.totalcost = totalcost;
		this.solution = solution;
	}
}
