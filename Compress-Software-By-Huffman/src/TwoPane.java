import javafx.application.Application;

import javafx.geometry.Pos;

import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.control.Label;

import javafx.scene.layout.GridPane;

import javafx.scene.layout.HBox;

import javafx.scene.layout.Priority;

import javafx.stage.Stage;

public class TwoPane extends Application {

    final static private int BUTTON_WIDTH = 40;

    final static private int BUTTON_HEIGHT = 40;

    public static void main(String[] args) {

        launch(args);

    }

    @Override

    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("简易计算器");

        Label label = new Label("");

        label.setAlignment(Pos.CENTER);

        label.setMinWidth(100);

        HBox expresssionPanel = new HBox(label);

        expresssionPanel.setAlignment(Pos.CENTER);

        GridPane keyboardPanel = new GridPane();

        for(int i = 1; i <= 9; ++i){

            Button btn = new Button(String.valueOf(i));

            btn.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

            btn.setOnAction(e -> label.setText(label.getText() + btn.getText()));

            keyboardPanel.add(btn, (i - 1) % 3, i > 3 ? (i > 6 ? 2 : 1) : 0);

        }

        Button zero = new Button(String.valueOf("0"));

        zero.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        zero.setOnAction(e -> label.setText(label.getText() + zero.getText()));

        Button plus = new Button(String.valueOf("+"));

        plus.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        plus.setOnAction(e -> label.setText(label.getText() + plus.getText()));

        Button minus = new Button(String.valueOf("-"));

        minus.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        minus.setOnAction(e -> label.setText(label.getText() + minus.getText()));

        Button time = new Button(String.valueOf("*"));

        time.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        time.setOnAction(e -> label.setText(label.getText() + time.getText()));

        Button divide = new Button(String.valueOf("/"));

        divide.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        divide.setOnAction(e -> label.setText(label.getText() + divide.getText()));

        Button calc = new Button(String.valueOf("="));

        calc.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        calc.setOnAction(e -> label.setText(label.getText() + calc.getText())/*TODO 计算逻辑代码待完成*/);

        keyboardPanel.add(zero, 0, 3);

        keyboardPanel.add(plus, 1, 3);

        keyboardPanel.add(minus, 2, 3);

        keyboardPanel.add(time, 0, 4);

        keyboardPanel.add(divide, 1, 4);

        keyboardPanel.add(calc, 2, 4);

        HBox root = new HBox(expresssionPanel, keyboardPanel);

        HBox.setHgrow(expresssionPanel, Priority.ALWAYS);

        primaryStage.setScene(new Scene(root));

        primaryStage.show();

    }

}