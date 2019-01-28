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
     * here stored ideal values for layer neurons
     * using in training process
     */
    private double[] idealValues;

    /**
     * here stored delta weights values for layer neurons
     * using in training process
     */
    private double[][] deltaWeights;

    /**
     *  Error of neuron. Finds by formula. Used for backprop algorithm
     */
    private double[] errors;

    /**
     * Constructor for layer of neural network
     * @param nextLayerSize quantity of neurons in the next layer
     * @param layerSize quantity of neurons in this layer
     */
    public Layer(int layerSize, int nextLayerSize) {
        values = new double[layerSize];
        bias = 1;
        idealValues = new double[layerSize];
        deltaWeights = new double[layerSize][nextLayerSize];
        errors = new double[layerSize];

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
     * Method updates delta weights (sum it to get mean one)
     * That implementation used for delta learning method
     * @param educationSpeed education speed coefficient
     * @param nextLayer
     * @param mode DELTA if delta method used for learning, BACKPROP for backpropogation mode
     */
    public void findDeltaWeights(double educationSpeed, Layer nextLayer, Training.mode mode) {
        for (int neuronIndex = 0; neuronIndex < values.length; neuronIndex++) {
            for (int boundNeuronIndex = 0; boundNeuronIndex < nextLayer.values.length; boundNeuronIndex++) {
                double delta = 0;
                if (mode == Training.mode.DELTA) {
                    delta = nextLayer.getIdealOutput(boundNeuronIndex) - nextLayer.getValue(boundNeuronIndex);
                } else {
                    delta = nextLayer.getError(boundNeuronIndex);
                }
                deltaWeights[neuronIndex][boundNeuronIndex] += educationSpeed * values[neuronIndex] * delta;
            }
        }
    }

    /**
     * Method calculate mean weight value and add it to weight. Delta weights are reset to next learning cycle
     * @param divider it's a number of add delta weights during learning cycle.(to get mean val)
     *               (w1+w2+w3)/3 -3 is divider.
     */
    public void correctWeights(int divider) {
        for (int neuronIndex = 0; neuronIndex < values.length; neuronIndex++) {
            for (int boundNeuronIndex = 0; boundNeuronIndex < deltaWeights[1].length; boundNeuronIndex++) {
                weights[neuronIndex][boundNeuronIndex] += deltaWeights[neuronIndex][boundNeuronIndex] / divider;
                deltaWeights[neuronIndex][boundNeuronIndex] = 0;
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

    /**
     * Method sets ideal values depending on input image type.
     * Using for output layer.
     * @param digit- what digit on input image
     */
    public void findIdealValues(int digit) {
        for (int i = 0; i < idealValues.length; i++) {
            int idealOutput = 0;
            if (i == digit) {
                idealOutput = 1;
            }
            idealValues[i] = idealOutput;
        }
    }

    /**
     * Method sets ideal values depending on input image type.
     * Using for hidden layers
     * @param nextLayer is layer that is next to this one for example last layer for pre-last hidden layer.
     */
    public void findIdealValues(Layer nextLayer) {
        for (int neuronIndex = 0; neuronIndex < values.length; neuronIndex++) {
            int nextLayerSize = nextLayer.getLayerSize();
            double idealOutput = 0;

            for (int boundNeuronIndex = 0; boundNeuronIndex < nextLayerSize; boundNeuronIndex++) {
                idealOutput += nextLayer.getIdealOutput(boundNeuronIndex) / weights[neuronIndex][boundNeuronIndex];
            }

            idealValues[neuronIndex] = idealOutput/nextLayerSize;

            if (idealValues[neuronIndex] > 1) {
                idealValues[neuronIndex] = 1;
            } else if (idealValues[neuronIndex] < 0) {
                idealValues[neuronIndex] = 0;
            }
        }
    }

    /**
     *  method finds and updates error value for every neuron of layer
     *  That implementation is for last layer case
     */
    public void findErrors() {
        for (int i = 0; i < errors.length; i++) {
            errors[i] = (idealValues[i] - values[i]) * (1 - values[i]) * values[i];
        }
    }

    /**
     *  method finds and updates error value for every neuron of layer
     *  That implementation is for hidden layers
     *
     * @param nextLayer is next layer (for example output for pre-last layer)
     */
    public void findErrors(Layer nextLayer) {
        for (int neuronIndex = 0; neuronIndex < values.length; neuronIndex++) {
            double sumError = 0;
            for (int boundNeuronIndex = 0; boundNeuronIndex < nextLayer.getLayerSize(); boundNeuronIndex++) {
                sumError += nextLayer.getError(boundNeuronIndex) * weights[neuronIndex][boundNeuronIndex];
            }
            errors[neuronIndex] = values[neuronIndex] * (1 - values[neuronIndex]) * sumError;
        }
    }
    /*
     * getters and setters
     */
    public int  getLayerSize() {
        return values.length;
    }

    public double getIdealOutput(int index) {
        return idealValues[index];
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

    public double getError(int index) {
        return errors[index];
    }
}
