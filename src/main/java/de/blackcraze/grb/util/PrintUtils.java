package de.blackcraze.grb.util;

import java.util.Date;

import org.joda.time.Duration;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import de.blackcraze.grb.model.PrintableTable;
import de.blackcraze.grb.util.wagu.Block;
import de.blackcraze.grb.util.wagu.Board;
import de.blackcraze.grb.util.wagu.Table;

public final class PrintUtils {

	private PrintUtils() {
	}

	public final static String prettyPrint(PrintableTable... stocks) {
		StringBuilder returnHandle = new StringBuilder();
		for (PrintableTable stock : stocks) {
			if (stock != null) {
				int boardWidth = stock.getWidth();
				int headerWidth = boardWidth - 2; // außenränder
				StringBuilder b = new StringBuilder();
				b.append("```dsconfig\n");
				Board board = new Board(boardWidth);
				// board.showBlockIndex(true); // DEBUG
				Block header = new Block(board, headerWidth, 1, stock.getHeader());
				header.setDataAlign(Block.DATA_MIDDLE_LEFT);
				board.setInitialBlock(header);

				Table table = new Table(board, boardWidth, stock.getTitles(), stock.getRows(), stock.getWidths(),
						stock.getAligns());
				board.appendTableTo(0, Board.APPEND_BELOW, table);

				for (int i = 0; i < stock.getFooter().size(); i++) {
					Block footer = new Block(board, stock.getWidths().get(i), 1, stock.getFooter().get(i));
					footer.setDataAlign(stock.getAligns().get(i));
					if (i == 0) {
						header.getMostBelowBlock().setBelowBlock(footer);
					} else {
						header.getMostBelowBlock().getMostRightBlock().setRightBlock(footer);
					}
				}

				board.build();
				b.append(board.getPreview());
				b.append("```\n");

				if (returnHandle.length() + b.length() > 2000) {
					// discord limits output to 2000 chars
					return returnHandle.toString();
				} else {
					returnHandle.append(b.toString());
				}
			}
		}
		return returnHandle.toString();
	}

	public final static String getgetDiffFormatted(Date from, Date to) {
		Duration duration = new Duration(to.getTime() - from.getTime()); // in
		// milliseconds
		PeriodFormatter formatter = new PeriodFormatterBuilder().printZeroNever()//
				.appendWeeks().appendSuffix("w").appendSeparator(" ")//
				.appendDays().appendSuffix("d").appendSeparator(" ")//
				.appendHours().appendSuffix("h").appendSeparator(" ")//
				.appendMinutes().appendSuffix("m").appendSeparator(" ")//
				.appendSeconds().appendSuffix("s")//
				.toFormatter();
		return formatter.print(duration.toPeriod(PeriodType.yearMonthDayTime()));
	}

}
