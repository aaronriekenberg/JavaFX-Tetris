package org.aaron.javafx.tetris;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

public class TetrisConstants {

	private TetrisConstants() {

	}

	public static final ContiguousSet<Integer> ROWS_SET = ContiguousSet.create(
			Range.closedOpen(0, 25), DiscreteDomain.integers());

	public static final ContiguousSet<Integer> COLUMNS_SET = ContiguousSet
			.create(Range.closedOpen(0, 15), DiscreteDomain.integers());

}
