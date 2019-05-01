package CanbanBoard;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage = new Stage();
    private static ComboBox<Priority> comboBox;
    private static ObservableList<Priority> priorityList = FXCollections.observableArrayList();

    public static ObservableList<Priority> getPriorityList() {
        return priorityList;
    }

    public static void setComboBox(ComboBox<Priority> comboBox) {
        Main.comboBox = comboBox;
    }

    public static ComboBox<Priority> getComboBox() {
        return comboBox;
    }

    @Override
    public void start(Stage pStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainStage.fxml"));
        pStage.setTitle("Kanban Board");
        pStage.setScene(new Scene(root, 600, 400));
        pStage.show();
        primaryStage = pStage;
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/CanbanBoard/img/book.png"));

        priorityList.add(Priority.EXTREME);
        priorityList.add(Priority.HIGH);
        priorityList.add(Priority.MODERATE);
        priorityList.add(Priority.LOW);
    }

    public static Stage getPrimaryStage(){
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
