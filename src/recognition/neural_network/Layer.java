/**
 * @brief This class contains one layer of neural network
 * @author Vladislav Shikhanov
 **/
package recognition.neural_network;
import java.io.Serializable;
import java.util.Random;

public class Layer implements Serializable {
    /**
     * Here stored values of neurons
     */
    private double[] values;

    /**
     * Here stored weights, points from current layer neuron to next layer neuron
     */
    private double[][] weights;

    /**
     * Here stored biases, that using for find values of neurons
     */
    private double bias;

    /**
     * Constructor for layer of neural network
     * @param layerSize quantity of neurons in this layer
     * @param nextLayerSize quantity of neurons in the next layer
     */
    public Layer(int layerSize, int nextLayerSize) {
        values = new double[layerSize];
        bias = 1;
        initWeights(layerSize, nextLayerSize);
    }

    /**
     * Method initialises weights with random numbers by Gaussian distribution
     * @param nextLayerSize quantity of neurons in next layer
     * @param layerSize quantity of neurons in this layer
     */
    private void initWeights(int layerSize, int nextLayerSize) {
        weights = new double[layerSize][nextLayerSize];
        Random rand = new Random();
        for (int neuronIndex = 0; neuronIndex < layerSize; neuronIndex++) {
            for (int boundNeuronIndex = 0; boundNeuronIndex < nextLayerSize; boundNeuronIndex++) {
                weights[neuronIndex][boundNeuronIndex] = rand.nextGaussian();
            }
        }
    }

    /**
     * This method counts values of neurons depending on input weights and previous layer
     * @param inputLayer contents previous layer for current one
     */
    public void findValues(Layer inputLayer) {
        for (int neuronIndex = 0; neuronIndex < values.length; neuronIndex++) {
            values[neuronIndex] = inputLayer.getBias();
            for (int inputNeuronIndex = 0; inputNeuronIndex < inputLayer.getValues().length; inputNeuronIndex++) {
                values[neuronIndex] +=
                        inputLayer.getWeight(inputNeuronIndex, neuronIndex) * inputLayer.getValues()[inputNeuronIndex];
            }
            values[neuronIndex] = 1 / (1 + Math.exp(- values[neuronIndex]));
        }
    }

    /**
     * Method calculate mean weight value and add it to weight. Delta weights are reset to next learning cycle
     * @param delta is an array of delta weights values to which should be corrected weights of layer
     * @param divider sizes of images collection used for delta weights get
     */
    public void correctWeights(double[][] delta, int divider) {
        for (int neuronIndex = 0; neuronIndex < values.length; neuronIndex++) {
            for (int boundNeuronIndex = 0; boundNeuronIndex < delta[1].length; boundNeuronIndex++) {
                weights[neuronIndex][boundNeuronIndex] += delta[neuronIndex][boundNeuronIndex] / divider;
                delta[neuronIndex][boundNeuronIndex] = 0;
            }
        }
    }

    /**
     * Method loads given image to input layer of network
     * @param img image
     */
    public void mountImageToLayer(int[] img) {
        if (img.length != values.length) {
            System.out.println("Image size not equal to input layer size!!");
            return;
        }

        for (int i = 0; i < values.length; i++) {
            values[i] = (double)img[i]/255;
        }
    }
    /*
     * getters and setters
     */
    public int  getLayerSize() {
        return values.length;
    }

    public double getWeight(int neuronIndex, int boundNeuronIndex) {
        return weights[neuronIndex][boundNeuronIndex];
    }

    public double getValue(int index) {
        return values[index];
    }

    public double[] getValues() {
        return values;
    }

    public double getBias() {
        return bias;
    }
}
