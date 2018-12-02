package client;

import com.sun.glass.ui.View;
import common.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    private String clientFolder ="Files/Client/";

    @FXML
    TextField tfFileName;
    @FXML
    Button login,dld,uld;

    @FXML
    ListView<String> clientFileList;

    @FXML
    ListView<String> serverFileList;

    @FXML
    TextField loginField;
    @FXML
    PasswordField passField;
    @FXML
    Label auth;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Network.start();

        Thread t = new Thread(() -> {

            try {
                while (true) {
                    AbstractMessage am = Network.readObject();
                    if (am instanceof FileMessage) {
                        FileMessage fm = (FileMessage) am;
                        Files.write(Paths.get("Files/Client/" + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
                        refreshLocalFilesList();
                    }
                    if (am instanceof FileListMessage) {
                        refreshServerFile(((FileListMessage)am).getFiles());
                    }
                    if (am instanceof CommandMessage ) {
                        CommandMessage cm=(CommandMessage)am;

                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            } finally {
                Network.stop();
            }
        });
        t.setDaemon(true);
        t.start();
        clientFileList.setItems(FXCollections.observableArrayList());
        refreshLocalFilesList();
        clientFileList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        tfFileName.setText(newValue);
                        dld.setDisable(true);
                        login.setDisable(true);
                        uld.setDisable(false);
                    }
                }
        );
        serverFileList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        tfFileName.setText(newValue);
                        dld.setDisable(false);
                        login.setDisable(true);
                        uld.setDisable(true);
                    }
                }
        );
    }

    public void pressOnDownloadBtn(ActionEvent actionEvent) {
        if (tfFileName.getLength() > 0) {
            Network.sendMsg(new FileRequest(tfFileName.getText()));
            tfFileName.clear();
        }
    }

    public void pressOnLoginBtn(ActionEvent actionEvent){
        Network.sendMsg(new AuthMessage(loginField.getText(),passField.getText()));
        return;
    }

    public void pressOnUploadBtn(ActionEvent actionEvent) throws Exception{
        Network.sendMsg(new FileMessage(Paths.get(clientFolder+tfFileName.getText())));
        Network.sendMsg(new CommandMessage(CommandMessage.FILE_LIST));
    }

    public void refreshLocalFilesList() {
        if (Platform.isFxApplicationThread()) {
            try {
                clientFileList.getItems().clear();
                Files.list(Paths.get("Files/Client/")).map(p -> p.getFileName().toString()).forEach(o -> clientFileList.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Platform.runLater(() -> {
                try {
                    clientFileList.getItems().clear();
                    Files.list(Paths.get("Files/Client/")).map(p -> p.getFileName().toString()).forEach(o -> clientFileList.getItems().add(o));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    public void refreshServerFile(List<String> files) {
        Platform.runLater(() -> {
            ObservableList<String> items = FXCollections.observableArrayList(files);
            serverFileList.setItems(items);
        });
    }

}
