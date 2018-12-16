package recognition.simplestNeuron;

/**
 * This class implements simplest neuron for recognize 0 to 9 digits written in 3*5 matrix
 */
public class SimplestNeuron {
    /**
     * pre defined weight coefficients for simplest neuron
     */
    private int[] weight;
    private int bias;

    public SimplestNeuron(int[] weight, int bias) {
        this.weight = weight;
        this.bias = bias;
    }

    public SimplestNeuron(int[] weight) {
        this.weight = weight;
        this.bias = 0;
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

        for (int i = 0; i < 15; i++) {
            outputNeuron += inputNeurons[i]*weight[i];
        }

        outputNeuron += bias;

        return outputNeuron;
    }

    public int[] getWeight() {
        return weight;
    }

    public void setWeight(int[] weight) {
        this.weight = weight;
    }
}
