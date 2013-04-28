package org.aaron.javafx.tetris.pieces;

import java.util.List;

import javafx.scene.paint.Color;

import org.aaron.javafx.tetris.TetrisCoordinate;

import com.google.common.collect.ImmutableList;

public class SquarePiece extends AbstractTetrisPiece {

	private final List<TetrisCoordinate> coordinates;

	public SquarePiece(TetrisCoordinate centerCoordinate, int orientation) {
		super(centerCoordinate, Color.GREEN, orientation);
		coordinates = ImmutableList.of(centerCoordinate,
				centerCoordinate.plusRows(1), centerCoordinate.plusColumns(1),
				centerCoordinate.plusRowsAndColumns(1, 1));
	}

	public SquarePiece(TetrisCoordinate centerCoordinate) {
		this(centerCoordinate, 0);
	}

	@Override
	public List<TetrisCoordinate> getCoordinates() {
		return coordinates;
	}

	@Override
	public int getNumOrientations() {
		return 1;
	}

	@Override
	public AbstractTetrisPiece makeTetrisPiece(
			TetrisCoordinate centerCoordinate, int orientation) {
		return new SquarePiece(centerCoordinate, orientation);
	}

}
