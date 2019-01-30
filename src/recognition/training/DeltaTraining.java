/**
 * @brief This class contains methods for delta rule algorithm realisation
 * @author Vladislav Shikhanov
 */
package recognition.training;
import recognition.neural_network.Layer;
import java.util.ArrayDeque;

public class DeltaTraining extends Training {
    /**
     * Ideal values which are set depending on input image for every neuron in neural network
     */
    private ArrayDeque<double[]> idealValues;

    /**
     * Class constructor
     * @param layers layers of network which should be learned
     */
    public DeltaTraining(Layer[] layers) {
        super(layers);
        idealValues = new ArrayDeque<>();
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

        idealValues.addFirst(findIdealsOutLayer(digit));
        for (int layerIndex = layers.length - 2; layerIndex > 0; layerIndex--) {
            findIdealsHiddenLayer(layerIndex);
        }

        for (int layerIndex = 0; layerIndex < layers.length - 1; layerIndex++) {
            findDeltaWeights(layerIndex, eduSpeed);
        }
    }

    /**
     * Method finds ideal values depending on input image type.
     * Using for hidden layers
     */
    private void findIdealsHiddenLayer(int layerIndex) {
        double[] ideals = new double[layers[layerIndex].getLayerSize()];

        for (int neuronIndex = 0; neuronIndex < ideals.length; neuronIndex++) {
            double[] nextLayerIdeals = idealValues.getFirst();
            double idealValue = 0;

            for (int boundNeuronIndex = 0; boundNeuronIndex < nextLayerIdeals.length; boundNeuronIndex++) {
                idealValue += nextLayerIdeals[boundNeuronIndex] /
                        layers[layerIndex].getWeight(neuronIndex,boundNeuronIndex);
            }
            ideals[neuronIndex] = idealValue;
        }
        idealValues.addFirst(ideals);
    }

    /**
     * Method updates delta weights (sum it to get mean one)
     * @param layerIndex is index of layer for what delta weights founds
     * @param eduSpeed education speed coefficient
     */
    @Override
    protected void findDeltaWeights(int layerIndex, double eduSpeed) {
        double[][] delta = deltaWeights.remove(layerIndex);
        double[] ideals = idealValues.removeFirst();
        for (int neuronIndex = 0; neuronIndex < layers[layerIndex].getValues().length; neuronIndex++) {
            for (int boundNeuronIndex = 0; boundNeuronIndex < layers[layerIndex+1].getValues().length; boundNeuronIndex++) {
                delta[neuronIndex][boundNeuronIndex] += eduSpeed * layers[layerIndex].getValue(neuronIndex) *
                        (ideals[boundNeuronIndex] - layers[layerIndex+1].getValue(boundNeuronIndex));
            }
        }
        deltaWeights.add(layerIndex, delta);
    }
}
