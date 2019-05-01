package CanbanBoard;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;

public class KanbanCallback implements Callback <ListView<Task>, ListCell<Task>>{

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
                Controller.setSelectedTask(list.getSelectionModel().getSelectedItem());
                Controller.setActualObservableList(list.getItems());
                Controller.setActualList(list);
                Controller.setSelectedTaskIndex(list.getSelectionModel().getSelectedIndex());
                if (event2.getButton() == MouseButton.SECONDARY) {
                    ContextMenu contextMenu = TaskContextMenu.createContextMenu();
                    cell.setContextMenu(contextMenu);
                    contextMenu.show(cell, event2.getScreenX(), event2.getScreenY());
                }
            }
        });
        return cell;
    }
}
