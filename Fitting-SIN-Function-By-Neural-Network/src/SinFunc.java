import BPNetwork.RegressionBPNetwork;

import java.util.Random;
import java.util.Scanner;

/**
 * @program: IntelligentSystem
 * @description: 回归拟合Sin函数
 * @author: Shen Zhengyu
 * @create: 2020-10-17 13:52
 **/
public class SinFunc {


    public static void main(String[] args) {
        regressSin(0.01,2048,2048);
    }
    private static void regressSin(double requireError,int trainSetSize,int testSetSize){
        RegressionBPNetwork network = new RegressionBPNetwork(1,10,1,1,0.00001,500000,2.0);
        //原始
        double[][] randomData = getRandomData(trainSetSize);
        //映射
        double[][] randomDataCopy = randomData.clone();
        for (double[] doubles : randomDataCopy) {
            doubles[1] = signed2positive(doubles[1]);
        }
        System.out.println("开始训练");
        long start = System.currentTimeMillis();
        network.train(randomDataCopy);
        long end = System.currentTimeMillis();
        StringBuilder message = new StringBuilder();
        message.append("\n结束训练\n")
                .append("耗时：")
                .append(end-start)
                .append("毫秒\n")
                .append("平均误差为：")
                .append(calAverageError(network,randomDataCopy))
                .append("\n");
        System.out.println(message);
        double error = 0;
        double errorSum = 0;
        double[][] testData = getRandomData(testSetSize);
        int count = 0;
        for (int i = 0; i < testSetSize; i++) {
            testData[i][1] = signed2positive(testData[i][1]);
            network.setDatum(testData[i]);
            error = Math.abs(positive2signed(testData[i][1]) -
                    positive2signed(network.calResult()[0]));
            if(error < requireError){
                count++;
            }
            errorSum += error;
        }
        System.out.println("在" + trainSetSize + "大小的数据集上进行测试：");
        System.out.println("平均误差为：" + errorSum/testSetSize);
        System.out.println("正确率为：" + (100.0 * count / (double)testSetSize) + "%");
        System.out.println("接下来可以手动输入数据点，得到误差");
        Scanner in = new Scanner(System.in);
        double[] sample = new double[2];
        double result;
        while (true) {
            sample[0] = in.nextDouble();
            sample[1] = signed2positive(Math.sin(sample[0]));
            network.setDatum(sample);
            result = positive2signed(network.calResult()[0]);
            System.out.println("拟合函数给出结果: " + result);
            System.out.println("应有结果: " + positive2signed(sample[1]));
            System.out.println("误差为: " + Math.abs(result - positive2signed(sample[1])));
            System.out.println("误差百分比为: " + 100.0 * Math.abs(result - positive2signed(sample[1]))/Math.abs(positive2signed(sample[1])) + "%");

        }
    }
    //得到num个数据点位
    private static double[][] getRandomData(int num){
        double[][] data = new double[num][2];
        Random random = new Random();
        for (double[] datum : data) {
            //(-PI,PI)
            double input = (2 * random.nextDouble() - 1) * Math.PI;
            datum[0] = input;
            datum[1] = Math.sin(input);
        }
        return data;
    }

    public static double signed2positive(double signed){
        return (double)((signed+1.0)/2.0);
    }

    public static double positive2signed(double positive){
        return (double)(positive*2.0 - 1.0);
    }

    //样本数目规模的误差，此时的网络应该已经训练过
    public static double calAverageError(RegressionBPNetwork regressionBPNetwork,double[][] data){
        double errorSum = 0;
        double tmpError = 0;
        //这里的datum已经经过了gx映射
        for (double[] datum : data) {
            //给每个输入神经元给出输入值
            regressionBPNetwork.setDatum(datum);
            //转化为sin函数上的误差
            tmpError = Math.abs(positive2signed(regressionBPNetwork.calResult()[0])-positive2signed(datum[1]));
            errorSum += tmpError;
        }
        return errorSum/data.length;
    }
}
