package EarthSim.GUI;

import java.util.ArrayList;

import EarthSim.SimulationEngine.Planet;

public class DataBuffer {

	private ArrayList<Planet> _buffer;	
	private int _capacity = 1;
	private int _size = 0;

	public DataBuffer() {
		_size = 0;
		_capacity = 1;
		_buffer = new ArrayList<Planet>(1);
	}

	public DataBuffer(int capacity) {
		_size = 0;
		_capacity = capacity;
		_buffer = new ArrayList<Planet>(capacity);
	}	

	public boolean isEmpty() {
		return _size == 0;
	}

	public boolean isFull() {
		return _size == _capacity;
	}

	public synchronized void Put(Planet p) throws BufferFullException {

		if(isFull()) throw new BufferFullException();
		
		_buffer.add(p);
		_size++;
		notify();
	}		
	
	public synchronized Planet Pull() {
		
		Planet planet = null;
		
		if(_size > 0) {
			planet = _buffer.get(0);
			_buffer.remove(0);
			_size--;			
		}
		
		notify();
		return planet;
		
	}

	public class BufferFullException extends Exception {

		/**
		 * Required ID
		 */
		private static final long serialVersionUID = 1L;

	}
	
}
