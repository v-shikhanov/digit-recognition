/**
 * @brief This is class for neurl network training
 * @authors Vladislav Shikhanov
 **/
package recognition.neural_network;
import recognition.ImageIDX;
import recognition.ReadIDX;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Training {
    private Layer[] layers;
    private ArrayList<ImageIDX> trainingCol;
    private ArrayList<ImageIDX> trainingSet;
    public enum mode {
         DELTA,
         BACKPROP
    }

    /**
     * Class constructor
     */
    public Training() {
        trainingCol = new ArrayList<>();
        trainingSet = new ArrayList<>();
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
    public Layer[] train(Layer[] layers, double educationSpeed, int trainSize, mode trainMode) {
        this.layers = layers;
        formTrainingSet(trainSize);

        for (ImageIDX img : trainingSet) {
            updateNeurons(img.getPixels());

            if (trainMode == mode.DELTA) {
                trainDelta(img.getLabel(), educationSpeed);
            } else {
                findNeuronErrors(img.getLabel(), educationSpeed);
            }
        }
        correctWeights(trainingSet.size());
        return this.layers;
    }

    private void formTrainingSet(int size) {
        Random random = new Random();
        trainingSet.clear();

        if (size > 0) {
            for (int i = 0; i < size; i++) {
                trainingSet.add(trainingCol.get(random.nextInt(trainingCol.size())));
            }
        } else {
            trainingSet.addAll(trainingCol);
        }
    }

    /**
     * Method that updates delta weights by Delta algorithm. Final delta weight value is a sum of delta weights for every
     * image divided to quantity of images used in one learning cycle
     * @param digit digit that draw on image
     * @param educationSpeed speed education coefficient. is coefficient to control how strongly could weights deviate
     *                       from current values should be bigger for higher learning speed and less for higher accuracy
     *                       of weights correction
     */
    private void trainDelta(int digit, double educationSpeed) {
        findIdealOutputs(digit);

        for (int layerIndex = 0; layerIndex < layers.length - 1; layerIndex++) {
            layers[layerIndex].findDeltaWeights(educationSpeed, layers[layerIndex + 1], mode.DELTA);
        }
    }

    /**
     * Method updates weights to final delta value that is a sum of delta weights for every image divided to
     * quantity of images used in one learning cycle
     * @param divider quantity of images used for learning
     */
    private void correctWeights(int divider) {
        for (int layerIndex = 0; layerIndex < layers.length - 1; layerIndex++) {
            layers[layerIndex].correctWeights(divider);
        }
    }

    /**
     * Method that finds and updates ideal outputs for all layers depending on input image
     * @param digit digit that is draw on image
     */
    private void findIdealOutputs(int digit) {
        layers[layers.length-1].findIdealValues(digit);

        for (int layerIndex = layers.length -2; layerIndex > 0; layerIndex--) {
            layers[layerIndex].findIdealValues(layers[layerIndex+1]);
        }
    }

    /**
     * Method that updates delta weights by Back propagation algorithm. Final delta value is sum of delta weights for every
     * image divided to quantity of images used in one learning cycle
     * @param digit digit that draw on image
     * @param educationSpeed speed education coefficient. is coefficient to control how strongly could weights deviate
     *                       from current values should be bigger for higher learning speed and less for higher accuracy
     * of weights correction
     */
    private void findNeuronErrors(int digit, double educationSpeed) {
        layers[layers.length-1].findIdealValues(digit);
        layers[layers.length-1].findErrors();

        for (int layerIndex = layers.length -2; layerIndex >= 0; layerIndex--) {
            layers[layerIndex].findErrors(layers[layerIndex+1]);
            layers[layerIndex].findDeltaWeights(educationSpeed, layers[layerIndex+1], mode.BACKPROP);
        }
    }

    /**
     * Method updates neurons values in network (recalculate all network)
     * @param img input image for first layer of network
     */
    private void updateNeurons(int[] img) {
        layers[0].mountImageToLayer(img);

        for (int layerIndex = 1; layerIndex < layers.length; layerIndex++) {
            layers[layerIndex].findValues(layers[layerIndex-1]);
        }
    }
}

