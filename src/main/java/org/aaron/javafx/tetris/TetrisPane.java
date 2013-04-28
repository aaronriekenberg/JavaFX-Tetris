package org.aaron.javafx.tetris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;

public class TetrisPane implements TetrisModelListener {

	private final HashMap<TetrisCoordinate, TetrisPaneCellPixelCoordinates> tetrisCoordinatesToPixelCoordinate = new HashMap<>();

	private final ArrayList<Rectangle> currentPieceRectangles = new ArrayList<>();

	private final ArrayList<Rectangle> stackCellRectangles = new ArrayList<>();

	private final Pane pane = new Pane();

	private final TetrisModel tetrisModel;

	public TetrisPane(TetrisModel tetrisModel) {
		this.tetrisModel = tetrisModel;

		pane.setStyle("-fx-background-color: black;");

		final ChangeListener<Number> paneSizeChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				updatePixelCoordinates();
			}
		};

		pane.widthProperty().addListener(paneSizeChangeListener);
		pane.heightProperty().addListener(paneSizeChangeListener);

		tetrisModel.registerListener(this);

		updatePixelCoordinates();
	}

	public Pane getPane() {
		return pane;
	}

	private void updatePixelCoordinates() {
		tetrisCoordinatesToPixelCoordinate.clear();

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
				tetrisCoordinatesToPixelCoordinate.put(TetrisCoordinate.of(row,
						column), new TetrisPaneCellPixelCoordinates(
						currentXCoordinate, currentYCoordinate, width,
						rowHeight));
				currentXCoordinate += width;
			}
			currentYCoordinate += rowHeight;
		}

		rebuildCurrentPieceRectangles();
		rebuildStackCellRectangles();
	}

	private void rebuildCurrentPieceRectangles() {
		pane.getChildren().removeAll(currentPieceRectangles);
		currentPieceRectangles.clear();

		for (Map.Entry<TetrisCoordinate, Color> entry : tetrisModel
				.getDrawableCurrentPieceCells().entrySet()) {
			final TetrisCoordinate tetrisCoordinate = entry.getKey();
			final Color color = entry.getValue();
			final TetrisPaneCellPixelCoordinates pixelCoordinates = tetrisCoordinatesToPixelCoordinate
					.get(tetrisCoordinate);
			final Rectangle rectangle = RectangleBuilder.create()
					.x(pixelCoordinates.getX()).y(pixelCoordinates.getY())
					.width(pixelCoordinates.getWidth())
					.height(pixelCoordinates.getHeight()).fill(color)
					.stroke(Color.BLACK).strokeWidth(1).build();
			currentPieceRectangles.add(rectangle);
			pane.getChildren().add(rectangle);
		}
	}

	private void rebuildStackCellRectangles() {
		pane.getChildren().removeAll(stackCellRectangles);
		stackCellRectangles.clear();

		for (Map.Entry<TetrisCoordinate, Color> entry : tetrisModel
				.getDrawableStackCells().entrySet()) {
			final TetrisCoordinate tetrisCoordinate = entry.getKey();
			final Color color = entry.getValue();
			final TetrisPaneCellPixelCoordinates pixelCoordinates = tetrisCoordinatesToPixelCoordinate
					.get(tetrisCoordinate);
			final Rectangle rectangle = RectangleBuilder.create()
					.x(pixelCoordinates.getX()).y(pixelCoordinates.getY())
					.width(pixelCoordinates.getWidth())
					.height(pixelCoordinates.getHeight()).fill(color)
					.stroke(Color.BLACK).strokeWidth(1).build();
			stackCellRectangles.add(rectangle);
			pane.getChildren().add(rectangle);
		}
	}

	@Override
	public void handleTetrisModelUpdated(
			CurrentPieceUpdatedStatus currentPieceUpdatedStatus,
			StackCellsUpdatedStatus stackCellsUpdatedStatus) {
		if (currentPieceUpdatedStatus == CurrentPieceUpdatedStatus.CURRENT_PIECE_UPDATED) {
			rebuildCurrentPieceRectangles();
		}
		if (stackCellsUpdatedStatus == StackCellsUpdatedStatus.STACK_CELLS_UPDATED) {
			rebuildStackCellRectangles();
		}
	}

}
