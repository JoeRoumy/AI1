
import java.util.ArrayList;

public abstract class SearchProblem {

	public Operator[] operators;
	public State initState;
	public ArrayList<Snode> queue;
	public int expandedNodes;

	abstract Snode[] expand(Snode node);

	abstract boolean goalTest(State s);

	abstract int pathCost(Operator o);

	abstract void BF(Snode[] n);

	abstract void DF(Snode[] n);

	abstract void ID(Snode[] n, int depth);

	abstract void UC(Snode[] n);

	abstract void GR1(Snode[] n);

	abstract void GR2(Snode[] n);

	abstract void AS1(Snode[] n);

	abstract void AS2(Snode[] n);

	Snode generalSearch(SearchProblem problem, String strategy) {
		long loop = 0;
		int currentDepth = 0;

		do {
			queue = new ArrayList<Snode>(500);
			queue.add(new Snode(initState));
			System.out.println("Iteration number: " + currentDepth);

			while (queue.size() > 0) {
				loop++;
				Snode thisNode = queue.remove(0);
				if (goalTest((StateW) thisNode.state)) {
					return thisNode;
				}

				if (loop % 50000 == 0) {
					System.out.println("queue length:" + queue.size());
					System.out.println("epanded nodes:" + expandedNodes);
				}
				switch (strategy) {
				case "ID":
					ID(expand(thisNode), currentDepth);
					break;
				case "BF":
					BF(expand(thisNode));
					break;
				case "DF":
					DF(expand(thisNode));
					break;
				case "UC":
					UC(expand(thisNode));
					break;
				case "GR1":
					GR1(expand(thisNode));
					break;
				case "AS1":
					AS1(expand(thisNode));
					break;
				case "GR2":
					GR2(expand(thisNode));
					break;
				case "AS2":
					AS2(expand(thisNode));
					break;
				default:
					System.out.println("Invalid search strategy " + strategy);
					return null;
				}
			}
			currentDepth++;
		} while (strategy == "ID" && currentDepth < 500);

		return null;

		// switch (strategy) {
		// case "BF": queue = BF(queue,expand(thisNode,grid)); break;
		// case "DF": queue = DF(queue,expand(thisNode,grid)); break;
		// case "ID": queue = ID(queue,expand(thisNode,grid),currentDepth); break;
		// case "UC": queue = UC(queue,expand(thisNode,grid)); break;
		// case "GR1": queue = GR1(queue,expand(thisNode,grid)); break;
		// case "AS1": queue = AS1(queue,expand(thisNode,grid)); break;
		// case "GR2": queue =
		// GR2(queue,expand(thisNode,grid),grid.gridWidth>grid.gridLength?grid.gridWidth:grid.gridLength);
		// break;
		// case "AS2": queue =
		// AS2(queue,expand(thisNode,grid),grid.gridWidth>grid.gridLength?grid.gridWidth:grid.gridLength);
		// break;
		//// default: System.out.println("Invalid search strategy "+strategy); return
		// null;
		//// }
		//

	}

}

// private Snode SearchHelper(Grid grid, String strategy, int currentDepth) {

//

//
//
//
//
//
//
// }
// return null;
//
// }
//
// // to visualize the solution step by step
// private void PrintSolution(Grid grid, ArrayList<Snode> solution) {
//
// for (int i = 1; i < solution.size(); i++) {
// if(applyToGrid(grid, solution.get(i)))
// System.out.println(Arrays.deepToString(grid.getGrid()).replace("], ",
// "]\n").replace("[[", "[").replace("]]", "]"));
// }
//
// }
//
