package main;

import java.util.*;

/**
 * Created by Nyrmburk on 5/23/2016.
 * <p>
 * This is a list that is special because it aims to provide O(1) speed for all operations. Because of how it is
 * designed, it requires a bit more care when using it. Instead of keeping track of the item, keep track of the id that
 * is assigned.
 */
public class IdList<E> {

	IdGenerator idGenerator = new IdGenerator();

	private Object[] data;

	private int elementCount;

	public IdList() {

		this(10);
	}

	public IdList(int size) {

		data = new Object[size];
	}

	public int size() {

		return elementCount;
	}

	public boolean isEmpty() {
		return size() > 0;
	}

	public boolean contains(int id) {

		return idGenerator.containsId(id);
	}

	@SuppressWarnings("unchecked")
	public Iterator<E> iterator() {
		return new Iterator<E>() {

			int elementIndex = 0;
			int dataIndex = 0;

			@Override
			public boolean hasNext() {

				return elementIndex < size();
			}

			@Override
			public E next() {

				elementIndex++;

				while (data[dataIndex] == null)
					dataIndex++;

				return (E) data[dataIndex++];
			}
		};
	}

	public Object[] toArray() {

		return new Object[0];
	}

	public <T> T[] toArray(T[] a) {

		return null;
	}

	public int add(E e) {

		int id = idGenerator.getNextId();
		elementCount++;

		if (data.length <= id)
			grow(id+1);

		data[id] = e;

		return id;
	}

	@SuppressWarnings("unchecked")
	public E remove(int id) {

		boolean isRemoved = idGenerator.removeId(id);

		E element = null;
		if (isRemoved) {
			elementCount--;
			element = (E) data[id];
			data[id] = null;
		}

		return element;
	}

	public boolean containsAll(int[] c) {

		for (int id : c)
			if (data[id] == null) return false;

		return true;
	}

	public int[] addAll(Collection<? extends E> c) {

		int[] ids = new int[c.size()];

		int index = 0;
		for (E element : c)
			ids[index++] = add(element);

		return ids;
	}

	public boolean removeAll(int[] ids) {

		boolean allRemoved = true;

		for (int id : ids) {

			E element = remove(id);
			allRemoved = allRemoved && element != null;
		}

		return allRemoved;
	}

	public void retainAll(int[] ids) {

		Object[] newData = new Object[ids.length];

		for (int i = 0; i < ids.length; i++)
			newData[ids[i]] = data[ids[i]];

		System.arraycopy(newData, 0, data, 0, ids.length);
	}

	public void clear() {

		idGenerator = new IdGenerator();
		data = new Object[data.length];
	}

	@SuppressWarnings("unchecked")
	public E get(int id) {

		return (E) data[id];
	}

	@SuppressWarnings("unchecked")
	public E set(int id, E element) {

		E previousElement = (E) data[id];
		data[id] = element;
		return previousElement;
	}

	public int idOf(Object o) {

		int id = 0;

		while (data[id] != o)
			id++;

		return id;
	}

	public void grow(int size) {

		int oldCapacity = data.length;
		int newCapacity = oldCapacity + (oldCapacity >> 1);
		if (size > newCapacity)
			newCapacity = size;

		Object[] newData = new Object[newCapacity];

		System.arraycopy(data, 0, newData, 0, data.length);

		data = newData;
	}

	private class IdGenerator {

		private int nextId = 0;

		private HashSet<Integer> emptyIds = new HashSet<>();

		public int getNextId() {

			int id;

			if (!emptyIds.isEmpty()) {
				Iterator<Integer> it = emptyIds.iterator();
				id = it.next();
				emptyIds.remove(id);
			} else {
				id = nextId++;
			}

			return id;
		}

		public boolean removeId(int id) {

			if (!containsId(id)) {

				emptyIds.add(id);
				return true;
			}

			return false;
		}

		public boolean containsId(int id) {

			return id < nextId && !emptyIds.contains(id);
		}
	}
}
