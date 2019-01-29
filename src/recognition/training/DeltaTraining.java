package recognition.training;

import recognition.neural_network.Layer;

import java.util.ArrayDeque;

public class DeltaTraining extends Training {
    private ArrayDeque<double[]> idealValues;

    public DeltaTraining(Layer[] layers) {
        super(layers);
        idealValues = new ArrayDeque<>();
    }

    @Override
    public void train(Layer[] layers, int digit, double eduSpeed) {
        this.layers = layers;

        idealValues.addFirst(findIdealsOutLayer(digit));
        for (int layerIndex = layers.length - 2; layerIndex > 0; layerIndex--) {
            findIdealsHiddenLayer(layerIndex);
        }

        for (int layerIndex = 0; layerIndex < layers.length - 1; layerIndex++) {
            findDeltaWeights(layerIndex, eduSpeed);
        }
    }

    /**
     * Method finds ideal values depending on input image type.
     * Using for hidden layers
     */
    private void findIdealsHiddenLayer(int layerIndex) {
        double[] ideals = new double[layers[layerIndex].getLayerSize()];

        for (int neuronIndex = 0; neuronIndex < ideals.length; neuronIndex++) {
            double[] nextLayerIdeals = idealValues.getFirst();
            double idealValue = 0;

            for (int boundNeuronIndex = 0; boundNeuronIndex < nextLayerIdeals.length; boundNeuronIndex++) {
                idealValue += nextLayerIdeals[boundNeuronIndex] /
                        layers[layerIndex].getWeight(neuronIndex,boundNeuronIndex);
            }
            ideals[neuronIndex] = idealValue;
            //ideals[neuronIndex] = 1 / (1 + Math.exp(- ideals[neuronIndex]));
            //ideals[neuronIndex] = idealValue/nextLayerIdeals.length;
//            if (ideals[neuronIndex] > 1) {
//                ideals[neuronIndex] = 1;
//            } else if (ideals[neuronIndex] < 0) {
//                ideals[neuronIndex] = 0;
//            }
        }
        idealValues.addFirst(ideals);
    }

    /**
     * Method updates delta weights (sum it to get mean one)
     * That implementation used for delta learning method
     * @param layerIndex is index of layer for what delta weights founds
     * @param eduSpeed education speed coefficient
     */
    @Override
    protected void findDeltaWeights(int layerIndex, double eduSpeed) {
        double[][] delta = deltaWeights.remove(layerIndex);
        double[] ideals = idealValues.removeFirst();
        for (int neuronIndex = 0; neuronIndex < layers[layerIndex].getValues().length; neuronIndex++) {
            for (int boundNeuronIndex = 0; boundNeuronIndex < layers[layerIndex + 1].getValues().length; boundNeuronIndex++) {
                delta[neuronIndex][boundNeuronIndex] += eduSpeed * layers[layerIndex].getValue(neuronIndex) *
                        (ideals[boundNeuronIndex] - layers[layerIndex + 1].getValue(boundNeuronIndex));
            }
        }
        deltaWeights.add(layerIndex, delta);
    }
}
