package BPNetwork.unit;

/**
 * @program: IntelligentSystem
 * @description: 输出层中的结点
 * @author: Shen Zhengyu
 * @create: 2020-10-17 15:39
 **/
public class OutNode extends Node{
    public double desire;

    public OutNode(){}

    public OutNode(double bias) {
        super(bias);
    }

    //为softmax的使用铺垫
    public double calOutputInExp(){
        result = 0;
        for (Link inLink : inLinks) {
            result += inLink.weight * inLink.srcNode.result;
        }
        result = Math.exp(result + bias);
        return result;
    }
}
