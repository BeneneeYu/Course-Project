import java.lang.*;
import Block.IBlock;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.util.Timer;
import java.util.TimerTask;
import javafx.stage.WindowEvent;

public class gamepane {
    public gamepane(String username, int archivelocation, Game game, double volume) {

        //At first,choose the back ground music according to the data.
        switch (Main.bgm) {
            case (0):
                mainGameMediaPlayer.play();
                mainGameMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mainGameMediaPlayer.setVolume(volume / 15);
                break;
            case (1):
                mainGameMediaPlayer1.play();
                mainGameMediaPlayer1.setCycleCount(MediaPlayer.INDEFINITE);
                mainGameMediaPlayer1.setVolume(volume / 10);
                break;
        }

        //Start a timer.
        Timer timer = new Timer();

        //The array to keep the current block.
        Rectangle[] a = new Rectangle[4];

        //Initiate the stage of the game.
        Stage stage = new Stage();
        stage.setHeight(600);
        stage.setWidth(650);
        stage.setTitle("俄罗斯方块");
        stage.setResizable(false);
        game.score = 0;

        //Draw the whole gamepane.
        GridPane nextBlockPane = new GridPane();
        nextBlockPane.setPrefWidth(200);
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints(15);
            nextBlockPane.getColumnConstraints().add(column);
        }
        for (int i = 0; i < 10; i++) {
            RowConstraints row = new RowConstraints(18);
            nextBlockPane.getRowConstraints().add(row);
        }
        GridPane gamePane = new GridPane();
        for (int i = 0; i < 10; i++) {
            ColumnConstraints column = new ColumnConstraints(29.1);
            gamePane.getColumnConstraints().add(column);
        }
        for (int i = 0; i < 15; i++) {
            RowConstraints row = new RowConstraints(36.86);
            gamePane.getRowConstraints().add(row);
        }
        game.paintMap(gamePane);
        VBox infBox = new VBox(100);
        infBox.setPrefWidth(300);
        BorderPane wholePane = new BorderPane();
        wholePane.setRight(infBox);
        wholePane.setCenter(gamePane);
        wholePane.setLeft(nextBlockPane);
        Scene GameScene = new Scene(wholePane);
        stage.setScene(GameScene);
        stage.show();
        paint(game, a, gamePane, infBox, nextBlockPane);
        Rectangle rec = new Rectangle();
        rec.setWidth(gamePane.getWidth());
        rec.setHeight(gamePane.getHeight());
        rec.setFill(Color.BLACK);

        //Listen to the key pressed and control the game.
        gamePane.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case S:
                    gamePane.getChildren().clear();
                    game.goDown();
                    new operation().playLowerVolumeMusic("./Resource/Music/SEB_mino1.wav",0.1);
                    break;
                case A:
                    gamePane.getChildren().clear();
                    game.goLeft();
                    new operation().playLowerVolumeMusic("./Resource/Music/SEB_mino2.wav",0.1);
                    break;
                case D:
                    gamePane.getChildren().clear();
                    game.goRight();
                    new operation().playLowerVolumeMusic("./Resource/Music/SEB_mino3.wav",0.1);
                    break;
                case W:
                    gamePane.getChildren().clear();
                    if (!game.collide(game.posRow + 1, game.posColumn, game.currentBlock.nextRotatedCells())) {
                        game.currentBlock.rotate();
                        new operation().playLowerVolumeMusic("./Resource/Music/SEB_mino4.wav",0.1);
                    }
                    break;
                case SPACE:
                    gamePane.getChildren().clear();
                    game.goBottom();
                    new operation().playLowerVolumeMusic("./Resource/Music/SEB_mino5.wav",0.1);
                    break;
                case R:
                    Main.pasued = true;
                    Button yesButton = operation.getAColoredButton("重玩并存档");
                    Button noButton = operation.getAColoredButton("我点错了");
                    Button yoButton = operation.getAColoredButton("重玩");
                    Stage stageR = new Stage();
                    stageR.setTitle("确认界面");
                    stageR.setWidth(350);
                    stageR.setHeight(400);
                    VBox paneR = new VBox();
                    Scene scene = new Scene(paneR);
                    paneR.setPadding(new Insets(100, 100, 100, 100));
                    paneR.setSpacing(20);
                    Text t = new operation().beautifulText("你真的要重玩吗?");
                    t.setFont(Font.font(null, FontWeight.BOLD, 16));
                    t.setFill(Color.RED);
                    paneR.getChildren().addAll(t, yesButton,yoButton, noButton);
                    stageR.setScene(scene);
                    stageR.show();
                    yesButton.setOnAction(event -> {
                        operation.outGame(game,username,archivelocation);
                        game.getGame();
                            Main.pasued = false;
                            stageR.close();
                    });
                    yoButton.setOnAction(event -> {
                        game.getGame();
                        Main.pasued = false;
                        stageR.close();
                    });
                    noButton.setOnAction(event -> {
                            stageR.close();
                            Main.pasued  = false;
                    });
                    operation.setBackground(paneR, "./Resource/Image/timg5.jpg");
                    break;
//                case T:
//                    Main.mode = (Main.mode + 1) % 2;
//                    break;
                case NUMPAD1:
                    game.getIWeapon();
                    break;
                case NUMPAD2:
                    game.changeWeapon();
                    break;
                default:
                    break;
            }
            if (!game.reachTheBottom() && !game.somethingUnderBlock()) {
                paint(game, a, gamePane, infBox, nextBlockPane);
            } else {
                game.loadBlock();
                game.eliminate();
                game.currentBlock = game.nextBlock;
                game.nextBlock();
                paint(game, a, gamePane, infBox, nextBlockPane);
            }
        });
        gamePane.requestFocus();
        //Text is focused to receive key input.

        //Let the timer down the block automatically.
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!Main.pasued) {
                    Platform.runLater(() -> {
                        gamePane.getChildren().clear();
                        game.goDown();
                        if (!game.reachTheBottom() && !game.somethingUnderBlock()) {
                            paint(game, a, gamePane, infBox, nextBlockPane);

                        } else {
                            game.eliminateGroup();
                            paint(game, a, gamePane, infBox, nextBlockPane);
                            if (game.isOver()) {
                                this.cancel();
                                mainGameMediaPlayer.pause();
                                mainGameMediaPlayer1.pause();
                                stage.close();
                                operation.showRestartStage(username, archivelocation, volume);
                                operation.printRanking(username, game);
                            }
                        }
                    });
                }
            }
        }, 0, Main.speed);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!Main.pasued) {
                    Platform.runLater(() -> {
                        game.time++;
                        if (game.isOver()) {
                            this.cancel();
                        }
                    });
                }
            }
        }, 0, 1000);

        //If the player wants to quit,confirm the operation.
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                Main.pasued = true;
                Button yesButton = operation.getAColoredButton("退出并存档");
                Button yoButton = operation.getAColoredButton("退出");
                Button noButton = operation.getAColoredButton("我点错了");
                Stage stageR = new Stage();
                stageR.setTitle("确认界面");
                stageR.setWidth(350);
                stageR.setHeight(400);
                VBox paneR = new VBox();
                Scene scene = new Scene(paneR);
                paneR.setPadding(new Insets(100, 100, 100, 100));
                paneR.setSpacing(20);
                Text t = new operation().beautifulText("你真的要退出游戏吗?");
                t.setFont(Font.font(null, FontWeight.BOLD, 16));
                t.setFill(Color.RED);
                paneR.getChildren().addAll(t, yesButton,yoButton, noButton);
                stageR.setScene(scene);
                stageR.show();
                yesButton.setOnAction(e -> {
                    operation.outGame(game, username, archivelocation);
                        System.exit(0);
                });
                yoButton.setOnAction(e -> {
                    System.exit(0);
                });
                noButton.setOnAction(e -> {
                        stageR.close();
                        Main.pasued = false;
                });
                operation.setBackground(paneR, "./Resource/Image/timg5.jpg");
            }
        });
    }

    //The method to paint the changing pane.
    private void paint(Game game, Rectangle[] a, GridPane gamePane, VBox infBox, GridPane nextBlockPane) {
        String photoURL = "";
        switch (Main.photo) {
            case (0):
                photoURL = "./Resource/Image/timg.jpg";
                break;
            case (1):
                photoURL = "./Resource/Image/timg3.jpg";
                break;
        }
        game.shadowBottom();

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 10; j++) {
                for (int m = 0; m < 3; m++) {
                    if (((i >= game.posRow) && (i < game.posRow + 4)) && ((j >= game.posColumn) && (j < game.posColumn + 4))) {
                        if (game.currentBlock.getCells()[i - game.posRow][j - game.posColumn]) {
                            Stop[] stops = new Stop[]{new Stop(0, Color.WHITE), new Stop(1, Color.BROWN)};
                            LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
                            a[m] = new Rectangle();
                            a[m].setWidth(29.1);
                            a[m].setHeight(36.86);
                            a[m].setFill(lg1);
                            gamePane.add(a[m], j, i);
                        }
                    }
                    if (game.cells[i][j]) {
                        Stop[] stops = new Stop[]{new Stop(0, Color.WHITE), new Stop(1, Color.BLUE)};
                        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
                        Rectangle block = new Rectangle();
                        block.setWidth(29.1);
                        block.setHeight(36.86);
                        block.setFill(lg1);
                        gamePane.add(block, j, i);
                    }
//                    if (((i >= game.dropRow) && (i < game.dropRow + 4)) && ((j >= game.dropColumn) && (j < game.dropColumn + 4)) && (game.posRow < 10)) {
//                        if (game.currentBlock.getCells()[i - game.dropRow][j - game.dropColumn]) {
//                            Stop[] stops = new Stop[]{new Stop(0, Color.WHITE), new Stop(1, Color.GRAY)};
//                            LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
//                            Rectangle block = new Rectangle();
//                            block.setWidth(29.1);
//                            block.setHeight(36.86);
//                            block.setFill(lg1);
//                            gamePane.add(block, j, i);
//                        }
//                    }

                }
            }
        }

        //If the game mode is 'blind',draw a rectangle dug out of the circle.
        if (Main.mode == 1) {
            Rectangle rec = new Rectangle();
            Circle cir = new Circle();
            cir.setCenterY((game.posRow + 1.5) * 36.86);
            cir.setCenterX((game.posColumn + 1.5) * 29.1);
            cir.setRadius(70.4436413);
            if (game.currentBlock instanceof IBlock) {
                cir.setRadius(70.4436413 * 4 / 3);
            }
            rec.setX(0);
            rec.setY(0);
            rec.setWidth(296.0);
            rec.setHeight(564.8);
            gamePane.add(Shape.subtract(rec, cir), 0, 0, 10, 15);
        }
        //Reshow the infBox because the information changes.
        infBox.getChildren().clear();
        Label time = new Label("Time : " + game.time);
        time.setFont(new Font("Arial", 28));
        time.setTextFill(Color.BLACK);
        Label score = new Label("score : " + game.score);
        score.setFont(new Font("Arial", 28));
        score.setTextFill(Color.BLACK);
        Label changeWeaponTime = new Label("changeWeaponTime : " + game.changeWeaponTime);
        changeWeaponTime.setFont(new Font("Arial", 11));
        changeWeaponTime.setTextFill(Color.BLACK);
        Label getIWeaponTime = new Label("getIWeaponTime : " + game.getIWeaponTime);
        getIWeaponTime.setFont(new Font("Arial", 11));
        getIWeaponTime.setTextFill(Color.BLACK);
        infBox.getChildren().addAll(time, score, getIWeaponTime, changeWeaponTime);
        if (Main.newi) {
            nextBlockPane.getChildren().clear();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (game.nextBlock.getCells()[i][j]) {
                        Rectangle block = new Rectangle();
                        block.setWidth(18);
                        block.setHeight(18);
                        block.setFill(Color.GREEN);
                        nextBlockPane.add(block, j + 5, i + 1);
                    }
                }
            }
            Label nextBlock = new Label("下一个方块是 ：");
            nextBlock.setFont(new Font("Arial", 15));
            nextBlock.setTextFill(Color.BLACK);
            nextBlockPane.add(nextBlock, 0, 0, 8, 1);
            Button stop = operation.getAColoredButton("暂停");
            nextBlockPane.add(stop, 0, 10, 9, 2);
            stop.setOnAction(e -> {
                Main.pasued = true;
            });
            Button nostop = operation.getAColoredButton("继续");
            nextBlockPane.add(nostop, 0, 15, 9, 2);
            nostop.setOnAction(e -> {
                Main.pasued = false;
                gamePane.requestFocus();
            });
            Button watch = operation.getAColoredButton("查看排行榜");
            nextBlockPane.add(watch, 0, 20, 9, 2);
            watch.setOnAction(e -> {
                Main.pasued = true;
                operation.printRanking("nothing", game);
            });

            Main.newi = false;
        }
        operation.setBackground(gamePane, photoURL);
        operation.setBackground(infBox, "./Resource/Image/timg (1).jpg");
        operation.setBackground(nextBlockPane, "./Resource/Image/timg (1).jpg");
    }

    //Initialize the mediaplayer to play music.
    private MediaPlayer mainGameMediaPlayer = new MediaPlayer(new Media(getClass().getResource("./Resource/Music/Butterfly.mp3").toString()));
    private MediaPlayer mainGameMediaPlayer1 = new MediaPlayer(new Media(getClass().getResource("./Resource/Music/流动的城市.mp3").toString()));
    }