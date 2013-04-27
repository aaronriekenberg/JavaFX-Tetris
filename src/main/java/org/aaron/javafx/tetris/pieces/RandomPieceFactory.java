package org.aaron.javafx.tetris.pieces;

import org.aaron.javafx.tetris.TetrisCoordinate;

public class RandomPieceFactory {

	public RandomPieceFactory() {

	}

	public AbstractTetrisPiece createRandomPiece(
			TetrisCoordinate centerCoordinate) {
		return new SquarePiece(centerCoordinate);
	}

}
