/**
 *
 * @file
 * @brief Here is Main of project Digit recognition. From here called  methods
 * to complete task
 *
 *
 * @authors Vladislav Shikhanov
 *
 *		  http://move-llc.ru
 ************************************************************************************/

package digitRecognition;
import static digitRecognition.ReadIDX.testCol;
public class Main {

    /********************************************************************************
     *
     * main Method get path to images, read it, sort, check etc.
     *
     *******************************************************************************/
    public static void main(String[] args) {
        ReadIDX readIDX = new ReadIDX();
        ErrorFunction errorFunction = new ErrorFunction();
        SortByKNN sortByKNN = new SortByKNN();


        readIDX.getImages(false, true);

        sortByKNN.findKNN(testCol.get(4), 5, 18,18, 200, 2);

       // errorFunction.randomClassifier();



    }
}