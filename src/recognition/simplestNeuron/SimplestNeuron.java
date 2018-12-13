package recognition.simplestNeuron;

/**
 * This class implements simplest neuron for recognize 1 or 0 written in 3*3 matrix
 */
public class SimplestNeuron {
    /**
     * pre defined weight coefficients for simplest neuron
     */
    private final int[][] weight = {{2,1,2},{4,-4,4},{2,-1,2}};
    private final int bias = -5;

    /**
     * Output Neuron implementation for defining one or zero on picture
     * @param inputNeurons 9 cell matrix
     * @return 1 if number is 1, 0 if 0;
     */
    public int outputNeuron(int[][] inputNeurons) {
        int outputNeuron = 0;

        if (inputNeurons.length != 3) {
            return -1;
        }

        for (int string = 0; string < 3; string++) {
            for (int row = 0; row < 3; row++) {
                outputNeuron += inputNeurons[string][row] * weight[string][row];
            }
        }

        outputNeuron += bias;

        if (outputNeuron > 0) {
           return 0;
        } else {
           return 1;
        }
    }
}
