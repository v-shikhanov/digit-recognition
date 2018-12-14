package recognition.simplestNeuron;

import java.util.Random;
import java.util.Scanner;

public class ImagesManager {
    private Scanner scanner = new Scanner(System.in);

    public void selectEnteringMethod() {

        char[] image = new char[15];
        int enteringMethod = -1;
        while (enteringMethod < 0 || enteringMethod >3) {
            System.out.println("Please select entering method: " +
                    "\n0 = manual image entering,\n" +
                    "1 = random generator,\n" +
                    "2 = pre-defined images");
            if (scanner.hasNextInt()) {
                enteringMethod = scanner.nextInt();
            } else {
                scanner.next();
            }

        }

        switch (enteringMethod) {
            case 0 : image = getUserImage(); break;

            case 1 : image = createRandomImage(); break;

            case 2 : image = usePredefinedImages(); break;

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
        return image;
    }

    private char[] usePredefinedImages() {
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

        switch (new Random().nextInt(9)) {
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

    private void digitRecognize(int[] inputNeurons) {
        int result;
        int bestResult = -1000;
        int recognizedDigit =0;
        OutputNeuronsCollection outputNeuronsCollection = new OutputNeuronsCollection();

        for (int i =0; i < 10; i++) {
            result = outputNeuronsCollection.neurons[i].outputNeuron(inputNeurons);
            if (result > bestResult) {
                bestResult = result;
                recognizedDigit = i;
            }
        }

        System.out.println("\nThe digit on picture is " + recognizedDigit);
    }
}
