package recognition.neural_network;


import java.io.Serializable;

public class Layer implements Serializable {

    private Neuron[] neurons;

    /**
     * Constructor for layer of neural network
     * @param inputSize quantity of input neurons for this layer
     * @param outputSize quantity of neurons in this layer
     */
    public Layer(int inputSize, int outputSize) {
        this.neurons = new Neuron[outputSize];

        for (int i = 0; i < outputSize; i++) {
            this.neurons[i]= new Neuron(inputSize);
        }
    }

    /**
     * Getter for neurons of this layer
     * @return all neurons in this layer
     */
    public Neuron[] getNeurons() {
        return neurons;
    }

}
