import java.util.*;

public class Graphm implements Graph {
    private double[][] matrix;
    private int numEdge;
    private int[] mark;
    private int[] prev;










    public Graphm(int n) {
        prev = new int[n];
        mark = new int[n];
        matrix = new double[n][n];
        numEdge = 0;
    }

    public int n() {
        return mark.length;
    }

    public int e() {
        return numEdge;
    }

    public Edge first(int v) {
        for (int i = 0; i < mark.length; i++)
            if (matrix[v][i] != 0)
                return new Edgem(v, i);
        return null;
    }

    public Edge next(Edge w) {
        if (w == null) return null;
        for (int i = w.v2() + 1; i < mark.length; i++)
            if (matrix[w.v1()][i] != 0)
                return new Edgem(w.v1(), i);
        return null;
    }

    public boolean isEdge(Edge w) {
        if (w == null) return false;
        else return matrix[w.v1()][w.v2()] != 0;
    }

    public boolean isEdge(int i, int j) {
        if (i <= 0 || j <= 0) return false;
        else return matrix[i][j] != 0;
    }

    public int v1(Edge w) {
        return w.v1();
    }

    public int v2(Edge w) {
        return w.v2();
    }

    public void setEdge(Edge w, double weight) {
        assert weight != 0 : "cannot set weight to 0";
        if (matrix[w.v1()][w.v2()] == 0) numEdge++;
        matrix[w.v1()][w.v2()] = weight;
    }

    public void setEdge(int v1, int v2, double weight) {
        assert weight != 0 : "cannot set weight to 0";
        if (matrix[v1][v2] == 0) numEdge++;
        matrix[v1][v2] = weight;
    }

    public void delEdge(Edge w) {
        if (matrix[w.v1()][w.v2()] != 0) numEdge--;
        matrix[w.v1()][w.v2()] = 0;
    }

    public void delEdge(int v1, int v2) {
        if (matrix[v1][v2] != 0) numEdge--;
        matrix[v1][v2] = 0;
    }

    public double weight(Edge w) {
        return matrix[w.v1()][w.v2()];

    }

    public double weight(int v1, int v2) {
        return matrix[v1][v2];
    }

    public void setMark(int v, int val) {
        mark[v] = val;
    }
    public int getMark(int v) {
        return mark[v];
    }
    public int getPrev(int v) {
        return prev[v];
    }

    public void setPrev(int v, int val) {
        prev[v] = val;
    }


    //传入整张图，s：顶点序号，D：距离数组
     private void Dijkstra (int s, double[] D) {
        for (int i = 0; i < this.n(); i++) {
            D[i] = Integer.MAX_VALUE;
        }
        D[s] = 0;
        //只是要执行顶点次数次而已
        for (int i = 0; i < this.n(); i++) {
            //找到距离最近的顶点
            int v = minVertex(this, D);
            //为这个顶点设置已访问
                this.setMark(v, 1);
            //一个顶点的mark，代表它的前一点是路
            //为
            if (Integer.MAX_VALUE == D[v]) return; // 没有下一个顶点可以访问了
            //更新distance数组
            for (Edge w = this.first(v); this.isEdge(w); w = this.next(w))
                if ((D[v] + this.weight(w)) < D[this.v2(w)]) {
                    D[this.v2(w)] = D[v] + this.weight(w);
                    this.setPrev(this.v2(w),this.v1(w));
                }
        }
         for (int i = 0; i < this.n(); i++) {
             this.setMark(i, 0);
         }
    }

    //传入整张图，总城市数，起点城市号，终点城市号
     String Dijkstra (String ss, String dd,boolean isDistance) {
        int s = Integer.parseInt(Operation.myMap2.get(ss));
         int d = Integer.parseInt(Operation.myMap2.get(dd));
         int cities = this.n();
        double[] Dis = new double[cities];
        this.Dijkstra(s,Dis);
        if (Dis[d] != Integer.MAX_VALUE) {
            int tmp = d;
            StringBuilder sb = new StringBuilder();
            StringBuilder result = new StringBuilder();
            while (true) {
                int i = this.getPrev(tmp);
                sb.append(Operation.myMap.get(tmp + "")).append("→");
                tmp = i;
                if (tmp == s) {
                    sb.append(Operation.myMap.get(tmp + ""));
                    break;
                }
            }
            result.append("最短路线是：").append(sb.reverse().toString()).append("\n");
            if (isDistance) {
                result.append("最短距离是：").append(Operation.convert(Dis[d])).append("km").append("\n");
            } else {
                result.append("最短时间是：").append(Operation.convert(Dis[d])).append("min").append("\n");
            }
            return result.toString();
        }
        else {
            return "无可达路径";
        }
    }
        //遍历找到离原点的最小值
    static int minVertex(Graph G, double[] D) {
        // Initialize v to any unvisited vertex
        int v = 0;
        for (int i = 0; i < G.n(); i++)
            if (G.getMark(i) == 0) {
                v = i; break;
            }
        // Find the unvisited vertex with the smallest value
        for (int i = 0; i < G.n(); i++)
            if ((G.getMark(i) == 0) && (D[i] < D[v]))
                v = i;
        return v;
    }
public void showEdge(){
    for (int i = 0; i < 26; i++) {
        System.out.println(this.first(i).v1()+" " + this.first(i).v2() + " " + weight(this.first(i)));
    }
}
}
