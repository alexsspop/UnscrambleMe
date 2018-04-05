package gui;

import javafx.fxml.FXML;

public class MainWindowController {
    @FXML protected void HandleButtonAction () {
        System.out.println("Pressed");
    }

    @FXML public void HandleMouseEntered() {
        System.out.println("Enter");
    }
}
