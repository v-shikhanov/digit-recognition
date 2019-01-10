package recognition.simplest_neuron;

import java.io.Serializable;
import java.util.Random;

/**
 * This class implements simplest neuron for recognize 0 to 9 digits written in 3*5 matrix
 */
public class SimplestNeuron implements Serializable {
    /**
     * pre defined weight coefficients for simplest neuron
     */
    private double[] weight;

    private int size;

    public SimplestNeuron(int size) {
        double[] w = new double[size];
        this.size = size;
        for (int i = 0; i < 15; i++) {
            w[i] = new Random().nextDouble();
        }

        this.weight = w;
    }
    /**
     * Output Neuron implementation for defining numbers from 0 to 9 on picture
     * @param inputNeurons 15 cell array
     * @return output Neuron value
     */
    public double outputNeuron(int[] inputNeurons) {
        double outputNeuron = 0;

        if (inputNeurons.length != weight.length) {
            System.out.println("Incorrect input neurons matrix size");
            return -100;
        }

        for (int i = 0; i < size; i++) {
            outputNeuron += inputNeurons[i]*weight[i];
        }

        return outputNeuron;
    }

    public double[] getWeight() {
        return weight;
    }

    public void setWeight(int index, double weight) {
        this.weight[index] = weight;
    }
}
