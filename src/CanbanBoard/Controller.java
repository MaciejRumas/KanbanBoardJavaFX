package CanbanBoard;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Controller implements Initializable {

    @FXML
    private ListView<Task> toDoList;

    @FXML
    private ListView<Task> doingList;

    @FXML
    private ListView<Task> doneList;

    private static Scene addScene;
    private static FXMLLoader fxmlLoader;
    private static Stage secondaryStage = new Stage();
    private static ObservableList<Task> toDoObservableList = FXCollections.observableArrayList();
    private static ObservableList<Task> doingObservableList = FXCollections.observableArrayList();
    private static ObservableList<Task> doneObservableList = FXCollections.observableArrayList();
    private static ObservableList<Task> actualObservableList = FXCollections.observableArrayList();
    private static ListView<Task> toDoListView;
    private static ListView<Task> doingListView;
    private static ListView<Task> doneListView;
    private static ListView<Task> actualList;
    private Callback<ListView<Task>, ListCell<Task>> callback = new KanbanCallback();

    public static void setActualObservableList(ObservableList<Task> actualObservableList) {
        Controller.actualObservableList = actualObservableList;
    }

    public static void setActualList(ListView<Task> actualList) {
        Controller.actualList = actualList;
    }

    public static void setSelectedTask(Task selectedTask) {
        Controller.selectedTask = selectedTask;
    }

    public static void setSelectedTaskIndex(int selectedTaskIndex) {
        Controller.selectedTaskIndex = selectedTaskIndex;
    }

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

    public static Scene getAddScene() {
        return addScene;
    }

    public static Stage getSecondaryStage() {
        return secondaryStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toDoListView = toDoList;
        doingListView = doingList;
        doneListView = doneList;

        toDoListView.setCellFactory(callback);
        doingListView.setCellFactory(callback);
        doneListView.setCellFactory(callback);
    }

    public void pressAddTaskButton(ActionEvent event) throws IOException {

        try {
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("addItemPopup.fxml"));
            addScene = new Scene(fxmlLoader.load());

            Controller.getSecondaryStage().setTitle("Add new task");
            Controller.getSecondaryStage().setScene(addScene);
            Controller.getSecondaryStage().setResizable(false);
            Controller.getSecondaryStage().getIcons().add(new Image("/CanbanBoard/img/add.png"));
            Controller.getSecondaryStage().show();

            Main.setComboBox((ComboBox<Priority>) Controller.getAddScene().lookup("#priorityComboBox"));
            Main.getComboBox().setItems(Main.getPriorityList());
            Main.getComboBox().getSelectionModel().selectFirst();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

    public void pressCloseMenuItem(ActionEvent event) {
        Main.getPrimaryStage().close();
    }

    public void pressAboutMenuItem(ActionEvent event) {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setContentText("All rights reserved \u00a9 Maciej Rumas");
        about.setHeaderText("Copyrighting");
        about.setTitle("About");
        about.show();
    }

    public void pressSaveMenuItem(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BIN files: ", "*.bin"));
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showSaveDialog(Main.getPrimaryStage());

        if (file != null) {

            try {
                ArrayList<Task> toDo = new ArrayList<>(toDoObservableList);
                ArrayList<Task> doing = new ArrayList<>(doingObservableList);
                ArrayList<Task> done = new ArrayList<>(doneObservableList);

                ArrayList<ArrayList<Task>> connectedLists = new ArrayList<>();
                connectedLists.add(toDo);
                connectedLists.add(doing);
                connectedLists.add(done);

                // write object to file
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(connectedLists);
                oos.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void pressOpenMenuItem(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BIN files: ", "*.bin"));
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(Main.getPrimaryStage());

        if (file != null) {

            try {
                FileInputStream fis = new FileInputStream(file.getPath());
                ObjectInputStream ois = new ObjectInputStream(fis);

                ArrayList<ArrayList<Task>> connectedLists = (ArrayList<ArrayList<Task>>) ois.readObject();

                toDoObservableList = FXCollections.observableArrayList(connectedLists.get(0));
                doingObservableList = FXCollections.observableArrayList(connectedLists.get(1));
                doneObservableList = FXCollections.observableArrayList(connectedLists.get(2));

                refreshLists();
                fis.close();
                ois.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public void pressExportCsvMenuItem(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files: ", "*.csv"));
        fileChooser.setTitle("Export as csv");
        File file = fileChooser.showSaveDialog(Main.getPrimaryStage());

        if (file != null) {

            try {
                FileWriter fileWriter = new FileWriter(file);

                fileWriter.append("taskName");
                fileWriter.append(",");
                fileWriter.append("expDate");
                fileWriter.append(",");
                fileWriter.append("priority");
                fileWriter.append(",");
                fileWriter.append("description");
                fileWriter.append(",");
                fileWriter.append("list");
                fileWriter.append("\n");

                writeTasksFromListToCsv(fileWriter, toDoObservableList, "toDo");
                writeTasksFromListToCsv(fileWriter, doingObservableList, "doing");
                writeTasksFromListToCsv(fileWriter, doneObservableList, "done");

                fileWriter.flush();
                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeTasksFromListToCsv(FileWriter fileWriter, ObservableList<Task> taskList, String listName) throws IOException {
        for (Task i : taskList) {
            fileWriter.append(i.getTaskName());
            fileWriter.append(",");
            fileWriter.append(i.getExpDate().toString());
            fileWriter.append(",");
            fileWriter.append(i.getPriority().toString());
            fileWriter.append(",");
            fileWriter.append(i.getDescription());
            fileWriter.append(",");
            fileWriter.append(listName);
            fileWriter.append("\n");
        }
    }

    public void pressImportCsvMenuItem(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files: ", "*.csv"));
        fileChooser.setTitle("Import csv file");
        File file = fileChooser.showOpenDialog(Main.getPrimaryStage());

        if (file != null) {

            toDoObservableList.clear();
            doingObservableList.clear();
            doneObservableList.clear();

            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader csvReader = new BufferedReader(fileReader);

                String buffor = csvReader.readLine();
                String nextLine;
                while ((nextLine = csvReader.readLine()) != null) {
                    String[] line = nextLine.split(",");
                    Task task = new Task(line[0], LocalDate.parse(line[1]), Priority.fromString(line[2]), line[3]);
                    if (line[4].equals("toDo")) {
                        toDoObservableList.add(task);
                    } else if (line[4].equals("doing")) {
                        doingObservableList.add(task);
                    } else {
                        doneObservableList.add(task);
                    }
                }
                fileReader.close();
                refreshLists();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void pressExportJsonMenuItem(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files: ", "*.json"));
        fileChooser.setTitle("Export as json");
        File file = fileChooser.showSaveDialog(Main.getPrimaryStage());

        if (file != null) {

            JSONObject mainObject = new JSONObject();
            JSONArray toDoJsonArray = addTasksToJSONArray(toDoObservableList);
            JSONArray doingJsonArray = addTasksToJSONArray(doingObservableList);
            JSONArray doneJsonArray = addTasksToJSONArray(doneObservableList);

            mainObject.put("toDo", toDoJsonArray);
            mainObject.put("doing", doingJsonArray);
            mainObject.put("done", doneJsonArray);

            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(mainObject.toJSONString());
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private JSONArray addTasksToJSONArray(ObservableList<Task> taskList) {
        JSONArray jsonArray = new JSONArray();
        for (Task i : taskList) {
            JSONObject taskDetails = new JSONObject();
            taskDetails.put("taskName", i.getTaskName());
            taskDetails.put("expDate", i.getExpDate().toString());
            taskDetails.put("priority", i.getPriority().toString());
            taskDetails.put("description", i.getDescription());
            jsonArray.add(taskDetails);
        }
        return jsonArray;
    }

    public void pressImportJsonMenuItem(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files: ", "*.json"));
        fileChooser.setTitle("Import json file");
        File file = fileChooser.showOpenDialog(Main.getPrimaryStage());

        if (file != null) {
            try {
                JSONParser jsonParser = new JSONParser();
                FileReader fileReader = new FileReader(file);
                Object object = jsonParser.parse(fileReader);
                JSONObject mainObject = (JSONObject) object;

                JSONArray toDoJsonArray = (JSONArray) mainObject.get("toDo");
                JSONArray doingJsonArray = (JSONArray) mainObject.get("doing");
                JSONArray doneJsonArray = (JSONArray) mainObject.get("done");

                toDoObservableList.clear();
                doingObservableList.clear();
                doneObservableList.clear();

                toDoObservableList = addTasksToObservableList(toDoJsonArray);
                doingObservableList = addTasksToObservableList(doingJsonArray);
                doneObservableList = addTasksToObservableList(doneJsonArray);

                refreshLists();
                fileReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private ObservableList<Task> addTasksToObservableList(JSONArray jsonArray) {
        ObservableList<Task> observableList = FXCollections.observableArrayList();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String taskName = (String) jsonObject.get("taskName");
            String stringExpDate = (String) jsonObject.get("expDate");
            LocalDate expDate = LocalDate.parse(stringExpDate);
            String stringPriority = (String) jsonObject.get("priority");
            Priority priority = Priority.fromString(stringPriority);
            String description = (String) jsonObject.get("description");
            Task task = new Task(taskName, expDate, priority, description);
            observableList.add(task);
        }
        return observableList;
    }

    public void shiftFromDoingToToDo(ActionEvent event) {
        try {
            if (doingObservableList.size() > 0) {
                toDoObservableList.add(selectedTask);
                toDoList.setItems(toDoObservableList);
                doingObservableList.remove(selectedTaskIndex);
                doingListView.refresh();
            }
        } catch (Exception exception) {
            System.out.println("Exception thrown: " + exception.getMessage());
        }
    }

    public void shiftFromToDoToDoing(ActionEvent event) {
        try {
            if (toDoObservableList.size() > 0) {
                doingObservableList.add(selectedTask);
                doingList.setItems(doingObservableList);
                toDoObservableList.remove(selectedTaskIndex);
                toDoListView.refresh();
            }
        } catch (Exception exception) {
            System.out.println("Exception thrown: " + exception.getMessage());
        }
    }

    public void shiftFromDoingToDone(ActionEvent event) {
        try {
            if (doingObservableList.size() > 0) {
                doneObservableList.add(selectedTask);
                doneListView.setItems(doneObservableList);
                doingObservableList.remove(selectedTaskIndex);
                doingListView.refresh();
            }
        } catch (Exception exception) {
            System.out.println("Exception thrown: " + exception.getMessage());
        }
    }

    public void shiftFromDoneToDoing(ActionEvent event) {
        try {
            if (doneObservableList.size() > 0) {
                doingObservableList.add(selectedTask);
                doingListView.setItems(doingObservableList);
                doneObservableList.remove(selectedTaskIndex);
                doneListView.refresh();
            }
        } catch (Exception exception) {
            System.out.println("Exception thrown: " + exception.getMessage());
        }
    }

    private void refreshLists() {
        toDoList.setItems(toDoObservableList);
        doingListView.setItems(doingObservableList);
        doneListView.setItems(doneObservableList);

        toDoList.refresh();
        doingListView.refresh();
        doneListView.refresh();
    }

}
