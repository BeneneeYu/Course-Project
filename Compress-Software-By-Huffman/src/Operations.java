import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SnapshotResult;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

public class Operations {
    public static Button getAColoredButton(String title){
        Button button = new Button(title);
        button.setStyle(button.getStyle() + "-fx-base:#6677cc;");

        button.setStyle(button.getStyle() + "-fx-text-fill:#ffdfdf;");
        button.setStyle(button.getStyle() + "-fx-font-size:11;");
        button.setStyle(button.getStyle() + "-fx-pref-width:120;");
        button.setStyle(button.getStyle() + "-fx-pref-height:40;");

        button.setStyle(button.getStyle() + "-fx-background-radius:2em;");
        button.setStyle(button.getStyle() + "-fx-padding: 10px;");
        return button;
    }
    public static void mistake(){
        Stage stage1 = new Stage();
        Pane pane1 = new Pane();
        pane1.getChildren().add(new Label("读取失败！"));
        stage1.setScene( new Scene(pane1));
        stage1.show();
    }
    public static void noticeStage(String stageName, String stageWord){
        Button OKButton = Operations.getAColoredButton("确认");
        Stage stageR = new Stage();
        stageR.setTitle(stageName);
        stageR.setWidth(350);
        stageR.setHeight(400);
        VBox paneR = new VBox();
        Scene scene = new Scene(paneR);
        paneR.setPadding(new Insets(100, 100, 100, 100));
        paneR.setSpacing(20);
        Text t = new Text(stageWord);
        t.setFont(Font.font(null, FontWeight.BOLD, 16));
        t.setFill(Color.BLACK);
        paneR.getChildren().addAll(t,OKButton);
        stageR.setScene(scene);
        stageR.show();
        OKButton.setOnAction(event ->
                stageR.close());
    }
    public static void chooseStage(String word1, String word2, File srcFile, String dstFile) {
        Button yesButton = Operations.getAColoredButton("替换");
        Button noButton = Operations.getAColoredButton("不替换");
        Stage stageR = new Stage();
        stageR.setTitle("确认界面");
        stageR.setWidth(350);
        stageR.setHeight(400);
        VBox paneR = new VBox();
        Scene scene = new Scene(paneR);
        paneR.setPadding(new Insets(100, 100, 100, 100));
        paneR.setSpacing(20);
        Text t = new Text("同名文件已存在，要替换吗?");
        t.setFont(Font.font(null, FontWeight.BOLD, 16));
        t.setFill(Color.RED);
        paneR.getChildren().addAll(t, yesButton, noButton);
        stageR.setScene(scene);
        stageR.show();
        yesButton.setOnAction(event1 -> {
            HuffmanCode.zipFile(srcFile.getAbsolutePath(), dstFile);
            stageR.close();
            Operations.noticeStage(word1, word2);
        });
        noButton.setOnAction(event1 -> stageR.close());
    }
}
