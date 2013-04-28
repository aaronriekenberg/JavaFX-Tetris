package org.aaron.javafx.tetris;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TetrisController extends Application {

	private final TetrisModel tetrisModel;

	private final TetrisPane tetrisPane;

	private final TetrisScorePane tetrisScorePane;

	public TetrisController() {
		tetrisModel = new TetrisModel();
		tetrisPane = new TetrisPane(tetrisModel);
		tetrisScorePane = new TetrisScorePane(tetrisModel);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("JavaFX Tetris");

		final BorderPane rootPane = new BorderPane();
		rootPane.setCenter(tetrisPane.getPane());
		rootPane.setBottom(tetrisScorePane.getPane());
		primaryStage.setScene(new Scene(rootPane, 300, 550));
		primaryStage.show();

		final Timeline timeline = new Timeline(new KeyFrame(
				Duration.millis(250), new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						tetrisModel.periodicUpdate();
					}
				}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();

		registerKeyboardHandler(tetrisPane.getPane());
	}

	private void registerKeyboardHandler(Pane pane) {
		pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				boolean consumed = false;

				switch (keyEvent.getCode()) {
				case DOWN:
					tetrisModel.moveCurrentPieceDown();
					consumed = true;
					break;
				case SPACE:
					tetrisModel.dropCurrentPiece();
					consumed = true;
					break;
				case LEFT:
					tetrisModel.moveCurrentPieceLeft();
					consumed = true;
					break;
				case RIGHT:
					tetrisModel.moveCurrentPieceRight();
					consumed = true;
					break;
				case UP:
					tetrisModel.rotateCurrentPiece();
					consumed = true;
					break;
				case P:
					tetrisModel.togglePause();
					consumed = true;
					break;
				case R:
					tetrisModel.reset();
					consumed = true;
					break;
				default:
					break;
				}

				if (consumed) {
					keyEvent.consume();
				}
			}
		});
		pane.setFocusTraversable(true);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
