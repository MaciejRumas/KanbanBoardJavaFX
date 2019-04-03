package CanbanBoard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class addItemPopupController {
    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextArea descriptionBox;

    public void pressAddButton(ActionEvent actionEvent){

        if(titleTextField.getText().isEmpty() ||  datePicker.getValue() == null || descriptionBox.getText().isEmpty()){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Some values are missing!");
            error.setHeaderText("Error");
            error.setTitle("Error");
            error.show();
        }
        else{
            Task newTask = new Task(titleTextField.getText(),datePicker.getValue(), Main.getComboBox().getValue(), descriptionBox.getText());
            Controller.getToDoObservableList().add(newTask);
            Controller.getToDoListView().setItems(Controller.getToDoObservableList());
            Controller.getSecondaryStage().close();
        }
    }

    public void pressCancelButton(ActionEvent actionEvent){
        Controller.getSecondaryStage().close();
    }
}
