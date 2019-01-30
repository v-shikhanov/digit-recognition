/**
 * @brief This class contains methods for back propagation algorithm realisation
 * @author Vladislav Shikhanov
 */
package recognition.training;
import recognition.neural_network.Layer;
import java.util.ArrayDeque;

public class BackPropTraining extends Training{
    /**
     * Ideal values which are set depending on input image for every neuron in last neural network layer
     */
    private double[] idealValues;

    /**
     * Errors values which are counts for every neuron in network, counted depending on input image
     */
    private ArrayDeque<double[]> errors;

    /**
     * Class constructor
     * @param layers layers of network which should be learned
     */
    public BackPropTraining(Layer[] layers) {
        super(layers);
        errors = new ArrayDeque<>();
    }

    /**
     * Main class method that finds correction values for network weights
     * @param layers layers of network which should be learned (image already loaded to network)
     * @param digit digit draw on image
     * @param eduSpeed speed education coefficient. is coefficient to control how strongly could weights deviate
     *                 from current values should be bigger for higher learning speed and less for higher accuracy
     *                 of weights correction
     */
    @Override
    public void train(Layer[] layers, int digit, double eduSpeed) {
        this.layers = layers;
        idealValues = findIdealsOutLayer(digit);
        findLastLayerErrors();

        for (int layerIndex = layers.length - 2; layerIndex >= 0; layerIndex--) {
            findDeltaWeights(layerIndex, eduSpeed);
            if (layerIndex == 0) { break; }
            findHiddenLayerErrors(layerIndex);
        }
        errors.clear();
    }

    /**
     *  Method finds and updates error value for every neuron of layer
     *  That implementation is for last layer case
     */
    private void findLastLayerErrors() {
        double[] layerErrors = new double[layers[layers.length-1].getLayerSize()];
        for (int i = 0; i < layerErrors.length; i++) {
            double neuronValue = layers[layers.length-1].getValue(i);
            layerErrors[i] = (idealValues[i] - neuronValue) * (1 - neuronValue) * neuronValue;
        }
        errors.add(layerErrors);
    }

    /**
     *  Method finds and updates error value for every neuron of layer
     *  That implementation is for hidden layers
     * @param layerIndex index of layer for what errors should be found
     */
    private void findHiddenLayerErrors(int layerIndex) {
        double[] layerErrors = new double[layers[layerIndex].getLayerSize()];
        for (int neuronIndex = 0; neuronIndex < layers[layerIndex].getLayerSize(); neuronIndex++) {
            double sumError = 0;
            double neuronValue = layers[layerIndex].getValue(neuronIndex);
            for (int boundNeuronIndex = 0; boundNeuronIndex < layers[layerIndex+1].getLayerSize(); boundNeuronIndex++) {
                sumError += errors.getFirst()[boundNeuronIndex] *
                        layers[layerIndex].getWeight(neuronIndex, boundNeuronIndex);
            }
            layerErrors[neuronIndex] = neuronValue * (1 - neuronValue) * sumError;
        }
        errors.addFirst(layerErrors);
    }

    /**
     * Method updates delta weights (sum it to get mean one)
     * @param layerIndex is index of layer for what delta weights founds
     * @param eduSpeed education speed coefficient
     */
    @Override
    protected void findDeltaWeights(int layerIndex, double eduSpeed) {
        double[][] delta = deltaWeights.remove(layerIndex);
        for (int neuronIndex = 0; neuronIndex < layers[layerIndex].getValues().length; neuronIndex++) {
            for (int boundNeuronIndex = 0; boundNeuronIndex < layers[layerIndex + 1].getValues().length; boundNeuronIndex++) {
                delta[neuronIndex][boundNeuronIndex] += eduSpeed * layers[layerIndex].getValue(neuronIndex) *
                        errors.getFirst()[boundNeuronIndex];
            }
        }
        deltaWeights.add(layerIndex, delta);
    }
}
