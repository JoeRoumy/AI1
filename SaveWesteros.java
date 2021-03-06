import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

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
		Grid grid = new Grid();
		 System.out.println(Arrays.deepToString(grid.getGrid()).replace("], ","]\n").replace("[[", "[").replace("]]", "]"));
//			System.out.println(grid.johnsx+","+grid.johnsy);
		 SaveWesteros ai = new SaveWesteros(new StateW(grid.gridWidth, grid.gridLength, 0, grid.getPositions()));
		Date time = new Date();
		ai.Search(grid, "DF", true);
		System.out.println("Milliseconds taken: "+((new Date().getTime()) - time.getTime()));
	}

	public SaveWesteros(StateW initstate) {
		this.initState = initstate;
		operators = Operator.values();
	}

	// breadth first enqueueing function
	void BF(Snode[] n) {
		for (int i = n.length - 1; i > -1; i--) {
			if (n[i] != null)
				queue.add(queue.size(), n[i]);
		}
	}

	// depth first function
	void DF(Snode[] n) {
		for (int i = n.length - 1; i > -1; i--) {
			if (n[i] != null)
				queue.add(0, n[i]);
		}
	}

	// iterative deepening enqueueing function
	void ID(Snode[] n, int maxDeapth) {
		for (int i = n.length - 1; i > -1; i--) {
			if (n[i] != null && n[i].getDepth() <= maxDeapth)
				queue.add(0, n[i]);

		}
	}

	// uniform cost enqueueing function
	void UC(Snode[] n) {

		for (int i = 0; i < n.length; i++) {
			if (n[i] != null) {
				if (queue.size() == 0) {
					queue.add(n[i]);
					continue;
				}
				queue.add(binarySearchCost(queue, 0, queue.size() - 1, n[i].cost), n[i]);
			}
		}

	}

	// greedy enqueueing function with first heuristic
	void GR1(Snode[] n) {

		for (int i = 0; i < n.length; i++) {
			if (n[i] != null) {
				if (queue.size() == 0) {
					queue.add(n[i]);
					continue;
				}
				queue.add(binarySearchHeuristic1(queue, 0, queue.size() - 1, calculateHeuristic1(n[i])), n[i]);
			}
		}

	}

	// a star enqueueing function with first heuristic
	void AS1(Snode[] n) {

		for (int i = 0; i < n.length; i++) {
			if (n[i] != null) {
				if (queue.size() == 0) {
					queue.add(n[i]);
					continue;
				}
				queue.add(binarySearchCostAndHeuristic1(queue, 0, queue.size() - 1, n[i].cost + calculateHeuristic1(n[i])), n[i]);

			}
		}

	}

	// greedy enqueueing function with first heuristic
	void GR2(Snode[] n) {

		for (int i = 0; i < n.length; i++) {
			if (n[i] != null) {
				if (queue.size() == 0) {
					queue.add(n[i]);
					continue;
				}
				queue.add(binarySearchHeuristic2(queue, 0, queue.size() - 1,
						distanceToClosestWalker( grid.gridLength,n[i]), grid.gridLength), n[i]);
			}
		}

	}

	// a star enqueueing function with first heuristic
	void AS2(Snode[] n) {

		for (int i = 0; i < n.length; i++) {
			if (n[i] != null) {
				if (queue.size() == 0) {
					queue.add(n[i]);
					continue;
				}
				queue.add(binarySearchCostAndHeuristic2(queue, 0, queue.size() - 1,
						n[i].cost + distanceToClosestWalker( grid.gridLength,n[i]), grid.gridLength), n[i]);
			}
		}

	}

	// control the search and visualization
	private Result Search(Grid grid, String strategy, Boolean visualize) {
		this.grid = grid;
		Snode leaf = generalSearch(this, strategy);

		if (leaf == null) {
			System.out.println("Nope, expanded nodes = " + expandedNodes);
			return null;
		}

		ArrayList<Snode> solution = new ArrayList<>(9999);
		solution = GenerateSolution(solution, leaf);
		int totalCost = 0;

		if (visualize) {
			PrintSolution(grid, solution);
		}
		else {
			for (Iterator<Snode> iterator = solution.iterator(); iterator.hasNext();) {
				Snode snode = (Snode) iterator.next();
				System.out.println(snode.operator);
				totalCost+=snode.cost;
			}
			System.out.println("\n\nTotal cost: "+totalCost);

		}

		System.out.println("Nodes expanded: " + expandedNodes);
		return new Result(solution, totalCost, expandedNodes);

	}

	// backtracks from goal to root to get the path for the solution
	private ArrayList<Snode> GenerateSolution(ArrayList<Snode> solution, Snode leaf) {

		while (leaf.parent != null) {
			solution.add(0, leaf);
			leaf = leaf.parent;
		}

		return solution;

	}

	// to visualize the solution step by step
	private void PrintSolution(Grid grid, ArrayList<Snode> solution) {
		System.out.println("\n\n\nGlass capacity :"+grid.glassCapacity);
//		System.out
//				.println(Arrays.deepToString(grid.getGrid()).replace("], ", "]\n").replace("[[", "[").replace("]]", "]")
//						+ "\n\n");
//		System.out.println(distanceToClosestWalker(grid.gridLength, solution.get(0)));
		int totalCost = 0;

		for (int i = 0; i < solution.size(); i++) {
			if (applyToGrid(grid, solution.get(i))) {
				System.out.println(
						Arrays.deepToString(grid.getGrid()).replace("], ", "]\n").replace("[[", "[").replace("]]", "]")
								+ "\n\n");
				totalCost+=solution.get(i).cost;

			}
			// System.out.println(grid.getGrid()[0][2]);
			// String prnt="";
			// for (int j = 0; j < grid.gridWidth; j++) {
			// for (int j2 = 0; j2 < grid.gridWidth; j2++) {
			// prnt += grid.getGrid()[j][j2]+",";
			//
			// }
			// prnt+="]\n[";
			// }
			// System.out.println("["+prnt.replace(",]", "]")+"\n\n");
			// }
			System.out.print(solution.get(i).operator + "\n");
//			System.out.print(((StateW) solution.get(i).state).x + ",");
//			System.out.println(((StateW) solution.get(i).state).y);
//			System.out.println(distanceToClosestWalker(grid.gridLength, solution.get(i)));
//			System.out.print(((StateW) solution.get(i).state).walkersLeft+" :: ");
//			System.out.print(((StateW) solution.get(i).state).totalGlassUsed+" :: ");
//			System.out.println(calculateHeuristic1(solution.get(i))); 
//			System.out.println(solution.get(i).parent.parent);((StateW) solution.get(i).state).walkerPositions +
			System.out.println("\n-------------------\n");
		}
		System.out.println("solution contains: " + solution.size() + " steps");
		System.out.println(
				"solution used: " + ((StateW) solution.get(solution.size() - 1).state).totalGlassUsed + " glass");
		System.out.println("Total cost: "+totalCost);

	}

	// to generate the child nodes of a given node
	Snode[] expand(Snode node) {
		Snode[] newNodes = new Snode[Operator.values().length];
		expandedNodes++;

		int gridLength = grid.gridLength;
		int gridWidth = grid.gridWidth;
		ArrayList<Integer> obstaclePositions = grid.obstaclePositions();

		int currentDepth = node.depth;
		State currentState = node.state;
		Direction currentDirection = ((StateW) currentState).direction;
		int currentX = ((StateW) currentState).x;
		int currentY = ((StateW) currentState).y;
		int currentPosition = (currentY * gridWidth) + currentX;
		int currentGlassCount = ((StateW) currentState).glassRemaining;
		int currentWalkerCount = ((StateW) currentState).walkersLeft;

		Direction[] directions = Direction.values();

		ArrayList<Integer> adjacentWalkers = new ArrayList<Integer>();
		if ((currentPosition % gridWidth != gridWidth - 1)
				&& ((StateW) currentState).walkerPositions.contains(currentPosition + 1))
			adjacentWalkers.add(currentPosition + 1);
		if ((currentPosition % gridWidth != 0) && ((StateW) currentState).walkerPositions.contains(currentPosition - 1))
			adjacentWalkers.add(currentPosition - 1);
		if (((StateW) currentState).walkerPositions.contains(currentPosition + gridWidth))
			adjacentWalkers.add(currentPosition + gridWidth);
		if (((StateW) currentState).walkerPositions.contains(currentPosition - gridWidth))
			adjacentWalkers.add(currentPosition - gridWidth);

		for (int i = 0; i < newNodes.length; i++) {
			ArrayList<Integer> currentWalkerPositions = new ArrayList<Integer>();
			currentWalkerPositions.addAll(((StateW) currentState).walkerPositions);
			Direction newDirection;
			int newX;
			int newY;
			int newGlassCount = currentGlassCount;
			int newWalkerCount = currentWalkerCount;
			int newTotalGlassUsed = ((StateW) currentState).totalGlassUsed;
			boolean isVALID = true;

			Operator myOperator = operators[i];
			switch (myOperator) {
			case Stab:
				newDirection = currentDirection;
				newX = currentX;
				newY = currentY;
				if (currentGlassCount == 0 || adjacentWalkers.size() == 0) {
					isVALID = false;
					break;
				}
				newGlassCount = currentGlassCount - 1;
				newTotalGlassUsed = ((StateW) currentState).totalGlassUsed + 1;
				newWalkerCount = currentWalkerCount - adjacentWalkers.size();
				for (int j = 0; j < adjacentWalkers.size(); j++) {
					int index = currentWalkerPositions.indexOf(adjacentWalkers.get(j));
					// currentWalkerPositions.set(index,-1 );
					currentWalkerPositions.remove(index);
				}

				break;
			case Forward:
				newDirection = currentDirection;
				switch (currentDirection) {
				case N:
					newY = currentY - 1;
					newX = currentX;
					break;
				case E:
					newX = currentX + 1;
					newY = currentY;
					break;
				case S:
					newY = currentY + 1;
					newX = currentX;
					break;
				case W:
					newX = currentX - 1;
					newY = currentY;
					break;
				default:
					newX = newY = 0;
					break;
				}
				newGlassCount = currentGlassCount;
				newWalkerCount = currentWalkerCount;
				int newPosition = (newY * gridWidth) + newX;
				if (newX < 0 || newY < 0 || newX >= gridWidth || newY >= gridLength
						|| currentWalkerPositions.contains(newPosition) || obstaclePositions.contains(newPosition)) {
					isVALID = false;
					break;
				}

				if (newPosition == grid.stonePosition()) {
					newGlassCount = grid.glassCapacity;
				}

				break;
			case RotLeft:
				newDirection = currentDirection.ordinal() == 0 ? directions[directions.length - 1]
						: directions[currentDirection.ordinal() - 1];
				newX = currentX;
				newY = currentY;
				newGlassCount = currentGlassCount;
				newWalkerCount = currentWalkerCount;
				break;
			// case RotRight:
			// newDirection = directions[(currentDirection.ordinal() + 1) %
			// directions.length];
			// newX = currentX;
			// newY = currentY;
			// newGlassCount = currentGlassCount;
			// newWalkerCount = currentWalkerCount;
			// break;
			default:
				newDirection = null;
				newX = newY = newGlassCount = newWalkerCount = 0;
				isVALID = false;
				break;
			}
			Snode newNode = null;

			if (isVALID == true) {
				int newCost = node.cost + pathCost(myOperator);
				State newState = new StateW(newDirection, newX, newY, newGlassCount, newTotalGlassUsed, newWalkerCount,
						currentWalkerPositions);
				newNode = new Snode(newState, node, myOperator, currentDepth + 1, newCost);
			}
			newNodes[i] = checkRepeated(newNode)? newNode: null;
		}
		return newNodes;
	}

	int pathCost(Operator myOperator) {
		switch (myOperator) {
		case Stab:
			return 6;
		case Forward:
			return 3;
		default:
			return 2;
		}
	}

	// to reflect the dequeueing on the grid
	// private boolean applyToGrid(Grid grid, Snode step) {
	// switch(step.operator) {
	// case Forward:
	// if(((StateW)step.parent.state).y*grid.gridWidth+((StateW)step.parent.state).x
	// == grid.stonePosition())
	// grid.getGrid()[((StateW)step.parent.state).x][((StateW)step.parent.state).y]
	// = 'S';
	// else
	// grid.getGrid()[((StateW)step.parent.state).x][((StateW)step.parent.state).y]
	// = '\u0000';
	// grid.getGrid()[((StateW)step.state).x][((StateW)step.state).y] = 'J';
	// return true;
	// case Stab:
	// if(((StateW)step.state).x+1<grid.gridWidth &&
	// grid.getGrid()[((StateW)step.state).x+1][((StateW)step.state).y] == 'W')
	// grid.getGrid()[((StateW)step.state).x+1][((StateW)step.state).y] = '\u0000';
	// if(((StateW)step.state).x>0 &&
	// grid.getGrid()[((StateW)step.state).x-1][((StateW)step.state).y] == 'W')
	// grid.getGrid()[((StateW)step.state).x-1][((StateW)step.state).y] = '\u0000';
	// if(((StateW)step.state).y+1<grid.gridLength &&
	// grid.getGrid()[((StateW)step.state).x][((StateW)step.state).y+1] == 'W')
	// grid.getGrid()[((StateW)step.state).x][((StateW)step.state).y+1] = '\u0000';
	// if(((StateW)step.state).y>0 &&
	// grid.getGrid()[((StateW)step.state).x][((StateW)step.state).y-1] == 'W')
	// grid.getGrid()[((StateW)step.state).x][((StateW)step.state).y-1] = '\u0000';
	// return true;
	// default:
	// return false;
	//
	// }
	// }
	//
	// to reflect the dequeueing on the grid
	private boolean applyToGrid(Grid grid, Snode step) {
		switch (step.operator) {
		case Forward:
			if (((StateW) step.parent.state).y * grid.gridWidth + ((StateW) step.parent.state).x == grid
					.stonePosition())
				grid.getGrid()[((StateW) step.parent.state).y][((StateW) step.parent.state).x] = 'S';
			else
				grid.getGrid()[((StateW) step.parent.state).y][((StateW) step.parent.state).x] = '\u0000';
			grid.getGrid()[((StateW) step.state).y][((StateW) step.state).x] = 'J';
			return true;
		case Stab:
			if (((StateW) step.state).x + 1 < grid.gridWidth
					&& grid.getGrid()[((StateW) step.state).y][((StateW) step.state).x + 1] == 'W')
				grid.getGrid()[((StateW) step.state).y][((StateW) step.state).x + 1] = '\u0000';
			if (((StateW) step.state).x > 0
					&& grid.getGrid()[((StateW) step.state).y][((StateW) step.state).x - 1] == 'W')
				grid.getGrid()[((StateW) step.state).y][((StateW) step.state).x - 1] = '\u0000';
			if (((StateW) step.state).y + 1 < grid.gridLength
					&& grid.getGrid()[((StateW) step.state).y + 1][((StateW) step.state).x] == 'W')
				grid.getGrid()[((StateW) step.state).y + 1][((StateW) step.state).x] = '\u0000';
			if (((StateW) step.state).y > 0
					&& grid.getGrid()[((StateW) step.state).y - 1][((StateW) step.state).x] == 'W')
				grid.getGrid()[((StateW) step.state).y - 1][((StateW) step.state).x] = '\u0000';
			return true;
		default:
			return false;

		}
	}

	// to insert in ucs
	private int binarySearchCost(ArrayList<Snode> q, int start, int end, int newCost) {

//		// base case
//		if (start == end) {
//			return start;
//		}
//		// calculate mid of list
//		int center = (end + start) / 2;
//		// redirect to left or right half
//		if (q.get(center).cost > newCost) {
//			return binarySearchCost(q, start, center, newCost);
//		} else {
//			return binarySearchCost(q, center + 1, end, newCost);
//		}
		int i = 0;
		for (; i < q.size(); i++) {
			if(q.get(i).cost>newCost)
				break;
		}
		return i;
	}

	// to insert in gs1
	private int binarySearchHeuristic1(ArrayList<Snode> q, int start, int end, double newHeuristic) {
//		// base case
//		if (start == end) {
//			return start;
//		}
//		// calculate mid of list
//		int center = (end + start) / 2;
//		// redirect to left or right half
		int i = 0;
		for (; i < q.size(); i++) {
		
			if(calculateHeuristic1(q.get(i))>newHeuristic)
				break;
		}
		return i;

//		if (h > newHeuristic) {
//			return binarySearchHeuristic1(q, start, center, newHeuristic);
//		} else {
//			return binarySearchHeuristic1(q, center + 1, end, newHeuristic);
//		}

	}

	// to insert in as1
	private int binarySearchCostAndHeuristic1(ArrayList<Snode> q, int start, int end, double newCostAndHeuristic) {
		// base case
//		if (start == end) {
//			return start;
//		}
//		// calculate mid of list
//		int center = (end + start) / 2;
//		// redirect to left or right half
		int i = 0;
		for (; i < q.size(); i++) {

			if(calculateHeuristic1(q.get(i))+ q.get(i).cost>newCostAndHeuristic)
				break;
		}
		return i;
//		double h = Math.sqrt(((StateW) q.get(center).state).walkersLeft * ((StateW) q.get(center).state).totalGlassUsed)
//				/ 3.0;
//
//		if (h  > newCostAndHeuristic) {
//			return binarySearchCostAndHeuristic1(q, start, center, newCostAndHeuristic);
//		} else {
//			return binarySearchCostAndHeuristic1(q, center + 1, end, newCostAndHeuristic);
//		}

	}

	// to insert in gs2
	private int binarySearchHeuristic2(ArrayList<Snode> q, int start, int end, int newHeuristic, int dim) {
		// base case
//		if (start == end) {
//			return start;
//		}
//		// calculate mid of list
//		int center = (end + start) / 2;
//		// redirect to left or right half
		
		int i = 0;
		for (; i < q.size(); i++) {
			int x = distanceToClosestWalker(dim,q.get(i));
			System.out.println(x);
			if(x>newHeuristic)
				break;
		}
		return i;
//		if (distanceToClosestWalker(dim, q.get(center)) > newHeuristic) {
//			return binarySearchHeuristic2(q, start, center, newHeuristic, dim);
//		} else {
//			return binarySearchHeuristic2(q, center + 1, end, newHeuristic, dim);
//		}

	}

	// to insert in as2
	private int binarySearchCostAndHeuristic2(ArrayList<Snode> q, int start, int end, int newCostAndHeuristic,
			int dim) {
		// base case
//		if (start == end) {
//			return start;
//		}
//		// calculate mid of list
//		int center = (end + start) / 2;
//		// redirect to left or right half
//		if (distanceToClosestWalker(dim, q.get(center)) + q.get(center).cost > newCostAndHeuristic) {
//			return binarySearchCostAndHeuristic2(q, start, center, newCostAndHeuristic, dim);
//		} else {
//			return binarySearchCostAndHeuristic2(q, center + 1, end, newCostAndHeuristic, dim);
//		}
		int i = 0;
		for (; i < q.size(); i++) {
			if(distanceToClosestWalker(dim,q.get(i))+ q.get(i).cost>newCostAndHeuristic)
				break;
		}
		return i;

	}

	private int distanceToClosestWalker(int dim,Snode node) {
//		int xs = ((StateW) node.state).x;
//		int ys = ((StateW) node.state).y;
//		ArrayList<Integer> list = ((StateW) node.state).walkerPositions;
//
//		for (int d = 1; d <= dim; d++) {
//			for (int i = 0; i <= 2 * d; i++) {
//				int x1 = xs - d;
//				int y1 = ys - d + i;
//
//				int x2 = xs - d + i;
//				int y2 = ys - d;
//
//				int x3 = xs + d;
//				int y3 = ys + d - i;
//
//				int x4 = xs + d - i;
//				int y4 = ys + d;
//
//				int loc = x1 + dim * y1;
//				if (x1 >= 0 && y1 >= 0 && (loc = list.indexOf(loc)) >= 0) {
//					if (list.get(loc) != '\u0000') {
//						int a = Math.abs(x1 - xs);
//						int b = Math.abs(y1 - ys);
//						return a + b + (a > 0 && b > 0 ? 1 : 0);
//					}
//				}
//				loc = x2 + dim * y2;
//				if (x2 >= 0 && y2 >= 0 && (loc = list.indexOf(loc)) >= 0) {
//					if (list.get(loc) != '\u0000') {
//						int a = Math.abs(x2 - xs);
//						int b = Math.abs(y2 - ys);
//						return a + b + (a > 0 && b > 0 ? 1 : 0);
//					}
//				}
//				loc = x3 + dim * y3;
//				if (x3 >= 0 && y3 >= 0 && (loc = list.indexOf(loc)) >= 0) {
//					if (list.get(loc) != '\u0000') {
//						int a = Math.abs(x3 - xs);
//						int b = Math.abs(y3 - ys);
//						return a + b + (a > 0 && b > 0 ? 1 : 0);
//					}
//				}
//				loc = x4 + dim * y4;
//				if (x4 >= 0 && y4 >= 0 && (loc = list.indexOf(loc)) >= 0) {
//					if (list.get(loc) != '\u0000') {
//						int a = Math.abs(x4 - xs);
//						int b = Math.abs(y4 - ys);
//						return a + b + (a > 0 && b > 0 ? 1 : 0);
//					}
//				}
//			}
//		}
		int ret = 2*dim;
		StateW s = ((StateW)node.state);
		ArrayList<Integer> w =s.walkerPositions;
		if(w.size()==0)
			return -1;
		for (int i = 0; i < w.size(); i++) {
			int wX = w.get(i)%dim;
			int wY = w.get(i)/dim;
			int dx = Math.abs(wX-s.x);
			int dy = Math.abs(wY-s.y);
			if(dx+dy < ret) {
				ret = dx+dy;
			}
		}
		return ret;

	}

	@Override
	boolean goalTest(State s) {
		return ((StateW) s).isGoal;
	}
	
	double calculateHeuristic1(Snode n) {
		double w = ((StateW) n.state).walkersLeft, g = ((StateW) n.state).totalGlassUsed;
		return Math.sqrt(w*(g == 0? w:g))/3.0;
	}
	
	boolean checkRepeated(Snode n) {
		if(n == null)
			return false;
		Snode parent = n.parent;
		StateW state = (StateW) n.state;
		while (parent != null) {
			StateW parentState = (StateW) parent.state;
			if(state.x == parentState.x && state.y == parentState.y &&  state.direction == parentState.direction && state.glassRemaining == parentState.glassRemaining && state.totalGlassUsed == parentState.totalGlassUsed && state.walkersLeft == parentState.walkersLeft) {
				return false;
			}
			parent = parent.parent;
		}
		
		return true;
	}
	
}
