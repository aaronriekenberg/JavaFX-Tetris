package org.aaron.javafx.tetris.pieces;

import java.util.List;
import java.util.Random;

import org.aaron.javafx.tetris.TetrisConstants;
import org.aaron.javafx.tetris.TetrisCoordinate;

import com.google.common.collect.ImmutableList;

public class RandomPieceFactory {

	private final Random random = new Random();

	private final List<TetrisPiece> pieces;

	public RandomPieceFactory() {
		final TetrisCoordinate newPieceCenterCoordinate = TetrisCoordinate.of(
				0, (TetrisConstants.COLUMNS_SET.size() / 2) - 1);

		final ImmutableList.Builder<TetrisPiece> piecesListBuilder = new ImmutableList.Builder<TetrisPiece>();
		piecesListBuilder.add(new SquarePiece(newPieceCenterCoordinate));
		piecesListBuilder.add(new LinePiece(newPieceCenterCoordinate));
		piecesListBuilder.add(new TPiece(newPieceCenterCoordinate));
		piecesListBuilder.add(new LeftZPiece(newPieceCenterCoordinate));
		piecesListBuilder.add(new RightZPiece(newPieceCenterCoordinate));
		piecesListBuilder.add(new LeftLPiece(newPieceCenterCoordinate));
		piecesListBuilder.add(new RightLPiece(newPieceCenterCoordinate));

		this.pieces = piecesListBuilder.build();
	}

	public TetrisPiece createRandomPiece() {
		return pieces.get(random.nextInt(pieces.size()));
	}
}
