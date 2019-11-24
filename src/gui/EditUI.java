package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * creates the edit screen.
 */
public final class EditUI {

    /**
     * empty constructor just to make checkstyle happy.
     */
    private EditUI() {

    }

    /**holds the borderpane.*/
    private static BorderPane pane;
    /**holds the stage.*/
    private static Stage stage;
    /**holds the scene.*/
    private static Scene scene;
    /**holds the vbox for the monster controls.*/
    private static VBox monsterBox;
    /**holds the vbox for monster controls.*/
    private static VBox treasureBox;
    /**holds the add monster button.*/
    private static Button addMonsterBtn;
    /**holds the remove monster button.*/
    private static Button remMonsterBtn;
    /**holds the add treasure button.*/
    private static Button addTreasureBtn;
    /**holds the remove treasure button.*/
    private static Button remTreasureBtn;
    /**holds save edits button.*/
    private static Button saveBtn;
    /**displays the count of monsters to be added or removed.*/
    private static Label monsterCount;
    /**displays the count of treasures to be added or removed.*/
    private static Label treasureCount;
    /**holds the amount of monsters to be added or removed.*/
    private static int monsterNum;
    /**holds the amount of treasures to be added or removed.*/
    private static int treasureNum;

    /**
     * initialize all gui elements in this scene.
     */
    private static void init() {
        pane = new BorderPane();
        stage = new Stage();
        scene = new Scene(pane);
        scene.getStylesheets().addAll("res/style.css");
        addMonsterBtn = new Button();
        remMonsterBtn = new Button();
        addTreasureBtn = new Button();
        remTreasureBtn = new Button();
        saveBtn = new Button();
        monsterCount = new Label("Monster Count: 0");
        treasureCount = new Label("Treasure Count: 0");
        monsterNum = 0;
        treasureNum = 0;
    }

    /**
     * initializes the stage.
     * @param selected string representation of the selected space.
     */
    private static void setStage(String selected) {
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(selected + " Editing Screen");
        stage.setMinHeight(200);
        stage.setMinWidth(350);
    }

    /**
     * initializes the buttons.
     * @param theController the controller.
     * @param myGui the gui.
     */
    private static void setBtns(Controller theController, Gui myGui) {
        addMonsterBtn.setText("Add Monster");
        addMonsterBtn.setOnAction(e -> {
            monsterNum++;
            setLabelText();
        });
        remMonsterBtn.setText("Remove Monster");
        remMonsterBtn.setOnAction(e -> {
            monsterNum--;
            setLabelText();
        });
        addTreasureBtn.setText("Add Treasure");
        addTreasureBtn.setOnAction(e -> {
            treasureNum++;
            setLabelText();
        });
        remTreasureBtn.setText("Remove Treasure");
        remTreasureBtn.setOnAction(e -> {
            treasureNum--;
            setLabelText();
        });
        saveBtn.setText("Save");
        saveBtn.setOnAction(e -> {
            theController.save(monsterNum, treasureNum);
            monsterNum = 0;
            treasureNum = 0;
            setLabelText();
            stage.close();
        });
    }

    private static void setLabelText() {
        monsterCount.setText("Monster Count: " + monsterNum);
        treasureCount.setText("Treasure Count: " + treasureNum);
    }

    /**
     * loads and displays the edit screen.
     * @param theController the controller.
     * @param myGui the gui.
     */
    public static void display(Controller theController, Gui myGui) {
        String selected = myGui.getSelectedSpaceStr();
        init();
        setStage(selected);
        setBtns(theController, myGui);
        monsterBox = new VBox(addMonsterBtn, remMonsterBtn, monsterCount);
        treasureBox = new VBox(addTreasureBtn, remTreasureBtn, treasureCount);
        monsterBox.setSpacing(10);
        treasureBox.setSpacing(10);
        pane.setLeft(monsterBox);
        pane.setRight(treasureBox);
        pane.setBottom(saveBtn);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
