package BPNetwork;

import BPNetwork.unit.InNode;
import BPNetwork.unit.Link;
import BPNetwork.unit.Node;
import BPNetwork.unit.OutNode;

import java.util.Random;

/**
 * @program: IntelligentSystem
 * @description: 回归BP神经网络
 * @author: Shen Zhengyu
 * @create: 2020-10-17 15:46
 **/
public class RegressionBPNetwork {
    //输入层
    private InNode[] inLayer;
    //隐藏层们
    private Node[][] hiddenLayers;
    //输出层
    private OutNode[] outLayer;
    //学习率
    private double learningRate;
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
    //使用过全部训练数据完成一次forward运算以及一次BP运算,是完成了一次epoch
    private int epochNum;
    public RegressionBPNetwork(){};
    public RegressionBPNetwork(int inNum, int hiddenNum, int hiddenLayerNum, int outNum, double threshold, int epochNum, double learningRate) {
        this.learningRate = learningRate;
        this.inNum = inNum;
        this.hiddenNum = hiddenNum;
        this.hiddenLayerNum = hiddenLayerNum;
        this.outNum = outNum;
        this.threshold = threshold;
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
            outLayer[i] = new OutNode(-random.nextGaussian());
        }
        for (int i = 0; i < hiddenLayerNum; i++) {
            hiddenLayers[i] = new Node[hiddenNum];
            for (int i1 = 0; i1 < hiddenNum; i1++) {
                hiddenLayers[i][i1] = new Node(-random.nextGaussian());
            }
        }
        //联结三种层
        for (Node node : hiddenLayers[0]) {
            for (InNode inNode : inLayer) {
                Link link = new Link(random.nextGaussian());
                link.setSrcNode(inNode);
                link.setTgtNode(node);
            }
        }
        for (int i = 0; i < hiddenLayerNum - 1; i++) {
            for (Node node : hiddenLayers[i]) {
                for (int i1 = 0; i1 < hiddenNum; i1++) {
                    Link link = new Link(random.nextGaussian());
                    link.setSrcNode(node);
                    link.setTgtNode(hiddenLayers[i + 1][i1]);
                }
            }
        }
        for (OutNode outNode : outLayer) {
            for (int i = 0; i < hiddenNum; i++) {
                Link link = new Link(random.nextGaussian());
                link.setSrcNode(hiddenLayers[hiddenLayerNum-1][i]);
                link.setTgtNode(outNode);
            }
        }
    }


    //{PI/2,0,-PI/2,1,0,-1}
    public void setDatum(double[] value) {
        //输入的对应输出
        for (int i = 0; i < inNum; i++) {
            inLayer[i].result = value[i];
        }
        for (int i = 0; i < outNum; i++) {
            outLayer[i].desire = value[i + inNum];
        }
    }

    public double[] calResult() {
        //从后向前计算
        for (int i = hiddenLayerNum - 1; i >= 0; i--) {
            for (Node node : hiddenLayers[i]) {
                node.calResult();
            }
        }
        //算出结果数组
        double[] result = new double[outNum];
        for (int i = 0; i < result.length; i++) {
            result[i] = outLayer[i].calResult();
        }
        return result;
    }


    public double[] getDesire() {
        double[] result = new double[outNum];
        for (int i = 0; i < outNum; i++) {
            result[i] = outLayer[i].desire;
        }
        return result;
    }

    //loss function
    public double getSquaredErrors(double[] outputs, double[] desire) {
        double error = 0.0;
        for (int i = 0; i < outputs.length && i < desire.length; i++) {
            error += Math.pow(desire[i] - outputs[i], 2);
        }
        error /= 2.0;
        return error;
    }

    public void train(double[][] data) {
        StringBuilder message = new StringBuilder();
        int length = data.length;
        int epochCount = 0;
        boolean flag = false;
        int successNum;
        while (!flag) {
            successNum = 0;
            epochCount++;
            //将每一组样本点输入
            for (double[] datum : data) {
                setDatum(datum);
                if (getSquaredErrors(calResult(), getDesire()) >= threshold) {
                    //一个输出层结点的输出
                    double oi;
                    //一个输出层结点被期望的输出
                    //∂Error/∂Oi = oi-di
                    double di;
                    //∂Error/∂wj = (oi-di)*oi*(1-oi)*oj
                    //dt = (oi-di)*oi*(1-oi)
                    double dt;
                    for (OutNode outNode : outLayer) {
                        di = outNode.desire;
                        oi = outNode.result;
                        dt = (oi - di) * oi * (1.0 - oi);
                        outNode.deltaBias =-learningRate * dt;
                        outNode.bias += outNode.deltaBias;
                        for (Link inLink : outNode.inLinks) {
                            //changeRate=(oi-di)oi(1-oi)oj
                            inLink.changeRate = dt * inLink.srcNode.result;
                        }
                    }
                    for (Node[] hiddenLayer : hiddenLayers) {
                        for (Node node : hiddenLayer) {
                            dt = 0;
                            for (Link outLink : node.outLinks) {
                                dt += outLink.weight * outLink.changeRate;
                                dt *= (1.0 - node.result);
                                node.deltaBias = -learningRate * dt;
                                node.bias += node.deltaBias;
                            }
                            for (Link inLink : node.inLinks) {
                                inLink.changeRate = dt * inLink.srcNode.result;
                            }
                        }
                    }
                    for (OutNode outNode : outLayer) {
                        for (Link inLink : outNode.inLinks) {
                            //deltawji = r(di-oi)oi(1-oi)oj=-r*dt*oj=-r*changeRate
                            inLink.deltaWeight = -learningRate * inLink.changeRate;
                            inLink.weight += inLink.deltaWeight;
                        }
                    }
                    for (Node[] hiddenLayer : hiddenLayers) {
                        for (Node node : hiddenLayer) {
                            for (Link inLink : node.inLinks) {
                                inLink.deltaWeight = -learningRate * inLink.changeRate;
                                inLink.weight += inLink.deltaWeight;
                            }
                        }
                    }
                } else {
                    successNum++;
                }
            }
            //所有误差都小于阈值
            if (successNum == length) {
                flag = true;
                message.append("\n").append("Epoch ").append(epochCount).append(": 100%!");
                System.out.print(message.toString());
            } else {
                double rate = successNum / (double) length * 100.0;
                message.append("\n")
                        .append("Epoch ")
                        .append(epochCount)
                        .append(": ")
                        .append(String.format("%.1f", rate))
                        .append("%");
                if (epochCount % 100 == 0) {
                    System.out.print(message.toString());
                    message.delete(0, message.length());
                }
            }

            if (epochCount > epochNum) {
                System.out.println("训练次数超过上限!");
                flag = true;
            }
        }
    }

    private double sigmoidDerivative(double x) {
        return x * (1.0 - x);
    }
}
