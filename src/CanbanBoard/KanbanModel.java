package CanbanBoard;



public class KanbanModel {

    private static KanbanModel kanbanModel = new KanbanModel();

    public static KanbanModel getInstance(){
        return kanbanModel;
    }

    private KanbanModel(){

    }

}
