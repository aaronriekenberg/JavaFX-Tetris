package org.aaron.javafx.tetris.pieces;

import javafx.scene.paint.Color;

import org.aaron.javafx.tetris.TetrisCoordinate;

public abstract class AbstractTetrisPiece implements TetrisPiece {

	private final TetrisCoordinate centerCoordinate;

	private final Color color;

	private final int orientation;

	public AbstractTetrisPiece(TetrisCoordinate centerCoordinate, Color color,
			int orientation) {
		this.centerCoordinate = centerCoordinate;
		this.color = color;
		this.orientation = orientation;
	}

	@Override
	public TetrisCoordinate getCenterCoordinate() {
		return centerCoordinate;
	}

	@Override
	public int getCenterRow() {
		return centerCoordinate.getRow();
	}

	@Override
	public int getCenterColumn() {
		return centerCoordinate.getColumn();
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public int getOrientation() {
		return orientation;
	}

	@Override
	public int getNextOrientation() {
		return ((getOrientation() + 1) % getNumOrientations());
	}

	@Override
	public TetrisPiece cloneWithNewCenterCoordinate(
			TetrisCoordinate newCenterCoordinate) {
		return makeTetrisPiece(newCenterCoordinate, getOrientation());
	}

	@Override
	public TetrisPiece cloneWithNewCenterRow(int newCenterRow) {
		return cloneWithNewCenterCoordinate(TetrisCoordinate.of(newCenterRow,
				getCenterColumn()));
	}

	@Override
	public TetrisPiece cloneWithNewCenterColumn(int newCenterColumn) {
		return cloneWithNewCenterCoordinate(TetrisCoordinate.of(getCenterRow(),
				newCenterColumn));
	}

}
