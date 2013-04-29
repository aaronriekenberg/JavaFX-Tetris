package org.aaron.javafx.tetris;

import java.util.Set;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

public class TetrisConstants {

	private TetrisConstants() {

	}

	public static final Range<Integer> ROWS_RANGE = Range.closedOpen(0, 25);

	public static final Set<Integer> ROWS_SET = ContiguousSet.create(
			ROWS_RANGE, DiscreteDomain.integers());

	public static final Range<Integer> COLUMNS_RANGE = Range.closedOpen(0, 15);

	public static final Set<Integer> COLUMNS_SET = ContiguousSet.create(
			COLUMNS_RANGE, DiscreteDomain.integers());

}
