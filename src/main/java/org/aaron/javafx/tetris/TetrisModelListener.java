package org.aaron.javafx.tetris;

interface TetrisModelListener {

	public void handleTetrisModelUpdated(
			CurrentPieceUpdatedStatus currentPieceUpdatedStatus,
			StackCellsUpdatedStatus stackCellsUpdatedStatus);

}
