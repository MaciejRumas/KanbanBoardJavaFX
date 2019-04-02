package CanbanBoard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

        Task newTask = new Task(titleTextField.getText(),datePicker.getValue(), Main.getComboBox().getValue(), descriptionBox.getText());

        Controller.getToDoObservableList().add(newTask);

        Controller.getToDoListView().setItems(Controller.getToDoObservableList());

        Controller.getSecondaryStage().close();
    }

    public void pressCancelButton(ActionEvent actionEvent){
        Controller.getSecondaryStage().close();
    }
}
