import java.util.ArrayList;
import java.util.Arrays;


/*
 * need to consider repeated states
 * keep nodes from being deleted after dequeue >>>> parent nodes
 * heuristic considers number of white walkers
 * total number of glass used
 * */


/*
 * 						 number of walkers left
 * first heuristic is : ------------------------- 
 * 						3 * number of glass used
 *
 *
 *	second heuristic is distance from closest walker
 *
 * */


public class SaveWesteros extends SearchProblem {
	
public Grid grid;
	
	public static void main(String[] args) {
		Grid grid = new Grid(666);
//		System.out.println(Arrays.deepToString(grid.getGrid()).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
		SaveWesteros ai = new SaveWesteros(new StateW(grid.gridWidth, grid.gridLength, 0, grid.getPositions()));
		ai.Search(grid, "BF", true);
	}
	
	public SaveWesteros(StateW initstate) {
		this.initState = initstate;
		operators = Operator.values();
	}
	
	
	// breadth first enqueueing function
    void BF(Snode[] n) {
        for(int i = n.length-1; i > -1; i--) {
        	if(n[i]!=null)
            queue.add(queue.size(),n[i]);
        }
    }

	// depth first function
	void DF(Snode[] n) {
		for(int i = n.length-1;i>-1;i--) {
        	if(n[i]!=null)
			queue.add(0,n[i]);
		}
	}

	// iterative deepening enqueueing function
	void ID(Snode[] n,int maxDeapth) {
		for(int i = n.length-1;i>-1;i--) {
			if(n[i] != null && n[i].getDepth()<=maxDeapth)
					queue.add(0,n[i]);
		}
	}

	// uniform cost enqueueing function
	void UC(Snode[] n) {
		
		for (int i = 0; i < n.length; i++) {
        	if(n[i]!=null) {
        		if(queue.size()==0) {
        			queue.add(n[i]);
        			continue;
        		}
			queue.add(binarySearchCost(queue,0,queue.size()-1, n[i].cost),n[i]);
        	}
		}
	
	}

	// greedy enqueueing function with first heuristic
	void GR1(Snode[] n) {

		for (int i = 0; i < n.length; i++) {
        	if(n[i]!=null) {
        		if(queue.size()==0) {
        			queue.add(n[i]);
        			continue;
        		}
			queue.add(binarySearchHeuristic1(queue,0,queue.size()-1,((StateW) n[i].state).walkersLeft/(3.0*((StateW) n[i].state).totalGlassUsed)),n[i]);
        	}
		}
	
	}


	// a star enqueueing function with first heuristic
	void AS1(Snode[] n) {

		for (int i = 0; i < n.length; i++) {
        	if(n[i]!=null) {
        		if(queue.size()==0) {
        			queue.add(n[i]);
        			continue;
        		}
			queue.add(binarySearchCostAndHeuristic1(queue,0,queue.size()-1, n[i].cost+((StateW) n[i].state).walkersLeft/(3.0*((StateW) n[i].state).totalGlassUsed)),n[i]);
		
        	}}
	
	}
	
	// greedy enqueueing function with first heuristic
	void GR2(Snode[] n) {

		for (int i = 0; i < n.length; i++) {
        	if(n[i]!=null) {
        		if(queue.size()==0) {
        			queue.add(n[i]);
        			continue;
        		}
			queue.add(binarySearchHeuristic2(queue,0,queue.size()-1,distanceToClosestWalker(grid.gridLength,n[i]),grid.gridLength),n[i]);
        	}
		}
	
	}


	// a star enqueueing function with first heuristic
	void AS2(Snode[] n) {

		for (int i = 0; i < n.length; i++) {
        	if(n[i]!=null) {
        		if(queue.size()==0) {
        			queue.add(n[i]);
        			continue;
        		}
			queue.add(binarySearchCostAndHeuristic2(queue,0,queue.size()-1, n[i].cost+distanceToClosestWalker(grid.gridLength,n[i]),grid.gridLength),n[i]);
        	}
		}
	
	}
	

	// control the search and visualization
	private ArrayList<Snode> Search(Grid grid, String strategy, Boolean visualize) {
		this.grid = grid;
		Snode leaf = generalSearch(this, strategy);
		 
		 if (leaf == null) {
			System.out.println("Nope, expanded nodes = "+expandedNodes);
			return null;
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

	
	// to visualize the solution step by step
	private void PrintSolution(Grid grid, ArrayList<Snode> solution) {

		for (int i = 1; i < solution.size(); i++) {
			if(applyToGrid(grid, solution.get(i)))
//				System.out.println(grid.getGrid()[0][2]);
//			System.out.println(Arrays.deepToString(grid.getGrid()).replace("], ", "]\n").replace("[[", "[").replace("]]", "]")+"\n\n");
			System.out.println(solution.get(i).operator+"\n");
			System.out.print(((StateW)solution.get(i).state).x+",");
            System.out.println(((StateW)solution.get(i).state).y);
		}
		
	}
	
    // to generate the child nodes of a given node
    Snode[] expand(Snode node) {
        Snode [] newNodes = new Snode[Operator.values().length];
        expandedNodes++;
      
        
        
        int gridLength = grid.gridLength;
        int gridWidth = grid.gridWidth;
        ArrayList<Integer> obstaclePositions = grid.obstaclePositions();
        
        int currentDepth = node.depth;
        State currentState = node.state;
        Direction currentDirection = ((StateW)currentState).direction;
        int currentX = ((StateW)currentState).x;
        int currentY = ((StateW)currentState).y;
        int currentPosition = (currentY * gridWidth) + currentX;
        int currentGlassCount = ((StateW)currentState).glassRemaining;
        int currentWalkerCount = ((StateW)currentState).walkersLeft;
        ArrayList<Integer> currentWalkerPositions = new ArrayList<Integer>();
        currentWalkerPositions.addAll(((StateW)currentState).walkerPositions);
        Direction [] directions =  Direction.values();
        
        ArrayList<Integer> adjacentWalkers = new ArrayList<Integer>();
        if(currentWalkerPositions.contains(currentPosition + 1))
            adjacentWalkers.add(currentPosition + 1);
        if(currentWalkerPositions.contains(currentPosition - 1))
            adjacentWalkers.add(currentPosition - 1);
        if(currentWalkerPositions.contains(currentPosition + gridWidth))
            adjacentWalkers.add(currentPosition + gridWidth);
        if(currentWalkerPositions.contains(currentPosition - gridWidth))
            adjacentWalkers.add(currentPosition - gridWidth);
        
        for(int i = 0; i < newNodes.length; i++) {
            Direction newDirection;
            int newX;
            int newY;
            int newGlassCount = currentGlassCount;
            int newWalkerCount = currentGlassCount;
            int newTotalGlassUsed = ((StateW)currentState).totalGlassUsed;
            boolean isVALID = true;

            Operator myOperator = operators[i];
            switch (myOperator) {
                case Stab:
                    newDirection = currentDirection;
                    newX = currentX;
                    newY = currentY;
                    if(currentGlassCount == 0 || adjacentWalkers.size() == 0 ) {
                        isVALID = false;
                        break;
                    }
                    newGlassCount = currentGlassCount - 1;
                    newTotalGlassUsed = ((StateW)currentState).totalGlassUsed+1;
                    newWalkerCount = currentWalkerCount - adjacentWalkers.size();
                    for(int j = 0; j < adjacentWalkers.size(); j++) {
                        int index = currentWalkerPositions.indexOf(adjacentWalkers.get(j));
                        currentWalkerPositions.set(index,-1 );
                    }
                    
                    break;
                case Forward:
                    newDirection = currentDirection;
                    switch(currentDirection) {
                        case N : newY = currentY - 1; newX = currentX; break;
                        case E : newX = currentX + 1; newY = currentY; break;
                        case S : newY = currentY + 1; newX = currentX; break;
                        case W : newX = currentX - 1; newY = currentY; break;
                        default: newX = newY = 0; break;
                    }
                    newGlassCount = currentGlassCount;
                    newWalkerCount = currentWalkerCount;
                    int newPosition = (newY * gridWidth) + newX;
                    if(newX < 0 || newY < 0 || newX >= gridWidth || newY >= gridLength || currentWalkerPositions.contains(newPosition) || obstaclePositions.contains(newPosition)) {
                        isVALID = false;
                        break;
                    }
                    
                    if(newPosition == grid.stonePosition()) {
                    	newGlassCount = grid.glassCapacity;
                    }
                    
                    break;
                case RotLeft :
                    newDirection = currentDirection.ordinal() == 0 ? directions[directions.length - 1] : directions[currentDirection.ordinal() - 1];
                    newX = currentX;
                    newY = currentY;
                    newGlassCount = currentGlassCount;
                    newWalkerCount = currentWalkerCount;
                    break;
                case RotRight:
                    newDirection = directions[(currentDirection.ordinal() + 1)  % directions.length];
                    newX = currentX;
                    newY = currentY;
                    newGlassCount = currentGlassCount;
                    newWalkerCount = currentWalkerCount;
                    break;
                default:
                    newDirection = null;
                    newX = newY = newGlassCount = newWalkerCount = 0;
                    isVALID = false;
                    break;
            }
            Snode newNode = null;
            
            if(isVALID == true) {
                int newCost = node.cost + pathCost(myOperator);
                State newState = new StateW(newDirection, newX, newY, newGlassCount,newTotalGlassUsed, newWalkerCount, currentWalkerPositions );
                newNode = new Snode(newState, node, myOperator, currentDepth + 1, newCost);
            }
            newNodes[i] = newNode;
        }        
        return newNodes;
    }
    
     int pathCost(Operator myOperator) {
        switch(myOperator) {
            case Stab: return 5;
            case Forward: return 1;
            default: return 0;
        }
    }

	
	// to reflect the dequeueing on the grid
	private boolean applyToGrid(Grid grid, Snode step) {
		switch(step.operator) {
		case Forward:
			grid.getGrid()[((StateW)step.parent.state).y][((StateW)step.parent.state).x] = '\u0000';
			grid.getGrid()[((StateW)step.state).y][((StateW)step.state).x] = 'J';
			return true;
		case Stab:
			if(((StateW)step.state).x+1<grid.gridWidth && grid.getGrid()[((StateW)step.state).y][((StateW)step.state).x+1] == 'W')
				grid.getGrid()[((StateW)step.state).y][((StateW)step.state).x+1] = '\u0000';
			if(((StateW)step.state).x>0 && grid.getGrid()[((StateW)step.state).y][((StateW)step.state).x-1] == 'W')
				grid.getGrid()[((StateW)step.state).y][((StateW)step.state).x-1] = '\u0000';
			if(((StateW)step.state).y+1<grid.gridLength && grid.getGrid()[((StateW)step.state).y+1][((StateW)step.state).x] == 'W')
				grid.getGrid()[((StateW)step.state).y+1][((StateW)step.state).x] = '\u0000';
			if(((StateW)step.state).y>0 && grid.getGrid()[((StateW)step.state).y-1][((StateW)step.state).x] == 'W')
				grid.getGrid()[((StateW)step.state).y-1][((StateW)step.state).x] = '\u0000';
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
			return binarySearchCost(q,center+1,end,newCost);
		}

	}
	
//	 to insert in gs1
	private int binarySearchHeuristic1(ArrayList<Snode> q, int start, int end, double newHeuristic) {
		//base case
		if(start == end) {
			return start;
		}
		//calculate mid of list
		int center = (end+start)/2;
		//redirect to left or right half
		if(((StateW)q.get(center).state).walkersLeft/(3.0*((StateW)q.get(center).state).totalGlassUsed)>newHeuristic) {
			return binarySearchHeuristic1(q,start,center,newHeuristic);
		}else {
			return binarySearchHeuristic1(q,center+1,end,newHeuristic);
		}

	}
	
	// to insert in as1
	private int binarySearchCostAndHeuristic1(ArrayList<Snode> q, int start, int end, double newCostAndHeuristic) {
		//base case
		if(start == end) {
			return start;
		}
		//calculate mid of list
		int center = (end+start)/2;
		//redirect to left or right half
		if(((StateW)q.get(center).state).walkersLeft/(3.0*((StateW)q.get(center).state).totalGlassUsed)+q.get(center).cost>newCostAndHeuristic) {
			return binarySearchCostAndHeuristic1(q,start,center,newCostAndHeuristic);
		}else {
			return binarySearchCostAndHeuristic1(q,center+1,end,newCostAndHeuristic);
		}

	}
	
	
//	 to insert in gs2
	private int binarySearchHeuristic2(ArrayList<Snode> q, int start, int end, int newHeuristic,int dim) {
		//base case
		if(start == end) {
			return start;
		}
		//calculate mid of list
		int center = (end+start)/2;
		//redirect to left or right half
		if(distanceToClosestWalker(dim,q.get(center))>newHeuristic) {
			return binarySearchHeuristic2(q,start,center,newHeuristic,dim);
		}else {
			return binarySearchHeuristic2(q,center+1,end,newHeuristic,dim);
		}

	}
	
	// to insert in as2
	private int binarySearchCostAndHeuristic2(ArrayList<Snode> q, int start, int end, int newCostAndHeuristic,int dim) {
		//base case
		if(start == end) {
			return start;
		}
		//calculate mid of list
		int center = (end+start)/2;
		//redirect to left or right half
		if(distanceToClosestWalker(dim,q.get(center))+q.get(center).cost>newCostAndHeuristic) {
			return binarySearchCostAndHeuristic2(q,start,center,newCostAndHeuristic,dim);
		}else {
			return binarySearchCostAndHeuristic2(q,center+1,end,newCostAndHeuristic,dim);
		}

	}
	
	private int distanceToClosestWalker(int dim, Snode node) {		
		int xs = ((StateW)node.state).x;
		int ys = ((StateW)node.state).y; 
		ArrayList<Integer> list = ((StateW)node.state).walkerPositions;
		
		for (int d = 1; d <= dim; d++)
		{
		    for (int i = 0; i <= 2*d; i++) {
					int x1 = xs-d;
					int y1 = ys-d+i;
					
					int x2 = xs-d+i;
					int y2 = ys-d;
					
					int x3 = xs+d;
					int y3 = ys+d-i;
					
					int x4 = xs+d-i;
					int y4 = ys+d;
					
					int loc = x1+ dim*y1;
					if(x1>=0 && y1>=0 && (loc=list.indexOf(loc))>=0) {
						if(list.get(loc) != '\u0000') {
							int a = Math.abs(x1-xs);
							int b = Math.abs(y1-ys);
							return a+b+(a>0&&b>0?1:0);
						}
					}
					loc=x2+dim*y2;
					if(x2>=0 && y2>=0 && (loc=list.indexOf(loc))>=0) {
						if(list.get(loc) != '\u0000') {
							int a = Math.abs(x2-xs);
							int b = Math.abs(y2-ys);
							return a+b+(a>0&&b>0?1:0);
						}
					}
					loc=x3+dim*y3;
					if(x3>=0 && y3>=0 && (loc=list.indexOf(loc))>=0) {
						if(list.get(loc) != '\u0000') {
							int a = Math.abs(x3-xs);
							int b = Math.abs(y3-ys);
							return a+b+(a>0&&b>0?1:0);
						}
					}
					loc=x4+dim*y4;
					if(x4>=0 && y4>=0 && (loc=list.indexOf(loc))>=0) {
						if(list.get(loc) != '\u0000') {
							int a = Math.abs(x4-xs);
							int b = Math.abs(y4-ys);
							return a+b+(a>0&&b>0?1:0);
						}
					}
			}
		}
		
		
		return -1;
	}

	@Override
	boolean goalTest(State s) {
		return ((StateW)s).isGoal;
	}

	
}
