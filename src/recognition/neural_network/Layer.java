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
     * here stored weights, that using for find values of neurons, depending on input neurons
     */
    private double[][] weights;

    /**
     * here stored values casted to 1 via sigmoid function
     * using in training process
     */
    private double[] castedValues;

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
     * Constructor for layer of neural network
     * @param prevLayerSize quantity of input neurons for this layer
     * @param layerSize quantity of neurons in this layer
     */
    public Layer(int prevLayerSize, int layerSize) {
        this.values = new double[layerSize];
        this.castedValues = new double[layerSize];
        this.idealValues = new double[layerSize];
        this.weights = new double[layerSize][prevLayerSize]; //neuron number and it's connection to prev layer
        this.deltaWeights = new double[layerSize][prevLayerSize];

        initWeights(prevLayerSize, layerSize);
    }

    /**
     * Method initialise weights with random numbers
     * @param prevLayerSize quantity of input neurons for this layer
     * @param layerSize quantity of neurons in this layer
     */
    private void initWeights(int prevLayerSize, int layerSize) {
        Random rand = new Random();
        for (int neuronIndex = 0; neuronIndex < layerSize; neuronIndex++) {
            for (int inputNeuronIndex = 0; inputNeuronIndex < prevLayerSize; inputNeuronIndex++) {
                this.weights[neuronIndex][inputNeuronIndex] = rand.nextDouble();
            }
        }
    }

    /**
     * This method counts values of neurons depending on input weights and layer
     * @param inputLayer values of input layer
     */
    public void updateValues(double[] inputLayer) {
        if (inputLayer.length != weights[1].length) {
            System.out.println("Incorrect Layer Length! updateValues-er");
        }

        for (int neuronIndex = 0; neuronIndex < values.length; neuronIndex++) {
            values[neuronIndex] = 0;
            for (int inputNeuronIndex = 0; inputNeuronIndex < inputLayer.length; inputNeuronIndex++) {
                values[neuronIndex] += weights[neuronIndex][inputNeuronIndex]*inputLayer[inputNeuronIndex];
            }
        }
    }

    /**
     * Method casts neurons values to 1
     */
    public void castValuesWithSigmoid() {
        for (int neuronIndex = 0; neuronIndex < values.length; neuronIndex++) {
            castedValues[neuronIndex] = 1 / (1 + Math.exp(- values[neuronIndex]));
        }
    }

    /**
     * Method updates delta weights (sum it to get mean one)
     * @param educationSpeed education speed coefficient
     * @param inputLayer values of neurons in previous layer
     */
    public void updateDeltaWights(double educationSpeed, double[] inputLayer) {
        for (int neuronIndex = 0; neuronIndex < values.length; neuronIndex++) {
            for (int inputNeuronIndex = 0; inputNeuronIndex < inputLayer.length; inputNeuronIndex++) {
                deltaWeights[neuronIndex][inputNeuronIndex] +=
                        educationSpeed * inputLayer[inputNeuronIndex] * (idealValues[neuronIndex] - castedValues[neuronIndex]);
            }
        }
    }

    /**
     * Method calculate mean weight value and add it to weight. Delta weights are reset to next learning cycle
     * @param divider it's a number of add delta weights during learning cycle.(to get mean val)
     *               (w1+w2+w3)/3 -3 is divider.
     */
    public void correctWights(int divider) {
        for (int neuronIndex = 0; neuronIndex < values.length; neuronIndex++) {
            for (int inputNeuronIndex = 0; inputNeuronIndex < deltaWeights[1].length; inputNeuronIndex++) {
                weights[neuronIndex][inputNeuronIndex] += deltaWeights[neuronIndex][inputNeuronIndex] / divider;
                deltaWeights[neuronIndex][inputNeuronIndex] = 0;
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
            values[i] = img[i];
        }
    }

    /**
     * Method sets ideal values depending on input image type.
     * Using for output layer.
     * @param digit- what digit on input image
     */
    public void setIdealValues(int digit) {
        for (int i = 0; i < idealValues.length; i++) {
            int idealOutput = 0;
            if (i == digit) {
                idealOutput = 1;
            }
            this.idealValues[i] = idealOutput;
        }
    }

    /**
     * Method sets ideal values depending on input image type.
     * Using for hidden layers
     * @param nextLayer is layer that is next to this one for example last layer for pre-last hidden layer.
     */
    public void setIdealOutputs(Layer nextLayer) {
        for (int neuronIndex = 0; neuronIndex < values.length; neuronIndex++) {
            double idealOutput = 0;
            int nextLayerSize = nextLayer.getIdealOutputsLength();
            for (int position = 0; position < nextLayer.getIdealOutputsLength(); position++) {
                idealOutput += nextLayer.getIdealOutput(position) / nextLayer.getWeight(position, neuronIndex);
            }
            this.idealValues[neuronIndex] = idealOutput/nextLayerSize;
        }
    }

    /*
     * getters and setters
     */
    public int  getIdealOutputsLength() {
        return idealValues.length;
    }

    public double getIdealOutput(int index) {
        return idealValues[index];
    }

    public double getWeight(int neuronIndex, int boundNeuronIndex) {
        return weights[neuronIndex][boundNeuronIndex];
    }

    public double[] getValues() {
        return values;
    }

}
