/**
 *
 * @file
 * @brief Here is sorting images methods by difference between pixels wise
 *
 * @authors Vladislav Shikhanov
 *
 *		  http://move-llc.ru
 ************************************************************************************/

package recognition;
import java.util.ArrayList;
import java.util.Collections;

import static recognition.ReadIDX.ImageIDX;

public class SortByKNN {

    private ReadIDX readIDX = new ReadIDX();
    private ErrorFunction errorFunction = new ErrorFunction();

    private ArrayList<SearchingParameters> searchingParameters= new ArrayList<>();

    /********************************************************************************
     * getBestSearchingParameters method is a test for getting the best parameters of search
     *
     * @param referenceImg reference image
     * @param imgCollection collection to find the best images
     * @param size number of elements in output collection
     ************************************************************************************/
    void getBestSearchingParameters( ImageIDX referenceImg, ArrayList<ImageIDX> imgCollection, int size)  {
        for ( int k = 0; k < 50; ) {
            if ( k < 5) {
                k++;
            } else if ( k < 10) {
                k+= 2;
            } else if ( k <60) {
                k+= 10;
            }

            double shadeCoef = 1;
            while ( shadeCoef*k < 40 && shadeCoef < 5 ){

                System.out.println("  ");
                System.out.println(" K:" + k);
                System.out.println(" Shade Coefficient :" + shadeCoef);
                shadeCoef +=  0.25;

               createCollection(referenceImg, imgCollection, 500,k,shadeCoef,false);
               createCollection(referenceImg, imgCollection, 500,k,shadeCoef,true);
            }
        }
        Collections.sort(searchingParameters, new SearchingParametersComparator());

        System.out.println("\n\n\nSEARCHING FINISHED!!!! \n\n\n");

        for (int i = 0; i < 50; i++) {
            System.out.println(" Errors rate :" + searchingParameters.get(i).errorsRate);
            System.out.println(" K:" + searchingParameters.get(i).k);
            System.out.println(" Shade Coefficient :" + searchingParameters.get(i).shadeCoef);
            System.out.println(" Use square :" + searchingParameters.get(i).useSquare);
            System.out.println("  ");
        }
    }



    /********************************************************************************
     * createCollection method Complete searching by given collection, compare every image from it
     * to reference one and formed the collection with given size with nearest images.
     * This images get rating- It's a number which characterizes how different the comparing image
     * to reference one. This rate is count by average number of Euclidian distance got for n pixels
     * of comparing image to one pixel of reference img  Then from N nearest pixels selected K with
     * nearest shade values. Then k distances averaged. When average numbers got for all pixels of
     * reference image they are averages one more time
     * It's the rate of comparing image.
     *
     *
     * @param referenceImg reference image
     * @param imgCollection collection to find the best images
     * @param size number of elements in output collection
     * @param k number of neighbors that method should be found
     * @param shadeCoef is a coefficient of redundancy of searching. For example k=2
     *                  if shadeCoef = 3, method will firstly find 6 neighbors and form
     *                  them it will sort k with nearest shade value. For smart search.
     * @param useSquare when true Euclidian distance counts by classic rule, when
     *                  false, counts like a sum of differences
     *
     * @return collection with nearest images
     ************************************************************************************/
    ArrayList<ImageIDX>  createCollection(ImageIDX referenceImg, ArrayList<ImageIDX> imgCollection, int size, int k,
                    double shadeCoef, boolean useSquare) {

        ArrayList<ImageIDX> col = new ArrayList<>();

        for ( int i =0; i < imgCollection.size(); i++) {
            ImageIDX comparingImg = imgCollection.get(i);
            comparingImg.rate = getImageRate(referenceImg, comparingImg, k, shadeCoef, useSquare);
            col.add(comparingImg);
        }

        Collections.sort(col, new ImagesComparator());


        for (int i = size; i < col.size(); ){
            col.remove(i);
        }
        double errorsRate = errorFunction.errorFunction(col,referenceImg.label);
        System.out.println("Errors rate:" + errorsRate);
        SearchingParameters params = new SearchingParameters(errorsRate,k, shadeCoef,useSquare);
        searchingParameters.add(params);
        return col;
    }


    /********************************************************************************
     *
     * getImageRate method Used for get rating of image - It's a number which characterizes
     * how different the comparing image to reference one. This rate is count by average number of
     * Euclidian distance got for n pixels of comparing image to one pixel of reference img
     * Then from N nearest pixels selected K with nearest shade values. Then k distances averaged.
     * When average numbers got for all pixels of reference image they are averages one more time
     * It's the rate of comparing image.
     *
     *
     * @param referenceImg reference image
     * @param comparingImg image where method should find neighbors
     * @param k number of neighbors that method should be found
     * @param shadeCoef is a coefficient of redundancy of searching. For example k=2
     *                  if shadeCoef = 3, method will firstly find 6 neighbors and form
     *                  them it will sort k with nearest shade value. For smart search.
     * @param useSquare when true Euclidian distance counts by classic rule, when
     *                  false, counts like a sum of differences
     *
     * @return average Euclide distance to k nearest pixels sorted by shade value
     ************************************************************************************/
    double getImageRate ( ImageIDX referenceImg, ImageIDX comparingImg, int k,
                               double shadeCoef, boolean useSquare) {

        ArrayList<Double> eucDist = new ArrayList<>();
        double imageRate = 0;

        for (int s = 0; s < 28; s++) {
            int r = 0;
            for (; r < 28; r++) {
                if (referenceImg.pixel[s][r] > 0) {
                    double dist = findEuclideDistance
                            (comparingImg, k, s, r, referenceImg.pixel[s][r],shadeCoef, useSquare);

                    if (dist == 5000 || dist == 6000) {
                        System.out.println("Bad searching parameters");
                        return 5000;
                    } else {
                        eucDist.add(dist);
                    }
                }
            }
        }
        for ( double euclideDist: eucDist ) {
            imageRate += euclideDist;
        }
        imageRate /= eucDist.size();

        return imageRate;
    }

    /********************************************************************************
     *
     * findEuclideDistance method Used for finding N nearest neighbors (pixeles) in tested
     * image to the given pixel of reference image. Then from collection of found images
     * selected a k images with nearest shade(pixel) Value. It could improve quality of
     * sorting.
     *
     * @param comparingImg image where method should find neighbors
     * @param k number of neighbors that method should find
     * @param sInit is a number of string in matrix, where searching should be starting
     * @param rInit is a number of row in matrix, where searching should be starting
     * @param shadeVal is a value from 0 to 256 of referenced pixel
     * @param shadeCoef is a coefficient of redundancy of searching. For example k=2
     *                  if shadeCoef = 3, method will firstly find 6 neighbors and form
     *                  them it will sort k with nearest shade value. For smart search.
     * @param useSquare when true Euclidian distance counts by classic rule, when
     *                  false, counts like a sum of differences
     *
     * @return average Euclide distance to k nearest pixels sorted by shade value
     ************************************************************************************/
    double findEuclideDistance (ImageIDX comparingImg, int k,  int sInit, int rInit, int shadeVal,
                    double shadeCoef, boolean useSquare) {

        ArrayList<Neighbour> neighbours= new ArrayList<>();
        PixelScanner pixelScanner = new PixelScanner(0,0,0);
        Coordinate coordinate = new Coordinate(sInit, rInit);


        double averageDist = 0;
        /*
        *  This real number of searching.
        *  From them method will find k neighbors with nearest
        *  values of shade of gray. It should be less or equal pixels qnt
        */
        if ( k < 1 ) {
            System.out.println("K is incorrect!");
            return 6000;
        }

        double n = k * shadeCoef;
        n = (int)n;
        if ( n < k ) {
            n = k;
        } else if (n > 783 ) {
            n = 783;
        }

        for ( int i = 0; i < (int)n; ){
            int shadeDeviation = 0;
            double euclidianDist = 0;
            if (comparingImg.pixel[coordinate.s][coordinate.r] > 0) {

                if ( useSquare ) {
                    euclidianDist = Math.sqrt((coordinate.s - sInit) * (coordinate.s - sInit) +
                            (coordinate.r - rInit) * (coordinate.r - rInit));
                } else {
                    euclidianDist = Math.abs(coordinate.s- sInit) + Math.abs(coordinate.r-rInit) ;
                }

                shadeDeviation = Math.abs(shadeVal - comparingImg.pixel[coordinate.s][coordinate.r]);
                Neighbour neighbour = new Neighbour(shadeDeviation, euclidianDist);
                neighbours.add(neighbour); //Neighbor found
                i++;
            } coordinate = pixelScanner.findNextPixel(coordinate);

            if ( pixelScanner.searchIsOver ) {
                System.out.println("Were found less neighbors then requested" + neighbours.size());
                return 50000;
            }
        }

        Collections.sort(neighbours, new NeighborsComparator());


        for (int i = 0; i < k; i++) {
            averageDist += neighbours.get(i).euclidianDist;
        }

        averageDist /= k;


        return averageDist;
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
        PixelScanner componentsForSearching = new PixelScanner(0,0,0);
        Coordinate currentPos = new Coordinate(sInit,rInit);
        for(int i = 0; i < 783; i++) {
            currentPos = componentsForSearching.findNextPixel(currentPos);
            System.out.println(currentPos.s + " " + currentPos.r);
        }
    }


    /********************************************************************************
     *
     * PixelScanner is a class for searching next pixel on image, like a pixel radar.
     *
     * searchingDirection - direction of searching
     *
     * goal - how many steps should be going at one direction
     *
     * steps - how many steps is already completed
     *
     * searchIsOver - if something goes wrong set true inside the method findNextPixel
     *
     * findNextPixel -Searching for the next pixel
     *******************************************************************************/
    private class PixelScanner{
        int searchingDirection; //direction of searching
        int goal; //how many steps should be going at one direction
        int steps; //how many steps is already completed
        boolean searchIsOver;

        PixelScanner (int searchingDirection, int goal, int steps) {
            this.searchingDirection = searchingDirection;
            this.goal = goal;
            this.steps = steps;
        }

        /********************************************************************************
         *
         * findNextPixel method Searching for the next pixel complete by spiral, like a radar
         * so searching could move to the left, to the down, to the up and to the right.
         *
         *
         * @param currentPos -  position from which method should complete searching
         * @return currentPos updated position
         ************************************************************************************/
        Coordinate findNextPixel (Coordinate currentPos) {
            int  emptyMoves = 0; //for protection  from endless while caused "out of border"  searching exit
            searchIsOver = false;
            do {
                /*
                 * Here we make a steps
                 */
                switch (searchingDirection) {
                    case 0:                 // move right
                        currentPos.r++;
                        if (steps == 0) {
                            goal++;
                        }
                        break;
                    case 1:                 // move down
                        currentPos.s++;
                        break;
                    case 2:                 // move left
                        currentPos.r--;
                        if (steps == 0) {
                            goal++;
                        }
                        break;
                    case 3:                 // move up
                        currentPos.s--;
                        break;
                    default:
                        break;
                }
                steps++;

                /*
                 * Here we select the step direction
                 */
                if (steps >= goal) {
                    if (searchingDirection < 3 ) {
                        searchingDirection++;
                        steps  = 0;
                    } else {
                        searchingDirection = 0;
                        steps  = 0;
                    }

                    if ( emptyMoves < 4 ) {
                        emptyMoves++;
                    } else {
                        currentPos.s = 27;
                        currentPos.r = 27;
                        System.out.println( "Searching Is Over ");
                        searchIsOver = true;
                        break;
                    }
                }
            } while ( currentPos.s < 0 || currentPos.s > 27 || currentPos.r < 0 || currentPos.r > 27 ) ;
            return currentPos;
        }
    }

    /********************************************************************************
     *
     * Neighbour class is a combination of parameters about neighbor pixel
     *
     * shade deviation - deviation from reference pixel
     * euclidianDist - distance from reference pixel
     *******************************************************************************/
    private class Neighbour {
        int shadeDeviation;
        double euclidianDist;
        Neighbour (int shadeDeviation, double euclidianDist) {
            this.shadeDeviation = shadeDeviation;
            this.euclidianDist = euclidianDist;
        }
    }


    /********************************************************************************
     *
     * Coordinate class is a combination of row and string values
     *
     * s - number of string in matrix
     * r - number of row in matrix
     *******************************************************************************/
    class Coordinate{
        int s;
        int r;

        Coordinate (int s, int r) {
            this.s = s;
            this.r = r;
        }
    }


    /********************************************************************************
     *
     * SearchingParameters is a test class to find the best searching configuration
     *
     * errorsRate - errors rate for given images collection
     *
     * k- number of nearest pixels to find
     *
     * shadeCoef - is a coefficient of redundancy of searching. For example k=2
     *          if shadeCoef = 3, method will firstly find 6 neighbors and form
     *          them it will sort k with nearest shade value. For smart search.
     *
     * useSquare - mode of Euclid dist count true -via sqrt(a2+b2) false -just sum
     *          of dif
     *******************************************************************************/
    class SearchingParameters{
        double errorsRate;
        int k;
        double shadeCoef;
        boolean useSquare;

        SearchingParameters (double errorsRate, int k, double shadeCoef, boolean useSquare) {
            this.errorsRate = errorsRate;
            this.k = k;
            this.shadeCoef = shadeCoef;
            this.useSquare = useSquare;
        }
    }


    /********************************************************************************
     * Comparators classes
     *******************************************************************************/
    private class SearchingParametersComparator implements java.util.Comparator<SearchingParameters> {
        @Override
        public int compare(SearchingParameters param1, SearchingParameters param2) {
            return Double.compare(param1.errorsRate, param2.errorsRate);
        }
    }

    private class NeighborsComparator implements java.util.Comparator<Neighbour> {
        @Override
        public int compare(Neighbour neighbour1, Neighbour neighbour2) {
            return Integer.compare(neighbour1.shadeDeviation, neighbour2.shadeDeviation);
        }
    }

    private class ImagesComparator implements java.util.Comparator<ImageIDX> {
        @Override
        public int compare(ImageIDX img1, ImageIDX img2) {
            return Double.compare(img1.rate, img2.rate);
        }
    }
}
