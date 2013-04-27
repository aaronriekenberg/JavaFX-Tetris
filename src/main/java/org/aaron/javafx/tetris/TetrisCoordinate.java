package org.aaron.javafx.tetris;

public class TetrisCoordinate {

	private final int row;

	private final int column;

	public TetrisCoordinate(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public static TetrisCoordinate of(int row, int column) {
		return new TetrisCoordinate(row, column);
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public boolean isValid() {
		return (row >= 0) && (row < TetrisConstants.NUM_ROWS) && (column >= 0)
				&& (column < TetrisConstants.NUM_COLUMNS);
	}

	@Override
	public String toString() {
		return "TetrisCoordinate [row=" + row + ", column=" + column + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TetrisCoordinate other = (TetrisCoordinate) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

}
