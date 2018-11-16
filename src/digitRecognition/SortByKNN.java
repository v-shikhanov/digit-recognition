/**
 *
 * @file
 * @brief Here is sorting images methods by difference between pixels wise
 *
 * @authors Vladislav Shikhanov
 *
 *		  http://move-llc.ru
 ************************************************************************************/

package digitRecognition;
import java.util.ArrayList;
import java.util.Collections;

import static digitRecognition.ReadIDX.ImageIDX;

public class SortByKNN {
    private ReadIDX readIDX = new ReadIDX();
    private ComponentsForSearching componentsForSearching = new ComponentsForSearching(0,0,0);

    void sortImages(ImageIDX referenceImg, ArrayList<ImageIDX> imgCollection, int k, int n) {

    }

    void createKnnMatrix ( ImageIDX referenceImg, ImageIDX comparedImg, int k) {
        for (int s = 0; s< 28; s++) {
            int r = 0;
            System.out.print("\n");
            for (; r < 28; r++) {
                if (referenceImg.pixel[s][r] > 0) {
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
            }
        }
    }

    /********************************************************************************
     *
     * findKNN method Used for finding k nearest neighbors (pixeles) in tested
     * image to the given pixel of reference image
     *
     * @param comparedImg image where method should find neighbors
     * @param k number of neighbors that method should find
     * @param sInit is a number of string in matrix, where searching should be starting
     * @param rInit is a number of row in matrix, where searching should be starting
     * @param shadeVal is a value from 0 to 256 of referenced pixel
     * @param shadeCoef is a coefficient of redundancy of searching. For example k=2
     *                  if shadeCoef = 3, method will firstly find 6 neighbors and form
     *                  them it will sort k with nearest shade value. For smart search.
     *
     * @return average Euclide distance to k nearest pixels sorted by shade value
     ************************************************************************************/
    int findKNN (ImageIDX comparedImg, int k,  int sInit, int rInit, int shadeVal, double shadeCoef) {

        ArrayList<Neighbour> neighbours= new ArrayList<>();
        Neighbour referencePixel = new Neighbour(shadeVal,1000);
        Coordinate coordinate = new Coordinate(sInit, rInit);
        componentsForSearching.steps = 0;
        componentsForSearching.goal= 0;
        componentsForSearching.searchIsOver = false;  //reset at start of new searching
        componentsForSearching.searchingDirection = 0;  //to the start position

        int euclidianDist = 0;
        /*
        *  This real number of searching.
        *  From them method will find k neighbors with nearest
        *  values of shade of gray. It should be less or equal pixels qnt
        */
        double n = k * shadeCoef;
        n = (int)n;
        if (n > 783 ) {
            n = 783;
        }
        neighbours.add(referencePixel);

        for ( int i = 0; i < (int)n; ){
            if (comparedImg.pixel[coordinate.s][coordinate.r] > 0) {
                euclidianDist = Math.abs(coordinate.s- sInit) + Math.abs(coordinate.r-rInit) ;
                Neighbour neighbour = new Neighbour(comparedImg.pixel[coordinate.s][coordinate.r], euclidianDist);
                neighbours.add(neighbour); //Neighbor found
                i++;
            } coordinate = selectPixel(coordinate);

            if ( componentsForSearching.searchIsOver ) {
                System.out.println("Were found less neighbors then requested" + neighbours.size());
                return 50000;
            }
        }

        Collections.sort(neighbours, new NeighborsComparator());

        ArrayList<Integer> dist = new ArrayList<>();
        neighbours.indexOf(referencePixel);

        return 2;
    }


    /********************************************************************************
     *
     * selectPixel method Searching for the next pixel complete by spiral, like a radar
     * so searching could move to the left, to the down, to the up and to the right.
     *
     *
     * @param currentPos -  position from which method should complete searching
     * @return currentPos updated position
     ************************************************************************************/
    private Coordinate selectPixel (Coordinate currentPos) {
        int  emptyMoves = 0; //for protection  from endless while caused "out of border"  searching exit
        do {
            /*
             * Here we make a steps
             */
            switch (componentsForSearching.searchingDirection) {
                case 0:                 // move right
                    currentPos.r++;
                    if (componentsForSearching.steps == 0) {
                        componentsForSearching.goal++;
                    }
                    break;
                case 1:                 // move down
                    currentPos.s++;
                    break;
                case 2:                 // move left
                    currentPos.r--;
                    if (componentsForSearching.steps == 0) {
                        componentsForSearching.goal++;
                    }
                    break;
                case 3:                 // move up
                    currentPos.s--;
                    break;
                default:
                    break;
            }
            componentsForSearching.steps++;

            /*
             * Here we select the step direction
             */
            if (componentsForSearching.steps >= componentsForSearching.goal) {
                if (componentsForSearching.searchingDirection < 3 ) {
                    componentsForSearching.searchingDirection++;
                    componentsForSearching.steps  = 0;
                } else {
                    componentsForSearching.searchingDirection = 0;
                    componentsForSearching.steps  = 0;
                }

                if ( emptyMoves < 4 ) {
                    emptyMoves++;
                } else {
                    currentPos.s = 27;
                    currentPos.r = 27;
                    System.out.println( "Searching Is Over ");
                    componentsForSearching.searchIsOver = true;
                    break;
                }
            }

        } while ( currentPos.s < 0 || currentPos.s > 27 || currentPos.r < 0 || currentPos.r > 27 ) ;
        return currentPos;
    }

    /********************************************************************************
     *
     * coordShift service method for coordination searching test
     *
     *
     * @param sInit -  init string position
     * @param rInit - init row position
     ************************************************************************************/
    void coordShift(int sInit, int rInit ){
        Coordinate currentPos = new Coordinate(sInit,rInit);
        for(int i = 0; i < 783; i++) {
            currentPos = selectPixel(currentPos);
            System.out.println(currentPos.s + " " + currentPos.r);
        }
    }

    private class Neighbour {
        int shade;
        int euclidianDist;
        Neighbour (int shade, int euclidianDist) {
            this.shade = shade;
            this.euclidianDist = euclidianDist;
        }
    }


    class Coordinate{
        int s;
        int r;

        Coordinate (int s, int r) {
            this.s = s;
            this.r = r;
        }
    }

    private class ComponentsForSearching{
        int searchingDirection; //direction of searching
        int goal; //how many steps should be going at one direction
        int steps; //how many steps is already completed
        boolean searchIsOver;

        ComponentsForSearching (int searchingDirection, int goal, int steps) {
            this.searchingDirection = searchingDirection;
            this.goal = goal;
            this.steps = steps;
        }
    }

    private class NeighborsComparator implements java.util.Comparator<Neighbour> {
        @Override
        public int compare(Neighbour neighbour1, Neighbour neighbour2) {
            return Integer.compare(neighbour1.shade, neighbour2.shade);
        }
    }

}
