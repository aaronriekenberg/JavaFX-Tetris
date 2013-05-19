package org.aaron.javafx.tetris;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;

import javafx.scene.paint.Color;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;

class StackCells {

	private final ArrayList<ArrayList<Optional<Color>>> stackCells = new ArrayList<>();

	public StackCells() {
		reset();
	}

	public void reset() {
		stackCells.clear();
		stackCells.addAll(buildEmptyStackCellsColumnsList());
	}

	public Optional<Color> get(TetrisCoordinate tetrisCoordinate) {
		checkNotNull(tetrisCoordinate);
		checkArgument(tetrisCoordinate.isValid(),
				"Invalid tetrisCoordinate %s", tetrisCoordinate);

		return stackCells.get(tetrisCoordinate.getRow()).get(
				tetrisCoordinate.getColumn());
	}

	public void set(TetrisCoordinate tetrisCoordinate,
			Optional<Color> colorOption) {
		checkNotNull(tetrisCoordinate);
		checkArgument(tetrisCoordinate.isValid(),
				"Invalid tetrisCoordinate %s", tetrisCoordinate);
		checkNotNull(colorOption);

		stackCells.get(tetrisCoordinate.getRow()).set(
				tetrisCoordinate.getColumn(), colorOption);
	}

	public boolean rowIsFull(int row) {
		checkArgument(TetrisConstants.ROWS_SET.contains(row), "Invalid row %s",
				row);

		return (!stackCells.get(row).contains(Optional.absent()));
	}

	public void removeRow(int row) {
		checkArgument(TetrisConstants.ROWS_SET.contains(row), "Invalid row %s",
				row);

		stackCells.remove(row);
		stackCells.add(0, buildEmptyStackCellsRowList());
	}

	private ArrayList<ArrayList<Optional<Color>>> buildEmptyStackCellsColumnsList() {
		return new ArrayList<>(Collections2.transform(TetrisConstants.ROWS_SET,
				new Function<Integer, ArrayList<Optional<Color>>>() {
					@Override
					public ArrayList<Optional<Color>> apply(Integer row) {
						return buildEmptyStackCellsRowList();
					}
				}));
	}

	private ArrayList<Optional<Color>> buildEmptyStackCellsRowList() {
		return new ArrayList<>(Collections2.transform(
				TetrisConstants.COLUMNS_SET,
				new Function<Integer, Optional<Color>>() {
					@Override
					public Optional<Color> apply(Integer column) {
						return Optional.absent();
					}
				}));
	}

}
