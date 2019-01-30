/**
 * @brief That is common class used for learning algorithm implementation
 * @author Vladislav Shikhanov
 */
package recognition.training;
import recognition.neural_network.Layer;
import java.util.ArrayList;

public abstract class Training {
    /**
     * Layers of network which should be learned
     */
    protected Layer[] layers;
    /**
     * Is an array of delta weights values to which should be corrected weights of layer
     */
    protected ArrayList<double[][]> deltaWeights;

    /**
     * Class constructor
     * @param layers layers of network which should be learned
     */
    public Training(Layer[] layers) {
        this.layers = layers;
        deltaWeights = new ArrayList<>();
        resetDeltaWeights();
    }

    /**
     * This method uses for initialization of delta weights, or it reset to zero after learning cycle finish
     */
    public void resetDeltaWeights() {
        deltaWeights.clear();
        for (int index = 0; index < layers.length - 1; index++) {
            double[][] delta = new double[layers[index].getLayerSize()][layers[index+1].getLayerSize()];
            deltaWeights.add(delta);
        }
    }

    /**
     * Main class method that finds correction values for network weights
     * @param layers layers of network which should be learned (image already loaded to network)
     * @param digit digit draw on image
     * @param eduSpeed speed education coefficient. is coefficient to control how strongly could weights deviate
     *                 from current values should be bigger for higher learning speed and less for higher accuracy
     *                 of weights correction
     */
    public abstract void train(Layer[] layers, int digit, double eduSpeed);

    /**
     * Method updates delta weights (sum it to get mean one)
     * @param layerIndex is index of layer for what delta weights founds
     * @param eduSpeed education speed coefficient
     */
    protected abstract void findDeltaWeights(int layerIndex, double eduSpeed);

    /**
     * Method finds ideal values depending on input image type.
     * Using for output layer.
     * @param digit- what digit on input image
     */
    protected double[] findIdealsOutLayer(int digit) {
        double[] ideals = new double[layers[layers.length-1].getLayerSize()];
        for (int i = 0; i < ideals.length; i++) {
            int idealValue = 0;
            if (i == digit) {
                idealValue = 1;
            }
            ideals[i] = idealValue;
        }
        return ideals;
    }

    /*
       Getters and setters
     */
    public ArrayList<double[][]> getDeltaWeights() {
        return deltaWeights;
    }
}
