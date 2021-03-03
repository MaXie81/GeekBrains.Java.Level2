import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class ControlWinChat implements Initializable {
    @FXML
    public MenuItem mnBtnConnOpen;
    @FXML
    public MenuItem mnBtnConnClose;
    @FXML
    public MenuItem mnBtnSettings;
    @FXML
    public Menu mnFormat;
    @FXML
    public Menu mnStatus;
    @FXML
    public ToggleGroup grpFormat;
    @FXML
    public ToggleGroup grpStatus;
    @FXML
    public TextArea txtAreaChat;
    @FXML
    public ListView lstMember;
    @FXML
    public HBox boxSend;
    @FXML
    public TextField txtMessage;
    @FXML
    public Label lblConnect;
    @FXML
    public Label lblFormat;
    @FXML
    public Label lblStatus;

    final private String[] ARR_MEMBER = {
            "Аня Вечер", "Glock", "Сфера_31", "Дрон", "Лучший", "Женя-сказка", "Проходил_мимо", "Dj Rule*",
            "Катя Star", "Вася Бык", "Сны рядом", "Саша-праздник", "Бывший", "Монополия", "СестраТаланта",
            "Катька Ворошина", "Даня-босс", "Зона UFO", "М-радио", "Котичка", "Солнце в бокале", "Чума", "Зая"
    };
    private RadioMenuItem rmi;
    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private MultipleSelectionModel selectModel;

    private String nickName = "SuperStar";
    // набор внутренних переменных
    private boolean dataConnect;
    private String dataFormat;
    private List<String> arrMember = new ArrayList<>();
    // набор внешних переменных, которые будут отправляться "во вне"(на сервер)
    private String dataStatus;
    private String message;
    private List<String> arrRecipient = new ArrayList<>();

    // "внешние" процедуры
    private void extConnection(boolean flgOpen) {                   // открыть/закрыть соединение с чат-сервером
        setConnect(flgOpen);
        extArrMember();
    }
    private void extArrMember() {                                   // получить с чат-сервера список доступных Участников для общения
        setArrMember(ARR_MEMBER);
        set();
    }
    private void extSend() {
        if (txtMessage.getText().length() == 0) return;
        setArrRecipient();
        setMessage();
        txtAreaChat.appendText(formater.format(new Date()) + " " + nickName + ": " + message + "\n");
        set();
    }

    // "сеттеры"
    private void setConnect(boolean flgConn) {
        dataConnect = flgConn;
        if (dataConnect) txtAreaChat.clear();
    }
    private void setArrMember(String[] arr) {
        if (dataConnect) {                                          // если соединение с чат-сервером установлено, то ...
            arrMember.clear();
            arrMember.addAll(Arrays.asList(arr));                   // пока так ...
            lstMember.setItems(FXCollections.observableArrayList(arrMember));
        } else {
            arrMember.clear();
            lstMember.setItems(FXCollections.observableArrayList(arrMember));
        }
    }
    private void setMessage() {
        if (txtMessage.getText().length() == 0) return;
        message = txtMessage.getText();
    }
    private void setArrRecipient() {
        arrRecipient.clear();
        if (dataFormat.equals("общий"))
            arrRecipient.addAll(arrMember);
        else
            arrRecipient.addAll(selectModel.getSelectedItems());
    }
    private void set() {
        getFormat();
        getStatus();
        refresh();
    }
    private void getFormat() {
        if (dataConnect) {
            rmi = (RadioMenuItem) grpFormat.getSelectedToggle();
            dataFormat = rmi.getText();
        } else {
            dataFormat = "-";
        }
    }
    private void getStatus() {
        if (dataConnect) {
            rmi = (RadioMenuItem) grpStatus.getSelectedToggle();
            dataStatus = rmi.getText();
        } else {
            dataStatus = "-";
        }
    }

    //
    private void refreshArrMember() {
        lstMember.setOpacity(dataConnect ? 1.0 : 0.5);

        if (dataFormat.equals("общий"))
            lstMember.setSelectionModel(new NoSelectionModel());
        else {
            if (dataFormat.equals("группа")) {
                selectModel.setSelectionMode(SelectionMode.MULTIPLE);
            } else {
                selectModel.setSelectionMode(SelectionMode.SINGLE);
            }
            lstMember.setSelectionModel(selectModel);
        }
    }
    private void refresh() {
        refreshMenu();
        refreshChat();
        refreshArrMember();
        refreshMessage();
        refreshConnect();
        refreshFormat();
        refreshStatus();
    }
    private void refreshMenu() {
        if (dataConnect) {
            mnBtnConnOpen.setDisable(true);
            mnBtnConnClose.setDisable(false);
            mnBtnSettings.setDisable(true);
            mnFormat.setDisable(false);
            mnStatus.setDisable(false);
        } else {
            mnBtnConnOpen.setDisable(false);
            mnBtnConnClose.setDisable(true);
            mnBtnSettings.setDisable(false);
            mnFormat.setDisable(true);
            mnStatus.setDisable(true);
        }
    }
    private void refreshChat() {
        if (dataConnect) {
            txtAreaChat.setOpacity(1.0);
        } else {
            txtAreaChat.setOpacity(0.5);
        }
    }
    private void refreshMessage() {
        if (dataConnect) {
            boxSend.setDisable(false);
            txtMessage.clear();
            txtMessage.requestFocus();
        } else {
            boxSend.setDisable(true);
        }
    }
    private void refreshConnect() {
        if (dataConnect) {
            lblConnect.setText("открыто");
            lblConnect.setTextFill(Color.BLUE);
        } else {
            lblConnect.setText("закрыто");
            lblConnect.setTextFill(Color.RED);
        }
    }
    private void refreshFormat() {
        lblFormat.setText(dataFormat);
    }
    private void refreshStatus() {
        lblStatus.setText(dataStatus);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectModel = lstMember.getSelectionModel();
        extConnection(false);
    }

    public void doOpenConnection(ActionEvent actionEvent) {extConnection(true);}

    public void doCloseConnection(ActionEvent actionEvent) {extConnection(false);}

    public void doExit(ActionEvent actionEvent) {
        extConnection(false);
        System.exit(0);
    }

    public void doChangeFormat(ActionEvent actionEvent) {set();}

    public void doChangeStatus(ActionEvent actionEvent) {set();}

    public void doSend(ActionEvent actionEvent) {extSend();}

    public void doSendOnEnter(KeyEvent keyEvent) {if (keyEvent.getCode() == KeyCode.ENTER) extSend();}
}
