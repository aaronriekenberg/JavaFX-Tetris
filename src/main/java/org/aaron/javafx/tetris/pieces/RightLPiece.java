package org.aaron.javafx.tetris.pieces;

import javafx.scene.paint.Color;

import org.aaron.javafx.tetris.TetrisCoordinate;

import com.google.common.collect.ImmutableList;

public class RightLPiece extends AbstractTetrisPiece {

	private static ImmutableList<TetrisCoordinate> buildCoordinates(
			TetrisCoordinate centerCoordinate, int orientation) {
		switch (orientation) {
		case 0:
			return ImmutableList.of(centerCoordinate,
					centerCoordinate.plusRows(1), centerCoordinate.plusRows(2),
					centerCoordinate.plusRowsAndColumns(2, 1));
		case 1:
			return ImmutableList.of(centerCoordinate,
					centerCoordinate.plusColumns(1),
					centerCoordinate.plusColumns(2),
					centerCoordinate.plusRowsAndColumns(-1, 2));
		case 2:
			return ImmutableList.of(centerCoordinate,
					centerCoordinate.plusRows(-1),
					centerCoordinate.plusRows(-2),
					centerCoordinate.plusRowsAndColumns(-2, -1));
		default:
			return ImmutableList.of(centerCoordinate,
					centerCoordinate.plusColumns(-1),
					centerCoordinate.plusColumns(-2),
					centerCoordinate.plusRowsAndColumns(1, -2));
		}
	}

	public RightLPiece(TetrisCoordinate centerCoordinate, int orientation) {
		super(centerCoordinate, Color.CRIMSON, orientation, buildCoordinates(
				centerCoordinate, orientation));

	}

	public RightLPiece(TetrisCoordinate centerCoordinate) {
		this(centerCoordinate, 0);
	}

	@Override
	public int getNumOrientations() {
		return 4;
	}

	@Override
	public TetrisPiece makeTetrisPiece(TetrisCoordinate centerCoordinate,
			int orientation) {
		return new RightLPiece(centerCoordinate, orientation);
	}

}
