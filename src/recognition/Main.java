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

package recognition;

import recognition.simplestNeuron.ImagesManager;

public class Main {

    /********************************************************************************
     *
     * main Method get path to images, read it, sort, check etc.
     *
     *******************************************************************************/
    public static void main(String[] args) {
        while (true) {
            new ImagesManager().selectEnteringMethod();
        }
    }
}