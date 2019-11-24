package gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mydnd.*;

public class Gui extends Application {

    private Controller theController;
    private BorderPane root;
    private Stage primaryStage;
    private MenuBar menu_bar;
    private VBox leftBox;
    private ListView<String> space_list;
    private TextArea des_ta;
    private ChoiceBox<String> door_menu;
    private Button edit_btn;

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

    private BorderPane setUpRoot() {
        BorderPane temp = new BorderPane();
        setMenuBar();
        temp.setTop(menu_bar);
        setDoorMenu();
        temp.setRight(door_menu);
        setSpaceList();
        setEditBtn();
        setLeftBox();
        temp.setLeft(leftBox);
        setDesTA();
        temp.setCenter(des_ta);
        return temp;
    }

    private void setMenuBar() {
        MenuItem save = new MenuItem("Save");
        MenuItem load = new MenuItem("Load");
        Menu file = new Menu("File");
        file.getItems().setAll(save, load);
        menu_bar = new MenuBar(file);
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

    private void setDesTA() {
        des_ta = new TextArea();
        des_ta.setPrefSize(200, 200);
    }

    private void setLeftBox() {
        leftBox = new VBox(space_list, edit_btn);
        leftBox.setSpacing(25);
    }

    private void setSpaceList() {
        space_list = new ListView<>();
        space_list.setPrefSize(150, 300);
        ObservableList<String> spaceList = FXCollections.observableArrayList("Chamber 1", "Chamber 2", "Chamber 3", "Chamber 4", "Chamber 5");
        for (int i = 0; i < theController.getLevelPassages().size(); i++) {
            spaceList.add("Passage " + (i+1));
        }
        space_list.getItems().addAll(spaceList);
        space_list.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> setDesTAText());
    }

    private void setEditBtn() {
        edit_btn = new Button();
        edit_btn.setText("Edit");
        edit_btn.setOnMouseClicked(mouseEvent -> theController.edit());
    }

    private void setDesTAText() {
        Space mySpace = theController.getSelectedSpace();
        des_ta.setText(mySpace.getDescription());
        setDoorMenuList();
    }

    private void setDoorMenu() {
        door_menu = new ChoiceBox<>();
        door_menu.setPrefWidth(150);
        door_menu.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            if ((Integer)number2 != -1) {
                theController.displayDoorInfo(door_menu.getItems().get((Integer) number2));
            }
        });
    }

    private void setDoorMenuList() {
        door_menu.getItems().setAll(theController.getDoorMenuList());
    }

    public String getSelectedSpaceStr() {
        return space_list.getSelectionModel().getSelectedItem();
    }

    public int parseSelectionNum(String selected) {
        int ind1, ind2;
        ind1 = selected.length() - 2;
        ind2 = selected.length();
        String sNum = selected.substring(ind1, ind2);
        if (sNum.charAt(0) == ' ') {
            sNum = String.valueOf(sNum.charAt(1));
        }
        return Integer.parseInt(sNum);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
