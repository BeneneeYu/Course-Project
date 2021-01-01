package BPNetwork.unit;

import java.util.ArrayList;

/**
 * @program: IntelligentSystem
 * @description: 每层中的结点
 * @author: Shen Zhengyu
 * @create: 2020-10-17 13:52
 **/
public class Node {
    public ArrayList<Link> inLinks = new ArrayList<>();
    public ArrayList<Link> outLinks = new ArrayList<>();
    public double bias;
    public double deltaBias;
    public double deltaBiasSum;
    public double result;
     public Node() { }
    public Node(double bias) {
        this.bias = bias;
    }

    public final double calResult() {
        result = 0;
        for (Link inLink : inLinks) {
            result += inLink.weight * inLink.srcNode.result;
        }
        result = sigmoid(result+bias);
        return result;
    }

    public double sigmoid(double x){
        return 1 / (1 + Math.exp(-x));
    }
}
