package org.aaron.javafx.tetris.pieces;

import java.util.ArrayList;
import java.util.Random;

import org.aaron.javafx.tetris.TetrisConstants;
import org.aaron.javafx.tetris.TetrisCoordinate;

public class RandomPieceFactory {

	private final Random random = new Random();

	private final ArrayList<TetrisPiece> newPieces = new ArrayList<>();

	public RandomPieceFactory() {
		final TetrisCoordinate newPieceCenterCoordinate = TetrisCoordinate.of(
				0, (TetrisConstants.COLUMNS.size() / 2) - 1);
		newPieces.add(new SquarePiece(newPieceCenterCoordinate));
		newPieces.add(new LinePiece(newPieceCenterCoordinate));
		newPieces.add(new TPiece(newPieceCenterCoordinate));
		newPieces.add(new LeftZPiece(newPieceCenterCoordinate));
		newPieces.add(new RightZPiece(newPieceCenterCoordinate));
	}

	public TetrisPiece createRandomPiece() {
		return newPieces.get(random.nextInt(newPieces.size()));
	}
}
