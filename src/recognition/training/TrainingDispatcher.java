/**
 * @brief This is class for neurl network training
 * @authors Vladislav Shikhanov
 **/
package recognition.training;
import recognition.ImageIDX;
import recognition.ReadIDX;
import recognition.neural_network.Layer;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class TrainingDispatcher {
    private DeltaTraining deltaTraining;
    private BackPropTraining backPropTraining;
    private Layer[] layers;
    private ArrayList<ImageIDX> trainingCol;
    private ArrayList<ImageIDX> trainingSet;
    private ArrayList<double[][]> deltaWeights;
    public enum mode {
         DELTA,
         BACKPROP
    }

    /**
     * Class constructor
     */
    public TrainingDispatcher(Layer[] layers) {
        deltaTraining = new DeltaTraining(layers);
        backPropTraining = new BackPropTraining(layers);
        trainingCol = new ArrayList<>();
        trainingSet = new ArrayList<>();
        deltaWeights = new ArrayList<>();
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
                deltaTraining.train(this.layers, img.getLabel(), educationSpeed);
            } else {
                backPropTraining.train(this.layers, img.getLabel(), educationSpeed);
            }
        }

        if (trainMode == mode.DELTA) {
            deltaWeights.addAll(deltaTraining.getDeltaWeights());
            deltaTraining.resetDeltaWeights();
        } else {
            deltaWeights.addAll(backPropTraining.getDeltaWeights());
            backPropTraining.resetDeltaWeights();
        }

        correctWeights(trainingSet.size());
        deltaWeights.clear();
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
     * Method updates neurons values in network (recalculate all network)
     * @param img input image for first layer of network
     */
    private void updateNeurons(int[] img) {
        layers[0].mountImageToLayer(img);

        for (int layerIndex = 1; layerIndex < layers.length; layerIndex++) {
            layers[layerIndex].findValues(layers[layerIndex-1]);
        }
    }

    /**
     * Method updates weights to final delta value that is a sum of delta weights for every image divided to
     * quantity of images used in one learning cycle
     * @param divider quantity of images used for learning
     */
    private void correctWeights(int divider) {
        for (int layerIndex = 0; layerIndex < layers.length - 1; layerIndex++) {
            layers[layerIndex].correctWeights(deltaWeights.get(layerIndex), divider);
        }
    }
}

