import BPNetwork.ClassificationBPNetwork;
import BPNetwork.FileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @program: IntelligentSystem
 * @description: 手写汉字分类
 * @author: Shen Zhengyu
 * @create: 2020-10-17 13:52
 **/
public class RecognizeChinese {
    //设置基础数值
    public static final String chars = "博学笃志切问近思自由无用";
    private static int imageSize = 28;
    private static int numOfInput = imageSize * imageSize;
    public static final int num_of_category = 12;
    //划分训练集和测试集
    private static int numOfTrainGroup = (int) (0.7 * 620);
    private static int numOfTestGroup = 620 - numOfTrainGroup;

    //先拿所有数据训练，然后拿部分数据测试
    //{               {0,0,1,1,1,……,0,1,1,0,0,0,0,1,0,0,0}}
    //第二维数组的最后指示这样的像素矢量对应什么字
    //第一维长度是样本总数
    private static double[][] getSamples(int num, int offset) throws IOException {
        double[][] samples = new double[num_of_category * num][numOfInput + num_of_category];
        BufferedImage image;
        int index;
        for (int m = 0; m < num; m++) {
            for (int i = 1; i <= num_of_category; i++) {
                index = m * num_of_category + i - 1;
                image = ImageIO.read(new File("train/" + i + "/" + (m + 1 + offset) + ".bmp"));
                for (int j = 0; j < imageSize; j++)
                    for (int k = 0; k < imageSize; k++)
                        samples[index][j * imageSize + k] = (image.getRGB(k, j) & 0xffffff) == 0xffffff ? 0 : 1;
                samples[index][numOfInput + i - 1] = 1;
            }
        }
        return samples;
    }




    private static double[] getNormalisedDatumWithoutCategory(double[] sample) {
        int len = sample.length;
        //sample数组的最后一位标志属于第几类
        //大小是像素数+类数，前是特征向量，后是点位
        double[] new_sample = new double[numOfInput + num_of_category];
        System.arraycopy(sample, 0, new_sample, 0, len);
        return new_sample;
    }

    //先找到最大值，再抓到字
    private static char getChar(double[] results) {
        int index = 0;
        double value = 0;
        for (int i = 0; i < results.length; i++)
            if (value <= results[i]) {
                value = results[i];
                index = i;
            }
        return chars.charAt(index);
    }

    private static int getNum(double[] results) {
        int index = 0;
        double value = 0;
        for (int i = 0; i < results.length; i++)
            if (value <= results[i]) {
                value = results[i];
                index = i;
            }
        return index+1;
    }



    public static void main(String[] args) throws IOException {
        ClassificationBPNetwork network = new ClassificationBPNetwork(numOfInput, 128, 1, num_of_category,
                0.2, 0.01, 0.5, 0.99, 15);
        System.out.println("加载图片中");
        long start_t = System.currentTimeMillis();
        double[][] samples = getSamples(numOfTrainGroup, 0);
        long end_t = System.currentTimeMillis();
        System.out.println("耗时" + (end_t - start_t) + "ms，加载完成");
        System.out.println("开始训练");
        start_t = System.currentTimeMillis();
        network.train(samples);
        end_t = System.currentTimeMillis();
        System.out.println("耗时" + (end_t - start_t) / 1000 + "s，训练完成");
        System.out.println("开始在训练集上测试");
        int suc_num = 0;
        for (int i = 0; i < samples.length; i++) {
            network.setDatum(samples[i]);
            char result = getChar(network.calResult());
            char desire = getChar(network.getDesire());
            if (result == desire)
                suc_num++;
//            else{
//                System.out.println("希望是" + desire +"，结果是" + result);
//            }
        }
        System.out.println("训练集上的正确率:");
        System.out.println(suc_num / (double) samples.length);
        System.out.println("开始在测试集上测试");
        suc_num = 0;
        double[][] test_samples = getSamples(numOfTestGroup, numOfTrainGroup);
        System.out.println("测试集上的正确率:");
        for (int i = 0; i < test_samples.length; i++) {
            network.setDatum(test_samples[i]);
            char result = getChar(network.calResult());
            char desire = getChar(network.getDesire());
            if (result == desire)
                suc_num++;
        }
        System.out.println("测试数" + test_samples.length);
        System.out.println("正确数" + suc_num);
        System.out.println(suc_num / (double) test_samples.length);

        int testNum = 1800;
        String fileName = "output.txt";
        FileUtil.createFile(fileName);
        for (int i = 1; i <= testNum; i++) {
            double[] sample;
            sample = new double[numOfInput];
            BufferedImage image = ImageIO.read(new File("test/" + i + ".bmp"));
            for (int j = 0; j < imageSize; j++)
                for (int k = 0; k < imageSize; k++)
                    sample[j * imageSize + k] = (image.getRGB(k, j) & 0xffffff) == 0xffffff ? 0 : 1;
            network.setDatum(getNormalisedDatumWithoutCategory(sample));
            if(i == testNum){
                FileUtil.writeFileByLineWithoutGangN(fileName,String.valueOf(getNum(network.recResult())));
            }else{
                FileUtil.writeFileByLine(fileName,String.valueOf(getNum(network.recResult())));
            }
        }

    }
}
