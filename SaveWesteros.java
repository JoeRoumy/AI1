import java.util.ArrayList;
import java.util.Iterator;


/*
 * need to consider repeated states
 * keep nodes from being deleted after dequeue >>>> parent nodes
 * heuristic considers number of white walkers
 * total number of glass used
 * */



public class SaveWesteros extends SearchProblem {
	
	public ArrayList<Snode> queue;
	public int expandedNodes;
	
	public static void main(String[] args) {
		Grid g = new Grid(666);
	}
	
	
	// breadth first enqueueing function
    private ArrayList<Snode> BF(ArrayList<Snode>nodes,Snode[] n) {
        for(int i = n.length-1; i > -1; i--) {
        	if(n[i]!=null)
            nodes.add(nodes.size()-1,n[i]);
        }
        return nodes;
    }

	// depth first function
	private ArrayList<Snode> DF(ArrayList<Snode>nodes,Snode[] n) {
		for(int i = n.length-1;i>-1;i--) {
        	if(n[i]!=null)
			nodes.add(0,n[i]);
		}
		return nodes;

	}

	// iterative deepening enqueueing function
	private ArrayList<Snode> ID(ArrayList<Snode>nodes,Snode[] n,int maxDeapth) {
		for(int i = n.length-1;i>-1;i--) {
			if(n[i] != null && n[i].getDepth()<=maxDeapth)
					nodes.add(0,n[i]);
		}
		return nodes;

	}

	// uniform cost enqueueing function
	private ArrayList<Snode> UC(ArrayList<Snode>nodes,Snode[] n) {
		
		for (int i = 0; i < n.length; i++) {
        	if(n[i]!=null)
			nodes.add(binarySearchCost(nodes,0,nodes.size()-1, n[i].cost),n[i]);
		}
	
		return nodes;

	}

	// greedy enqueueing function
	private ArrayList<Snode> GR(ArrayList<Snode>nodes,Snode[] n) {

		for (int i = 0; i < n.length; i++) {
        	if(n[i]!=null)
			nodes.add(binarySearchHeuristic(nodes,0,nodes.size()-1, n[i].state.heuristic),n[i]);
		}
	
		return nodes;
	}

	// a star enqueueing function
	private ArrayList<Snode> AS(ArrayList<Snode>nodes,Snode[] n) {

		for (int i = 0; i < n.length; i++) {
        	if(n[i]!=null)
			nodes.add(binarySearchCostAndHeuristic(nodes,0,nodes.size()-1, n[i].cost+n[i].state.heuristic),n[i]);
		}
	
		return nodes;

	}

	// control the search and visualization
	private ArrayList<Snode> Search(Grid grid, String strategy, Boolean visualize) {

		Snode leaf = null;

		 for (int i = 0; leaf == null; i++) {
			leaf = SearchHelper(grid, strategy, i);
			System.out.println("Iteration number: "+i+2);
		}
		
		 ArrayList<Snode> solution = new ArrayList<>(50);
		 
		 solution = GenerateSolution(solution, leaf);
		 
		if(visualize) {
			PrintSolution(grid, solution);
		}
		
		System.out.println("Nodes expanded: "+expandedNodes);
		
		return solution;
		
	}

	// backtracks from goal to root to get the path for the solution
	private ArrayList<Snode> GenerateSolution(ArrayList<Snode> solution, Snode leaf) {
		
		while (leaf.parent!=null) {
			solution.add(0, leaf);
			leaf =leaf.parent;
		}
		
		return solution;
		
	}

	
	// helper to search to facilitate looping for id  
	// switches between the different strategies and handles basic search procedure
	private Snode SearchHelper(Grid grid, String strategy, int currentDepth) {
		//solution
		Snode leaf = null;
		
		//initialization
		queue = new ArrayList<Snode>(500);
		
		//instantiating the queue
		queue.add(new Snode(initState));
			
		
		while (queue.size()>0) {
			Snode thisNode = queue.remove(0); 
			if(thisNode.state.isGoal) {
				return thisNode;
			}

			switch (strategy) {
			case "BF":	queue = BF(queue,expand(thisNode));		break;
			case "DF":	queue = DF(queue,expand(thisNode));		break;
			case "ID":	queue = ID(queue,expand(thisNode),currentDepth);		break;
			case "UC":	queue = UC(queue,expand(thisNode));		break;
			case "GR1":	queue = GR(queue,expand(thisNode));		break;
			case "AS2":	queue = AS(queue,expand(thisNode));		break;
			default: System.out.println("Invalid search strategy "+strategy);			return null;
			}
		
		}
		return null;

	}
	
	// to visualize the solution step by step
	private void PrintSolution(Grid grid, ArrayList<Snode> solution) {

		for (int i = 1; i < solution.size(); i++) {
			if(applyToGrid(grid, solution.get(i)))
			System.out.println(grid.getGrid().toString());
		}
		
	}
	
	// to generate the child nodes of a given node
	private Snode[] expand(Snode node) {
		Snode[] newNodes = new Snode[operators.length];
		expandedNodes++;
		
		// TODO Auto-generated method stub

		
		return newNodes;
	}
	
	// to reflect the dequeueing on the grid
	private boolean applyToGrid(Grid grid, Snode step) {
		switch(step.operator) {
		case Forward:
			switch(step.state.direction) {
			case E: grid.getGrid()[grid.johnsx][grid.johnsy] = '\u0000';
					grid.getGrid()[++grid.johnsx][grid.johnsy] = 'J';
				break;
			case N:	grid.getGrid()[grid.johnsx][grid.johnsy] = '\u0000';
					grid.getGrid()[grid.johnsx][--grid.johnsy] = 'J';
				break;
			case S:	grid.getGrid()[grid.johnsx][grid.johnsy] = '\u0000';
					grid.getGrid()[grid.johnsx][++grid.johnsy] = 'J';
				break;
			case W:	grid.getGrid()[grid.johnsx][grid.johnsy] = '\u0000';
					grid.getGrid()[--grid.johnsx][grid.johnsy] = 'J';
				break;
			}
			return true;
		case Stab:
			if(grid.getGrid()[grid.johnsx+1][grid.johnsy] == 'W')
				grid.getGrid()[grid.johnsx+1][grid.johnsy] = '\u0000';
			if(grid.getGrid()[grid.johnsx-1][grid.johnsy] == 'W')
				grid.getGrid()[grid.johnsx-1][grid.johnsy] = '\u0000';
			if(grid.getGrid()[grid.johnsx][grid.johnsy+1] == 'W')
				grid.getGrid()[grid.johnsx][grid.johnsy+1] = '\u0000';
			if(grid.getGrid()[grid.johnsx][grid.johnsy-1] == 'W')
				grid.getGrid()[grid.johnsx][grid.johnsy-1] = '\u0000';
			return true;
		default:
			return false;
		
		}
	}
	
	
	
	
	
	// to insert in ucs
	private int binarySearchCost(ArrayList<Snode> q, int start, int end, int newCost) {
		//base case
		if(start == end) {
			return start;
		}
		//calculate mid of list
		int center = (end+start)/2;
		//redirect to left or right half
		if(q.get(center).cost>newCost) {
			return binarySearchCost(q,start,center,newCost);
		}else {
			return binarySearchCost(q,center,end,newCost);
		}

	}
	
	// to insert in gs
	private int binarySearchHeuristic(ArrayList<Snode> q, int start, int end, int newHeuristic) {
		//base case
		if(start == end) {
			return start;
		}
		//calculate mid of list
		int center = (end+start)/2;
		//redirect to left or right half
		if(q.get(center).state.heuristic>newHeuristic) {
			return binarySearchHeuristic(q,start,center,newHeuristic);
		}else {
			return binarySearchHeuristic(q,center,end,newHeuristic);
		}

	}
	
	// to insert in as
	private int binarySearchCostAndHeuristic(ArrayList<Snode> q, int start, int end, int newCostAndHeuristic) {
		//base case
		if(start == end) {
			return start;
		}
		//calculate mid of list
		int center = (end+start)/2;
		//redirect to left or right half
		if(q.get(center).state.heuristic+q.get(center).cost>newCostAndHeuristic) {
			return binarySearchCostAndHeuristic(q,start,center,newCostAndHeuristic);
		}else {
			return binarySearchCostAndHeuristic(q,center,end,newCostAndHeuristic);
		}

	}
	
	
}
