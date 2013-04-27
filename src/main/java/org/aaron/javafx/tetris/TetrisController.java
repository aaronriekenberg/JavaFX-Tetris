package org.aaron.javafx.tetris;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TetrisController extends Application {

	private final TetrisPane tetrisPane = new TetrisPane();

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("JavaFX Tetris");
		primaryStage.setScene(new Scene(tetrisPane.getPane(), 300, 550));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
