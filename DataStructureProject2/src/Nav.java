import java.io.*;

public class Nav {
    Graphm g1 = new Graphm(26);
    Graphm g2 = new Graphm(26);
    Graphm g3 = new Graphm(26);
    Nav() {
        //分别是人、车、公交


        //初始化各道路,不受限制
        Edgem[] edgems = new Edgem[80];
        String path = "data/distance.txt";
        File f = new File(path);
        if (f.exists()) { // 判断文件或目录是否存在
            if (f.isFile()) {
                BufferedReader br = null;//该缓冲流有一个readLine()独有方法
                try {
                    br = new BufferedReader(new FileReader(path));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String s = null;
                char c1;
                char c2;
                int count = 0;
                double weight;
                try {
                    while ((s = br.readLine()) != null) {//readLine()每次读取一行
                        c1 = s.charAt(0);
                        c2 = s.charAt(2);
                        weight = Double.parseDouble(s.substring(4));
                        edgems[count] = new Edgem(c1,c2,weight);
                        edgems[count+1] = new Edgem(c2,c1,weight);
                        //默认都能走，默认双向通行
                        count+=2;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        //人行路线，不受单双向限制
        g1.setEdge(edgems[2],1.20);
        g1.setEdge(edgems[3],1.20);
        for (int i = 6; i < 80; i++) {
            g1.setEdge(edgems[i],edgems[i].we());
        }
//        for (int i = 0; i < 80; i++) {
//            g1.setEdge(edgems[i],edgems[i].we());
//        }
        //车行路线，考虑单行道问题，考虑人行道问题
        //屏蔽VU，WV，XW，UX,PT,TP,TQ,QT,PQ,QP,QR,RQ,QS,SQ

                for (int i = 0; i < 80; i++) {
                    if (i == 46 || i == 47 || i == 48 || i == 49 || i == 50 || i == 51 || i == 52 || i == 53 || i == 54 || i == 55 || i == 65 || i == 66 || i == 73 || i == 75)
                    {

                    }
                    else {
                        g2.setEdge(edgems[i], edgems[i].we());
                    }
        }
        //公交路线
        g3.setEdge(new Edgem('F','K',4),4);
        g3.setEdge(new Edgem('K','F',4),4);
        g3.setEdge(new Edgem('K','T',5),5);
        g3.setEdge(new Edgem('T','K',5),5);
        g3.setEdge(new Edgem('B','G',2),2);
        g3.setEdge(new Edgem('G','B',2),2);
        g3.setEdge(new Edgem('G','L',3),3);
        g3.setEdge(new Edgem('L','G',3),3);
        g3.setEdge(new Edgem('L','R',2),2);
        g3.setEdge(new Edgem('R','L',2),2);
        g3.setEdge(new Edgem('R','Z',2),2);
        g3.setEdge(new Edgem('Z','R',2),2);
        g3.setEdge(new Edgem('Z','Y',1),1);
        g3.setEdge(new Edgem('Y','Z',1),1);
        //人行路线已规划好
    }
    public String walkNav(Graphm g,String s1,String s2){
        return g.Dijkstra(s1, s2,true);
    }
    public String driveNav(Graphm g,String s1,String s2){
        return g.Dijkstra(s1, s2,true);
    }
    public String busNav(Graphm g,String s1,String s2){
        return g.Dijkstra(s1, s2,false);
    }

    public void walkNavMode2(Graphm g,String s1){
        for (int i = 0; i < g.n(); i++) {
            if (i != Integer.parseInt(Operation.myMap2.get(s1 + "")))
            System.out.println("从" + Operation.myMap.get(i + "")+"点步行到" + s1 + "点" + g.Dijkstra(Operation.myMap.get(i + ""), s1, true));

        }
    }

}
