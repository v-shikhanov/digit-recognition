package recognition.training;

import recognition.neural_network.Layer;

import java.util.ArrayList;

public abstract class Training {
    protected Layer[] layers;
    protected ArrayList<double[][]> deltaWeights;

    public Training(Layer[] layers) {
        this.layers = layers;
        deltaWeights = new ArrayList<>();
        resetDeltaWeights();
    }

    public void resetDeltaWeights() {
        deltaWeights.clear();
        for (int index = 0; index < layers.length - 1; index++) {
            double[][] delta = new double[layers[index].getLayerSize()][layers[index+1].getLayerSize()];
            deltaWeights.add(delta);
        }
    }

    public abstract void train(Layer[] layers, int digit, double eduSpeed);
    protected abstract void findDeltaWeights(int layerIndex, double eduSpeed);
    /**
     * Method finds ideal values depending on input image type.
     * Using for output layer.
     * @param digit- what digit on input image
     */
    protected double[] findIdealsOutLayer(int digit) {
        double[] ideals = new double[layers[layers.length-1].getLayerSize()];
        for (int i = 0; i < ideals.length; i++) {
            int idealValue = 0;
            if (i == digit) {
                idealValue = 1;
            }
            ideals[i] = idealValue;
        }
        return ideals;
    }

    public ArrayList<double[][]> getDeltaWeights() {
        return deltaWeights;
    }
}
