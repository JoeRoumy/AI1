import java.util.ArrayList;

public class StateW extends State {
    
    //    public enum Direction {N, S, W, E};
    
    public Direction direction;
    public int x;
    public int y;
    public int glassRemaining;
    public int totalGlassUsed;
    public int walkersLeft;
    public boolean isGoal;
    public ArrayList<Integer> walkerPositions;
    
    
    
    public StateW(int maxX, int maxY, int glassRemaining,  ArrayList<Integer> walkers) {
        this.direction = Direction.N;
        this.x = maxX - 1;
        this.y = maxY - 1;
        this.glassRemaining = glassRemaining;
        this.walkersLeft = walkers.size();
        this.isGoal = walkersLeft == 0;
        this.totalGlassUsed = 0;
        this.walkerPositions = walkers;
        if(this.isGoal)
        	System.out.println("goal created!!");
    }
    
    public StateW(Direction direction, int x, int y, int glassRemaining, int totGlass, int walkersLeft, ArrayList<Integer> walkerPositions ) {
        this.direction = direction;
        this.x = x;
        this.y = y;
        this.glassRemaining = glassRemaining;
        this.walkersLeft = walkerPositions.size();
        this. isGoal = walkersLeft == 0;
        this.walkerPositions = walkerPositions;
        this.totalGlassUsed = totGlass;
        if(this.isGoal)
        	System.out.println("goal created!!");
        
    }
    
}

