import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Operation {
    public static Button getAColoredButton(String title){
        Button button = new Button(title);
        button.setStyle(button.getStyle() + "-fx-base:#6677cc;");

        button.setStyle(button.getStyle() + "-fx-text-fill:#ffdfdf;");
        button.setStyle(button.getStyle() + "-fx-font-size:11;");
        button.setStyle(button.getStyle() + "-fx-pref-width:220;");
        button.setStyle(button.getStyle() + "-fx-pref-height:40;");

        button.setStyle(button.getStyle() + "-fx-background-radius:2em;");
        button.setStyle(button.getStyle() + "-fx-padding: 10px;");
        return button;
    }

    public static Map<String,String> reverse(Map<String,String> M){
        Map<String, String>  myMap2 = new HashMap<String,String>();

        Set<String> keys = M.keySet();
        for(String key: keys){
            String value = M.get(key);
            myMap2.put(value,key);
        }
        return myMap2;
    }
    static double convert(double value){
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(value));
    }
    static HashMap<String, String > myMap = new HashMap<String, String>(){{
        put("0","A");
        put("1","B");
        put("2","C");
        put("3","D");
        put("4","E");
        put("5","F");
        put("6","G");
        put("7","H");
        put("8","I");
        put("9","J");
        put("10","K");
        put("11","L");
        put("12","M");
        put("13","N");
        put("14","O");
        put("15","P");
        put("16","Q");
        put("17","R");
        put("18","S");
        put("19","T");
        put("20","U");
        put("21","V");
        put("22","W");
        put("23","X");
        put("24","Y");
        put("25","Z");
    }};
     static Map<String, String>  myMap2 = Operation.reverse(myMap);
     private static int[] ca = {70,233};
    private static int[] cb = {141,468};
    private static int[] cc = {161,591};
    private static int[] cd = {245,165};
    private static int[] ce = {279,243};
    private static int[] cf = {284,335};
    private static int[] cg = {292,419};
    private static int[] ch = {295,566};
    private static int[] ci = {414,77};
    private static int[] cj = {431,223};
    private static int[] ck = {460,289};
    private static int[] cl = {464,394};
    private static int[] cm = {447,465};
    private static int[] cn = {545,43};
    private static int[] co = {592,159};
    private static int[] cp = {590,252};
    private static int[] cq = {672,320};
    private static int[] cr = {613,404};
    private static int[] cs = {710,381};
    private static int[] ct = {688,192};
    private static int[] cu = {785,118};
    private static int[] cv = {891,26};
    private static int[] cw = {966,107};
    private static int[] cx = {884,118};
    private static int[] cy = {904,316};
    private static int[] cz = {789,356};

    static HashMap<String, int[] > myCoordinateMap = new HashMap<String, int[]>(){{
        put("A",ca);
        put("B",cb);
        put("C",cc);
        put("D",cd);
        put("E",ce);
        put("F",cf);
        put("G",cg);
        put("H",ch);
        put("I",ci);
        put("J",cj);
        put("K",ck);
        put("L",cl);
        put("M",cm);
        put("N",cn);
        put("O",co);
        put("P",cp);
        put("Q",cq);
        put("R",cr);
        put("S",cs);
        put("T",ct);
        put("U",cu);
        put("V",cv);
        put("W",cw);
        put("X",cx);
        put("Y",cy);
        put("Z",cz);
    }};

     public static void draw(Pane pane,int startX,int startY,int endX,int endY){
         Line line = new Line();
         line.setStartX(startX);
         line.setStartY(startY);
         line.setEndX(endX);
         line.setEndY(endY);
         line.setStrokeWidth(12);
         line.setStrokeLineCap(StrokeLineCap.BUTT);


         pane.getChildren().add(line);
     }
}
