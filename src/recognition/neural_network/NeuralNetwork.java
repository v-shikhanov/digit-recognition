/**
 * @brief This is class that contains neural network
 * @authors Vladislav Shikhanov
 **/
package recognition.neural_network;
import java.io.*;

public class NeuralNetwork implements Serializable {
    private Layer[] layers;
    private int[] layersSizes;

    /**
     * Constructor for neural network, load it from file if it exists
     * @param layersSizes array with quantity of neurons in every layer
     *                    for example NeuralNetwork{15,10,10,10});
     */
    public NeuralNetwork(int[] layersSizes) {
        if (layersSizes.length < 2) {
            System.out.println("Incorrect network size!");
            return;
        }
        this.layersSizes = layersSizes;
        layers = new Layer[layersSizes.length];
        layers[0] = new Layer(0, layersSizes[0]);

        for (int i = 1; i < layers.length; i++) {
            this.layers[i] = new Layer(layersSizes[i-1], layersSizes[i]);
        }

        File savedNeurons = new File("neurons.tmp");
        if (!savedNeurons.isFile()) {
            for (int i = 1; i < layers.length; i++) {
                this.layers[i] = new Layer(layersSizes[i-1], layersSizes[i]);
            }
            return;
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(savedNeurons);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            layers = (Layer[])objectInputStream.readObject();
            System.out.println("\nNeurons are load from file");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method recognize what digit is written on given image
     * @param img array with all pixels in image
     * @return digit which was written on image
     */
    public int recognizeDigit(int[] img) {
        double result;
        double bestResult = -1000;
        int recognizedDigit = 0;
        int outputLayerSize = layersSizes[layersSizes.length-1];

        if (img.length != layersSizes[0]) {
            System.out.println("Size of image not matched with neural network input size!!!");
            return -100;
        }
        updateNeurons(img);

        for (int neuronIndex = 0; neuronIndex < outputLayerSize; neuronIndex++) {
            result = layers[layers.length-1].getValues()[neuronIndex];
            if (result > bestResult) {
                bestResult = result;
                recognizedDigit = neuronIndex;
            }
        }
        return recognizedDigit;
    }

    /**
     * Method updates neurons values in network (recalculate all network)
     * @param img input image for first layer of network
     */
    private void updateNeurons(int[] img) {
        layers[0].mountImageToLayer(img);

        for (int layerIndex = 1; layerIndex < layers.length; layerIndex++) {
            double[] prevLayerValues = layers[layerIndex-1].getValues();
            layers[layerIndex].updateValues(prevLayerValues);
        }
    }

    /**
     * method saves neural network to file
     */
    public void save() {
        File savedNeurons = new File("neurons.tmp");
        if (!savedNeurons.isFile()) {
            try {
                savedNeurons.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("\nFile creation is impossible!");
                return;
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(savedNeurons);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(layers);
            objectOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        Getters and setters
     */
    public Layer[] getLayers() {
        return layers;
    }

    public void setLayers(Layer[] layers) {
        this.layers = layers;
    }
}
