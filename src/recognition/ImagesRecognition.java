/**
 * @brief This is class for control images recognition
 *
 *
 * @authors Vladislav Shikhanov
 *****************************************************************************/

package recognition;

import recognition.neural_network.NeuralNetwork;
import recognition.neural_network.Training;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ImagesRecognition {
    private NeuralNetwork neuralNetwork;
    private ArrayList<ImageIDX> testCol;
    private Training training;
    private  Scanner scanner = new Scanner(System.in);

    /**
     * class constructor
     */
    ImagesRecognition() {
        int[] networkSizes = {784, 10};
        this.neuralNetwork = new NeuralNetwork(networkSizes);
        this.testCol = new ArrayList<>();
        this.training = new Training();

        File testImages = new File("t10k-images.idx3-ubyte");
        File testLabels = new File("t10k-labels.idx1-ubyte");

        testCol = new ReadIDX().updateCollection(testLabels,testImages);
    }

    /**
     * This is method for selecting action with neural network- trying to recognise image, or learn network
     */
    public void selectEnteringMethod() {
        int enteringMethod = -1;
        while (enteringMethod < 0 || enteringMethod > 3) {
            System.out.println("\n\nPlease select operation: " +
                    "\n0 = Learning,\n" +
                    "1 = Random image,\n" +
                    "2 = Prediction accuracy check\n" +
                    "3 = Learning to goal\n"
            );
            if (scanner.hasNextInt()) {
                enteringMethod = scanner.nextInt();
            } else {
                scanner.next();
            }
        }

        switch (enteringMethod) {
            case 0 : learn(0.5); break;

            case 1 : recognizeRandomImage(); break;

            case 2 : checkAccuracy(); break;

            case 3 : learnToGoal(); break;
        }
    }

    /**
     * That is method for one learning cycle for neural network. (One learning with 60000 images)
     * @param educationSpeed-is coefficient to control how strongly could weights deviate from current values
     *                      should be bigger for higher learning speed and less for higher accuracy of weights correction
     */
    private void learn(double educationSpeed) {
        neuralNetwork.setLayers(training.train(neuralNetwork.getLayers(), educationSpeed));
        neuralNetwork.save();
    }

    /**
     *  Method that selects random image from test collection and trying to recognize it. Going learning in case of
     *  incorrect result
     */
    private void recognizeRandomImage() {
        ImageIDX img = testCol.get(new Random().nextInt(testCol.size()));
        int recognizedDigit;

        img.printImage();

        recognizedDigit = neuralNetwork.recognizeDigit(img.getPixels());

        System.out.println("\nThe digit on picture is " + recognizedDigit);

        if (img.getLabel() != recognizedDigit) {
            System.out.println("Recognized incorrect, going learning!");
            learn(0.5);
        }
    }

    /**
     * That method for recognise every image in test collection and define accuracy  of recognition
     * @return accuracy in percents
     */
    private double checkAccuracy() {
        double checkingResult;
        double success = 0;

        for (ImageIDX img : testCol) {
            if (img.getLabel() == neuralNetwork.recognizeDigit(img.getPixels())) {
                success++;
            }
        }

        checkingResult = success / testCol.size() * 100;
        System.out.println("Prediction accuracy is " + checkingResult + "%");
        return checkingResult;
    }

    /**
     *  method completes learning cycles and trying to reach goal of accuracy for neural network.
     *  User can set accuracy in percents, max number of attempts and number of learning cycles in one attempt
     */
    private void learnToGoal() {
        int tryNumber = 0;
        double accuracy;
        double educationSpeed;
        double goal = -1;
        int maxAttempts = -1;
        int learnsBetweenChecks = -1;


        while (goal < 0 || goal > 100) {
            System.out.println("Please, insert prediction accuracy learning goal in percent");
            if (scanner.hasNextDouble()) {
                goal = scanner.nextDouble();
                break;
            } else {
                scanner.next();
            }
        }

        while (maxAttempts < 1) {
            System.out.println("Please, insert max tryings in learning process");
            if (scanner.hasNextInt()) {
                maxAttempts = scanner.nextInt();
                break;
            } else {
                scanner.next();
            }
        }

        while (learnsBetweenChecks < 1) {
            System.out.println("Please, insert number of learning cycles between accuracy checks");
            if (scanner.hasNextInt()) {
                learnsBetweenChecks = scanner.nextInt();
                break;
            } else {
                scanner.next();
            }
        }

        System.out.println("Accuracy check before learning : ");
        educationSpeed = findEducationSpeed(checkAccuracy());


        while (true) {
            System.out.println("Learning try number " + tryNumber + " with " + learnsBetweenChecks + " learning" +
                    " cycles in one try with " + educationSpeed + " education speed was started \n");

            for (int i = 0; i < learnsBetweenChecks; i++) {
                learn(educationSpeed);
            }

            accuracy = checkAccuracy();
            educationSpeed = findEducationSpeed(accuracy);

            if (accuracy > goal) {
                System.out.println("Learning reached goal, number of learning cycles is " + tryNumber * learnsBetweenChecks);
                break;
            }

            tryNumber++;

            if (tryNumber > maxAttempts) {
                System.out.println("Trying number is bigger than max value, learning not reached goal :((");
                break;
            }
        }
    }

    /**
     * Method defines education speed depending on current prediction accuracy
     * @param accuracy prediction accuracy in percent
     * @return speed coefficient
     */
    private double findEducationSpeed (double accuracy) {
        double educationSpeed = 0.5;
        if (accuracy < 10) {
            educationSpeed = 10;
        } else if (accuracy < 15) {
            educationSpeed = 5;
        }
        else if (accuracy < 20) {
            educationSpeed = 3;
        } else if (accuracy < 30) {
            educationSpeed = 2;
        } else if (accuracy < 40) {
            educationSpeed = 1.5;
        } else if (accuracy < 50) {
            educationSpeed = 1.4;
        } else if (accuracy < 60) {
            educationSpeed = 1.2;
        } else if (accuracy < 70) {
            educationSpeed = 1;
        } else if (accuracy < 80) {
            educationSpeed = 0.65;
        } else if (accuracy < 82) {
            educationSpeed = 0.5;
        } else if (accuracy < 83) {
            educationSpeed = 0.4;
        } else if (accuracy < 84) {
            educationSpeed = 0.3;
        } else if (accuracy < 85) {
            educationSpeed = 0.2;
        } else if (accuracy < 85.4) {
            educationSpeed = 0.1;
        } else if (accuracy < 86) {
            educationSpeed = 0.05;
        } else if (accuracy < 100) {
            educationSpeed = 0.005;
        }
        return educationSpeed;
    }
}
