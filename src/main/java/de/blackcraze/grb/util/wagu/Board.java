/*

The MIT License (MIT)

Copyright (c) 2016 Thahzan Mohomed

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/
package de.blackcraze.grb.util.wagu;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thedath Oudarya
 */
public class Board {

    protected boolean showBlockIndex;

    protected int boardWidth;

    private Block initialBlock;

    private List<Charr> charrs;

    private String preview;

    public static final int APPEND_RIGHT = 16;

    public static final int APPEND_BELOW = 17;

    public Board(int boardWidth) {
        this.boardWidth = boardWidth;
        this.charrs = new ArrayList<>();
        this.preview = "";
        this.showBlockIndex = false;
        Block.nextIndex = 0;
    }

    public Board setInitialBlock(Block initialBlock) {
        this.initialBlock = initialBlock;
        return this;
    }

    public boolean isBlockIndexShowing() {
        return showBlockIndex;
    }

    public void showBlockIndex(boolean showBlockIndex) {
        this.showBlockIndex = showBlockIndex;
    }

    public Board appendTableTo(int appendableBlockIndex, int appendableDirection, Table table) {
        Block tableBlock = table.tableToBlocks();
        Block block = getBlock(appendableBlockIndex);
        if (appendableDirection == APPEND_RIGHT) {
            block.setRightBlock(tableBlock);
            rearranegCoordinates(block);
        } else if (appendableDirection == APPEND_BELOW) {
            block.setBelowBlock(tableBlock);
            rearranegCoordinates(block);
        } else {
            throw new RuntimeException(Resource.getError("BLOCK_DIRECTION", locale));
            /* ORIGINAL VERSION OF PREVIOUS LINE BELOW
            throw new RuntimeException("Invalid block appending direction given"); */
        }
        return this;
    }

    private void rearranegCoordinates(Block block) {
        Block rightBlock = block.getRightBlock();
        Block belowBlock = block.getBelowBlock();
        if (rightBlock != null && belowBlock == null) {
            block.setRightBlock(rightBlock);
            rearranegCoordinates(rightBlock);
        } else if (rightBlock == null && belowBlock != null) {
            block.setBelowBlock(belowBlock);
            rearranegCoordinates(belowBlock);
        } else if (rightBlock != null && belowBlock != null) {
            int rightIndex = rightBlock.getIndex();
            int belowIndex = belowBlock.getIndex();
            int blockIdDiff = rightIndex - belowIndex;
            if (blockIdDiff > 0) {
                if (blockIdDiff == 1) {
                    block.setRightBlock(rightBlock);
                    block.setBelowBlock(belowBlock);
                    rearranegCoordinates(rightBlock);
                    rearranegCoordinates(belowBlock);
                } else {
                    block.setRightBlock(rightBlock);
                    rearranegCoordinates(rightBlock);
                    block.setBelowBlock(belowBlock);
                    rearranegCoordinates(belowBlock);
                }
            } else if (blockIdDiff < 0) {
                blockIdDiff *= -1;
                if (blockIdDiff == 1) {
                    block.setBelowBlock(belowBlock);
                    block.setRightBlock(rightBlock);
                    rearranegCoordinates(belowBlock);
                    rearranegCoordinates(rightBlock);
                } else {
                    block.setBelowBlock(belowBlock);
                    rearranegCoordinates(belowBlock);
                    block.setRightBlock(rightBlock);
                    rearranegCoordinates(rightBlock);
                }
            }
        }
    }

    public Block getBlock(int blockIndex) {
        if (blockIndex >= 0) {
            return getBlock(blockIndex, initialBlock);
        } else {
            throw new RuntimeException(Resource.getError("BLOCK_NEGATIVE", locale), blockIndex);
            /* ORIGINAL VERSION OF PREVIOUS LINE BELOW
            throw new RuntimeException("Block index cannot be negative. " + blockIndex + " given."); */
        }
    }

    private Block getBlock(int blockIndex, Block block) {
        Block foundBlock = null;
        if (block.getIndex() == blockIndex) {
            return block;
        } else {
            if (block.getRightBlock() != null) {
                foundBlock = getBlock(blockIndex, block.getRightBlock());
            }
            if (foundBlock != null) {
                return foundBlock;
            }
            if (block.getBelowBlock() != null) {
                foundBlock = getBlock(blockIndex, block.getBelowBlock());
            }
            if (foundBlock != null) {
                return foundBlock;
            }
        }
        return foundBlock;
    }

    public Board build() {
        if (charrs.isEmpty()) {
            //rearranegCoordinates(initialBlock);
            buildBlock(initialBlock);
            dumpCharrsFromBlock(initialBlock);

            int maxY = -1;
            int maxX = -1;
            for (Charr charr : charrs) {
                int testY = charr.getY();
                int testX = charr.getX();
                if (maxY < testY) {
                    maxY = testY;
                }
                if (maxX < testX) {
                    maxX = testX;
                }
            }
            String[][] dataPoints = new String[maxY + 1][boardWidth];
            for (Charr charr : charrs) {
                String currentValue = dataPoints[charr.getY()][charr.getX()];
                String newValue = String.valueOf(charr.getC());
                if (currentValue == null || !currentValue.equals("+")) {
                    dataPoints[charr.getY()][charr.getX()] = newValue;
                }
            }

            for (String[] dataPoint : dataPoints) {
                for (String point : dataPoint) {
                    if (point == null) {
                        point = String.valueOf(Charr.S);
                    }
                    preview = preview.concat(point);
                }
                preview = preview.concat(String.valueOf(Charr.NL));
            }
        }

        return this;
    }

    public String getPreview() {
        build();
        return preview;
    }

    public Board invalidate() {
        invalidateBlock(initialBlock);
        charrs = new ArrayList<>();
        preview = "";
        return this;
    }

    private void buildBlock(Block block) {
        if (block != null) {
            block.build();
            buildBlock(block.getRightBlock());
            buildBlock(block.getBelowBlock());
        }
    }

    private void dumpCharrsFromBlock(Block block) {
        if (block != null) {
            charrs.addAll(block.getChars());
            dumpCharrsFromBlock(block.getRightBlock());
            dumpCharrsFromBlock(block.getBelowBlock());
        }
    }

    private void invalidateBlock(Block block) {
        if (block != null) {
            block.invalidate();
            invalidateBlock(block.getRightBlock());
            invalidateBlock(block.getBelowBlock());
        }
    }

}
