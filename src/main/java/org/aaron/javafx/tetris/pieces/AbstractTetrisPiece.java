package org.aaron.javafx.tetris.pieces;

import java.util.List;

import javafx.scene.paint.Color;

import org.aaron.javafx.tetris.TetrisCoordinate;

public abstract class AbstractTetrisPiece {

	private final TetrisCoordinate centerCoordinate;

	private final Color color;

	private final int orientation;

	public AbstractTetrisPiece(TetrisCoordinate centerCoordinate, Color color,
			int orientation) {
		this.centerCoordinate = centerCoordinate;
		this.color = color;
		this.orientation = orientation;
	}

	public TetrisCoordinate getCenterCoordinate() {
		return centerCoordinate;
	}

	public int getCenterRow() {
		return centerCoordinate.getRow();
	}

	public int getCenterColumn() {
		return centerCoordinate.getColumn();
	}

	public Color getColor() {
		return color;
	}

	public abstract List<TetrisCoordinate> getCoordinates();

	public int getOrientation() {
		return orientation;
	}

	public abstract int getNumOrientations();

	public int getNextOrientation() {
		return ((getOrientation() + 1) % getNumOrientations());
	}

	public abstract AbstractTetrisPiece makeTetrisPiece(
			TetrisCoordinate centerCoordinate, int orientation);

	public AbstractTetrisPiece cloneWithNewCenterCoordinate(
			TetrisCoordinate newCenterCoordinate) {
		return makeTetrisPiece(newCenterCoordinate, getOrientation());
	}

	public AbstractTetrisPiece cloneWithNewCenterRow(int newCenterRow) {
		return cloneWithNewCenterCoordinate(TetrisCoordinate.of(newCenterRow,
				getCenterColumn()));
	}

	public AbstractTetrisPiece cloneWithNewCenterColumn(int newCenterColumn) {
		return cloneWithNewCenterCoordinate(TetrisCoordinate.of(getCenterRow(),
				newCenterColumn));
	}

}
