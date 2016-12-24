package engineTester;

import Multiplayer.Multiplayer;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StartGameMenuController {

    @FXML
    private CheckBox host;
    @FXML
    private TextField adress;

    @FXML
    public void onClickSingleplayer() {

        Stage stage = (Stage) host.getScene().getWindow();
        stage.close();
        SinglePlayer singleplayer = new SinglePlayer();
        singleplayer.Start();
    }

    @FXML
    public void onClickMultiplayer() {
        Stage stage = (Stage) host.getScene().getWindow();
        stage.close();
        new Multiplayer(host.isSelected(), adress.getText());
    }

    @FXML
    public void onClickExit() {
        Stage stage = (Stage) host.getScene().getWindow();
        stage.close();
    }
}
