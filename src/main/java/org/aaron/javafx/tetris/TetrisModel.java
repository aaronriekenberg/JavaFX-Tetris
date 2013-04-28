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

	private final HashMap<TetrisCoordinate, Color> drawableCells = new HashMap<>();

	private Optional<TetrisPiece> currentPieceOption = Optional.absent();

	private int numLines = 0;

	private boolean paused = false;

	private boolean gameOver = false;

	private int deferPublishCount = 0;

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

		drawableCells.clear();

		currentPieceOption = Optional.absent();

		numLines = 0;

		paused = false;

		gameOver = false;

		deferPublishCount = 0;

		publishTetrisModelEvent();
	}

	public Map<TetrisCoordinate, Color> getDrawableCells() {
		return ImmutableMap.copyOf(drawableCells);
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
				currentPieceOption = Optional.of(currentPieceMoved);
			} else {
				addPieceToStack(currentPiece);
				currentPieceOption = Optional.absent();
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
				currentPieceOption = Optional.of(currentPieceMoved);
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
				currentPieceOption = Optional.of(currentPieceMoved);
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
		drawableCells.clear();

		if (currentPieceOption.isPresent()) {
			final TetrisPiece currentPiece = currentPieceOption.get();
			for (TetrisCoordinate tetrisCoordinate : currentPiece
					.getCoordinates()) {
				drawableCells.put(tetrisCoordinate, currentPiece.getColor());
			}
		}

		for (int row = 0; row < TetrisConstants.NUM_ROWS; ++row) {
			for (int column = 0; column < TetrisConstants.NUM_COLUMNS; ++column) {
				final Optional<Color> colorOption = stackCells.get(row).get(
						column);
				if (colorOption.isPresent()) {
					drawableCells.put(new TetrisCoordinate(row, column),
							colorOption.get());
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
			currentPieceOption = Optional.of(newPiece);
		} else {
			currentPieceOption = Optional.absent();
			gameOver = true;
		}
	}

	private void addPieceToStack(TetrisPiece tetrisPiece) {
		for (TetrisCoordinate tetrisCoordinate : tetrisPiece.getCoordinates()) {
			stackCells.get(tetrisCoordinate.getRow()).set(
					tetrisCoordinate.getColumn(),
					Optional.of(tetrisPiece.getColor()));
		}
		handleFilledStackRows();
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
				l.handleTetrisModelUpdated();
			}
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
