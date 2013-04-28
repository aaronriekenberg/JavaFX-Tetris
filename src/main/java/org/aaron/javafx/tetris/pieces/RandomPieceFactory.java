package org.aaron.javafx.tetris.pieces;

import java.util.ArrayList;
import java.util.Random;

import org.aaron.javafx.tetris.TetrisCoordinate;

public class RandomPieceFactory {

	private final Random random = new Random();

	private interface PieceConstructor {

		public TetrisPiece construct(TetrisCoordinate centerCoordinate);

	}

	private final ArrayList<PieceConstructor> pieceConstructors = new ArrayList<>();

	public RandomPieceFactory() {
		pieceConstructors.add(new PieceConstructor() {
			@Override
			public TetrisPiece construct(TetrisCoordinate centerCoordinate) {
				return new SquarePiece(centerCoordinate);
			}
		});
		pieceConstructors.add(new PieceConstructor() {
			@Override
			public TetrisPiece construct(TetrisCoordinate centerCoordinate) {
				return new LinePiece(centerCoordinate);
			}
		});
		pieceConstructors.add(new PieceConstructor() {
			@Override
			public TetrisPiece construct(TetrisCoordinate centerCoordinate) {
				return new TPiece(centerCoordinate);
			}
		});
	}

	public TetrisPiece createRandomPiece(TetrisCoordinate centerCoordinate) {
		return pieceConstructors.get(random.nextInt(pieceConstructors.size()))
				.construct(centerCoordinate);
	}
}
