import java.util.ArrayList;
import java.util.LinkedList;


/*
 * need to consider repeated states
 * keep nodes from being deleted after dequeue >>>> parent nodes
 * heuristic considers number of white walkers
 * 
 * */



public class SaveWesteros extends SearchProblem {
	
	public ArrayList<Snode> queue;
	public int expandedNodes;
	
	
    private ArrayList<Snode> BF(ArrayList<Snode>nodes,Snode[] n) {
        for(int i = n.length-1; i > -1; i--) {
            nodes.add(nodes.size()-1,n[i]);
        }
        return nodes;
    }

	private ArrayList<Snode> DF(ArrayList<Snode>nodes,Snode[] n) {
		for(int i = n.length-1;i>-1;i--) {
			nodes.add(0,n[i]);
		}
		return nodes;

	}

	private ArrayList<Snode> ID(ArrayList<Snode>nodes,Snode[] n,int maxDeapth) {
		for(int i = n.length-1;i>-1;i--) {
			if(n[i].getDepth()<=maxDeapth)
					nodes.add(0,n[i]);
		}
		return nodes;

	}

	private ArrayList<Snode> UC(ArrayList<Snode>nodes,Snode[] n) {
		// TODO Auto-generated method stub
		return null;

	}

	private ArrayList<Snode> GR1(Grid grid) {
		// TODO Auto-generated method stub
		return null;

	}

	private ArrayList<Snode> GR2(Grid grid) {
		// TODO Auto-generated method stub
		return null;

	}

	private ArrayList<Snode> AS1(Grid grid) {
		// TODO Auto-generated method stub
		return null;

	}

	private ArrayList<Snode> AS2(Grid grid) {
		// TODO Auto-generated method stub
		return null;

	}


	private void Search(Grid grid, String strategy, Boolean visualize) {
		//solution
		Snode leaf = null;
		
		//initialization
		queue = new ArrayList<Snode>(500);
		
		//instantiating the queue
		queue.add(new Snode(initState));
			
		
		while (queue.size()>0) {
			Snode thisNode = queue.remove(0);
			if(thisNode.state.isGoal) {
				
			}

			switch (strategy) {
			case "BF":	queue = BF(grid);		break;
			case "DF":	queue = DF(queue,expand(thisNode));		break;
//			case "ID":	queue = ID(queue,expand(thisNode));		break;
			case "UC":	queue = UC(queue,expand(thisNode));		break;
			case "GR1":	queue = GR1(grid);		break;
			case "GR2":	queue = GR2(grid);		break;
			case "AS1":	queue = AS1(grid);		break;
			case "AS2":	queue = AS2(grid);		break;
			default: System.out.println("Invalid search strategy "+strategy);			return;
			}
		
		}
		
		if(visualize) {
			PrintSolution(grid, leaf);
		}
		
		System.out.println("Nodes expanded: "+expandedNodes);
	}
	
	
	private void PrintSolution(Grid grid, Snode leaf) {
		// TODO Auto-generated method stub

	}
	
	private Snode[] expand(Snode node) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
