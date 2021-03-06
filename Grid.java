import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Grid {
    
    
    private static final int minDimension = 4;
    private static final int maxDimension = 6;
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
    
    
    //static grid constructor for testing
    public Grid(int x) {
        Wpositions = new ArrayList<>();
        Opositions = new ArrayList<>();
        
        if(x < 3) {
            grid = new char[minDimension][minDimension];
            gridLength = minDimension;
            gridWidth = minDimension;
            grid[3][3] = 'J';
            glassCapacity = 2;
            
            if(x == 1) {
                grid[3][0] = 'W';
                grid[3][2] = 'S';
                Wpositions.add(12);
                Sposition = 14;
            }
            if(x == 2) {
                
                grid[0][0] = 'W';
                grid[0][2] = 'W';
                grid[1][1] = 'W';
                grid[1][3] = 'W';
                grid[2][0] = 'S';
                grid[2][3] = 'O';
                grid[3][1] = 'O';
                Wpositions.add(0);
                Wpositions.add(2);
                Wpositions.add(5);
                Wpositions.add(7);
                Opositions.add(11);
                Opositions.add(13);
                Sposition = 8;
                
            }
        }
        else {
            grid = new char[maxDimension][maxDimension];
            gridLength = maxDimension;
            gridWidth = maxDimension;
            grid[5][5] = 'J';
            glassCapacity = 4;
            
            grid[0][1] = 'W';
            grid[1][0] = 'W';
            grid[2][3] = 'W';
            grid[3][1] = 'W';
            grid[4][1] = 'W';
            grid[2][0] = 'O';
            grid[4][4] = 'O';
            grid[5][4] = 'S';
            Wpositions.add(1);
            Wpositions.add(6);
            Wpositions.add(15);
            Wpositions.add(19);
            Wpositions.add(25);
            Opositions.add(12);
            Opositions.add(28);
            Sposition = 34;
            
            
            
        }
        
    }
    
    
    // grid constructor that generates a random sized grid with random positions
    public Grid(){
        gridLength = ThreadLocalRandom.current().nextInt(minDimension, maxDimension + 1);
        // gridWidth  = ThreadLocalRandom.current().nextInt(minDimension, maxDimension + 1);
        gridWidth = gridLength;
        johnsx = gridWidth-1;
        johnsy = gridLength-1;
        
        //generating random positions of white walkers and dragon stone on the grid
        generatePositions(gridLength, gridWidth);
        int WpositionSize = Wpositions.size();
        int OpositionSize = Opositions.size();
        glassCapacity  = ThreadLocalRandom.current().nextInt(1, WpositionSize + 2);
        
        
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
    
    //populates array lists containing random positions of white walkers, obstacles, and dragon stone
    @SuppressWarnings("unlikely-arg-type")
    public static void generatePositions(int length, int width) {
        
        allPositions = new ArrayList<Integer>();
        Wpositions = new ArrayList<Integer>();
        Opositions = new ArrayList<Integer>();
        
        
        /*
         * A randomly sized kernel iterates over the grid assigning random positions for obstacles and white
         *  walkers within the kernel range. If the kernel reaches the end of the grid, stone position is
         *  randomly generated and the function returns
         * */
        int minPosition = 0;
        int offset = ThreadLocalRandom.current().nextInt(3, width + 1 );
        //        System.out.println("The offset is " + offset  );
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
        // System.out.println(allPositions);
        boolean isInArray = true;
        int stonePosition;
        do {
            stonePosition = ThreadLocalRandom.current().nextInt(1, length * width - 1);
            // isInArray =  Arrays.asList(allPositions).contains(stonePosition);
            isInArray = allPositions.contains(stonePosition);
        }while(isInArray == true);
        
        Sposition = stonePosition;
        
    }
    
    
    
    //main method
    //    public static void main(String [] args) {
    //        Grid myGrid = new Grid(2);
    //
    //        System.out.println(Arrays.deepToString(myGrid.grid).replace("], ", "]\n"));
    //        System.out.println( myGrid.johnsx + "," + myGrid.johnsy +  "\n" + allPositions + "\n\n" + Wpositions + "\n" + Opositions + "\n" + Sposition);
    //
    //
    //    }
    
    
    
    //getters...
    
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

