package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    }

    /**
     * initializes the stage.
     * @param selected string representation of the selected space.
     */
    private static void setStage(String selected) {
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(selected + " Editing Screen");
        stage.setMinHeight(150);
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
        monsterBox = new VBox(addMonsterBtn, remMonsterBtn);
        treasureBox = new VBox(addTreasureBtn, remTreasureBtn);
        pane.setLeft(monsterBox);
        pane.setRight(treasureBox);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
