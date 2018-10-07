import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Grid {
    
    private static final int minDimension = 4;
    private char [][] grid;
    private static ArrayList<Integer> allPositions;
    
    //static initial position
    public int johnsx = minDimension-1;
    public int johnsy = minDimension-1;
    
    public int gridLength;
    public int gridWidth;
    
    private static ArrayList<Integer> Wpositions;
    private static ArrayList<Integer> Opositions;
    private static int Sposition;
    
    public int glassCapacity;
    
    public Grid(int x) {
        grid = new char[minDimension][minDimension];
        gridLength = minDimension;
        gridWidth = minDimension;
        grid[3][3] = 'J';
        grid[0][0] = 'W';
        grid[0][2] = 'W';
        grid[1][1] = 'W';
        grid[1][3] = 'W';
        grid[2][0] = 'S';
        grid[2][2] = 'W';
        grid[2][3] = 'O';
        grid[3][1] = 'O';
        glassCapacity = 3;
        
    }
    
    
    public Grid(){
        gridLength = ThreadLocalRandom.current().nextInt(minDimension, minDimension + 1);
        gridWidth  = ThreadLocalRandom.current().nextInt(minDimension, minDimension + 1);
        
        johnsx = gridWidth-1;
        johnsy = gridLength-1;
        
        //generating random positions of white walkers and dragon stone on the grid
        generatePositions(gridLength, gridWidth);
        int WpositionSize = Wpositions.size();
        int OpositionSize = Opositions.size();
        glassCapacity  = ThreadLocalRandom.current().nextInt(1,WpositionSize+1);

        
        // generating an empty grid then populating it
        grid = new char [gridLength][gridWidth];
        //populating the grid
        grid[gridLength - 1][gridWidth - 1] = 'J';
        
        
        for(int i = 0; i < WpositionSize; i++) {
            int currentPosition = Wpositions.get(i);
            int row = currentPosition / gridWidth;
            int column = currentPosition % gridWidth;
            grid[row][column] = 'W';
        }
        
        for(int i = 0; i < OpositionSize; i++) {
            int currentPosition = Opositions.get(i);
            int row = currentPosition / gridWidth;
            int column = currentPosition % gridWidth;
            grid[row][column] = 'O';
        }
        
        int Srow = Sposition / gridWidth;
        int Scol = Sposition % gridWidth;
        grid[Srow][Scol] = 'S';
        
        
        
        //        for(int i = 0; i < WpositionSize; i++) {
        //            char spot = 'W';
        //            int currentPosition = Wpositions.get(i);
        //            if(i == positionSize - 1)
        //                spot = 'S';
        //            else {
        //                if(currentPosition < 0)
        //                    spot = 'O';
        //                else spot = 'W';
        //            }
        //            currentPosition = Math.abs(currentPosition);
        //            int row = currentPosition / gridWidth;
        //            int column = currentPosition % gridWidth;
        //
        //            grid[row][column] = spot;
        //
        //        }
        
        
        
    }
    
    //returns an arrayList of positions of all white walkers and dragon stone with dragon stone position being the last element of the returned array
    public static void generatePositions(int length, int width) {
        
        allPositions = new ArrayList<Integer>();
        Wpositions = new ArrayList<Integer>();
        Opositions = new ArrayList<Integer>();
        
        
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
            
            allPositions.add(position);
            if(isObstacle == 0)
                Wpositions.add(position);
            else Opositions.add(position);
            minPosition = position + 1;
        }
        boolean isInArray = true;
        int stonePosition;
        do {
            stonePosition = ThreadLocalRandom.current().nextInt(1, length * width - 1);
            isInArray =  Arrays.asList(allPositions).contains(stonePosition);
        }while(isInArray == true);
        
        Sposition = stonePosition;
        
    }
    
    public static void main(String [] args) {
        Grid myGrid = new Grid();
        
        System.out.println(Arrays.deepToString(myGrid.grid).replace("], ", "]\n"));
        
    }
    
    public char [][] getGrid(){
        return this.grid;
    }
    
    public ArrayList<Integer> getPositions(){
        return Wpositions;
    }
    public ArrayList<Integer> obstaclePositions(){
        return Opositions;
    }
    public int stonePosition() {
        return Sposition;
    }
    
    
    
}

