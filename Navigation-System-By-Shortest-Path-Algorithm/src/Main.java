import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Graphm gg = new Graphm(5);
//        Edgem e1 = new Edgem(0,1);
//        gg.setEdge(e1,20);
//        Edgem e2 = new Edgem(1,2);
//        gg.setEdge(e2,30);
//        Edgem e3 = new Edgem(2,3);
//        gg.setEdge(e3,20);
//        Edgem e4 = new Edgem(3,4);
//        gg.setEdge(e4,20);
//        Edgem e5 = new Edgem(0,4);
//        gg.setEdge(e5,100);
////        gg.Dijkstra(gg,5,0,4);
////        gg.Dijkstra(gg,2,1,3);
//
//        Edgem EAB = new Edgem('A', 'B');
//        Edgem Emz = new Edgem('M', 'Z');

        Nav n = new Nav();
        n.walkNavMode2(n.g1,"B");
    }
}
