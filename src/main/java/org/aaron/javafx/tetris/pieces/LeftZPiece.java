package org.aaron.javafx.tetris.pieces;

import javafx.scene.paint.Color;

import org.aaron.javafx.tetris.TetrisCoordinate;

import com.google.common.collect.ImmutableList;

public class LeftZPiece extends AbstractTetrisPiece {

	private static ImmutableList<TetrisCoordinate> buildCoordinates(
			TetrisCoordinate centerCoordinate, int orientation) {
		switch (orientation) {
		case 0:
			return ImmutableList.of(centerCoordinate,
					centerCoordinate.plusColumns(-1),
					centerCoordinate.plusRows(1),
					centerCoordinate.plusRowsAndColumns(1, 1));
		default:
			return ImmutableList.of(centerCoordinate,
					centerCoordinate.plusRows(1),
					centerCoordinate.plusColumns(1),
					centerCoordinate.plusRowsAndColumns(-1, 1));
		}
	}

	public LeftZPiece(TetrisCoordinate centerCoordinate, int orientation) {
		super(centerCoordinate, Color.YELLOW, orientation, buildCoordinates(
				centerCoordinate, orientation));

	}

	public LeftZPiece(TetrisCoordinate centerCoordinate) {
		this(centerCoordinate, 0);
	}

	@Override
	public int getNumOrientations() {
		return 2;
	}

	@Override
	public TetrisPiece makeTetrisPiece(TetrisCoordinate centerCoordinate,
			int orientation) {
		return new LeftZPiece(centerCoordinate, orientation);
	}

}
