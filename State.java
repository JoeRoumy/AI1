import java.util.ArrayList;

public class State {
    
    public enum Direction {N, S, W, E};
    
    public Direction direction;
    public int x;
    public int y;
    public int glassRemaining;
    public int totalGlassUsed;
    public int walkersLeft;
    public boolean isGoal;
    public ArrayList<Integer> walkerPositions;
    
    
    
    public State(int maxX, int maxY, int glassRemaining, int heuristic, int walkersLeft) {
        this.direction = Direction.N;
        this.x = maxX - 1;
        this.y = maxY - 1;
        this.glassRemaining = glassRemaining;
        this.walkersLeft = walkersLeft;
        this.isGoal = walkersLeft == 0;
    }
    
    public State(Direction direction, int x, int y, int glassRemaining, int heuristic, int walkersLeft ) {
        this.direction = direction;
        this.x = x;
        this.y = y;
        this.glassRemaining = glassRemaining;
        this.walkersLeft = walkersLeft;
        this. isGoal = walkersLeft == 0;
        
    }
    
}
