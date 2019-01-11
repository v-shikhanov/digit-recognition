package recognition.neural_network;

import java.io.Serializable;
import java.util.Random;

public class Neuron implements Serializable {

    private double value;

    private double[] weights;
    private double bias;

    /**
     * Constructor for case when neuron is part non-input layer
     * @param inputSize -quantity of neurons in previous layer
     */
    public Neuron(int inputSize) {
        initWeights(inputSize);
        this.value = 0;
    }

    /**
     * Method initialise weights with random numbers
     * @param inputSize is a quantity of neurons in previous layer
     */
    private void initWeights(int inputSize) {
        if (inputSize > 0) {
            this.weights = new double[inputSize];
            for (int i = 0; i < inputSize; i++) {
                this.weights[i] = new Random().nextDouble();
            }
        }
    }

    /**
     * Method count neuron value depending of prev layer state and weights
     * @param prevLayer previous layer
     */
    public void countOutput(Neuron[] prevLayer) {
        double output = 0;

        if (prevLayer.length != weights.length) {
            System.out.println("Incorrect input neurons matrix size");
        }

        for (int i = 0; i < weights.length; i++) {
            output += prevLayer[i].getValue()*weights[i];
        }

        output += bias;
        this.value = output;
    }

    /**
     * This method used only for case with two layers in network
     * Method count neuron value depending of prev layer state and weights
     * @param prevLayer previous layer
     */
    public void countOutput(int[] prevLayer) {
        double output = 0;

        if (prevLayer.length != weights.length) {
            System.out.println("Incorrect input neurons matrix size");
        }

        for (int i = 0; i < weights.length; i++) {
            output += prevLayer[i]*weights[i];
        }

        output += bias;
        this.value = output;
    }

    /**
     * Getters and setters
     */
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeight(int index, double weight) {
        this.weights[index] = weight;
    }


}
