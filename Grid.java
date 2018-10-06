import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Grid {
    
    private static final int minDimension = 4;
    private char [][] grid;
    private static ArrayList<Integer> allPositions;
    
    public Grid(int x) {
        grid = new char[minDimension][minDimension];
        grid[3][3] = 'J';
        grid[0][0] = 'W';
        grid[0][2] = 'W';
        grid[1][1] = 'W';
        grid[1][3] = 'W';
        grid[2][0] = 'S';
        grid[2][2] = 'W';
        grid[2][3] = 'O';
        grid[3][1] = 'O';
        
        
    }
    
    
    public Grid(){
        int gridLength = ThreadLocalRandom.current().nextInt(minDimension, minDimension + 1);
        int gridWidth  = ThreadLocalRandom.current().nextInt(minDimension, minDimension + 1);
        
        //generating random positions of white walkers and dragon stone on the grid
        ArrayList<Integer> positions = generatePositions(gridLength, gridWidth);
        int positionSize = positions.size();
        
        
        // generating an empty grid then populating it
        grid = new char [gridLength][gridWidth];
        //populating the grid
        grid[gridLength - 1][gridWidth - 1] = 'J';
        
        for(int i = 0; i < positionSize; i++) {
            char spot;
            int currentPosition = positions.get(i);
            if(i == positionSize - 1)
                spot = 'S';
            else {
                if(currentPosition < 0)
                    spot = 'O';
                else spot = 'W';
            }
            currentPosition = Math.abs(currentPosition);
            int row = currentPosition / gridWidth;
            int column = currentPosition % gridWidth;
            
            grid[row][column] = spot;
            
        }
        
        
        
    }
    
    //returns an arrayList of positions of all white walkers and dragon stone with dragon stone position being the last element of the returned array
    public static ArrayList<Integer> generatePositions(int length, int width) {
        
        allPositions = new ArrayList<Integer>();
        
        // to be documented...
        int minPosition = 0;
        int offset = ThreadLocalRandom.current().nextInt(1, width + 1 );
        System.out.println("The offset is " + offset  );
        int maxPosition =  minPosition + offset ;
        
        //reserve the last position of the grid to the initial state of Jon Snow
        
        while(maxPosition < (length * width) - offset ) {
            maxPosition = minPosition + offset ;
            int position =  ThreadLocalRandom.current().nextInt(minPosition, maxPosition);
            int isObstacle = ThreadLocalRandom.current().nextInt(0,2);
            System.out.println("isObstacle " + isObstacle);
            if(isObstacle == 0)
                allPositions.add(position);
            else allPositions.add(-1 * position);
            minPosition = position + 1;
        }
        boolean isInArray = true;
        int stonePosition;
        do {
            stonePosition = ThreadLocalRandom.current().nextInt(1, length * width - 1);
            isInArray =  Arrays.asList(allPositions).contains(stonePosition);
        }while(isInArray == true);
        
        allPositions.add(stonePosition);
        return allPositions;
    }
    
    public static void main(String [] args) {
        Grid myGrid = new Grid();
        
        System.out.println(Arrays.deepToString(myGrid.grid).replace("], ", "]\n"));
        
    }
    
    public char [][] getGrid(){
        return this.grid;
    }
    
    public ArrayList<Integer> getPositions(){
        return allPositions;
    }
    
    
}

