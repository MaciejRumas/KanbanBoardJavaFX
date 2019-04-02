package CanbanBoard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class editItemPopupController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private  TextField titleTextField;

    @FXML
    private ComboBox<Priority> priorityComboBox;

    @FXML
    private TextArea descriptionBox;

        public void pressEditButton(ActionEvent event){
            Controller.getSelectedTask().setDescription(descriptionBox.getText());
            Controller.getSelectedTask().setTaskName(titleTextField.getText());
            Controller.getSelectedTask().setExpDate(datePicker.getValue());
            Controller.getSelectedTask().setPriority(priorityComboBox.getValue());
            Controller.getActualList().refresh();
            TaskContextMenu.getThirdStage().close();
        }

        public void pressCancelButton(ActionEvent actionEvent){
            TaskContextMenu.getThirdStage().close();
        }

        public void pressGetCurrentButton(ActionEvent event){
            titleTextField.setText(Controller.getSelectedTask().getTaskName());
            datePicker.setValue(Controller.getSelectedTask().getExpDate());
            descriptionBox.setText(Controller.getSelectedTask().getDescription());
            priorityComboBox.getSelectionModel().select(Controller.getSelectedTask().getPriority());
        }

}