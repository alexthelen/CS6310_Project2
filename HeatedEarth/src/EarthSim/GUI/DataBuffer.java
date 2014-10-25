package EarthSim.GUI;

import java.util.ArrayList;

public class DataBuffer<T> {

	private ArrayList<T> _buffer;	
	private int _capacity = 1;
	private int _size = 0;
	private int _maxSize = 0;

	/**
	 * @return the maxSize
	 */
	public int getMaxSize() {
		return _maxSize;
	}

	public DataBuffer() {
		_size = 0;
		_capacity = 1;
		_buffer = new ArrayList<T>(1);		
	}

	public DataBuffer(int capacity) {
		_size = 0;
		_capacity = capacity;
		_buffer = new ArrayList<T>(capacity);
	}	

	public boolean isEmpty() {
		return _size == 0;
	}

	public boolean isFull() {
		return _size == _capacity;
	}
	
	public synchronized void Clear() {		
		_buffer.clear();		
		_buffer = null;
		_buffer = new ArrayList<T>(_capacity);	
		_size = 0;
		_maxSize = 0;
	}	
	
	public int getSize() {
		return _buffer.size();
	}

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
