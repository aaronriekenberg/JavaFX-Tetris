package org.aaron.javafx.tetris.pieces;

import java.util.List;

import javafx.scene.paint.Color;

import org.aaron.javafx.tetris.TetrisCoordinate;

import com.google.common.collect.ImmutableList;

public class TPiece extends AbstractTetrisPiece {

	private final List<TetrisCoordinate> coordinates;

	public TPiece(TetrisCoordinate centerCoordinate, int orientation) {
		super(centerCoordinate, Color.CYAN, orientation);
		switch (orientation) {
		case 0:
			coordinates = ImmutableList.of(centerCoordinate,
					centerCoordinate.plusColumns(1),
					centerCoordinate.plusColumns(-1),
					centerCoordinate.plusRows(1));
			break;
		case 1:
			coordinates = ImmutableList.of(centerCoordinate.plusRows(-1),
					centerCoordinate, centerCoordinate.plusRows(1),
					centerCoordinate.plusColumns(1));
			break;
		case 2:
			coordinates = ImmutableList.of(centerCoordinate,
					centerCoordinate.plusColumns(1),
					centerCoordinate.plusColumns(-1),
					centerCoordinate.plusRows(-1));
			break;
		default:
			coordinates = ImmutableList.of(centerCoordinate.plusRows(-1),
					centerCoordinate, centerCoordinate.plusRows(1),
					centerCoordinate.plusColumns(-1));
			break;
		}
	}

	public TPiece(TetrisCoordinate centerCoordinate) {
		this(centerCoordinate, 0);
	}

	@Override
	public List<TetrisCoordinate> getCoordinates() {
		return coordinates;
	}

	@Override
	public int getNumOrientations() {
		return 4;
	}

	@Override
	public TetrisPiece makeTetrisPiece(TetrisCoordinate centerCoordinate,
			int orientation) {
		return new TPiece(centerCoordinate, orientation);
	}

}
