package recognition.simplestNeuron;

import java.util.Random;
import java.util.Scanner;

public class ImagesManager {
    private Scanner scanner = new Scanner(System.in);
    private OutputNeuronsCollection neurons = new OutputNeuronsCollection();

    private int selectedImage;

    public void selectEnteringMethod() {

        char[] image = new char[15];
        int enteringMethod = -1;
        while (enteringMethod < 0 || enteringMethod > 3) {
            System.out.println("\n\nPlease select operation: " +
                    "\n0 = manual image entering,\n" +
                    "1 = random generator,\n" +
                    "2 = pre-defined images \n" +
                    "3 = Self learning mode with pre-defined images\n"
            );
            if (scanner.hasNextInt()) {
                enteringMethod = scanner.nextInt();
            } else {
                scanner.next();
            }
        }

        switch (enteringMethod) {
            case 0 : image = getUserImage(); break;

            case 1 : image = createRandomImage(); break;

            case 2 : image = usePredefinedImages(new Random().nextInt(10)); break;

            case 3 : selfLearn(0,0); return;

        }

        digitRecognize(convertImage(image));
    }

    private char[] getUserImage() {
        char[] image = new char[15];
        String line;
        System.out.println("Input 3x5 grid('_' for white, any other for black).Five strings, 3 symbol in one, or one ");
        for (int string = 0; string < 5; string++) {
            do {
                line = scanner.next();
                if (line.length() != 3) {
                    System.out.println("String should contents exactly 3 symbols");
                }
            } while (line.length() !=3);

            for (int i = 0; i < 3; i++) {
                int index = i + string*3;
                image[index] = line.charAt(i);
            }
        }
        selectedImage = 10;
        return image;
    }

    private char[] createRandomImage() {
        char[] image = new char[15];
        for (int i = 0; i < 15; i++) {
            if (new Random().nextBoolean()){
                image[i] = '_';
            } else {
                image[i] = 'X';
            }
        }
        selectedImage = 10;
        return image;
    }

    private boolean selfLearn(int totalAttempts, int learningCycles) {
        if (totalAttempts > 10000) {
            System.out.println("More then 10000 attempts without result :(");
            return false;
        }

        for (int i = 0; i < 10; i++) {
           totalAttempts++;
           char[] img = usePredefinedImages(i);
           if(!digitRecognize(convertImage(img))) {
               learningCycles++;
               if( selfLearn(totalAttempts,learningCycles)) {
                   return true;
               } else {
                   return false;
               }
           }
        }
        System.out.println("Learning finished! Total attempts: " + totalAttempts +
                " Learning cycles: " + learningCycles );
        return true;

    }

    private char[] usePredefinedImages(int id) {
        char[] i0 =  {
                'X','X','X',
                'X','_','X',
                'X','_','X',
                'X','_','X',
                'X','X','X',
        };

        char[] i1=  {
                '_','X','_',
                '_','X','_',
                '_','X','_',
                '_','X','_',
                '_','X','_',
        };

        char[] i2 =  {
                'X','X','X',
                '_','_','X',
                'X','X','X',
                'X','_','_',
                'X','X','X',
        };

        char[] i3 =  {
                'X','X','X',
                '_','_','X',
                'X','X','X',
                '_','_','X',
                'X','X','X',
        };

        char[] i4 =  {
                'X','_','X',
                'X','_','X',
                'X','X','X',
                '_','_','X',
                '_','_','X',
        };

        char[] i5 =  {
                'X','X','X',
                'X','_','_',
                'X','X','X',
                '_','_','X',
                'X','X','X',
        };

        char[] i6 =  {
                'X','X','X',
                'X','_','_',
                'X','X','X',
                'X','_','X',
                'X','X','X',
        };

        char[] i7 =  {
                'X','X','X',
                '_','_','X',
                '_','_','X',
                '_','_','X',
                '_','_','X',
        };

        char[] i8 =  {
                'X','X','X',
                'X','_','X',
                'X','X','X',
                'X','_','X',
                'X','X','X',
        };

        char[] i9 =  {
                'X','X','X',
                'X','_','X',
                'X','X','X',
                '_','_','X',
                'X','X','X',
        };

        selectedImage = id;
        switch (id) {
            case 0 : return i0;
            case 1 : return i1;
            case 2 : return i2;
            case 3 : return i3;
            case 4 : return i4;
            case 5 : return i5;
            case 6 : return i6;
            case 7 : return i7;
            case 8 : return i8;
            case 9 : return i9;
        }
        return i9;
    }



    private int[] convertImage(char[] img) {
        int[] inputNeurons = new int[15];
        char cell;

        for (int string = 0; string < 5; string++) {
            System.out.print("\n");
            for (int i = string*3; i < string*3 + 3; i++) {
                cell = img[i];
                System.out.print(cell);
                if (cell == '_') {
                    inputNeurons[i] = 0;
                } else {
                    inputNeurons[i] = 1;
                }
            }
        }
        return inputNeurons;
    }

    private boolean digitRecognize(int[] inputNeurons) {
        int userReaction = -1;
        double result;
        double bestResult = -1000;
        int recognizedDigit = 0;

        for (int i =0; i < 10; i++) {
            result = neurons.getOutputNeurons()[i].outputNeuron(inputNeurons);
            if (result > bestResult) {
                bestResult = result;
                recognizedDigit = i;
            }
        }

        System.out.println("\nThe digit on picture is " + recognizedDigit);

        if (selectedImage != recognizedDigit && selectedImage < 10) {
            System.out.println("Recognized incorrect, going learning!");
            neurons.learnNeurons();
            return false;
        }

        if (selectedImage == 10) {
            while (userReaction < 0 || userReaction >1) {
                System.out.println("Recognition result is correct? Type 1 if ok, or 0 if not");
                if (scanner.hasNextInt()) {
                    userReaction = scanner.nextInt();
                } else {
                    scanner.next();
                }
            }

            if (userReaction == 0) {
                System.out.println("Recognized incorrect, going learning!");
                neurons.learnNeurons();
                return false;
            } else {
                System.out.println("Really good!");
                return true;
            }
        }
        return true;
    }

}
