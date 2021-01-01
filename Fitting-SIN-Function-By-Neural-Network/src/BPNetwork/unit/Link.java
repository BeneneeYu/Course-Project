package BPNetwork.unit;

/**
 * @program: IntelligentSystem
 * @description: 链
 * @author: Shen Zhengyu
 * @create: 2020-10-17 13:55
 **/
public class Link {
    public Node srcNode;
    public Node tgtNode;
    public double weight;
    public double deltaWeight;
    public double deltaWeightSum;
    public double changeRate;

    public Link(double weight) {
        this.weight = weight;
    }

    public void setSrcNode(Node srcNode) {
        this.srcNode = srcNode;
        srcNode.outLinks.add(this);
    }

    public void setTgtNode(Node tgtNode) {
        this.tgtNode = tgtNode;
        tgtNode.inLinks.add(this);
    }
}
