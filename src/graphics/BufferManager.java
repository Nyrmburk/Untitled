package graphics;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by Nyrmburk on 6/19/2016.
 */
public class BufferManager {

	private ByteBuffer data;
	private LinkedList<DataBlock> blocks = new LinkedList<>();

	public BufferManager(int capacity) {

		data = ByteBuffer.allocateDirect(capacity);
	}

	public DataBlock allocate(int size) {

		DataBlock block = getBestFit(size);

		if (block != null)
			insertBlock(block);

		return block;
	}

	private DataBlock[] getEmptyBlocks() {

		Iterator<DataBlock> it = blocks.descendingIterator();
		LinkedList<DataBlock> empty = new LinkedList<>();

		int end = data.capacity();

		if (blocks.isEmpty())
			return new DataBlock[]{new DataBlock(this, 0, end)};

		while (it.hasNext()) {
			DataBlock block = it.next();
			int start = block.start + block.size;
			int size = end - start;
			if (size != 0)
				empty.add(new DataBlock(this, start, size));
			end = block.start;
		}

		DataBlock[] blockArray = new DataBlock[empty.size()];
		empty.toArray(blockArray);
		return blockArray;
	}

	private DataBlock getBestFit(int size) {

		DataBlock[] blockArray = getEmptyBlocks();

		DataBlock bestFit = null;
		int bestFitSize = Integer.MAX_VALUE;
		for (DataBlock emptyBlock : blockArray) {

			if (emptyBlock.size > size) {

				if (emptyBlock.size < bestFitSize) {

					bestFitSize = emptyBlock.size;
					bestFit = emptyBlock;
				}
			}
		}

		return bestFit;
	}

	private void insertBlock(DataBlock block) {

		ListIterator<DataBlock> it = blocks.listIterator();

		if (blocks.isEmpty()) {
			blocks.add(block);
			return;
		}

		while (it.hasNext()) {

			if (it.next().start < block.size) {
				it.previous();
				it.add(block);
				return;
			}
		}
	}

	public static class DataBlock {

		BufferManager bufferManager;
		private int start;
		private int size;

		public DataBlock(BufferManager bufferManager, int start, int size) {

			this.bufferManager = bufferManager;
			this.start = start;
			this.size = size;
		}

		public ByteBuffer getBufferView() {

			bufferManager.data.position(start);
			ByteBuffer buffer = bufferManager.data.slice();
			buffer.limit(size);
			buffer.rewind();
			return buffer;
		}

		public void free() {

			bufferManager.blocks.remove(this);
			bufferManager = null;
		}
	}
}
