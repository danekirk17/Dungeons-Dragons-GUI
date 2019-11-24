package gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mydnd.Space;

/**
 * creates the main gui.
 */
public class Gui extends Application {

    /**holds the controller.*/
    private Controller theController;
    /**holds the borderpane.*/
    private BorderPane root;
    /**holds the stage.*/
    private Stage primaryStage;
    /**holds the menu bar.*/
    private MenuBar menuBar;
    /**holds the vbox for the space list and edit button.*/
    private VBox leftBox;
    /**holds the space list.*/
    private ListView<String> spaceListView;
    /**holds the text area for description.*/
    private TextArea desTA;
    /**holds the door menu.*/
    private ChoiceBox<String> doorMenu;
    /**holds the edit button.*/
    private Button editBtn;

    /**
     * runs the main window.
     * @param s the main stage.
     */
    @Override
    public void start(Stage s) {
        primaryStage = s;
        theController = new Controller(this);
        root = setUpRoot();
        Scene scene = new Scene(root, 800, 400);
        scene.getStylesheets().add("res/style.css");
        primaryStage.setTitle("Dungeon Master Toolkit");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * initialize the border pane.
     * @return the border pane.
     */
    private BorderPane setUpRoot() {
        BorderPane temp = new BorderPane();
        setMenuBar();
        temp.setTop(menuBar);
        setDoorMenu();
        temp.setRight(doorMenu);
        setSpaceList();
        setEditBtn();
        setLeftBox();
        temp.setLeft(leftBox);
        setDesTA();
        temp.setCenter(desTA);
        return temp;
    }

    /**
     * initialize the menu bar.
     */
    private void setMenuBar() {
        MenuItem save = new MenuItem("Save");
        MenuItem load = new MenuItem("Load");
        Menu file = new Menu("File");
        file.getItems().setAll(save, load);
        menuBar = new MenuBar(file);
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                theController.writeFile();
            }
        });
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                theController.readFile();
            }
        });
    }

    /**
     * initialize the text area for descriptions.
     */
    private void setDesTA() {
        desTA = new TextArea();
        desTA.setPrefSize(200, 200);
    }

    /**
     * refresh the description text area.
     */
    public void setDesTAText() {
        Space mySpace = theController.getSelectedSpace();
        if (mySpace != null) {
            String des = getSelectedSpaceStr() + "\n\n" + mySpace.getDescription();
            desTA.setText(des);
            setDoorMenuList();
        }
    }

    /**
     * initialize the vbox with space list and edit button.
     */
    private void setLeftBox() {
        leftBox = new VBox(spaceListView, editBtn);
        leftBox.setSpacing(25);
    }

    /**
     * initialize the listview with the space list.
     */
    private void setSpaceList() {
        spaceListView = new ListView<>();
        spaceListView.setPrefSize(150, 300);
        ObservableList<String> spaceList = FXCollections.observableArrayList("Chamber 1", "Chamber 2", "Chamber 3", "Chamber 4", "Chamber 5");
        for (int i = 0; i < theController.getLevelPassages().size(); i++) {
            spaceList.add("Passage " + (i + 1));
        }
        spaceListView.getItems().addAll(spaceList);
        spaceListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> setDesTAText());
    }

    /**
     * initialize the edit button.
     */
    private void setEditBtn() {
        editBtn = new Button();
        editBtn.setText("Edit");
        editBtn.setOnMouseClicked(mouseEvent -> EditUI.display(theController, this));
    }

    /**
     * initialize the door menu.
     */
    private void setDoorMenu() {
        doorMenu = new ChoiceBox<>();
        doorMenu.setPrefWidth(150);
        doorMenu.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            if ((Integer) number2 != -1) {
                theController.displayDoorInfo(doorMenu.getItems().get((Integer) number2));
            }
        });
    }

    /**
     * refresh the list of doors in the choicebox.
     */
    private void setDoorMenuList() {
        doorMenu.getItems().setAll(theController.getDoorMenuList());
    }

    /**
     * get the string representation of the currently selected space in the space list.
     * @return the string representation of the currently selected space in the space list.
     */
    public String getSelectedSpaceStr() {
        return spaceListView.getSelectionModel().getSelectedItem();
    }

    /**
     * return the num at the end of the string.
     * @param selected the string representation of a space.
     * @return the num at the end of the string.
     */
    public int parseSelectionNum(String selected) {
        int ind1;
        int ind2;
        ind1 = selected.length() - 2;
        ind2 = selected.length();
        String sNum = selected.substring(ind1, ind2);
        if (sNum.charAt(0) == ' ') {
            sNum = String.valueOf(sNum.charAt(1));
        }
        return Integer.parseInt(sNum);
    }

    /**
     * main function to run the program.
     * @param args stuff being passed.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
