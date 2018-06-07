package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import monitoringsystemturbo.model.app.Application;

import java.io.File;

public class AddApplicationPresenter {

    @FXML
    private Button addButton;
    private Stage primaryStage;
    @FXML
    private TextField nameApplication;
    @FXML
    private TextField fullPathApplication;
    private Application application = null;


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void onAdd() {
        if (isPathCorrect()) {
            application = new Application(nameApplication.getText(), fullPathApplication.getText());
            primaryStage.close();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Path Error");
            errorAlert.setContentText("Incorrect path!");
            errorAlert.showAndWait();
        }
    }

    private boolean isPathCorrect() {
        String path = fullPathApplication.getText();
        File file = new File(path);
        return file.exists() && path.endsWith(".exe");
    }

    @FXML
    public void onFind() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select EXE file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("EXE Files", "*.exe"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            nameApplication.setText(FilenameUtils.getBaseName(selectedFile.getName()));
            fullPathApplication.setText(selectedFile.getAbsolutePath());
            addButton.setDisable(false);
        }
    }

    public void onCancel() {
        primaryStage.close();
    }

    public Application getApplication() {
        return application;
    }
}
