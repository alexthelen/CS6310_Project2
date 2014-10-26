package EarthSim.GUI;

import java.util.ArrayList;

/**
 * Buffer used to transport a piece of data from a producer to a consumer.
 * 
 * @author TJ Baxter
 * @version 1
 *
 */
public class DataBuffer<T> {

	private ArrayList<T> _buffer;	
	private int _capacity = 1;
	private int _size = 0;
	private int _maxSize = 0;

	/**
	 * @return an {@code int} with the maximum amount of elements present in the buffer at any given time
	 */
	public int getMaxSize() {
		return _maxSize;
	}

	/**
	 * <CTOR>
	 */
	public DataBuffer() {
		_size = 0;
		_capacity = 1;
		_buffer = new ArrayList<T>(1);		
	}

	/**
	 * <CTOR>
	 * 
	 * @param capacity 	an {@code int} with the maximum capacity of the buffer
	 */
	public DataBuffer(int capacity) {
		_size = 0;
		_capacity = capacity;
		_buffer = new ArrayList<T>(capacity);
	}	

	/**
	 * 
	 * @return a {@code boolean} value specifying if there are no elements in the buffer
	 */
	public boolean isEmpty() {
		return _size == 0;
	}

	/**
	 * 
	 * @return a {@code boolean} value specifying if all the slots in the buffer are occupied
	 */
	public boolean isFull() {
		return _size == _capacity;
	}
	
	/**
	 * Remove all elements from the buffer
	 */
	public synchronized void Clear() {		
		_buffer.clear();		
		_buffer = null;
		_buffer = new ArrayList<T>(_capacity);	
		_size = 0;
		_maxSize = 0;
	}	
	
	/**
	 * Get the capacity of the buffer
	 * @return an {@code int} specifying the maximum amount of elements the buffer can hold
	 */
	public int getSize() {
		return _buffer.size();
	}

	/**
	 * Insert a new element at the end of the buffer
	 * @param p	The element to be inserted
	 * @return a {@code boolean} value specifying if the buffer was full at the moment of insertion
	 */
	public synchronized boolean Put(T p) {
		
		if(isFull()) return true;
		
		_buffer.add(p);
		_size++;
		notify();
		
		if (_size > _maxSize) {
			_maxSize = _size;
		}
		
		return false;
	}		
	
	/**
	 * Retrieve the first element in the buffer and remove it.
	 * @return the element being retrieved
	 */
	public synchronized T Pull() {
		
		T planet = null;
		
		if(_size > 0) {
			planet = _buffer.get(0);
			_buffer.remove(0);
			_size--;			
		}
		
		notify();
		return planet;
		
	}

//	public class BufferFullException extends Exception {
//
//		/**
//		 * Required ID
//		 */
//		private static final long serialVersionUID = 1L;
//
//	}
	
}
