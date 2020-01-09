import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class UI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Pane leftVBox = new Pane();
        VBox rightVBox = new VBox();
        stage.setTitle("MapSearch");
        Image image = new Image("tagged.png");
        ImageView imageView = new ImageView();
        imageView.setImage(image);

        leftVBox.getChildren().addAll(imageView);


        Label text = new Label("Let's go travelling!");
        text.setStyle(text.getStyle() + "-fx-font-size:22;");
        rightVBox.getChildren().addAll(text);
        rightVBox.setPadding(new Insets(30, 100, 100, 50));
        rightVBox.setSpacing(15);

        HBox start = new HBox(); // 创建一个水平箱子
        Label label = new Label("起点："); // 创建一个标签
        TextField field = new TextField(); // 创建一个单行输入框
        field.setPrefSize(200, 15); // 设置单行输入框的推荐宽高
        field.setEditable(true); // 设置单行输入框能否编辑
        field.setPromptText("请输入起点，大写字母A~Z中的一个"); // 设置单行输入框的提示语
        field.setAlignment(Pos.CENTER_LEFT); // 设置单行输入框的对齐方式
        field.setPrefColumnCount(11); // 设置单行输入框的推荐列数
        start.getChildren().addAll(label, field); // 给水平箱子添加一个单行输入框

        HBox des = new HBox(); // 创建一个水平箱子
        TextField field2 = new TextField(); // 创建一个单行输入框
        field2.setPrefSize(200, 15); // 设置单行输入框的推荐宽高
        field2.setEditable(true); // 设置单行输入框能否编辑
        field2.setPromptText("请输入终点，大写字母A~Z中的一个，不得与起点相同"); // 设置单行输入框的提示语
        field2.setAlignment(Pos.CENTER_LEFT); // 设置单行输入框的对齐方式
        field2.setPrefColumnCount(11); // 设置单行输入框的推荐列数
        des.getChildren().addAll(new Label("终点："), field2); // 给水平箱子添加一个单行输入框

        HBox mode = new HBox(); // 创建一个水平箱子
        TextField field3 = new TextField(); // 创建一个单行输入框
        field3.setPrefSize(200, 15); // 设置单行输入框的推荐宽高
        field3.setEditable(true); // 设置单行输入框能否编辑
        field3.setPromptText("请输入模式，1，2，3中的一种"); // 设置单行输入框的提示语
        field3.setAlignment(Pos.CENTER_LEFT); // 设置单行输入框的对齐方式
        field3.setPrefColumnCount(11); // 设置单行输入框的推荐列数
        mode.getChildren().addAll(new Label("模式："), field3); // 给水平箱子添加一个单行输入框

        TextArea informationField = new TextArea(); // 创建一个单行输入框
        informationField.setPrefSize(100, 400); // 设置单行输入框的推荐宽高
        informationField.setPromptText("导航信息将在此处展示"); // 设置单行输入框的提示语

        informationField.setEditable(false); // 设置单行输入框能否编辑
        informationField.setPrefColumnCount(20); // 设置单行输入框的推荐列数
        informationField.setWrapText(true);
        Button walk =  Operation.getAColoredButton("我要徒步");
        Button drive =  Operation.getAColoredButton("我要驾车");
        Button bus =  Operation.getAColoredButton("我要公交");
        Button reset =  Operation.getAColoredButton("重置");


        walk.setOnAction(event ->
                {
                    switch (field3.getText()){
                        case "1":
                            Nav nav = new Nav();
                            String s = nav.walkNav(nav.g1,field.getText(),field2.getText());
                            char[] c=s.toCharArray();
                            char[] list = new char[26];
                            int count = 0;
                            for(int i=0;i<s.length();i++){
                                if(c[i]>='A'&&c[i]<='Z'){
                                    list[count] = c[i];
                                    count++;
                                }
                                }
                            leftVBox.getChildren().clear();
                            leftVBox.getChildren().addAll(imageView);

                            for (int pos = 1; pos < count; pos++) {
                                Operation.draw(leftVBox,Operation.myCoordinateMap.get((String.valueOf(list[pos - 1])))[0],Operation.myCoordinateMap.get((String.valueOf(list[pos - 1])))[1],Operation.myCoordinateMap.get((String.valueOf(list[pos])))[0],Operation.myCoordinateMap.get((String.valueOf(list[pos])))[1]);

                            }

                            informationField.setText(s);
                            break;
                        case "2":
                            Nav nav1 = new Nav();
                            nav1.walkNavMode2(nav1.g1,field2.getText());
                            break;
                    }


                }
                );
        drive.setOnAction(event ->
                {
                    Nav nav = new Nav();
                    informationField.setText(nav.driveNav(nav.g2,field.getText(),field2.getText()));

                }

        );
        bus.setOnAction(event ->
                {
                    Nav nav = new Nav();
                    informationField.setText(nav.busNav(nav.g3,field.getText(),field2.getText()));
                }
        );
        reset.setOnAction(event ->
                {
                    field.getText();
                    field.setText("");
                    field2.getText();
                    field2.setText("");
                    field3.getText();
                    field3.setText("");
                    informationField.getText();
                    informationField.setText("");
                }
                );
        rightVBox.getChildren().addAll(start,des,mode,walk,drive,bus,reset,informationField);
        //取出宽度和高度
        double width = image.getWidth()+ 390;
        double height = image.getHeight();
        HBox box = new HBox(leftVBox,rightVBox);
        HBox.setHgrow(leftVBox, Priority.ALWAYS);
        Scene scene = new Scene(box,width,height);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
