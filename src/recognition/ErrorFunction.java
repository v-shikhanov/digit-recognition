/**
 *
 * @file
 * @brief Here is Error function for checking accuracy of Classifier work
 * Also here implemented RandomClassifier which assigns random image to class from
 * zero  to nine
 *
 *
 * @authors Vladislav Shikhanov
 *
 *		  http://move-llc.ru
 ************************************************************************************/
package recognition;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import static recognition.ReadIDX.ImageIDX;
import static recognition.ReadIDX.testCol;

class ErrorFunction {
    /*********************** Collections with images definition*********************/
    private ReadIDX readIDX = new ReadIDX();
    private ImagesByClass imagesByClass = new ImagesByClass();

    /********************************************************************************
     *
     * accordanceAnalyse Method checks all collections with drawings to accordance
     * assigned images to real values
     *
     * @param imagesByClass is compilation of collection of numbers (collection with
     *                      drawings of 0, of 1 etc)
     *******************************************************************************/
    void accordanceAnalyse(ImagesByClass imagesByClass) {
        for ( int i = 0; i < 10; i++) {
            Double errors = 0.0;
            DecimalFormat dec = new DecimalFormat("##0.00");
            switch (i) {
                case 0: {
                    errors = errorFunction(imagesByClass.zero,i);
                    System.out.println("Errors rate for class " + i + " = " + dec.format(errors) + "%");
                    break;
                }
                case 1: {
                    errors = errorFunction(imagesByClass.one,i);
                    System.out.println("Errors rate for class " + i + " = " + dec.format(errors)  + "%");
                    break;
                }
                case 2: {
                    errors = errorFunction(imagesByClass.two,i);
                    System.out.println("Errors rate for class " + i + " = " + dec.format(errors)  + "%");
                    break;
                }
                case 3: {
                    errors = errorFunction(imagesByClass.three,i);
                    System.out.println("Errors rate for class " + i + " = " + dec.format(errors)  + "%");
                    break;
                }
                case 4: {
                    errors = errorFunction(imagesByClass.four,i);
                    System.out.println("Errors rate for class " + i + " = " + dec.format(errors)  + "%");
                    break;
                }
                case 5: {
                    errors = errorFunction(imagesByClass.five,i);
                    System.out.println("Errors rate for class " + i + " = " + dec.format(errors)  + "%");
                    break;
                }
                case 6: {
                    errors = errorFunction(imagesByClass.six,i);
                    System.out.println("Errors rate for class " + i + " = " + dec.format(errors)  + "%");
                    break;
                }
                case 7: {
                    errors = errorFunction(imagesByClass.seven,i);
                    System.out.println("Errors rate for class " + i + " = " + dec.format(errors)  + "%");
                    break;
                }
                case 8: {
                    errors = errorFunction(imagesByClass.eight,i);
                    System.out.println("Errors rate for class " + i + " = " + dec.format(errors)  + "%");
                    break;
                }
                case 9: {
                    errors = errorFunction(imagesByClass.nine,i);
                    System.out.println("Errors rate for class " + i + " = " + dec.format(errors)  + "%");
                    break;
                }

                default: {
                    return;
                }
            }
        }
    }




    /********************************************************************************
     *
     * errorFunction Method checks the collection and compare label of picture and
     * it accordance to the collection. For example if in collection with drawings of
     * number five would found the image labled as 3- it'll means error.
     *
     * @param col collection which should be checked
     * @param realValue is value of label which should be at collection
     * @return  errorsRate in %
     *******************************************************************************/
    double errorFunction(ArrayList<ImageIDX> col, int realValue) {
        double errors = 0;
        double errorsRate =0;

        if ( col.isEmpty() ) {
            System.out.println("Collection is empty");
            return 0;
        }

        for (ImageIDX aCol : col) {
            if (aCol.label != realValue) {
                errors++;
            }
        }

        errorsRate = ( errors/(double)col.size() )*100;

        return errorsRate;
    }



    /********************************************************************************
     *
     * randomClassifier Method  assigns random image from test collection
     * to class from zero  to nine
     *
     * @return  result - true if ok, false if something goes wrong
     *******************************************************************************/
    boolean randomClassifier() {
        for (ImageIDX aTestCol : testCol) {
            Random random = new Random();
            int rand = random.nextInt(10);
            switch (rand) {
                case 0: {
                    imagesByClass.zero.add(aTestCol);
                    break;
                }
                case 1: {
                    imagesByClass.one.add(aTestCol);
                    break;
                }
                case 2: {
                    imagesByClass.two.add(aTestCol);
                    break;
                }
                case 3: {
                    imagesByClass.three.add(aTestCol);
                    break;
                }
                case 4: {
                    imagesByClass.four.add(aTestCol);
                    break;
                }
                case 5: {
                    imagesByClass.five.add(aTestCol);
                    break;
                }
                case 6: {
                    imagesByClass.six.add(aTestCol);
                    break;
                }
                case 7: {
                    imagesByClass.seven.add(aTestCol);
                    break;
                }
                case 8: {
                    imagesByClass.eight.add(aTestCol);
                    break;
                }
                case 9: {
                    imagesByClass.nine.add(aTestCol);
                    break;
                }

                default: {
                    System.out.println("Random value incorrect");
                    return false;
                }
            }
        }
        System.out.println("Random classify complete\n");
        accordanceAnalyse(imagesByClass);

        return true;
    }





    /********************************************************************************
     *
     * ImagesByClass is a class for ten collections of images
     * Used for classify images by their content
     *
     *  zero - is a collection of images where draw 0
     *  one - is a collection of images where draw 1
     *  two - is a collection of images where draw 2
     *  three - is a collection of images where draw 3
     *  four - is a collection of images where draw 4
     *  five - is a collection of images where draw 5
     *  six - is a collection of images where draw 6
     *  seven - is a collection of images where draw 7
     *  eight - is a collection of images where draw 8
     *  nine - is a collection of images where draw 9
     *******************************************************************************/
    class ImagesByClass {
        ArrayList<ImageIDX> zero = new ArrayList();
        ArrayList<ImageIDX> one = new ArrayList();
        ArrayList<ImageIDX> two = new ArrayList();
        ArrayList<ImageIDX> three = new ArrayList();
        ArrayList<ImageIDX> four= new ArrayList();
        ArrayList<ImageIDX> five = new ArrayList();
        ArrayList<ImageIDX> six = new ArrayList();
        ArrayList<ImageIDX> seven = new ArrayList();
        ArrayList<ImageIDX> eight = new ArrayList();
        ArrayList<ImageIDX> nine = new ArrayList();

    }
}
