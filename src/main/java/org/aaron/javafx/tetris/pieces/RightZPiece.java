package org.aaron.javafx.tetris.pieces;

import java.util.List;

import javafx.scene.paint.Color;

import org.aaron.javafx.tetris.TetrisCoordinate;

import com.google.common.collect.ImmutableList;

public class RightZPiece extends AbstractTetrisPiece {

	private final List<TetrisCoordinate> coordinates;

	public RightZPiece(TetrisCoordinate centerCoordinate, int orientation) {
		super(centerCoordinate, Color.ORANGE, orientation);
		switch (orientation) {
		case 0:
			coordinates = ImmutableList.of(centerCoordinate,
					centerCoordinate.plusColumns(1),
					centerCoordinate.plusRows(1),
					centerCoordinate.plusRowsAndColumns(1, -1));
			break;
		default:
			coordinates = ImmutableList.of(centerCoordinate,
					centerCoordinate.plusRows(-1),
					centerCoordinate.plusColumns(1),
					centerCoordinate.plusRowsAndColumns(1, 1));
			break;
		}
	}

	public RightZPiece(TetrisCoordinate centerCoordinate) {
		this(centerCoordinate, 0);
	}

	@Override
	public List<TetrisCoordinate> getCoordinates() {
		return coordinates;
	}

	@Override
	public int getNumOrientations() {
		return 2;
	}

	@Override
	public TetrisPiece makeTetrisPiece(TetrisCoordinate centerCoordinate,
			int orientation) {
		return new RightZPiece(centerCoordinate, orientation);
	}

}
