package org.aaron.javafx.tetris;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javafx.scene.paint.Color;

import org.aaron.javafx.tetris.pieces.RandomPieceFactory;
import org.aaron.javafx.tetris.pieces.TetrisPiece;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;

class TetrisModel {

	private final CopyOnWriteArraySet<TetrisModelListener> listeners = new CopyOnWriteArraySet<>();

	private final RandomPieceFactory randomPieceFactory = new RandomPieceFactory();

	private final StackCells stackCells = new StackCells();

	private final HashMap<TetrisCoordinate, Color> drawableStackCells = new HashMap<>();

	private Optional<TetrisPiece> currentPieceOption = Optional.absent();

	private final HashMap<TetrisCoordinate, Color> drawableCurrentPieceCells = new HashMap<>();

	private int numLines = 0;

	private boolean paused = false;

	private boolean gameOver = false;

	private int deferPublishCount = 0;

	private CurrentPieceUpdatedStatus currentPieceUpdatedStatus = CurrentPieceUpdatedStatus.CURRENT_PIECE_UPDATED;

	private StackCellsUpdatedStatus stackCellsUpdatedStatus = StackCellsUpdatedStatus.STACK_CELLS_UPDATED;

	public TetrisModel() {
		reset();
	}

	public void registerListener(TetrisModelListener listener) {
		listeners.add(listener);
	}

	public void unregisterListener(TetrisModelListener listener) {
		listeners.remove(listener);
	}

	public void reset() {
		stackCells.reset();

		drawableStackCells.clear();

		currentPieceOption = Optional.absent();

		drawableCurrentPieceCells.clear();

		numLines = 0;

		paused = false;

		gameOver = false;

		deferPublishCount = 0;

		currentPieceUpdatedStatus = CurrentPieceUpdatedStatus.CURRENT_PIECE_UPDATED;

		stackCellsUpdatedStatus = StackCellsUpdatedStatus.STACK_CELLS_UPDATED;

		publishTetrisModelEvent();
	}

	public Map<TetrisCoordinate, Color> getDrawableCurrentPieceCells() {
		return ImmutableMap.copyOf(drawableCurrentPieceCells);
	}

	public Map<TetrisCoordinate, Color> getDrawableStackCells() {
		return ImmutableMap.copyOf(drawableStackCells);
	}

	public int getNumLines() {
		return numLines;
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void periodicUpdate() {
		if (gameRunning()) {
			deferPublish();
			if (currentPieceOption.isPresent()) {
				moveCurrentPieceDown();
			} else {
				addNewPiece();
			}
			resumePublish();
			publishTetrisModelEvent();
		}
	}

	public void moveCurrentPieceDown() {
		if (gameRunning() && currentPieceOption.isPresent()) {
			deferPublish();
			final TetrisPiece currentPiece = currentPieceOption.get();
			final TetrisPiece currentPieceMoved = currentPiece
					.cloneWithNewCenterRow(currentPiece.getCenterRow() + 1);
			if (isPieceLocationValid(currentPieceMoved)) {
				setCurrentPiece(currentPieceMoved);
			} else {
				addPieceToStack(currentPiece);
				clearCurrentPiece();
			}
			resumePublish();
			publishTetrisModelEvent();
		}
	}

	public void dropCurrentPiece() {
		if (gameRunning()) {
			deferPublish();
			while (currentPieceOption.isPresent()) {
				moveCurrentPieceDown();
			}
			resumePublish();
			publishTetrisModelEvent();
		}
	}

	public void moveCurrentPieceLeft() {
		if (gameRunning() && currentPieceOption.isPresent()) {
			deferPublish();
			final TetrisPiece currentPiece = currentPieceOption.get();
			final TetrisPiece currentPieceMoved = currentPiece
					.cloneWithNewCenterColumn(currentPiece.getCenterColumn() - 1);
			if (isPieceLocationValid(currentPieceMoved)) {
				setCurrentPiece(currentPieceMoved);
			}
			resumePublish();
			publishTetrisModelEvent();
		}
	}

	public void moveCurrentPieceRight() {
		if (gameRunning() && currentPieceOption.isPresent()) {
			deferPublish();
			final TetrisPiece currentPiece = currentPieceOption.get();
			final TetrisPiece currentPieceMoved = currentPiece
					.cloneWithNewCenterColumn(currentPiece.getCenterColumn() + 1);
			if (isPieceLocationValid(currentPieceMoved)) {
				setCurrentPiece(currentPieceMoved);
			}
			resumePublish();
			publishTetrisModelEvent();
		}
	}

	public void rotateCurrentPiece() {
		if (gameRunning() && currentPieceOption.isPresent()) {
			deferPublish();
			final TetrisPiece currentPiece = currentPieceOption.get();
			final TetrisPiece currentPieceMoved = currentPiece
					.cloneWithNextOrientation();
			if (isPieceLocationValid(currentPieceMoved)) {
				setCurrentPiece(currentPieceMoved);
			}
			resumePublish();
			publishTetrisModelEvent();
		}
	}

	public void togglePause() {
		paused = !paused;
		publishTetrisModelEvent();
	}

	private void addNewPiece() {
		final TetrisPiece newPiece = randomPieceFactory.createRandomPiece();
		if (isPieceLocationValid(newPiece)) {
			setCurrentPiece(newPiece);
		} else {
			clearCurrentPiece();
			gameOver = true;
		}
	}

	private void setCurrentPiece(TetrisPiece newCurrentPiece) {
		currentPieceOption = Optional.of(newCurrentPiece);
		currentPieceUpdatedStatus = CurrentPieceUpdatedStatus.CURRENT_PIECE_UPDATED;
	}

	private void clearCurrentPiece() {
		currentPieceOption = Optional.absent();
		currentPieceUpdatedStatus = CurrentPieceUpdatedStatus.CURRENT_PIECE_UPDATED;
	}

	private void addPieceToStack(TetrisPiece tetrisPiece) {
		for (TetrisCoordinate tetrisCoordinate : tetrisPiece.getCoordinates()) {
			stackCells.set(tetrisCoordinate,
					Optional.of(tetrisPiece.getColor()));
		}
		handleFilledStackRows();
		stackCellsUpdatedStatus = StackCellsUpdatedStatus.STACK_CELLS_UPDATED;
	}

	private void handleFilledStackRows() {
		final Range<Integer> closedRowsRange = TetrisConstants.ROWS_SET.range();
		int row = closedRowsRange.upperEndpoint();
		while (row >= closedRowsRange.lowerEndpoint()) {
			if (stackCells.rowIsFull(row)) {
				stackCells.removeRow(row);
				++numLines;
			} else {
				row -= 1;
			}
		}
	}

	private boolean isPieceLocationValid(TetrisPiece tetrisPiece) {
		for (TetrisCoordinate tetrisCoordinate : tetrisPiece.getCoordinates()) {
			if (!tetrisCoordinate.isValid()) {
				return false;
			}
			if (stackCells.get(tetrisCoordinate).isPresent()) {
				return false;
			}
		}
		return true;
	}

	private boolean gameRunning() {
		return ((!gameOver) && (!paused));
	}

	private void deferPublish() {
		++deferPublishCount;
	}

	private void resumePublish() {
		--deferPublishCount;
	}

	private void updateDrawableCells() {
		if (currentPieceUpdatedStatus == CurrentPieceUpdatedStatus.CURRENT_PIECE_UPDATED) {
			drawableCurrentPieceCells.clear();
			if (currentPieceOption.isPresent()) {
				final TetrisPiece currentPiece = currentPieceOption.get();
				for (TetrisCoordinate tetrisCoordinate : currentPiece
						.getCoordinates()) {
					drawableCurrentPieceCells.put(tetrisCoordinate,
							currentPiece.getColor());
				}
			}
		}

		if (stackCellsUpdatedStatus == StackCellsUpdatedStatus.STACK_CELLS_UPDATED) {
			drawableStackCells.clear();
			for (TetrisCoordinate tetrisCoordinate : TetrisCoordinates.ALL_VALID_TETRIS_COORDINATES) {
				final Optional<Color> colorOption = stackCells
						.get(tetrisCoordinate);
				if (colorOption.isPresent()) {
					drawableStackCells.put(tetrisCoordinate, colorOption.get());
				}
			}
		}
	}

	private void publishTetrisModelEvent() {
		if (deferPublishCount == 0) {
			updateDrawableCells();
			for (TetrisModelListener l : listeners) {
				l.handleTetrisModelUpdated(currentPieceUpdatedStatus,
						stackCellsUpdatedStatus);
			}
			currentPieceUpdatedStatus = CurrentPieceUpdatedStatus.CURRENT_PIECE_NOT_UPDATED;
			stackCellsUpdatedStatus = StackCellsUpdatedStatus.STACK_CELLS_NOT_UPDATED;
		}
	}

}
