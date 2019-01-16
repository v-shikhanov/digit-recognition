package recognition;

public class ImageIDX {
    private double rate;
    private int label;
    private int[] pixels = new int[784];

    ImageIDX (int label){
        this.label = label;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public void printImage(){
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
}
