package org.aaron.javafx.tetris;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

class TetrisScorePane implements TetrisModelListener {

	private final VBox vbox = new VBox();

	private final TetrisModel tetrisModel;

	private final Text text;

	public TetrisScorePane(TetrisModel tetrisModel) {
		this.tetrisModel = tetrisModel;

		text = new Text();
		text.setFill(Color.RED);
		text.setFont(Font.font(null, FontWeight.BOLD, 20));

		vbox.setStyle("-fx-background-color: black;");
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(text);

		tetrisModel.registerListener(this);
		handleTetrisModelUpdated(
				CurrentPieceUpdatedStatus.CURRENT_PIECE_UPDATED,
				StackCellsUpdatedStatus.STACK_CELLS_UPDATED);
	}

	public Pane getPane() {
		return vbox;
	}

	@Override
	public void handleTetrisModelUpdated(
			CurrentPieceUpdatedStatus currentPieceUpdatedStatus,
			StackCellsUpdatedStatus stackCellsUpdatedStatus) {
		if (tetrisModel.isGameOver()) {
			text.setText("Game Over Lines: " + tetrisModel.getNumLines());
		} else if (tetrisModel.isPaused()) {
			text.setText("Paused Lines: " + tetrisModel.getNumLines());
		} else {
			text.setText("Lines: " + tetrisModel.getNumLines());
		}
	}
}
