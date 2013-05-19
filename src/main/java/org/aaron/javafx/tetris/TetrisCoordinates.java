package org.aaron.javafx.tetris;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class TetrisCoordinates {

	private TetrisCoordinates() {

	}

	public static final Set<TetrisCoordinate> ALL_VALID_TETRIS_COORDINATES;
	static {
		final ImmutableSet.Builder<TetrisCoordinate> builder = new ImmutableSet.Builder<>();
		for (int row : TetrisConstants.ROWS_SET) {
			for (int column : TetrisConstants.COLUMNS_SET) {
				builder.add(TetrisCoordinate.of(row, column));
			}
		}
		ALL_VALID_TETRIS_COORDINATES = builder.build();
	}

}
