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
     * Method loads given image to input layer of network
     * @param img image
     */
    public void mountImageToLayer(int[] img) {
        if (img.length != neurons.length) {
            System.out.println("Image size not equal to input layer size!!");
            return;
        }

        for (int i =0; i < neurons.length; i++) {
            neurons[i].setValue(img[i]);
        }
    }

    /**
     * Getter for neurons of this layer
     * @return all neurons in this layer
     */
    public Neuron[] getNeurons() {
        return neurons;
    }

    public void setNeuron(int index, Neuron n) {
        this.neurons[index] = n;
    }
}
