/**
 *
 * @file
 * @brief Here is wrapper class (reader) for IDX files which contains
 * images with numbers examples.
 *
 *
 * @authors Vladislav Shikhanov
 *
 *		  http://move-llc.ru
 *****************************************************************************/

package recognition;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;




class ReadIDX {

    /*********************** Collections with images definition*********************/


    static ArrayList<ImageIDX> trainingCol = new ArrayList<>();
    static  ArrayList<ImageIDX> testCol = new ArrayList<>();



    /********************************************************************************
     *
     * getImages Method updating collection of read images.
     * But firstly it get the path(via getFolderPath method) to directory with images,
     * checking if the files are really exists here(via getFile method),
     * updating the collections (via updateCollection method)
     * and then making a show with read images, if it enabled.
     *
     * @param makeAShow -  when true, after collection update show with
     *                pictures starts
     * @param useStandardPath - when true, used standard path without
     *               requests to user
     ************************************************************************************/
    void getImages(boolean makeAShow, boolean useStandardPath) {

        String folderPath = getFolderPath(useStandardPath);
        System.out.println("Selected folder path: " + folderPath);

        File trainingImages = getFile(folderPath,"train-images.idx3-ubyte");
        File trainingLabels = getFile(folderPath,"train-labels.idx1-ubyte");

        File testImages = getFile(folderPath,"t10k-images.idx3-ubyte");
        File testLabels = getFile(folderPath,"t10k-labels.idx1-ubyte");


        updateCollection(trainingLabels, trainingImages, trainingCol);
        updateCollection(testLabels, testImages, testCol);



        System.out.println("Images collections updated!");

        if (makeAShow) {
            makeAShow(testCol);
        }
    }



    /********************************************************************************
     *
     * getFolderPath Method got path from user, or used default one
     *
     * Path  actual for Windows OS.
     * If use standard path option disabled, user can insert the path in manual mode
     * User can do it step by step, inserting only disk and folder names or can
     * write path manually and fully
     *
     *
     * @param useStandardPath - when true, used standard path without
     *                requests to user
     *
     * @return path
     *******************************************************************************/
    String getFolderPath(boolean useStandardPath){
        Scanner sc = new Scanner(System.in);
        String path = new String();
      //  String standardPath = "C:\\java_learn\\digit-recognition\\images";
        String standardPath = "D:\\GIT\\java\\digit-recognition\\images";
        if ( useStandardPath ){
            return standardPath;
        }

        System.out.println("\nProgram ready to get path to files with images\n");
        System.out.println("Type N to skip this process and use the default path");
        System.out.println("Type any symbol to continue");

        if (sc.hasNext("N")){
            return standardPath;
        }
        sc.nextLine();



        System.out.println("Please, Enter the disk name to insert path step by step, or write path completely now");
        path = path.concat(sc.nextLine());

        if ( path.length() > 1) {
            return path;
        }

        path = path.concat(":\\");

        for ( int i = 0; i < 100; i++ ){
            System.out.println("Please, Enter the folder number " + i + " name \n" +
                    "Type esc if you finished");

            if (sc.hasNext("esc")){
                sc.nextLine();
                return path;
            } else {
                path = path.concat(sc.nextLine() + "\\");
            }
        }
        return path;
    }




    /********************************************************************************
     *
     * getFile Method got file from folder
     *
     * If file doesn't exists, method asking user try another path
     *
     *
     * @param folderPath - path to the folder with files
     * @param fileName - name of file that should be found
     *
     * @return file
     *******************************************************************************/
    File getFile(String folderPath, String fileName) {
        File file = new File(folderPath + "\\"+fileName);

        if ( file.isFile() ) {
            System.out.println("File " + file.getName() + " was found");
        } else {
            System.out.println("File " + file.getName() + " wasn't found");
            getImages(false, false);
        }
        return file;
    }



    /********************************************************************************
     *
     * updateCollection Method updates collection with read images
     *
     * It use two files - one with labels to images (1- for image with draw of one)
     * the second one contains images which are represented like matrix 28*28 where
     * every element is a gradation of gray in current pixel 0-white, 255 black.
     *
     * Method checks compatibility of this two files and their continuity
     *
     * @param labels - file with labels to numbers
     * @param images - file with pixels description
     * @param col - Collection that should be updated
     *
     * @return boolean result of update. True if OK.
     *******************************************************************************/
    boolean updateCollection(File labels, File images, ArrayList<ImageIDX> col) {

        byte[] labelsData = new byte[0];
        byte[] imagesData = new byte[0];
        int labelsIndex = 8;
        int imagesIndex = 16;

        int labelsLength;
        int imagesLength;


        try ( FileInputStream stream = new FileInputStream(labels) ) {
            labelsData = stream.readAllBytes();

        } catch (IOException exc) {
            System.out.println("\nProblem with file reading");
        }

        try ( FileInputStream stream = new FileInputStream(images) ) {
            imagesData = stream.readAllBytes();

        } catch (IOException exc) {
            System.out.println("\nProblem with file reading");
        }


        labelsLength = convertBytesToInt(labelsData[4], labelsData[5], labelsData[6], labelsData[7]);
        imagesLength = convertBytesToInt(imagesData[4], imagesData[5], imagesData[6], imagesData[7]);

        /*
        Checking that labels and images files are equal and corrrect
        */
        if ((labelsLength != imagesLength) || (labelsData[3] != 1) || (imagesData[3] != 3) ||
                (labelsLength + labelsIndex != labelsData.length)
                || (imagesLength*784 + imagesIndex != imagesData.length)){

            System.out.println("Collection update failed");
            return false;
        }



        for( int i = 0; i < labelsLength; i++){
            ImageIDX img = new ImageIDX( Byte.toUnsignedInt(labelsData[labelsIndex]));
            labelsIndex++;

            for ( int r = 0; r < 28; r++){
                int s = 0;
                for ( ; s < 28; s++){
                    img.pixel[r][s] = Byte.toUnsignedInt(imagesData[imagesIndex]);
                    imagesIndex++;
                }
            }
            col.add(img);
        }
        return true;
    }



    /********************************************************************************
     *
     * convertBytesToInt Method converts four bytes to one int number
     *
     * @param b3 - most significant byte
     * @param b2 - byte2
     * @param b1 - byte1
     * @param b0 - less significant byte
     *
     *
     * @return int result of converting
     *******************************************************************************/
    int convertBytesToInt(byte b3, byte b2, byte b1, byte b0){
        int i =0;
        i = Byte.toUnsignedInt(b3);
        i = i << 8;
        i += Byte.toUnsignedInt(b2);
        i = i << 8;
        i += Byte.toUnsignedInt(b1);
        i = i << 8;
        i += Byte.toUnsignedInt(b0);
        return i;
    }



    /********************************************************************************
     *
     * makeAShow Method showing in terminal pictures from read collection
     *
     * @param col - Collection with images to show
     *******************************************************************************/
   void makeAShow( ArrayList<ImageIDX> col){
       System.out.println(col.size());

       for(int i = 0; i < col.size(); i++) {
           System.out.print("\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n ");
           System.out.print(col.get(i).label);
           for (int s = 0; s < 28; s++) {
               int r = 0;
               System.out.print("\n");
               for (; r < 28; r++) {
                   if (col.get(i).pixel[s][r] > 0) {
                       System.out.print("*");
                   } else {
                       System.out.print(" ");
                   }
               }
           }

           try{
               TimeUnit.SECONDS.sleep(1);
           }
           catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
   }



    /********************************************************************************
     *
     * ImageIDX is a class for one read image
     *
     * label - is real value that drawn on picture
     *
     * pixel - is a matrix 28*28 where every value is a gray gradation for
     *              pixel 0 = white, 255 = black
     *******************************************************************************/
    static class ImageIDX {
        double rate;
        int label;
        int[][] pixel = new int[28][28];
        ImageIDX (int label){
            this.label = label;
        }
    }
}
