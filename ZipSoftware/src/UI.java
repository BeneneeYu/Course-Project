import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class UI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("解压缩软件");
//        stage.setWidth(600);
//        stage.setHeight(600);
        Button compressButton =  Operations.getAColoredButton("单文件压缩");
        Button folderCompressButton =  Operations.getAColoredButton("文件夹压缩");
        Button decompressButton = Operations.getAColoredButton("单文件解压");
        Button folderDecompressButton = Operations.getAColoredButton("文件夹解压");

        //为压缩按钮添加功能
        compressButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("C:\\Users\\86460\\Desktop"));
//                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT", "*.dat");
//                fileChooser.getExtensionFilters().add(extFilter);
            File srcFile = fileChooser.showOpenDialog(stage);
//                FileInputStream fis = new FileInputStream(fileChooser.showOpenDialog(stage));
//                ObjectInputStream ois = new ObjectInputStream(fis);
            if (null != srcFile) {
//                String dstFile = srcFile.getAbsolutePath().substring(0, srcFile.getAbsolutePath().lastIndexOf(".")) + ".zip";
                String dstFile = srcFile.getAbsolutePath() + ".zip";
                File file = new File(dstFile);
                if(file.exists()){
                    Operations.chooseStage("压缩结果","压缩成功",srcFile,dstFile);
                }
                else {
                    HuffmanCode.zipFile(srcFile.getAbsolutePath(), dstFile);
                    Operations.noticeStage("压缩结果","压缩成功");
                }
//                ois.close();
//                fis.close();
                System.gc();
            }
        });
        //为解压缩按钮添加功能
        decompressButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("C:\\Users\\86460\\Desktop"));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIP", "*.zip");
            fileChooser.getExtensionFilters().add(extFilter);
            File srcFile = fileChooser.showOpenDialog(stage);
//                FileInputStream fis = new FileInputStream(fileChooser.showOpenDialog(stage));
//                ObjectInputStream ois = new ObjectInputStream(fis);
            if (null != srcFile) {
                String dstFile = srcFile.getAbsolutePath().substring(0, srcFile.getAbsolutePath().lastIndexOf("."));
                File file = new File(dstFile);
                //判断是否有已解压的文件，让用户选择是否替换
                if(file.exists()){
                    Operations.chooseStage("解压结果","解压成功",srcFile,dstFile);
                }
                else {
                    HuffmanCode.unZipFile(srcFile.getAbsolutePath(), dstFile);
                    Operations.noticeStage("解压结果","解压成功");
                }
//                ois.close();
//                fis.close();
                System.gc();
            }
        });
        folderCompressButton.setOnAction(
                event -> {
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    directoryChooser.setInitialDirectory(new File("C:\\Users\\86460\\Desktop"));

                    File srcFile = directoryChooser.showDialog(stage);
                    String path = srcFile.getPath();
                    if (null != srcFile) {
//                String dstFile = srcFile.getAbsolutePath().substring(0, srcFile.getAbsolutePath().lastIndexOf(".")) + ".zip";
                        String dstFile = srcFile.getAbsolutePath() + ".zip";

                        File file = new File(dstFile);
                        if (file.exists()) {
                            Operations.chooseStage("压缩结果", "压缩成功", srcFile, dstFile);
                        } else {
                            HuffmanCode.zipDirectory(srcFile.getAbsolutePath(), dstFile);
                            Operations.noticeStage("压缩结果", "压缩成功");
                        }
//                ois.close();
//                fis.close();
                        System.gc();
                    }
                }
        );
        folderDecompressButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("C:\\Users\\86460\\Desktop"));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIP", "*.zip");
            fileChooser.getExtensionFilters().add(extFilter);
            File srcFile = fileChooser.showOpenDialog(stage);
//                FileInputStream fis = new FileInputStream(fileChooser.showOpenDialog(stage));
//                ObjectInputStream ois = new ObjectInputStream(fis);
            if (null != srcFile) {
                String dstFile = srcFile.getAbsolutePath().substring(0, srcFile.getAbsolutePath().lastIndexOf("."));
//                File file = new File(dstFile);
//                //判断是否有已解压的文件，让用户选择是否替换
//                if(file.exists()){
//                    Operations.chooseStage("解压结果","解压成功",srcFile,dstFile);
//
//                }
//                else {
//                    HuffmanCode.unZipDirectory(srcFile.getAbsolutePath(), dstFile);
//                    Operations.noticeStage("解压结果","解压成功");
//                }
                HuffmanCode.unZipDirectory(srcFile.getAbsolutePath(), dstFile);
                Operations.noticeStage("解压结果","解压成功");
//                ois.close();
//                fis.close();
                System.gc();
            }
        });
        Pane pane = new Pane();
        pane.setPrefHeight(400);
        pane.setPrefWidth(400/0.618);

        VBox  hb = new VBox();

        BackgroundImage myBI= new BackgroundImage(new Image("bg.jpg",400/0.618,400,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
//then you set to your node
        pane.setBackground(new Background(myBI));
        hb.getChildren().addAll(compressButton,folderCompressButton,decompressButton,folderDecompressButton);
        hb.setPadding(new Insets(66, 88, 100, 266));
        hb.setSpacing(pane.getPrefHeight()/20);
        pane.getChildren().addAll(hb);
        Scene sc = new Scene(pane);
        stage.setScene(sc);
        stage.setResizable(false);
        stage.show();
    }

}
