package org.aaron.javafx.tetris;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;

public class TetrisPane {

	private final ArrayList<ArrayList<Rectangle>> tetrisCoordinateToRectangle = new ArrayList<>();

	private final Pane pane;

	public TetrisPane() {
		pane = new Pane();

		for (int row = 0; row < TetrisConstants.NUM_ROWS; ++row) {
			tetrisCoordinateToRectangle.add(new ArrayList<Rectangle>());
			for (int column = 0; column < TetrisConstants.NUM_COLUMNS; ++column) {
				final Rectangle rectangle = RectangleBuilder.create()
						.fill(Color.RED).stroke(Color.BLACK).strokeWidth(1)
						.build();
				tetrisCoordinateToRectangle.get(row).add(rectangle);
				pane.getChildren().add(rectangle);
			}
		}

		final ChangeListener<Number> paneSizeChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				paneSizeChanged();
			}
		};

		pane.widthProperty().addListener(paneSizeChangeListener);
		pane.heightProperty().addListener(paneSizeChangeListener);

		paneSizeChanged();
	}

	public Pane getPane() {
		return pane;
	}

	private void paneSizeChanged() {
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
				final Rectangle rectangle = tetrisCoordinateToRectangle
						.get(row).get(column);
				rectangle.setX(currentXCoordinate);
				rectangle.setY(currentYCoordinate);
				rectangle.setWidth(width);
				rectangle.setHeight(rowHeight);
				currentXCoordinate += width;
			}
			currentYCoordinate += rowHeight;
		}
	}
}
