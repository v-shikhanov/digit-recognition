/**
 * @brief This is class that contains layer of neural network
 * @authors Vladislav Shikhanov
 **/
package recognition.neural_network;

import java.io.Serializable;
import java.util.Random;

public class Layer implements Serializable {
    /**
     * here stored values of neurons
     * which are set from outside for input layer and count for other
     */
    private double[] values;

    /**
     * here stored weights, points from current layer neuron to next layer neuron
     */
    private double[][] weights;

    /**
     * here stored biases, that using for find values of neurons
     */
    private double bias;

    /**
     * Constructor for layer of neural network
     * @param nextLayerSize quantity of neurons in the next layer
     * @param layerSize quantity of neurons in this layer
     */
    public Layer(int layerSize, int nextLayerSize) {
        values = new double[layerSize];
        bias = 1;
        initWeights(layerSize, nextLayerSize);
    }

    /**
     * Method initialise weights with random numbers
     * @param nextLayerSize quantity of neurons in next layer
     * @param layerSize quantity of neurons in this layer
     */
    private void initWeights(int layerSize, int nextLayerSize) {
        weights = new double[layerSize][nextLayerSize];
        Random rand = new Random();
        for (int neuronIndex = 0; neuronIndex < layerSize; neuronIndex++) {
            for (int inputNeuronIndex = 0; inputNeuronIndex < nextLayerSize; inputNeuronIndex++) {
                weights[neuronIndex][inputNeuronIndex] = rand.nextGaussian();
            }
        }
    }

    /**
     * This method counts values of neurons depending on input weights and layer
     * @param inputLayer content of layer previous to current one
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
     * @param divider it's a number of add delta weights during learning cycle.(to get mean val)
     *               (w1+w2+w3)/3 -3 is divider.
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
