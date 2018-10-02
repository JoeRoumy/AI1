import java.util.ArrayList;
import java.util.LinkedList;


/*
 * need to consider repeated states
 * keep nodes from being deleted after dequeue
 * 
 * 
 * */



public class SaveWesteros extends SearchProblem {
	
	public ArrayList<Snode> queue;
	public int expandedNodes;
	
	
	private Solution BF(Grid grid) {
		// TODO Auto-generated method stub
		return null;

	}

	private LinkedList<Snode> DF(LinkedList<Snode>nodes,Snode[] n) {
		for(int i = n.length-1;i>-1;i--) {
			nodes.addFirst(n[i]);
		}
		return nodes;

	}

	private LinkedList<Snode> ID(LinkedList<Snode>nodes,Snode[] n,int maxDeapth) {
		for(int i = n.length-1;i>-1;i--) {
			if(n[i].getDepth()<=maxDeapth)
					nodes.addFirst(n[i]);
		}
		return nodes;

	}

	private Solution UC(Grid grid) {
		
		//initialization
		Solution sol = new Solution(100);
		queue = new ArrayList<Snode>(500);
		
		//instantiating the queue
		queue.add(new Snode(initState));
		
		while (queue.size()>0) {
			Snode thisNode = queue.remove(0);
//			if(goalTest.call(thisNode.state)) {
//				
//			}
			
		}
		
		return null;

	}

	private Solution GR1(Grid grid) {
		// TODO Auto-generated method stub
		return null;

	}

	private Solution GR2(Grid grid) {
		// TODO Auto-generated method stub
		return null;

	}

	private Solution AS1(Grid grid) {
		// TODO Auto-generated method stub
		return null;

	}

	private Solution AS2(Grid grid) {
		// TODO Auto-generated method stub
		return null;

	}

	private Grid GenGrid() {
		// TODO Auto-generated method stub
		return null;
	}

	private void Search(Grid grid, String strategy, Boolean visualize) {
		Solution solution = null;
		switch (strategy) {
		case "BF":	solution = BF(grid);		break;
//		case "DF":	solution = DF(grid);		break;
//		case "ID":	solution = ID(grid);		break;
		case "UC":	solution = UC(grid);		break;
		case "GR1":	solution = GR1(grid);		break;
		case "GR2":	solution = GR2(grid);		break;
		case "AS1":	solution = AS1(grid);		break;
		case "AS2":	solution = AS2(grid);		break;
		default: System.out.println("Invalid search strategy "+strategy);			return;
		}
		
		if(visualize) {
			PrintSolution(solution);
		}
		
		System.out.println("Nodes expanded: "+expandedNodes);
	}
	
	
	private void PrintSolution(Solution solution) {
		// TODO Auto-generated method stub

	}
	
	
	

}
