package ATMServer.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class exitWarningCtr {
    @FXML
    private Button Btn_back;
    @FXML
    private Button Btn_exit;

    public void back(){
        Stage stage=(Stage)Btn_back.getScene().getWindow();
        stage.close();
    }
    public void exit(){
        System.exit(0);
    }
}
