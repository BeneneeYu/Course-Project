package BPNetwork;

import BPNetwork.unit.InNode;
import BPNetwork.unit.Link;
import BPNetwork.unit.Node;
import BPNetwork.unit.OutNode;

import java.util.Random;

/**
 * @program: IntelligentSystem
 * @description: 分类神经网络
 * @author: Shen Zhengyu
 * @create: 2020-10-19 09:52
 **/
public class ClassificationBPNetwork {
    //输入层
    private InNode[] inLayer;
    //隐藏层们
    private Node[][] hiddenLayers;
    //输出层
    private OutNode[] outLayer;
    //学习率
    private double learningRate;
    //后续学习率
    private double learningRateAfter;
    //输入层规模
    private int inNum;
    //隐藏层规模
    private int hiddenNum;
    //隐藏层层数
    private int hiddenLayerNum;
    //输出层规模
    private int outNum;
    //阈值
    private double threshold;
    //权值衰减，防止过拟合
    //passRate
    private double passRate;
    //使用过全部训练数据完成一次forward运算以及一次BP运算,是完成了一次epoch
    private int epochNum;

    public ClassificationBPNetwork() {
    }

    public ClassificationBPNetwork(int inNum, int hiddenNum, int hiddenLayerNum, int outNum,double learningRate,double learningRateAfter, double threshold,double passRate, int epochNum) {
        this.learningRate = learningRate;
        this.learningRateAfter =learningRateAfter;
        this.inNum = inNum;
        this.hiddenNum = hiddenNum;
        this.hiddenLayerNum = hiddenLayerNum;
        this.outNum = outNum;
        this.threshold = threshold;
        this.passRate = passRate;
        this.epochNum = epochNum;
        Random random = new Random();
        inLayer = new InNode[inNum];
        hiddenLayers = new Node[hiddenLayerNum][hiddenNum];
        outLayer = new OutNode[outNum];
        //构建三种层
        for (int i = 0; i < inLayer.length; i++) {
            inLayer[i] = new InNode();
        }
        for (int i = 0; i < outLayer.length; i++) {
            outLayer[i] = new OutNode(0.5 * random.nextGaussian());
        }
        for (int i = 0; i < hiddenLayerNum; i++) {
            hiddenLayers[i] = new Node[hiddenNum];
            for (int i1 = 0; i1 < hiddenNum; i1++) {
                hiddenLayers[i][i1] = new Node(random.nextGaussian() * 0.5 - 0.5);
            }
        }
        //联结三种层
        for (Node node : hiddenLayers[0]) {
            for (InNode inNode : inLayer) {
//                Link link = new Link(random.nextGaussian() / inNum);

                Link link = new Link(random.nextGaussian() / Math.sqrt(inNum));
                link.setSrcNode(inNode);
                link.setTgtNode(node);
            }
        }
        //trick
        for (int i = 0; i < hiddenLayerNum - 1; i++) {
            for (Node node : hiddenLayers[i]) {
                for (int i1 = 0; i1 < hiddenNum; i1++) {
//                    Link link = new Link(random.nextGaussian() / hiddenNum);

                    Link link = new Link(random.nextGaussian() / Math.sqrt(hiddenNum));
                    link.setSrcNode(node);
                    link.setTgtNode(hiddenLayers[i + 1][i1]);
                }
            }
        }
        //trick
        for (OutNode outNode : outLayer) {
            for (int i = 0; i < hiddenNum; i++) {
//                Link link = new Link(random.nextGaussian() / hiddenNum);

                Link link = new Link(random.nextGaussian() / Math.sqrt(hiddenNum));
                link.setSrcNode(hiddenLayers[hiddenLayerNum - 1][i]);
                link.setTgtNode(outNode);
            }
        }
    }

    //require inNum+outNum长度的数据
    public void setDatum(double[] value) {
        //一个神经元对应一个像素点
        for (int i = 0; i < inNum; i++) {
            inLayer[i].result = value[i];
        }
        //前inNum个元素过后，就是表示属于哪一类的数据
        for (int i = 0; i < outNum; i++) {
            outLayer[i].desire = value[i + inNum];
        }
    }


    //
    public double[] recResult(){
        for (int i = 0; i < hiddenLayerNum; i++) {
            for (Node node : hiddenLayers[i]) {
                node.calResult();
            }
        }
        double[] result = new double[outNum];
        double sum = 0.0;
        //计算e的指数形式的输出，softmax
        for (OutNode outNode : outLayer) {
            sum += outNode.calOutputInExp();
        }
        for (int i = 0; i < outNum; i++) {
            outLayer[i].result = outLayer[i].result / sum;
            result[i] = outLayer[i].result;
        }
        return result;
    }
    //结果以每个字的概率显示存储在结果数组的对应位置显示
    public double[] calResult() {
        for (int i = 0; i < hiddenLayerNum; i++) {
            for (Node node : hiddenLayers[i]) {
                node.calResult();
            }
        }
        double[] result = new double[outNum];
        double sum = 0.0;
        //计算e的指数形式的输出，softmax
        for (OutNode outNode : outLayer) {
            sum += outNode.calOutputInExp();
        }
        //将数值softmax转化
        for (int i = 0; i < outNum; i++) {
            outLayer[i].result = outLayer[i].result / sum;
            result[i] = outLayer[i].result;
        }
        //存储权重和偏置的改变
        for (int i = 0; i < outNum; i++) {
            //得到I{ti=j}-p(tj=j|xi;θ)
            double delta = outLayer[i].result - outLayer[i].desire;
            outLayer[i].deltaBiasSum += delta;
            for (Link inLink : outLayer[i].inLinks) {
                //求和xi(I{ti=j}-p(tj=j|xi;θ))]
                //+λθ
                inLink.deltaWeightSum += inLink.srcNode.result * delta;
            }
        }
        //将数组返回
        return result;
    }


    public double[] getDesire() {
        double[] result = new double[outNum];
        for (int i = 0; i < outNum; i++) {
            result[i] = outLayer[i].desire;
        }
        return result;
    }

    //交叉熵损失函数
    public double getError() {
        double result = 0;
        for (OutNode outNode : outLayer) {
            result += outNode.desire * Math.log(outNode.result);
        }
        return -result;
    }

    public void train(double[][] sample) {
        //总样本数目
        int length = sample.length;
        int epochCount = 0;
        boolean flag = false;
        int successNum;
        double error;
        double averageError;
        while (!flag && epochCount < epochNum) {
            epochCount++;
            successNum = 0;
            error = 0.0;
            averageError = 0.0;
            for (int k = 0; k < length; k++) {
                //将训练集传给BP
                setDatum(sample[k]);
                calResult();
                error += getError();
                averageError += getError();
                    //以累计的误差调整
                double dt;
                //调整bias，存储权重变化
                //从输出层向隐藏层调节
                for (int i = 0; i < outNum; i++) {
                    outLayer[i].deltaBias = -learningRate * (outLayer[i].deltaBiasSum);
                    outLayer[i].bias += outLayer[i].deltaBias;
                    for (Link inLink : outLayer[i].inLinks) {
                        //平均得到每个
                        inLink.changeRate = inLink.deltaWeightSum;
                    }
                }
                //调整权重
                for (int i = 0; i < hiddenLayerNum; i++) {
                    for (int i1 = 0; i1 < hiddenNum; i1++) {
                        dt = 0;
                        for (Link outLink : hiddenLayers[i][i1].outLinks) {
                            dt += outLink.weight * outLink.changeRate;
                            //乘以softmax梯度,对bias求导
                            dt *= (1.0 - hiddenLayers[i][i1].result);
                            hiddenLayers[i][i1].deltaBias = -learningRate * dt;
                            hiddenLayers[i][i1].bias += hiddenLayers[i][i1].deltaBias;
                        }
                        for (Link inLink : hiddenLayers[i][i1].inLinks) {
                            inLink.changeRate = dt * inLink.srcNode.result;
                        }
                    }
                }
                for (int i = 0; i < outNum; i++) {
                    for (Link inLink : outLayer[i].inLinks) {
                        //调整隐藏层到输出层link权重
                        inLink.deltaWeight = -learningRate * inLink.changeRate;
                        inLink.weight += inLink.deltaWeight;
                    }
                }
                for (int i = 0; i < hiddenLayerNum; i++) {
                    for (int i1 = 0; i1 < hiddenNum; i1++) {
                        for (Link inLink : hiddenLayers[i][i1].inLinks) {
                            //调整输入层到隐藏层，隐藏层到隐藏层link权重
                            inLink.deltaWeight = -learningRate * inLink.changeRate;
                            inLink.weight += inLink.deltaWeight;
                        }
                    }
                }
                    for (int i1 = 0; i1 < outNum; i1++) {
                        outLayer[i1].deltaBiasSum = 0;
                        for (Link inLink : outLayer[i1].inLinks) {
                            inLink.deltaWeightSum = 0;
                        }
                    }
                    //如果平均误差到达精度，则成功数目加上这一轮batch
                    if (error  < threshold) {
                        successNum += 1;
                    }
                    error = 0;

            }
            averageError = averageError / length;
            StringBuilder message = new StringBuilder();
            if (successNum / (double)(length) >= passRate) {
                flag = true;
                message.append("Epoch ")
                        .append(epochCount)
                        .append(": 训练完成，平均误差为:")
                        .append(averageError);
            } else{
                double rate = successNum /(double)length * 100;
                message.append("Epoch ")
                        .append(epochCount)
                        .append(": ")
                        .append(String.format("%.1f", rate))
                        .append("% 平均误差为: ")
                        .append(averageError);
            }
            System.out.println(message.toString());
            if (epochCount >= 1)
                learningRate = learningRateAfter;
        }
    }

}
