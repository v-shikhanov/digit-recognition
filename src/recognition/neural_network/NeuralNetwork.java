package recognition.neural_network;

public class NeuralNetwork {

    private Layer[] layers;
    private int[] layersSizes;
    private Training training;


    /**
     * Constructor for neural network
     * @param layersSizes array with quantity of neurons in every layer
     *                    for example NeuralNetwork{15,10,10,10});
     *
     */
    public NeuralNetwork(int[] layersSizes) {
        if (layersSizes.length < 2) {
            System.out.println("Incorrect network size!");
            return;
        }

        this.layers = new Layer[layersSizes.length];
        this.layersSizes = layersSizes;

        this.layers[0] = new Layer(0, layersSizes[0]);

        for (int i = 1; i < layers.length; i++) {
            this.layers[i] = new Layer(layersSizes[i-1], layersSizes[i]);
        }

        training = new Training();
    }

    public void learn() {
        layers[1] = training.correctWeights(layers[1]);
    }

    public int recognizeDigit(int[] img) {
        double result;
        double bestResult = -1000;
        int recognizedDigit = 0;

        if (img.length != layersSizes[0]) {
            System.out.println("Size of image not matched with neural network input size!!!");
            return -100;
        }

        updateNeurons(img);

        for (int i = 0; i < layersSizes[layers.length-1]; i++) {
            result = layers[layers.length-1].getNeurons()[i].getValue();
            if (result > bestResult) {
                bestResult = result;
                recognizedDigit = i;
            }
        }

        return recognizedDigit;
    }

    private void updateNeurons(int[] img) {
        for (int i = 0; i < img.length; i++) {
            this.layers[0].getNeurons()[i].setValue(img[i]);
        }

        for (int layerNumber = 1; layerNumber < layers.length; layerNumber++) {
            int neuronsQt = this.layers[layerNumber].getNeurons().length;
            for (int neuronNumber = 0; neuronNumber < neuronsQt; neuronNumber++) {
                layers[layerNumber].getNeurons()[neuronNumber].countOutput(layers[layerNumber-1].getNeurons());
            }
        }
    }


}
