package CanbanBoard;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TaskContextMenu extends ContextMenu {

    private static ContextMenu contextMenu;
    private static ObservableList<Task> items = FXCollections.observableArrayList();
    private static Scene editScene;

    public static Stage getThirdStage() {
        return thirdStage;
    }

    private static Stage thirdStage;

    public static ContextMenu createContextMenu() {
        contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (Controller.getSelectedTaskIndex() != -1) {
                    Controller.getActualObservableList().remove(Controller.getSelectedTaskIndex());
                    Controller.getActualList().setItems(Controller.getActualObservableList());
                    Controller.getActualList().refresh();
                    Controller.setSelectedTask(null);

                }
            }
        });

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader2 = new FXMLLoader();
                    fxmlLoader2.setLocation(getClass().getResource("editItemPopup.fxml"));
                    editScene = new Scene(fxmlLoader2.load());
                    thirdStage = new Stage();
                    thirdStage.setResizable(false);
                    thirdStage.setTitle("Edit task");
                    thirdStage.setScene(editScene);
                    thirdStage.getIcons().add(new Image("/CanbanBoard/img/edit.png"));
                    Main.setComboBox((ComboBox<Priority>) editScene.lookup("#priorityComboBox"));
                    Main.getComboBox().setItems(Main.getPriorityList());
                    Main.getComboBox().getSelectionModel().selectFirst();
                    thirdStage.show();

                } catch (IOException e) {
                    Logger logger = Logger.getLogger(getClass().getName());
                    logger.log(Level.SEVERE, "Failed to create new Window.", e);
                }
            }
        });

        contextMenu.getItems().addAll(deleteItem, editItem);

        return contextMenu;
    }
}
