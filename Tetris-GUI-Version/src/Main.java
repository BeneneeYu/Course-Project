import java.lang.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
public class Main extends Application{
    static double volume = 6;
    static int speed = 1000;
    static int mode = 0;
    static int photo = 1;
    static int bgm = 1;
    static boolean newi = true;
    static boolean pasued = false;
    public void start(Stage primaryStage) throws Exception{
        Button startButton = operation.getAColoredButton("开始游戏");
        startButton.setOnAction(e -> {
            operation.landStage(volume);
            primaryStage.close();
            enterMediaPlayer.dispose();
        });
        Button loadButton = operation.getAColoredButton("重载游戏");
        loadButton.setOnAction(e -> {
            operation.showLoadingStage(volume);
            primaryStage.close();
            enterMediaPlayer.dispose();
        });

        Button setButton = operation.getAColoredButton("游戏设置");
        setButton.setOnAction(e -> {
                operation.showSettingStage();
        });

        Button helpButton = operation.getAColoredButton("游戏帮助");
        helpButton.setOnAction(e -> {
                operation.showHelpStage();
        });
        Button chooseButton = operation.getAColoredButton("选择关卡");
        chooseButton.setOnAction(e -> {
                operation.showSelectStage();
        });
        Button quitButton = operation.getAColoredButton("退出游戏");
        quitButton.setOnAction(e -> {
                System.exit(0);
        });

        VBox GamePane = new VBox();
        Scene GameScene = new Scene(GamePane);
        primaryStage.setScene(GameScene);
        primaryStage.setHeight(800);
        primaryStage.setWidth(700);
        primaryStage.setResizable(false);
        primaryStage.setTitle("俄罗斯方块");// Set the stage title
        primaryStage.show();
        GamePane.setPadding(new Insets(111));
        GamePane.setAlignment(Pos.BASELINE_CENTER);
        GamePane.setSpacing(50);
        operation.setBackground(GamePane,"./Resource/Image/timg2.jpg");
        GamePane.getChildren().addAll(startButton,loadButton,chooseButton,helpButton,setButton,quitButton);
        enterMediaPlayer.play();
        enterMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        }
    public static void main(String[] args)  {
        launch(args);
            }
    private MediaPlayer enterMediaPlayer = new MediaPlayer(new Media(getClass().getResource("./Resource/Music/PlantsVSZombie.mp3").toString()));
        }


