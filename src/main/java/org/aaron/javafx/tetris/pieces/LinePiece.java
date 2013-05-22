package org.aaron.javafx.tetris.pieces;

import javafx.scene.paint.Color;

import org.aaron.javafx.tetris.TetrisCoordinate;

import com.google.common.collect.ImmutableList;

public class LinePiece extends AbstractTetrisPiece {

	private static ImmutableList<TetrisCoordinate> buildCoordinates(
			TetrisCoordinate centerCoordinate, int orientation) {
		switch (orientation) {
		case 0:
			return ImmutableList.of(centerCoordinate,
					centerCoordinate.plusRows(1), centerCoordinate.plusRows(2),
					centerCoordinate.plusRows(3));
		case 1:
			return ImmutableList.of(centerCoordinate,
					centerCoordinate.plusColumns(1),
					centerCoordinate.plusColumns(2),
					centerCoordinate.plusColumns(3));
		case 2:
			return ImmutableList.of(centerCoordinate,
					centerCoordinate.plusRows(-1),
					centerCoordinate.plusRows(-2),
					centerCoordinate.plusRows(-3));
		default:
			return ImmutableList.of(centerCoordinate,
					centerCoordinate.plusColumns(-1),
					centerCoordinate.plusColumns(-2),
					centerCoordinate.plusColumns(-3));
		}
	}

	public LinePiece(TetrisCoordinate centerCoordinate, int orientation) {
		super(centerCoordinate, Color.RED, orientation, buildCoordinates(
				centerCoordinate, orientation));

	}

	public LinePiece(TetrisCoordinate centerCoordinate) {
		this(centerCoordinate, 0);
	}

	@Override
	public int getNumOrientations() {
		return 4;
	}

	@Override
	public TetrisPiece makeTetrisPiece(TetrisCoordinate centerCoordinate,
			int orientation) {
		return new LinePiece(centerCoordinate, orientation);
	}
}
