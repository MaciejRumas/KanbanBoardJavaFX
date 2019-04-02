package CanbanBoard;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Controller {

    @FXML
    private ListView<Task> toDoList;

    @FXML
    private ListView<Task> doingList;

    @FXML
    private ListView<Task> doneList;

    @FXML
    private Button fromToDoToDoing;

    @FXML
    private Button fromDoingToToDo;

    @FXML
    private Button fromDoingToDone;

    @FXML
    private Button fromDoneToDoing;

    private static  Button fromToDoToDoingBt;
    private static  Button fromDoingToToDoBt;
    private static  Button fromDoingToDoneBt;
    private static  Button fromDoneToDoingBt;
    private static Scene addScene;
    private static Stage secondaryStage = new Stage();
    private static ObservableList<Task> toDoObservableList =  FXCollections.observableArrayList();
    private static ObservableList<Task> doingObservableList =  FXCollections.observableArrayList();
    private static ObservableList<Task> doneObservableList =  FXCollections.observableArrayList();
    private static ObservableList<Task> actualObservableList = FXCollections.observableArrayList();
    private static ListView<Task> toDoListView;
    private static ListView<Task> doingListView;
    private static ListView<Task> doneListView;
    private static ListView<Task> actualList;
    private static ContextMenu contextMenu;
    private static Task selectedTask;
    private static int selectedTaskIndex;

    public static ListView<Task> getActualList() {
        return actualList;
    }

    public static ObservableList<Task> getActualObservableList() {
        return actualObservableList;
    }

    public static int getSelectedTaskIndex() {
        return selectedTaskIndex;
    }

    public static Task getSelectedTask() {
        return selectedTask;
    }

    public static ListView<Task> getToDoListView() {
        return toDoListView;
    }

    public static ObservableList<Task> getToDoObservableList() {
        return toDoObservableList;
    }

    public static Scene getAddScene(){
        return addScene;
    }

    public static Stage getSecondaryStage() {
        return secondaryStage;
    }

    public void pressAddTaskButton(ActionEvent event) throws IOException{

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("addItemPopup.fxml"));

            addScene = new Scene(fxmlLoader.load());
            Controller.getSecondaryStage().setTitle("Add new task");
            Controller.getSecondaryStage().setScene(addScene);
            Controller.getSecondaryStage().setResizable(false);
            Controller.getSecondaryStage().getIcons().add(new Image("/CanbanBoard/img/add.png"));
            Controller.getSecondaryStage().show();

            toDoListView = toDoList;
            doingListView = doingList;
            doneListView = doneList;

           toDoListView.setCellFactory(callback);

            Main.setComboBox((ComboBox<Priority>)Controller.getAddScene().lookup("#priorityComboBox"));
            Main.getComboBox().setItems(Main.getPriorityList());
            Main.getComboBox().getSelectionModel().selectFirst();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

    public void pressCloseMenuItem(ActionEvent event){
        Main.getPrimaryStage().close();
    }

    public void pressAboutMenuItem(ActionEvent event){
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setContentText("All rights reserved \u00a9 Maciej Rumas");
        about.setHeaderText("Copyrighting");
        about.setTitle("About");
        about.show();
    }

   public void shiftFromDoingToToDo(ActionEvent event) {
        try{
            if(doingObservableList.size() > 0) {
                toDoList.setCellFactory(callback);
                toDoObservableList.add(selectedTask);
                toDoList.setItems(toDoObservableList);
                doingObservableList.remove(selectedTaskIndex);
                doingListView.refresh();
            }
        }catch (Exception exception){
            System.out.println("Exception thrown: " + exception.getMessage());
        }
    }

    public void shiftFromToDoToDoing(ActionEvent event) {
        try{
            if(toDoObservableList.size() > 0) {
                doingList.setCellFactory(callback);
                doingObservableList.add(selectedTask);
                doingList.setItems(doingObservableList);
                toDoObservableList.remove(selectedTaskIndex);
                toDoListView.refresh();
            }
        }catch (Exception exception){
            System.out.println("Exception thrown: " + exception.getMessage());
        }
    }

    public void shiftFromDoingToDone(ActionEvent event) {
        try{
            if(doingObservableList.size() > 0) {
                doneList.setCellFactory(callback);
                doneObservableList.add(selectedTask);
                doneListView.setItems(doneObservableList);
                doingObservableList.remove(selectedTaskIndex);
                doingListView.refresh();
            }
        }catch (Exception exception){
            System.out.println("Exception thrown: " + exception.getMessage());
        }
    }

    public void shiftFromDoneToDoing(ActionEvent event) {
        try{
            if(doneObservableList.size() > 0) {
                doingList.setCellFactory(callback);
                doingObservableList.add(selectedTask);
                doingListView.setItems(doingObservableList);
                doneObservableList.remove(selectedTaskIndex);
                doneListView.refresh();
            }
        }catch (Exception exception){
            System.out.println("Exception thrown: " + exception.getMessage());
        }
    }

    Callback<ListView<Task>, ListCell<Task>> callback = new Callback<ListView<Task>, ListCell<Task>>() {
        @Override
        public ListCell<Task> call(ListView<Task> list) {
            final ListCell<Task> cell = new TaskCell();

            cell.setOnMouseEntered(event1 -> {
                if (cell.getText() != null) {

                    final Tooltip tooltip = new Tooltip();
                    tooltip.setText(((TaskCell) cell).getCellDescription());
                    cell.setTooltip(tooltip);
                } else {
                    cell.setTooltip(null);
                }
            });

            cell.setOnMouseReleased(event2 -> {
                if (cell.getText() != null) {
                    selectedTask = list.getSelectionModel().getSelectedItem();
                    actualObservableList = list.getItems();
                    actualList = list;
                    selectedTaskIndex = list.getSelectionModel().getSelectedIndex();
                    if(event2.getButton() == MouseButton.SECONDARY){
                        contextMenu = TaskContextMenu.createContextMenu();
                        cell.setContextMenu(contextMenu);
                        contextMenu.show(cell, event2.getScreenX(), event2.getScreenY());
                    }
                }
            });
            return cell;
        }
    };

}
