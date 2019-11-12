import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.StringTokenizer;

public class operation {
    public static void setBackground(Pane pane, String url) {
        pane.setBackground(new Background(new BackgroundImage(new Image(url, pane.getWidth(), pane.getHeight(), false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
    }

    //Output the instance of Game.
    public static void outGame(Game game, String username, int num) {
        FileOutputStream fos = null;
        //声明对象处理流
        ObjectOutputStream oos = null;
        try {
            File file0 = new File(username + "的" + game.getClass().getName() + num + ".dat");
            File file = new File("./OldGames/" + file0);
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
            oos.flush();
        } catch (FileNotFoundException e) {
            mistake();
        } catch (IOException e) {
            mistake();
        } finally {
            try {
                //关闭资源
                fos.close();
                oos.close();
            } catch (IOException e) {
                mistake();
            }
        }
    }

    public static int getNum(String a) {
        int Num = 0;
        if (a != null && !"".equals(a) && !"null".equals(a)) {
            StringTokenizer b = new StringTokenizer(a);
            b.nextToken();
            String c = b.nextToken();
            if (c != null && !"".equals(c) && !"null".equals(c)) {
                Num = Integer.parseInt(c);
                return Num;
            }
            return Num;
        }
        return Num;
    }

    public static String getName(String name) {
        if (name != null && !"".equals(name) && !"null".equals(name)) {
            StringTokenizer part = new StringTokenizer(name);
            String Name = part.nextToken();
            if (Name != null && !"".equals(Name)) {
                return Name;
            } else {
                return null;
            }
        }
        return null;
    }

    public static void printRanking(String Name, Game game) {
        try {
            DropShadow ds = new DropShadow();
            ds.setOffsetY(3.0f);
            ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
            Text t = new Text();
            Reflection r = new Reflection();
            r.setFraction(0.7f);
            t.setEffect(r);
            t.setEffect(ds);
            t.setCache(true);
            t.setX(10.0f);
            t.setY(270.0f);
            t.setFill(Color.BLACK);
            t.setText("综合分数排行榜：（基础分数算法：游戏消除分 + 三倍存活时间分）");
            t.setFont(Font.font(null, FontWeight.BOLD, 32));
            FileWriter wt = new FileWriter("./myfile.txt", true);
            BufferedReader in = new BufferedReader(new FileReader("./myfile.txt"));
            if (!Name.equals("nothing")) {
                BufferedWriter out = new BufferedWriter(wt);
                if (Main.speed == 330) {
                    out.write(Name + "\t" + 2 * (game.score + game.time * 3) + "\n");

                } else if (Main.speed == 220) {
                    out.write(Name + "\t" + 3 * (game.score + game.time * 3) + "\n");
                } else if (Main.mode == 1) {
                    out.write(Name + "\t" + 4 * (game.score + game.time * 3) + "\n");
                } else {
                    out.write(Name + "\t" + (game.score + game.time * 3) + "\n");
                }
                out.flush();
                out.close();
            }
            String[] nameAndScore = new String[256];
            for (int i = 0; i < 256; i++) {
                String tempStr = in.readLine();
                if (tempStr != null) {
                    if (!"".equals(tempStr)) {
                        nameAndScore[i] = tempStr;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            for (int i = 0; i < nameAndScore.length - 1; i++) {//冒泡趟数
                for (int j = 0; j < nameAndScore.length - i - 1; j++) {
                    //如果顺序不对，则交换两个元素
                    if (operation.getNum(nameAndScore[j + 1]) < operation.getNum(nameAndScore[j])) {
                        String temp = "";//定义一个临时变量
                        temp = nameAndScore[j];
                        nameAndScore[j] = nameAndScore[j + 1];
                        nameAndScore[j + 1] = temp;
                    }
                }
            }
            for (int m = nameAndScore.length - 1; m > nameAndScore.length - 11; m--) {
                t.setText(t.getText() + "\n" + "第" + (nameAndScore.length - m) + "名" + operation.getName(nameAndScore[m]) + "的分数是" + operation.getNum(nameAndScore[m]));
            }

            Stage rankStage = new Stage();
            VBox rankBox = new VBox();
            rankBox.getChildren().addAll(t, new operation().OKBUTTON(rankStage));
            Scene scene = new Scene(rankBox);
            operation.setBackground(rankBox, "./Resource/Image/timg3.jpg");
            rankStage.setScene(scene);
            rankStage.show();
            rankStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Initialize a button 'OK'.
    public Button OKBUTTON(Stage stage) {
        Button OK = operation.getAColoredButton("OK");
        OK.setStyle(OK.getStyle() + "-fx-pref-width:180;");
        OK.setStyle(OK.getStyle() + "-fx-base:#3366ff;");
        OK.setOnAction(e -> {
                stage.close();
                System.gc();
        });
        OK.setAlignment(Pos.CENTER);
        return OK;
    }

    //Beautify the text entered.
    public Text beautifulText(String s) {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        Text t = new Text();
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        t.setEffect(r);
        t.setEffect(ds);
        t.setCache(true);
        t.setX(10.0f);
        t.setY(270.0f);
        t.setFill(Color.GREEN);
        t.setText(s);
        t.setFont(Font.font(null, FontWeight.BOLD, 32));
        return t;
    }

    //A method to play lower volume music.
    public void playLowerVolumeMusic(String url,double multiple) {
        MediaPlayer a = new MediaPlayer(new Media(getClass().getResource(url).toString()));
        a.setVolume(multiple);
        a.play();
    }

    //The methods to create some kinds of panes.
    public static void showRestartStage(String username, int archivelocation, double volume) {
        Button restartButton = operation.getAColoredButton("重玩一局");
        Button quitButton = operation.getAColoredButton("退出游戏");
        Stage stage = new Stage();
        stage.setTitle("游戏结束");
        stage.setWidth(350);
        stage.setHeight(400);
        VBox pane = new VBox();
        Scene scene = new Scene(pane);
        pane.setPadding(new Insets(100, 100, 100, 100));
        pane.setSpacing(50);
        Text t = new operation().beautifulText("游戏结束，请选择");
        t.setFont(Font.font(null, FontWeight.BOLD, 16));
        t.setFill(Color.RED);
        pane.getChildren().addAll(t, restartButton, quitButton);
        stage.setScene(scene);
        stage.show();
        Game g = new Game();
        g.getGame();
        restartButton.setOnAction(e -> {
            new gamepane(username, archivelocation, g, volume);
            stage.close();
            System.gc();
        });
        quitButton.setOnAction(e -> {
            System.exit(0);
        });
        operation.setBackground(pane, "./Resource/Image/timg4.jpg");

    }

    //The method to show the stage to select game modes.
    public static void showSelectStage() {
        Stage stage = new Stage();
        VBox root = new VBox();
        operation.setBackground(root, "./Resource/Image/timg3.jpg");
        Scene scene = new Scene(root, 260, 310);
        stage.setScene(scene);
        stage.setTitle("请选择游戏模式");
        ToggleGroup group = new ToggleGroup();
        RadioButton button1 = new RadioButton("普通模式");
        button1.setSelected(true);
        button1.setUserData("普通模式");
        RadioButton button2 = new RadioButton("高速下落模式(2倍得分)");
        button2.setUserData("高速下落模式");
        RadioButton button4 = new RadioButton("超高速下落模式(3倍得分)");
        button4.setUserData("超高速下落模式");
        RadioButton button3 = new RadioButton("盲打模式(4倍得分)");
        button3.setUserData("盲打模式");
        RadioButton[] buttons2 = {button1,button2,button3,button4};
        setGroup(buttons2,group);
        root.getChildren().addAll(button1, button2, button4, button3, new operation().OKBUTTON(stage));
        root.setSpacing(50);
        button1.requestFocus();
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    switch (group.getSelectedToggle().getUserData().toString()) {
                        case ("普通模式"):
                            break;
                        case ("高速下落模式"):
                            Main.speed = 330;
                            break;
                        case ("超高速下落模式"):
                            Main.speed = 220;
                            break;
                        case ("盲打模式"):
                            Main.mode = 1;
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        scene.setRoot(root);
        stage.show();
    }

    //Show a stage to illustrate how to play my game.
    public static void showHelpStage() {

        Stage helpStage = new Stage();
        VBox helpBox = new VBox();
        operation.setBackground(helpBox, "./Resource/Image/timg3.png");
        Scene scene = new Scene(helpBox);
        helpStage.setScene(scene);
        helpStage.show();
        helpBox.getChildren().addAll(new operation().beautifulText("A: 左移\tS: 下移\tD: 右移\nW: 旋转\tSpace: 坠底\tQ: 退出并存档\nP: 暂停\tO: 回到游戏\tR: 重玩\n小键盘1:使用“跳过当前方块”道具\n小键盘2:使用“下一个方块变长条”道具\n"), new operation().OKBUTTON(helpStage));
        helpStage.setResizable(false);
        helpStage.setTitle("帮助");
        helpStage.setHeight(500);
        helpStage.setWidth(600);
        helpStage.show();
    }

    //Show a stage to let the player load his past game.
    public static void showLoadingStage(double volume) {
        Stage stage = new Stage();
        stage.setHeight(300);
        stage.setWidth(325);
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5);
        Button load = new Button("Load");
        pane.add(load, 1, 2);
        operation.setBackground(pane, "./Resource/Image/timg3.png");
        GridPane.setHalignment(load, HPos.RIGHT);
        load.setOnAction(event -> {
                try {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialDirectory(new File("./OldGames"));
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT", "*.dat");
                    fileChooser.getExtensionFilters().add(extFilter);
                    FileInputStream fis = new FileInputStream(fileChooser.showOpenDialog(stage));
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    Game g = ((Game) ois.readObject());
                    String Username = g.username;
                    String Archivelocation = g.usernum;
                    int ArchiveLocation = Integer.parseInt(Archivelocation);
                    new gamepane(Username, ArchiveLocation, g, volume);
                    ois.close();
                    fis.close();
                    stage.close();
                    System.gc();
                }
                catch (ClassNotFoundException e) {
                    mistake();
                } catch (IOException e) {
                    mistake();
                }
        });
        // Create a scene and place it in the stage
        Scene scene = new Scene(pane);
        stage.setTitle("Load"); // Set the stage title
        stage.setScene(scene); // Place the scene in the stage
        stage.show(); // Display the stage
    }

    //Show a stage to help player set the game.
    public static void showSettingStage() {
        Button set1 = operation.getAColoredButton("设置游戏背景音乐音量");
        set1.setOnAction(e -> {
            setVolume();
        });


        Button set2 = operation.getAColoredButton("设置背景音乐和图片");
        set2.setOnAction(e -> {
            setBGMandPHO();
        });
        Stage stage = new Stage();
        stage.setTitle("游戏设置");
        stage.setHeight(250);
        stage.setWidth(250);
        VBox pane = new VBox();
        operation.setBackground(pane, "./Resource/Image/timg3.jpg");
        stage.setScene(new Scene(pane));
        stage.show();
        pane.setSpacing(30);
        pane.setPadding(new Insets(30, 30, 30, 30));
        Button OK = operation.getAColoredButton("OK");
        OK.setStyle(OK.getStyle() + "-fx-pref-width:120;");
        OK.setStyle(OK.getStyle() + "-fx-base:#3366ff;");
        OK.setOnAction(e -> {
            stage.close();
            System.gc();
        });
        pane.getChildren().addAll(set1, set2, new operation().OKBUTTON(stage));

    }

    private static void setVolume() {
        Stage stage = new Stage();
        stage.setHeight(200);
        stage.setWidth(325);
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5);
        TextField Volume = new TextField();

        // Place nodes in the pane
        pane.add(new Label("Volume(0-10):"), 0, 0);
        pane.add(Volume, 1, 0);

        Button OK = new Button("OK");
        pane.add(OK, 1, 1);
        operation.setBackground(pane, "./Resource/Image/timg3.png");
        GridPane.setHalignment(OK, HPos.RIGHT);


        // Create a scene and place it in the stage
        Scene scene = new Scene(pane);
        stage.setTitle("设置游戏背景音乐音量"); // Set the stage title
        stage.setScene(scene); // Place the scene in the stage
        stage.show(); // Display the stage


        OK.setOnAction(e -> {
            if (!"".equals(Volume.getText())) {
                double volume = Integer.parseInt(Volume.getText());
                Main.volume = volume;
                stage.close();
                System.gc();
            }
        });
        operation.setBackground(pane, "./Resource/Image/timg3.png");

    }

    private static void setBGMandPHO() {
        Stage stage = new Stage();
        stage.setHeight(260);
        stage.setWidth(220);
        stage.setTitle("设置背景音乐和图片");
        VBox pane = new VBox();
        pane.setPadding(new Insets(20, 20, 20, 20));
        pane.setSpacing(20);
        ToggleGroup group = new ToggleGroup();
        RadioButton button1 = new RadioButton("激情四射的音乐");
        button1.setUserData("激情四射的音乐");
        RadioButton button2 = new RadioButton("儒雅随和的音乐");
        button2.setSelected(true);
        button2.setUserData("儒雅随和的音乐");
        RadioButton[] buttons = {button1,button2};
        setGroup(buttons,group);
        pane.getChildren().addAll(button1, button2);
        button2.requestFocus();
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    switch (group.getSelectedToggle().getUserData().toString()) {
                        case ("激情四射的音乐"):
                            Main.bgm = 0;
                            break;
                        case ("儒雅随和的音乐"):
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        ToggleGroup Group = new ToggleGroup();
        RadioButton Button1 = new RadioButton("浩瀚星辰作为背景图");
        Button1.setUserData("浩瀚星辰作为背景图");
        RadioButton Button2 = new RadioButton("汪洋大海作为背景图");
        Button2.setSelected(true);
        Button2.setUserData("汪洋大海作为背景图");
        RadioButton[] buttons1 = {Button1,Button2};
        setGroup(buttons1,Group);
        Button2.requestFocus();
        Group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (Group.getSelectedToggle() != null) {
                    switch (Group.getSelectedToggle().getUserData().toString()) {
                        case ("浩瀚星辰作为背景图"):
                            Main.photo = 0;
                            break;
                        case ("汪洋大海作为背景图"):
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        pane.getChildren().addAll(Button1, Button2, new operation().OKBUTTON(stage));
        stage.setScene(new Scene(pane));
        stage.show();
        operation.setBackground(pane, "./Resource/Image/timg3.png");
    }

    public static void landStage(double volume) {
        Stage stage = new Stage();
        stage.setHeight(300);
        stage.setWidth(325);
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5);
        TextField username = new TextField();
        TextField archiveLocation = new TextField();

        // Place nodes in the pane
        pane.add(new Label("Username:"), 0, 0);
        pane.add(username, 1, 0);
        pane.add(new Label("Archive location:"), 0, 1);
        pane.add(archiveLocation, 1, 1);
        Button land = new Button("Land");
        pane.add(land, 1, 2);
        operation.setBackground(pane, "./resource/image/timg3.png");
        GridPane.setHalignment(land, HPos.RIGHT);


        // Create a scene and place it in the stag
        stage.setTitle("land"); // Set the stage title
        stage.setScene(new Scene(pane)); //Create a scene and place it in the stage.
        stage.show(); // Display the stage
        land.setOnAction(e -> {
            String Username = username.getText();
            Game game = new Game(Username,archiveLocation.getText());
            game.getGame();
            new gamepane(Username, Integer.parseInt(archiveLocation.getText()), game, volume);
            stage.close();
            System.gc();

        });
    }
    private static void mistake(){
        Stage stage1 = new Stage();
        Pane pane1 = new Pane();
        pane1.getChildren().add(new Label("读取失败！"));
        stage1.setScene( new Scene(pane1));
        stage1.show();
    }

    //A bandage of operations.
    private static void setGroup(RadioButton[] buttons, ToggleGroup group){
        int num = 0;
        while (num < buttons.length){
            buttons[num].setToggleGroup(group);
            num++;
        }
    }
    public static Button getAColoredButton(String title){
        Button button = new Button(title);
        button.setStyle(button.getStyle() + "-fx-base:#9966cc;");
        button.setStyle(button.getStyle() + "-fx-text-fill:#ffdfdf;");
        button.setStyle(button.getStyle() + "-fx-font-size:11;");
        button.setStyle(button.getStyle() + "-fx-pref-width:180;");
        button.setStyle(button.getStyle() + "-fx-background-radius:2em;");
        button.setStyle(button.getStyle() + "-fx-padding: 10px;");
        return button;
    }


}