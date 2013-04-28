package org.aaron.javafx.tetris.pieces;

import java.util.List;

import javafx.scene.paint.Color;

import org.aaron.javafx.tetris.TetrisCoordinate;

public interface TetrisPiece {

	public TetrisCoordinate getCenterCoordinate();

	public int getCenterRow();

	public int getCenterColumn();

	public Color getColor();

	public List<TetrisCoordinate> getCoordinates();

	public int getOrientation();

	public int getNumOrientations();

	public int getNextOrientation();

	public TetrisPiece makeTetrisPiece(TetrisCoordinate centerCoordinate,
			int orientation);

	public TetrisPiece cloneWithNewCenterCoordinate(
			TetrisCoordinate newCenterCoordinate);

	public TetrisPiece cloneWithNewCenterRow(int newCenterRow);

	public TetrisPiece cloneWithNewCenterColumn(int newCenterColumn);
}
