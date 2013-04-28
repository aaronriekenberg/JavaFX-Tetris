package org.aaron.javafx.tetris;

import java.util.Set;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

public class TetrisConstants {

	private TetrisConstants() {

	}

	public static final Set<Integer> ROWS = ContiguousSet.create(
			Range.closedOpen(0, 25), DiscreteDomain.integers());

	public static final Set<Integer> COLUMNS = ContiguousSet.create(
			Range.closedOpen(0, 15), DiscreteDomain.integers());

}
