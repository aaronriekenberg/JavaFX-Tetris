package org.aaron.javafx.tetris;

import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;

public class TetrisPane implements TetrisModelListener {

	private final Pane pane = new Pane();

	private final TetrisModel tetrisModel;

	public TetrisPane(TetrisModel tetrisModel) {
		this.tetrisModel = tetrisModel;

		pane.setStyle("-fx-background-color: black;");

		final ChangeListener<Number> paneSizeChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				rebuildRectangles();
			}
		};

		pane.widthProperty().addListener(paneSizeChangeListener);
		pane.heightProperty().addListener(paneSizeChangeListener);

		tetrisModel.registerListener(this);
		rebuildRectangles();
	}

	public Pane getPane() {
		return pane;
	}

	private void rebuildRectangles() {
		pane.getChildren().clear();

		final Map<TetrisCoordinate, Color> drawableCells = tetrisModel
				.getDrawableCells();

		final int paneWidth = pane.widthProperty().intValue();
		final int paneHeight = pane.heightProperty().intValue();
		final int normalWidthPixels = (paneWidth - 1)
				/ TetrisConstants.NUM_COLUMNS;
		final int extraWidthPixels = (paneWidth - 1)
				% TetrisConstants.NUM_COLUMNS;
		final int normalHeightPixels = (paneHeight - 1)
				/ TetrisConstants.NUM_ROWS;
		final int extraHeightPixels = (paneHeight - 1)
				% TetrisConstants.NUM_ROWS;

		int currentYCoordinate = 0;
		for (int row = 0; row < TetrisConstants.NUM_ROWS; ++row) {
			final int rowHeight;
			if (row < extraHeightPixels) {
				rowHeight = normalHeightPixels + 1;
			} else {
				rowHeight = normalHeightPixels;
			}
			int currentXCoordinate = 0;
			for (int column = 0; column < TetrisConstants.NUM_COLUMNS; ++column) {
				final int width;
				if (column < extraWidthPixels) {
					width = normalWidthPixels + 1;
				} else {
					width = normalWidthPixels;
				}
				final TetrisCoordinate tetrisCoordinate = TetrisCoordinate.of(
						row, column);
				final Color color = drawableCells.get(tetrisCoordinate);
				if (color != null) {
					final Rectangle rectangle = RectangleBuilder.create()
							.x(currentXCoordinate).y(currentYCoordinate)
							.width(width).height(rowHeight).fill(color)
							.stroke(Color.BLACK).strokeWidth(1).build();
					pane.getChildren().add(rectangle);
				}
				currentXCoordinate += width;
			}
			currentYCoordinate += rowHeight;
		}
	}

	@Override
	public void handleTetrisModelUpdated() {
		rebuildRectangles();
	}
}
