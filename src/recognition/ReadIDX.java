/**
 * @brief Here is wrapper class (reader) for IDX files.
 * @authors Vladislav Shikhanov
 **/
package recognition;
import java.io.*;
import java.util.ArrayList;

public class ReadIDX {
    /**
     * updateCollection Method updates collection with read images
     *
     * It use two files - one with labels to images (1- for image with draw of one)
     * the second one contains images which are represented like matrix 28*28 where
     * every element is a gradation of gray in current pixel 0-white, 255 black.
     *
     * Method checks compatibility of this two files and their continuity
     *
     * @param labels - file with labels to numbers
     * @param images - file with pixels description
     *
     * @return collection of images
     **/
    public ArrayList<ImageIDX> updateCollection(File labels, File images) {
        ArrayList<ImageIDX> col = new ArrayList<>();
        byte[] labelsData = new byte[0];
        byte[] imagesData = new byte[0];
        int labelsIndex = 8;
        int imagesIndex = 16;
        int labelsLength;
        int imagesLength;

        try (FileInputStream stream = new FileInputStream(labels)) {
            labelsData = stream.readAllBytes();

        } catch (IOException exc) {
            System.out.println("\nProblem with file reading");
        }

        try (FileInputStream stream = new FileInputStream(images)) {
            imagesData = stream.readAllBytes();

        } catch (IOException exc) {
            System.out.println("\nProblem with file reading");
        }

        labelsLength = convertBytesToInt(labelsData[4], labelsData[5], labelsData[6], labelsData[7]);
        imagesLength = convertBytesToInt(imagesData[4], imagesData[5], imagesData[6], imagesData[7]);

        if ((labelsLength != imagesLength) || (labelsData[3] != 1) || (imagesData[3] != 3) ||
                (labelsLength + labelsIndex != labelsData.length)
                || (imagesLength*784 + imagesIndex != imagesData.length)){
            System.out.println("Collection update failed");
            return col;
        }

        for (int i = 0; i < labelsLength; i++){
            ImageIDX img = new ImageIDX( Byte.toUnsignedInt(labelsData[labelsIndex]));
            labelsIndex++;

            for (int index = 0; index < 784; index++){
                    img.getPixels()[index] = Byte.toUnsignedInt(imagesData[imagesIndex]);
                    imagesIndex++;
            }
            col.add(img);
        }
        return col;
    }

    /**
     * convertBytesToInt Method converts four bytes to one int number
     * @param b3 - most significant byte
     * @param b2 - byte2
     * @param b1 - byte1
     * @param b0 - less significant byte
     * @return int result of converting
     **/
    private int convertBytesToInt(byte b3, byte b2, byte b1, byte b0){
        int i;
        i = Byte.toUnsignedInt(b3);
        i = i << 8;
        i += Byte.toUnsignedInt(b2);
        i = i << 8;
        i += Byte.toUnsignedInt(b1);
        i = i << 8;
        i += Byte.toUnsignedInt(b0);
        return i;
    }
}




