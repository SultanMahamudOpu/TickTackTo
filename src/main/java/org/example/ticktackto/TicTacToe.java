package org.example.ticktackto;

import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TicTacToe extends Application {

    private Button[][] buttons = new Button[3][3];
    private boolean playerX = true;
    private Label statusLabel = new Label("Player X's turn");
    private boolean gameEnded = false;

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = createButton();
                grid.add(buttons[i][j], j, i);
            }
        }

        VBox vbox = new VBox(20, statusLabel, grid);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #4ca1af);");

        statusLabel.setFont(new Font("Arial", 24));
        statusLabel.setTextFill(Color.WHITE);
        statusLabel.setEffect(new DropShadow(5, Color.BLACK));
        statusLabel.setStyle("-fx-background-color: linear-gradient(to right, #ff7f50, #ff6f61); -fx-background-radius: 5; -fx-padding: 10;");

        Scene scene = new Scene(vbox, 450, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.show();
    }

    private Button createButton() {
        Button button = new Button();
        button.setMinSize(100, 100);
        button.setStyle("-fx-font-size: 24; -fx-background-color: linear-gradient(to bottom right, #66b2ff, #004080);"
                + "-fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #2c3e50; -fx-border-width: 2px;");
        button.setEffect(new DropShadow(5, Color.BLACK));

        button.setOnMouseEntered(e -> button.setEffect(new Glow(0.5)));
        button.setOnMouseExited(e -> button.setEffect(new DropShadow(5, Color.BLACK)));

        button.setOnAction(e -> {
            if (!gameEnded && button.getText().equals("")) {
                button.setText(playerX ? "X" : "O");
                button.setStyle("-fx-font-size: 24; -fx-background-color: " + (playerX ? "linear-gradient(to bottom right, #6699ff, #003366);" : "linear-gradient(to bottom right, #66cc99, #006633);")
                        + "-fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #2c3e50; -fx-border-width: 2px;");

                // Animation on click
                ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
                st.setToX(1.1);
                st.setToY(1.1);
                st.setAutoReverse(true);
                st.setCycleCount(2);
                st.play();

                playerX = !playerX;
                statusLabel.setText("Player " + (playerX ? "X" : "O") + "'s turn");
                checkWin();
            }
        });

        return button;
    }

    private void checkWin() {
        String winner = null;

        // Check rows
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(buttons[i][1].getText()) &&
                    buttons[i][1].getText().equals(buttons[i][2].getText()) &&
                    !buttons[i][0].getText().equals("")) {
                winner = buttons[i][0].getText();
                highlightWinningCombo(buttons[i][0], buttons[i][1], buttons[i][2]);
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (buttons[0][j].getText().equals(buttons[1][j].getText()) &&
                    buttons[1][j].getText().equals(buttons[2][j].getText()) &&
                    !buttons[0][j].getText().equals("")) {
                winner = buttons[0][j].getText();
                highlightWinningCombo(buttons[0][j], buttons[1][j], buttons[2][j]);
            }
        }

        // Check diagonals
        if (buttons[0][0].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][2].getText()) &&
                !buttons[0][0].getText().equals("")) {
            winner = buttons[0][0].getText();
            highlightWinningCombo(buttons[0][0], buttons[1][1], buttons[2][2]);
        }

        if (buttons[0][2].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][0].getText()) &&
                !buttons[0][2].getText().equals("")) {
            winner = buttons[0][2].getText();
            highlightWinningCombo(buttons[0][2], buttons[1][1], buttons[2][0]);
        }

        // If we have a winner
        if (winner != null) {
            gameEnded = true;
            statusLabel.setText("Player " + winner + " wins!");
            showEndGameAlert("Player " + winner + " wins!");
        } else if (isBoardFull()) {
            gameEnded = true;
            statusLabel.setText("It's a draw!");
            showEndGameAlert("It's a draw!");
        }
    }

    private boolean isBoardFull() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                if (button.getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void highlightWinningCombo(Button... winningButtons) {
        for (Button button : winningButtons) {
            button.setStyle("-fx-background-color: linear-gradient(to bottom right, yellow, gold); -fx-font-size: 24; -fx-text-fill: black; -fx-background-radius: 10;");
            button.setEffect(new Glow(0.8));
        }
    }

    private void showEndGameAlert(String message) {
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
            resetBoard();
        });
        pause.play();
    }

    private void resetBoard() {
        playerX = true;
        statusLabel.setText("Player X's turn");
        gameEnded = false;

        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setText("");
                button.setStyle("-fx-font-size: 24; -fx-background-color: linear-gradient(to bottom right, #66b2ff, #004080);"
                        + "-fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #2c3e50; -fx-border-width: 2px;");
                button.setEffect(new DropShadow(5, Color.BLACK));
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
