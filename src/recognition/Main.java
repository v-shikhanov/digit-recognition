package recognition;
import recognition.simplestNeuron.ImagesManager;
import recognition.simplestNeuron.Learning;

public class Main {
    public static void main(String[] args) {

        ImagesManager imagesManager = new ImagesManager();
        while (true) {
            imagesManager.selectEnteringMethod();
           // imagesManager.learning.correctWeights();
        }
    }
}