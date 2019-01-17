/**
 * @brief This is class for neurl network training
 *
 *
 * @authors Vladislav Shikhanov
 *****************************************************************************/

package recognition.neural_network;

import recognition.ImageIDX;
import recognition.ReadIDX;

import java.io.File;
import java.util.ArrayList;

public class Training {

    private Layer[] layers;
    private ArrayList<ImageIDX> trainingCol;


    /**
     * Class constructor
     */
    public Training () {
        trainingCol = new ArrayList<>();
        File trainingImages = new File("train-images.idx3-ubyte");
        File trainingLabels = new File("train-labels.idx1-ubyte");

        trainingCol = new ReadIDX().updateCollection(trainingLabels,trainingImages);
    }

    /**
     * Method that trains given layers with images from training collection and returns updated layers
     * @param layers - all layers of neural network
     * @param educationSpeed - speed education coefficient. is coefficient to control how strongly could weights deviate
     *                      from current values should be bigger for higher learning speed and less for higher accuracy
     *                      of weights correction
     * @return updated layers
     */
    public Layer[] train(Layer[] layers, double educationSpeed) {
        this.layers = layers;
        for (int i = 0; i < trainingCol.size(); i++) {
            ImageIDX img = trainingCol.get(i);
            updateDeltaWeights(img.getLabel(), img.getPixels(), educationSpeed);
        }
        correctWeights(trainingCol.size());
        return this.layers;
    }

    /**
     * Method that updates delta weights(used in learning process) final delta value is sum of delta weights for every
     * image divided to quantity of images used in one learning cycle
     * @param digit digit that draw on image
     * @param img image
     * @param educationSpeed speed education coefficient. is coefficient to control how strongly could weights deviate
     *                       from current values should be bigger for higher learning speed and less for higher accuracy
     *                       of weights correction
     */
    private void updateDeltaWeights(int digit, int[] img, double educationSpeed) {
        layers[0].mountImageToLayer(img);

        findIdealOutputs(digit);

        for (int layerIndex = 1; layerIndex < layers.length; layerIndex++) {
            double[] prevLayerValues = layers[layerIndex-1].getValues();
            layers[layerIndex].updateValues(prevLayerValues);
            layers[layerIndex].castValuesWithSigmoid();
            layers[layerIndex].updateDeltaWights(educationSpeed,prevLayerValues);
        }
    }


    /**
     * Method that updates weights to final delta value that is a sum of delta weights for every image divided to
     * quantity of images used in one learning cycle
     * @param divider quantity of images used for learning
     */
    private void correctWeights(int divider) {

        for (int layerIndex = 1; layerIndex < layers.length; layerIndex++) {
            layers[layerIndex].correctWights(divider);
        }
    }

    /**
     * Method that finds and updates ideal outputs for all layers depending on input image
     * @param digit digit that is draw on image
     */
    private void findIdealOutputs(int digit) {
        /*
           set ideal outputs to output layer
         */
        layers[layers.length-1].setIdealValues(digit);
        /*
          count ideal outputs to intermediate layers in backward order
         */
        for (int layerIndex = layers.length -2; layerIndex > 0; layerIndex--) {
            layers[layerIndex].setIdealOutputs(layers[layerIndex+1]);
        }
    }
}

