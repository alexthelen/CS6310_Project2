/**
 * 
 */
package ArgumentParser;

import EarthSim.ComponentType;

/**
 * Parser will take an array of strings containing the values of the 
 * flags utilized in a command line environment.
 * 
 * @author Pablo Gallastegui
 * @version 1
 *
 */
public class Parser {	
	
	private boolean _simulationShouldRunInOwnThread = false;
	private boolean _presentationShouldRunInOwnThread = false;
	private ComponentType _initiative = ComponentType.GUI;
	private int bufferLength = 1;

	/**
	 * @return the simulationShouldRunInOwnThread
	 */
	public boolean simulationShouldRunInOwnThread() {
		return _simulationShouldRunInOwnThread;
	}

	/**
	 * @param simulationShouldRunInOwnThread the simulationShouldRunInOwnThread to set
	 */
	public void setSimulationShouldRunInOwnThread(
			boolean simulationShouldRunInOwnThread) {
		this._simulationShouldRunInOwnThread = simulationShouldRunInOwnThread;
	}

	/**
	 * @return the presentationShouldRunInOwnThread
	 */
	public boolean presentationShouldRunInOwnThread() {
		return _presentationShouldRunInOwnThread;
	}

	/**
	 * @param presentationShouldRunInOwnThread the presentationShouldRunInOwnThread to set
	 */
	public void setPresentationShouldRunInOwnThread(
			boolean presentationShouldRunInOwnThread) {
		this._presentationShouldRunInOwnThread = presentationShouldRunInOwnThread;
	}

	/**
	 * @return the initiative
	 */
	public ComponentType getInitiative() {
		return _initiative;
	}

	/**
	 * @param ComponentType the initiative to set
	 */
	public void setInitiative(ComponentType initiative) {
		this._initiative = initiative;
	}

	/**
	 * @return the buffering
	 */
	public int getBufferLength() {
		return bufferLength;
	}

	/**
	 * @param topTemperature the topTemperature to set
	 * @throws Exception 
	 */
	public void setBufferLenght(String bufferLength) throws Exception {
		this.bufferLength = this.validateIntProperty(bufferLength, "Buffer Length", 1, Long.MAX_VALUE);
	}

	// constants
	public static final String EXCEPTION_EMPTY_PARAMETER_FORMAT = "Parameter %s is empty.";
	public static final String EXCEPTION_NON_INTEGER_PARAMETER = "Parameter %s is not an integer.";
	public static final String EXCEPTION_OUT_OF_RANGE = "Parameter %s is out of range.";
	public static final String EXCEPTION_NON_VALID_PARAMETER_FORMAT = "%s is not a valid parameter.";
	public static final String EXCEPTION_EMPTY_PARAMETER = "'-' found but no parameter name following.";
	public static final String EXCEPTION_EXTRA_PARAMETER = "'%s' is not a valid parameter.";
	public static final String EXCEPTION_CONFLICT = "The parameters -r and -t are exclusive of one another. Please make sure you use only one of them";

	/**
	 * <CTOR>
	 */
	public Parser() {
	}

	/**
	 * It will parse the strings array provided and will populate the private variables: minWordLength,
	 * fileContent, and delimiters with the values obtained from the arguments. If no value is
	 * provided for minWordLength and/or delimiters, a default value will be used.
	 *
	 * @param 	args		Array containing the argument strings
	 * @throws	Exception	when invalid parameters are provided
	 */
	public void parse(String[] args) throws Exception {
		int i = 0;
		
		// reset all the properties to default values
		this._simulationShouldRunInOwnThread = false;
		this._presentationShouldRunInOwnThread = false;
		this._initiative = ComponentType.GUI;
		this.bufferLength = 1;

		while (i < args.length) {
			String arg = args[i];

			if (arg.startsWith("-")) { // it's a named parameter, possibly -s -p -r -t -b
				if (arg.length() > 1) { // parameter is not a single "-"
					String parameterType = arg.substring(1, 2);

					if (parameterType.equals("s") || parameterType.equals("p") || 
							parameterType.equals("r") || parameterType.equals("t")) {

						// assign the parameter value to the appropriate property
						if (parameterType.equals("s")) {
							this.setSimulationShouldRunInOwnThread(true);
						} else if (parameterType.equals("p")) {
							this.setPresentationShouldRunInOwnThread(true);
						} else if (parameterType.equals("r") && (this.getInitiative() == ComponentType.GUI)) {
							this.setInitiative(ComponentType.Presentation);
						} else if (parameterType.equals("t") && (this.getInitiative() == ComponentType.GUI)) {
							this.setInitiative(ComponentType.Simulation);
						} else {
							throw new Exception(Parser.EXCEPTION_CONFLICT);
						}
						
					} else if (parameterType.equals("b")) {
						String possibleParameterContent = null;
						int skipIndex = 0;

						// Check to see if the user put a space in between the named parameter and
						// the value for it or not. Accept it either way
						if (arg.length() == 2) {
							if (args.length > (i + 1)) {
								possibleParameterContent = args[i + 1];

								// if the user did put a space, we will skip this parameter in the next iteration
								skipIndex = 1;
							} else { // this was the last parameter
								throw new Exception(String.format(Parser.EXCEPTION_EMPTY_PARAMETER_FORMAT, parameterType));
							}
						} else { //
							possibleParameterContent = arg.substring(2);
						}

						// everything has been validated, assign the parameter value to the appropriate property
						this.setBufferLenght(possibleParameterContent.trim());
					} else { // the letter after the "-" does not correspond to a known parameter
						throw new Exception(String.format(Parser.EXCEPTION_NON_VALID_PARAMETER_FORMAT, parameterType));
					}
				} else { // parameter is a single "-"
					throw new Exception(Parser.EXCEPTION_EMPTY_PARAMETER);
				}
			} else { // it is not a named parameter
				throw new Exception(String.format(Parser.EXCEPTION_EXTRA_PARAMETER, arg));
			}

			i++;
		}
	}
	
	private int validateIntProperty(String possibleContent, String propertyName, int minimumValue, long maximumValue) throws Exception {
		// check that the value of the parameter is valid (an integer)
		int propertyValue = 0;
		try {
			propertyValue = Integer.parseInt(possibleContent.trim());
		} catch (Exception ex) {
			throw new Exception (String.format(Parser.EXCEPTION_NON_INTEGER_PARAMETER, propertyName));
		}
		
		if ((propertyValue < minimumValue) || (propertyValue > maximumValue)) {
			throw new Exception (String.format(Parser.EXCEPTION_OUT_OF_RANGE, propertyName));
		}
		
		return propertyValue;
	}

}
