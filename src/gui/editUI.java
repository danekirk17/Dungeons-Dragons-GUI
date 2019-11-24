package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Stack;

public class editUI {

    private static BorderPane pane;
    private static Stage stage;
    private static Scene scene;
    private static VBox monsterBox;
    private static VBox treasureBox;
    private static Button addMonsterBtn;
    private static Button remMonsterBtn;
    private static Button addTreasureBtn;
    private static Button remTreasureBtn;

    private static void init() {
        pane = new BorderPane();
        stage = new Stage();
        scene = new Scene(pane);
        scene.getStylesheets().addAll("res/style.css");
        addMonsterBtn = new Button();
        remMonsterBtn = new Button();
        addTreasureBtn = new Button();
        remTreasureBtn = new Button();
    }

    private static void setStage(String selected) {
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(selected + " Editing Screen");
        stage.setMinHeight(150);
        stage.setMinWidth(350);
    }

    private static void setBtns(Controller theController, Gui myGui) {
        addMonsterBtn.setText("Add Monster");
        addMonsterBtn.setOnAction(e -> {
            theController.addMonster();
            myGui.setDesTAText();
        });
        remMonsterBtn.setText("Remove Monster");
        remMonsterBtn.setOnAction(e -> {
            theController.remMonster();
            myGui.setDesTAText();
        });
        addTreasureBtn.setText("Add Treasure");
        addTreasureBtn.setOnAction(e -> {
            theController.addTreasure();
            myGui.setDesTAText();
        });
        remTreasureBtn.setText("Remove Treasure");
        remTreasureBtn.setOnAction(e -> {
            theController.remTreasure();
            myGui.setDesTAText();
        });
    }

    public static void display(Controller theController, Gui myGui) {
        String selected = myGui.getSelectedSpaceStr();
        init();
        setStage(selected);
        setBtns(theController, myGui);
        monsterBox = new VBox(addMonsterBtn, remMonsterBtn);
        treasureBox = new VBox(addTreasureBtn, remTreasureBtn);
        pane.setLeft(monsterBox);
        pane.setRight(treasureBox);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
