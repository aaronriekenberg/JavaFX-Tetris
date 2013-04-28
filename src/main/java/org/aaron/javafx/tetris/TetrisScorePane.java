package org.aaron.javafx.tetris;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class TetrisScorePane implements TetrisModelListener {

	private final HBox hbox = new HBox();

	private final TetrisModel tetrisModel;

	private final Text text;

	public TetrisScorePane(TetrisModel tetrisModel) {
		this.tetrisModel = tetrisModel;
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(text = new Text());

		tetrisModel.registerListener(this);
		handleTetrisModelUpdated();
	}

	public Pane getPane() {
		return hbox;
	}

	@Override
	public void handleTetrisModelUpdated() {
		if (tetrisModel.isPaused()) {
			text.setText("Paused Lines: " + tetrisModel.getNumLines());
		} else {
			text.setText("Lines: " + tetrisModel.getNumLines());
		}
	}
}
