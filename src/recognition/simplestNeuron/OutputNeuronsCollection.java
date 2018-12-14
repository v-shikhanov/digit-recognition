package recognition.simplestNeuron;


public class OutputNeuronsCollection {

    private final int[][] weight = {
            {1,1,1, 1,-1,1, 1,-1,1, 1,-1,1, 1,1,1}, //0
            {-1,1,-1, -1,1,-1, -1,1,-1, -1,1,-1, -1,1,-1},//1
            {1,1,1, -1,-1,1, 1,1,1, 1,-1,-1, 1,1,1},//2
            {1,1,1, -1,-1,1, 1,1,1, -1,-1,1, 1,1,1},//3
            {1,-1,1, 1,-1,1, 1,1,1, -1,-1,1, -1,-1,1},//4
            {1,1,1, 1,-1,-1, 1,1,1, -1,-1,1, 1,1,1},//5
            {1,1,1, 1,-1,-1, 1,1,1, 1,-1,1, 1,1,1},//6
            {1,1,1, -1,-1,1, -1,-1,1, -1,-1,1, -1,-1,1},//7
            {1,1,1, 1,-1,1, 1,1,1, 1,-1,1, 1,1,1,},//8
            {1,1,1, 1,-1,1, 1,1,1, -1,-1,1, 1,1,1,}//9
    };

    private final int[] bias = {-1,6,0,0,4,0,-1,4,-2,-1};

    public SimplestNeuron[] neurons = new SimplestNeuron[15];

    public OutputNeuronsCollection() {
        for (int i =0; i < 10; i++) {
            neurons[i] = new SimplestNeuron(weight[i],bias[i]);
        }
    }
}
