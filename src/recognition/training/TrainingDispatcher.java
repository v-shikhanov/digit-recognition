/**
 * @brief This is class for neural network training process organisation
 * @author Vladislav Shikhanov
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
    /**
     * Collection of images using for training process
     */
    private ArrayList<ImageIDX> trainingCol;

    /**
     * Is a set of images from training collection for fastest learning
     */
    private ArrayList<ImageIDX> trainingSet;

    /**
     * Delta values for network weights correction which are found by back propagation or delta algorithm
     */
    private ArrayList<double[][]> deltaWeights;

    /**
     * For selection of learning mode
     */
    public enum mode {
         DELTA,
         BACKPROP
    }

    /**
     * Class constructor
     * @param layers - layers of network which should be learned
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
     * @param trainMode - selection of training algorithm
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

    /**
     * Method updating training set with random images from training collection
     * @param size - size of set, number of images
     */
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
     * Method updates neurons values in network (recalculates all network)
     * @param img input image for input layer of network
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

