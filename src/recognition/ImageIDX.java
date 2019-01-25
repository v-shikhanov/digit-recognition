/**
 * @brief This is class contains image and it's real value
 * @authors Vladislav Shikhanov
 **/
package recognition;

public class ImageIDX {
    private int label;
    private int[] pixels = new int[784];

    /**
     * class constructor
     * @param label- digit that draw on picture
     */
    ImageIDX(int label) {
        this.label = label;
    }

    /**
     *  Method prints image to console
     */
    public void printImage() {
        System.out.print("\n");
        System.out.print("\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n ");
        System.out.print(label);
        for ( int index = 1; index <= 784; index++){
            if (pixels[index-1] > 0) {
                System.out.print("*");
            } else {
                System.out.print(" ");
            }

            if ((index)%28 == 0) {
                System.out.print("\n");
            }
        }
    }
    /*
        Getters and Setters
    */
    public int getLabel() {
        return label;
    }

    public int[] getPixels() {
        return pixels;
    }
}
