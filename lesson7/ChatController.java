package ru.geekbrains;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import network.ChatMessageService;
import network.MessageProcessor;
import network.MessageService;
import ru.geekbrains.messages.MessageDTO;
import ru.geekbrains.messages.MessageType;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable, MessageProcessor {
    @FXML
    public TextArea chatArea;
    @FXML
    public ListView onlineUsers;
    @FXML
    public Button btnSendMessage;
    @FXML
    public TextField input;
    @FXML
    public HBox chatBox;
    @FXML
    public HBox inputBox;
    @FXML
    public MenuBar menuBar;
    @FXML
    public HBox authPanel;
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passField;

    private MessageService messageService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageService = new ChatMessageService("localhost", 65500, this);
        onlineUsers.setItems(FXCollections.observableArrayList("Все", "Вася", "Жанна", "Петя"));     // будем получать от Сервера
        onlineUsers.getSelectionModel().selectFirst();
    }

    public void showHelp(ActionEvent actionEvent) throws URISyntaxException, IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(new URI("https://docs.google.com/document/d/1wr0YEtIc5yZtKFu-KITqYnBtp8KC28v2FEYUANL0YAM/edit#"));
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }


    /**
     * Просто залипуха, можно будет что-то навесить
     * @param actionEvent
     */
    public void mockAction(ActionEvent actionEvent) {

    }

    public void pressEnter(ActionEvent actionEvent) {
        sendMessage();
    }

    public void btnSend(ActionEvent actionEvent) {
        sendMessage();
    }

    private void sendMessage() {
        String msg = input.getText();
        if (msg.length() > 0) {
            MessageDTO dto = new MessageDTO();
            String toUser = (String) onlineUsers.getSelectionModel().getSelectedItem();
            if (toUser.equals("Все")) {
                dto.setMessageType(MessageType.PUBLIC_MESSAGE);
            } else {
                dto.setMessageType(MessageType.PRIVATE_MESSAGE);
                dto.setTo(toUser);
                onlineUsers.getSelectionModel().selectFirst();
            }
            dto.setBody(msg);
            messageService.sendMessage(dto.convertToJson());
            input.clear();
        }
        onlineUsers.getSelectionModel().selectFirst();
    }

    private void showBroadcastMessage(String message) {
        chatArea.appendText(message + System.lineSeparator());
    }

    @Override
    public void processMessage(String msg) {
        MessageDTO dto = MessageDTO.convertFromJson(msg);
        System.out.println("Received message: " + dto.getMessageType());
        switch (dto.getMessageType()) {
            case PUBLIC_MESSAGE:
                showBroadcastMessage(dto.getFrom() + " " + dto.getBody());
                break;
            case PRIVATE_MESSAGE:
                showBroadcastMessage(dto.getFrom() + " " + dto.getBody());
                break;
            case AUTH_CONFIRM:
                authPanel.setVisible(false);
                chatBox.setVisible(true);
                inputBox.setVisible(true);
                menuBar.setVisible(true);
                break;
            case ERROR_MESSAGE:
                showError(dto);
                break;
        }
    }

    public void sendAuth(ActionEvent actionEvent) {
        String log = loginField.getText();
        String pass = passField.getText();
        if (log.equals("") || pass.equals("")) return;
        MessageDTO dto = new MessageDTO();
        dto.setLogin(log);
        dto.setPassword(pass);
        dto.setMessageType(MessageType.SEND_AUTH_MESSAGE);
        messageService.sendMessage(dto.convertToJson());
        System.out.println("Sent " + log + " " + pass);
    }

    private void showError(Exception e) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Something went wrong!");
//        alert.setHeaderText(e.getMessage());
//        VBox dialog = new VBox();
//        Label label = new Label("Trace:");
//        TextArea textArea = new TextArea();
//        //TODO
//        textArea.setText(e.getStackTrace()[0].toString());
//        dialog.getChildren().addAll(label, textArea);
//        alert.getDialogPane().setContent(dialog);
//        alert.showAndWait();
    }

    private void showError(MessageDTO dto) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Something went wrong!");
//        alert.setHeaderText(dto.getMessageType().toString());
//        VBox dialog = new VBox();
//        Label label = new Label("Trace:");
//        TextArea textArea = new TextArea();
//        //TODO
//        textArea.setText(dto.getBody());
//        dialog.getChildren().addAll(label, textArea);
//        alert.getDialogPane().setContent(dialog);
//        alert.showAndWait();
    }
}
