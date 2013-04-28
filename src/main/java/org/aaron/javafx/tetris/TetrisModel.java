package org.aaron.javafx.tetris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javafx.scene.paint.Color;

import org.aaron.javafx.tetris.pieces.RandomPieceFactory;
import org.aaron.javafx.tetris.pieces.TetrisPiece;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

public class TetrisModel {

	private final CopyOnWriteArraySet<TetrisModelListener> listeners = new CopyOnWriteArraySet<>();

	private final RandomPieceFactory randomPieceFactory = new RandomPieceFactory();

	private final ArrayList<ArrayList<Optional<Color>>> stackCells = new ArrayList<>();

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
		stackCells.clear();
		for (int row = 0; row < TetrisConstants.NUM_ROWS; ++row) {
			stackCells.add(buildEmptyStackCellsRowList());
		}

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
			for (int row = 0; row < TetrisConstants.NUM_ROWS; ++row) {
				for (int column = 0; column < TetrisConstants.NUM_COLUMNS; ++column) {
					final Optional<Color> colorOption = stackCells.get(row)
							.get(column);
					if (colorOption.isPresent()) {
						drawableStackCells.put(
								TetrisCoordinate.of(row, column),
								colorOption.get());
					}
				}
			}
		}
	}

	private void addNewPiece() {
		final TetrisCoordinate centerCoordinate = TetrisCoordinate.of(0,
				(TetrisConstants.NUM_COLUMNS / 2) - 1);
		final TetrisPiece newPiece = randomPieceFactory
				.createRandomPiece(centerCoordinate);
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
			stackCells.get(tetrisCoordinate.getRow()).set(
					tetrisCoordinate.getColumn(),
					Optional.of(tetrisPiece.getColor()));
		}
		handleFilledStackRows();
		stackCellsUpdatedStatus = StackCellsUpdatedStatus.STACK_CELLS_UPDATED;
	}

	private void handleFilledStackRows() {
		int row = TetrisConstants.NUM_ROWS - 1;
		while (row >= 0) {
			final boolean rowIsFull = (!stackCells.get(row).contains(
					Optional.absent()));
			if (rowIsFull) {
				stackCells.remove(row);
				stackCells.add(0, buildEmptyStackCellsRowList());
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
			if (stackCells.get(tetrisCoordinate.getRow())
					.get(tetrisCoordinate.getColumn()).isPresent()) {
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

	private ArrayList<Optional<Color>> buildEmptyStackCellsRowList() {
		final ArrayList<Optional<Color>> newRowList = new ArrayList<>(
				TetrisConstants.NUM_COLUMNS);
		for (int column = 0; column < TetrisConstants.NUM_COLUMNS; ++column) {
			newRowList.add(Optional.<Color> absent());
		}
		return newRowList;
	}
}
