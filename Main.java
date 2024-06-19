// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.util.Random;

/**
 * Wave Function Collapse  seabed algorithm, randomly generates a tileset unduh da sea(ASCII art baby)
 */
public class Main {
    //TODO bug: reefs sometimes exceed max size by exactly 1
    public static void main(String[] args) {

        final int ENTROPYLEVEL = 4;
        final int HEIGHT = 36;
        final int LENGTH = 18;
        final int GRIDSIZE = HEIGHT*LENGTH;
        final int REEFNUM = (GRIDSIZE)/36; //Current algo is 1 reef section per 36 square units
        final int MAXKELPNUM = (GRIDSIZE/6); //current algo is about 15% of grid is allowed to be kelp.
        final int MAXFISHNUM = (GRIDSIZE/10); //current algo is about 10% of grid is allowed to be fish.
        final int REEFMAXSIZE = REEFNUM/3;

        int[][] entropyGrid = new int[HEIGHT][LENGTH];
        String[][] objGrid = new String[HEIGHT][LENGTH];

        //String "tiles"
        String fish = ">-<>"; //1
        String reef = "¸,ø¤"; //2
        String kelp = "://:"; //3
        String sand = ":.:'"; //4
        String debug = "□"; //-1

        //initialize grid at highest entropy level
        initGrid(entropyGrid, ENTROPYLEVEL);

        //print out initial grid
        for(int i = 0; i < entropyGrid.length; i++) {

            for (int j : entropyGrid[i]) {

                System.out.print(j + " ");
            }
            System.out.println();
        }
        System.out.println();

        //employ collapse function here and edit entropy grid array

        //runtime block for generating entropy grid via recursion using WFC method

        System.out.println("Generating map...");

        for(int i = 0; i< REEFNUM; i++) {
            entropyGrid = createNewReef(entropyGrid, HEIGHT, LENGTH, REEFMAXSIZE);
        }

        //generates kelp squares
        entropyGrid = genKelp(entropyGrid, HEIGHT, LENGTH, MAXKELPNUM);
        //generates fish
        entropyGrid = genFish(entropyGrid, HEIGHT, LENGTH, MAXKELPNUM);

        System.out.println("finished.");

        //print out entropy grid
        for(int i = 0; i < entropyGrid.length; i++) {

            for (int j : entropyGrid[i]) {

                System.out.print(j + " ");

            }
            System.out.println();
        }
        System.out.println();



        //print out the final obj grid
        for(int i = 0; i < entropyGrid.length; i++) {

            for (int j : entropyGrid[i]) {

                switch(j){
                    case 4:
                        System.out.print(sand);
                        break;
                    case 3:
                        System.out.print(kelp);
                        break;
                    case 2:
                        System.out.print(reef);
                        break;
                    case 1:
                        System.out.print(fish);
                        break;
                    case -1:
                        System.out.print(debug);
                        break;
                }

            }
            System.out.println();
        }
        System.out.println();

    }


    //build recursive methods here for every tile type. After a random un-collapsed tile is picked then it will go through each tile method in order.
    // reef: should have  a maximum reef size it doesn't exceed, should have reef integrity(reef parts attached to other reef parts without holes. Size can be random withing max size constraint.)
    // kelp: should have sand for at least one of its neighbors. Should be a mx number of kelp patches in the entire grid depending on grid size (e.g. 10% or less?)
    // fish: should have sand or kelp for at least one of its borders(not surrounded on all sides by reef). Max number depending on grid size (20%?)
    // sand: all leftover tiles wll be sand.

    //later: add weighting system to random selection to simulate fish  behavior (preferring to be near kelp or holes in reef as "hiding spots" more than out in the open, or to be near other fish maybe? away from predators, etc.)

    /**
     *
     * @param entropyGrid
     * @param gridHeight
     * @param gridLength
     * @param maxSize
     * @return
     */
    public static int[][] createNewReef(int[][] entropyGrid, int gridHeight, int gridLength, int maxSize){

        Random rand = new Random();
        int HIndex = -1, LIndex = -1;
        Reef reef = new Reef(1);


        //picks a random 4-entropy tile index from the grid as a root piece. Should not pick an index that is less than 4 entropy.
        do{

            HIndex = rand.nextInt(gridHeight);
            LIndex = rand.nextInt(gridLength);

        }while(entropyGrid[HIndex][LIndex] != 4); //TODO should this be '<' instead of '!='?

        //plants reef "node" (the first reef piece)
        entropyGrid[HIndex][LIndex] = 2;


        int topH = HIndex-1, topL = LIndex;
        int leftH = HIndex, leftL = LIndex-1;
        int rightH = HIndex, rightL = LIndex+1;
        int bottomH = HIndex+1, bottomL = LIndex;
        int placeReef = 0;


        if(reef.getSize() < maxSize) { //TODO experiment with different weights for spawning off the rood node... if vs while loop here, etc.
            //top check
            if (topH >= 0) { //collapse if statements?
                if (entropyGrid[topH][topL] == 4) {
                    placeReef = rand.nextInt(2); //50/50 chance to drop a reef piece if the space is clear for it
                    //System.out.println(placeReef + " < top placeReef");
                    if (placeReef > 0) {
                        entropyGrid[topH][topL] = 2;
                        reef.incrSize();
                        //System.out.println("top changed");
                    }
                }
            } //debug test this
            //left check
            if (leftL >= 0 && entropyGrid[leftH][leftL] == 4) {

                placeReef = rand.nextInt(2);
                //System.out.println(placeReef + " < left placeReef");
                if (placeReef > 0) {
                    entropyGrid[leftH][leftL] = 2;
                    reef.incrSize();
                    //System.out.println("left changed");
                }

            }
            //right check
            if (rightL < gridLength && entropyGrid[rightH][rightL] == 4) {

                placeReef = rand.nextInt(2);
                //System.out.println(placeReef + " < right placeReef");
                if (placeReef > 0) {
                    entropyGrid[rightH][rightL] = 2;
                    reef.incrSize();
                    //System.out.println("right changed");
                }

            }
            //bottom check
            if (bottomH < gridHeight) { //collapse if statements?
                if (entropyGrid[bottomH][bottomL] == 4) {
                    placeReef = rand.nextInt(2); //50/50 chance to drop a reef piece if the space is clear for it
                    //System.out.println(placeReef + " < bottom placeReef");
                    if (placeReef > 0) {
                        entropyGrid[bottomH][bottomL] = 2;
                        reef.incrSize();
                        //System.out.println("bottom changed");
                    }
                }
            } //debug test this

            //randomly selects one of the new reef blocks to recurse off of
            int selectNext = rand.nextInt(4);
            //System.out.println("random select: " + selectNext);
            switch(selectNext){
                case 0: //top
                    if(topH >= 0 && entropyGrid[topH][topL] == 2){
                        entropyGrid = growReef(entropyGrid, reef, topH, topL, gridHeight, gridLength, reef.getSize(), maxSize);
                        break;
                    }
                case 1: //left
                    if(leftL >= 0 && entropyGrid[leftH][leftL] == 2){
                        entropyGrid = growReef(entropyGrid, reef, leftH, leftL, gridHeight, gridLength, reef.getSize(), maxSize);
                        break;
                    }
                case 2: //right
                    if(rightL < gridLength && entropyGrid[rightH][rightL] == 2){
                        entropyGrid = growReef(entropyGrid, reef, rightH, rightL, gridHeight, gridLength, reef.getSize(), maxSize);
                        break;
                    }
                case 3: //bottom
                    if(bottomH < gridHeight && entropyGrid[bottomH][bottomL] == 2){
                        entropyGrid = growReef(entropyGrid, reef, bottomH, bottomL, gridHeight, gridLength, reef.getSize(), maxSize);
                        break;
                    }
            }


        }


        return entropyGrid;


    }

    /**
     *
     * @param entropyGrid
     * @param reef
     * @param HIndex
     * @param LIndex
     * @param gridHeight
     * @param gridLength
     * @param size
     * @param maxSize
     * @return
     */
    private static int[][] growReef(int[][] entropyGrid, Reef reef, int HIndex, int LIndex,int gridHeight, int gridLength, int size, int maxSize){

        Random rand = new Random();

        int topH = HIndex-1, topL = LIndex;
        int leftH = HIndex, leftL = LIndex-1;
        int rightH = HIndex, rightL = LIndex+1;
        int bottomH = HIndex+1, bottomL = LIndex;
        int placeReef = 0;

        if(reef.getSize() < maxSize) { //TODO current algo makes reefs exactly of the max size... try this being an if check vs a while loop
            //top check
            if (topH >= 0) { //collapse if statements?
                if (entropyGrid[topH][topL] == 4) {
                    placeReef = rand.nextInt(2); //50/50 chance to drop a reef piece if the space is clear for it
                    //System.out.println(placeReef + " < top placeReef");
                    if (placeReef > 0) {
                        entropyGrid[topH][topL] = 2;
                        reef.incrSize();
                        //System.out.println("top changed");
                    }
                }
            } //debug test this
            //left check
            if (leftL >= 0 && entropyGrid[leftH][leftL] == 4) {

                placeReef = rand.nextInt(2);
                //System.out.println(placeReef + " < left placeReef");
                if (placeReef > 0) {
                    entropyGrid[leftH][leftL] = 2;
                    reef.incrSize();
                    //System.out.println("left changed");
                }

            }
            //right check
            if (rightL < gridLength && entropyGrid[rightH][rightL] == 4) {

                placeReef = rand.nextInt(2);
                //System.out.println(placeReef + " < right placeReef");
                if (placeReef > 0) {
                    entropyGrid[rightH][rightL] = 2;
                    reef.incrSize();
                    //System.out.println("right changed");
                }

            }
            //bottom check
            if (bottomH < gridHeight) { //collapse if statements?
                if (entropyGrid[bottomH][bottomL] == 4) {
                    placeReef = rand.nextInt(2); //50/50 chance to drop a reef piece if the space is clear for it
                    //System.out.println(placeReef + " < bottom placeReef");
                    if (placeReef > 0) {
                        entropyGrid[bottomH][bottomL] = 2;
                        reef.incrSize();
                        //System.out.println("bottom changed");
                    }
                }
            } //debug test this

            //randomly selects one of the 4 spots to continue growing off of... a reef piece must have been placed to continue growing off it.
            int selectNext = rand.nextInt(4);
            //System.out.println("random select: " + selectNext);
            switch(selectNext){
                case 0: //top
                    if(topH >= 0 && entropyGrid[topH][topL] == 2){
                        entropyGrid = growReef(entropyGrid, reef, topH, topL, gridHeight, gridLength, reef.getSize(), maxSize);
                        break;
                    }
                case 1: //left
                    if(leftL >= 0 && entropyGrid[leftH][leftL] == 2){
                        entropyGrid = growReef(entropyGrid, reef, leftH, leftL, gridHeight, gridLength, reef.getSize(), maxSize);
                        break;
                    }
                case 2: //right
                    if(rightL < gridLength && entropyGrid[rightH][rightL] == 2){
                        entropyGrid = growReef(entropyGrid, reef, rightH, rightL, gridHeight, gridLength, reef.getSize(), maxSize);
                        break;
                    }
                case 3: //bottom
                    if(bottomH < gridHeight && entropyGrid[bottomH][bottomL] == 2){
                        entropyGrid = growReef(entropyGrid, reef, bottomH, bottomL, gridHeight, gridLength, reef.getSize(), maxSize);
                        break;
                    }
            }

        }

        return entropyGrid;


    }

    /**
     *
     * @param entropyGrid
     * @param gridHeight
     * @param gridLength
     * @param maxNumKelp
     * @return
     */
    public static int[][] genKelp(int[][] entropyGrid, int gridHeight, int gridLength, int maxNumKelp){

            Random rand = new Random();
            int numKelp = 0;
            int HIndex = -1, LIndex = -1;

            //picks a random number of kelp that will be planted, up to the cap.
            maxNumKelp = rand.nextInt(maxNumKelp+1);

           //Rolls to plant kelp if conditions are met. Should be on a sand square. Places the number of kelp that was rolled previously.
            while(numKelp < maxNumKelp){
                //picks a random 4-entropy tile index from the grid as a root piece. Should not pick an index that is less than 4 entropy(should pick sand).
                do{

                    HIndex = rand.nextInt(gridHeight);
                    LIndex = rand.nextInt(gridLength);

                }while(entropyGrid[HIndex][LIndex] != 4);


                int topH = HIndex-1, topL = LIndex;
                int leftH = HIndex, leftL = LIndex-1;
                int rightH = HIndex, rightL = LIndex+1;
                int bottomH = HIndex+1, bottomL = LIndex;
                int placeKelp = 0;

                entropyGrid[HIndex][LIndex] = 3;

                numKelp += 1;
            }

        return entropyGrid;
    }

    public static int[][] genFish(int[][] entropyGrid, int gridHeight, int gridLength, int maxNumFish){

        Random rand = new Random();
        int numKelp = 0;
        int HIndex = -1, LIndex = -1;

        //picks a random number of kelp that will be planted, up to the cap.
        maxNumFish = rand.nextInt(maxNumFish+1);

        //Rolls to plant kelp if conditions are met. Should be on a sand square. Places the number of kelp that was rolled previously.
        while(numKelp < maxNumFish){
            //picks a random 4-entropy tile index from the grid as a root piece. Should not pick an index that is less than 4 entropy(should pick sand).
            do{

                HIndex = rand.nextInt(gridHeight);
                LIndex = rand.nextInt(gridLength);

            }while(entropyGrid[HIndex][LIndex] != 4);


            int topH = HIndex-1, topL = LIndex;
            int leftH = HIndex, leftL = LIndex-1;
            int rightH = HIndex, rightL = LIndex+1;
            int bottomH = HIndex+1, bottomL = LIndex;
            int placeKelp = 0;

            entropyGrid[HIndex][LIndex] = 1;

            numKelp += 1;
        }

        return entropyGrid;
    }

    /**
     * Initializes the entire entropy grid at the highest entropy level.
     * @param entropyGrid
     * @param entropyLevel
     * @return
     */
    public static int[][] initGrid(int[][] entropyGrid, int entropyLevel){

        //initialize entropy grid tiles with the entropy level(total number of tiles)

        for(int i = 0; i < entropyGrid.length; i++){

            for(int j = 0; j < entropyGrid[i].length; j++){

                entropyGrid[i][j] = entropyLevel;

            }

        }

        return entropyGrid;

    }


}