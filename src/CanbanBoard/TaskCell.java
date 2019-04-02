package CanbanBoard;

import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TaskCell extends ListCell<Task> {

    public String getCellDescription() {
        return cellDescription;
    }

    private String cellDescription;

    @Override
    public void updateItem(Task item, boolean empty){
        super.updateItem(item,empty);

        Circle circle = new Circle(7);
        if (item != null) {
            cellDescription = item.getTaskDescription();

            switch(item.getPriority()){
                case HIGH:
                    circle.setFill(Color.RED);
                    setGraphic(circle);
                    setText(item.toString());
                    break;

                case EXTREME:
                    circle.setFill(Color.MAROON);
                    setGraphic(circle);
                    setText(item.toString());
                    break;

                case MODERATE:
                    circle.setFill(Color.YELLOW);
                    setGraphic(circle);
                    setText(item.toString());
                    break;

                case LOW:
                    circle.setFill(Color.GREEN);
                    setGraphic(circle);
                    setText(item.toString());
                    break;
            }
        }
        else{
            setGraphic(null);
            setText(null);
        }
    }

}
