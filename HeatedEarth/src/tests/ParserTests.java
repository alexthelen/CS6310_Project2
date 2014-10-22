/**
 * 
 */
package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import EarthSim.ComponentType;

/**
 * @author pablo
 *
 */
public class ParserTests {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testEmptyParameters() throws Exception {		
		String[] args = {};
		ArgumentParser.Parser parser = new ArgumentParser.Parser();
		parser.parse(args);
		
		assertFalse(parser.presentationShouldRunInOwnThread());
		assertFalse(parser.simulationShouldRunInOwnThread());
		assertEquals(parser.getInitiative(), ComponentType.GUI);
		assertEquals(parser.getBufferLength(), 1);
	}


	@Test
	public void testNonIntegerParameter() throws Exception {
		exception.expect(Exception.class);
		exception.expectMessage(String.format(ArgumentParser.Parser.EXCEPTION_NON_INTEGER_PARAMETER, "Buffer Length"));
		
		String[] args = {"-b one"};
		ArgumentParser.Parser parser = new ArgumentParser.Parser();
		parser.parse(args);
	}

	@Test
	public void testDecimalParameter() throws Exception {
		exception.expect(Exception.class);
		exception.expectMessage(String.format(ArgumentParser.Parser.EXCEPTION_NON_INTEGER_PARAMETER, "Buffer Length"));
		
		String[] args = {"-b 1.5"};
		ArgumentParser.Parser parser = new ArgumentParser.Parser();
		parser.parse(args);
	}

	@Test
	public void testOutOfRangeDimension() throws Exception {
		exception.expect(Exception.class);
		exception.expectMessage(String.format(ArgumentParser.Parser.EXCEPTION_OUT_OF_RANGE, "Buffer Length"));
		
		String[] args = {"-b 0"};
		ArgumentParser.Parser parser = new ArgumentParser.Parser();
		parser.parse(args);
	}

	@Test
	public void testNonNamedParameter() throws Exception {
		exception.expect(Exception.class);
		exception.expectMessage(String.format(ArgumentParser.Parser.EXCEPTION_EXTRA_PARAMETER, "Parameter"));
		
		String[] args = {"Parameter"};
		ArgumentParser.Parser parser = new ArgumentParser.Parser();
		parser.parse(args);
	}

	@Test
	public void testEmptyParameterValue() throws Exception {
		exception.expect(Exception.class);
		exception.expectMessage(String.format(ArgumentParser.Parser.EXCEPTION_NON_INTEGER_PARAMETER, "Buffer Length"));
		
		String[] args = {"-b", "-s"};
		ArgumentParser.Parser parser = new ArgumentParser.Parser();
		parser.parse(args);
	}

	@Test
	public void testEmptyParameterValueLastParameter() throws Exception {
		exception.expect(Exception.class);
		exception.expectMessage(String.format(ArgumentParser.Parser.EXCEPTION_EMPTY_PARAMETER_FORMAT, "b"));
		
		String[] args = {"-s", "-b"};
		ArgumentParser.Parser parser = new ArgumentParser.Parser();
		parser.parse(args);
	}

	@Test
	public void testNonExistingParameter() throws Exception {
		exception.expect(Exception.class);
		exception.expectMessage(String.format(ArgumentParser.Parser.EXCEPTION_NON_VALID_PARAMETER_FORMAT, "q"));
		
		String[] args = {"-q 1"};
		ArgumentParser.Parser parser = new ArgumentParser.Parser();
		parser.parse(args);
	}

	@Test
	public void testSuccessfulParsingOfSimulationInOwnThread() throws Exception {		
		String[] args = {"-s"};
		ArgumentParser.Parser parser = new ArgumentParser.Parser();
		parser.parse(args);
		
		assertTrue(parser.simulationShouldRunInOwnThread());
	}

	@Test
	public void testSuccessfulParsingOfPresentationInOwnThread() throws Exception {		
		String[] args = {"-p"};
		ArgumentParser.Parser parser = new ArgumentParser.Parser();
		parser.parse(args);
		
		assertTrue(parser.presentationShouldRunInOwnThread());
	}

	@Test
	public void testSuccessfulParsingOfSimulationInitiative() throws Exception {		
		String[] args = {"-t"};
		ArgumentParser.Parser parser = new ArgumentParser.Parser();
		parser.parse(args);

		assertEquals(parser.getInitiative(), ComponentType.Simulation);
	}

	@Test
	public void testSuccessfulParsingOfPresentationInitiative() throws Exception {		
		String[] args = {"-r"};
		ArgumentParser.Parser parser = new ArgumentParser.Parser();
		parser.parse(args);

		assertEquals(parser.getInitiative(), ComponentType.Presentation);
	}

	@Test
	public void testConflictInitiative() throws Exception {	
		exception.expect(Exception.class);
		exception.expectMessage(ArgumentParser.Parser.EXCEPTION_CONFLICT);
		
		String[] args = {"-r", "-t"};
		ArgumentParser.Parser parser = new ArgumentParser.Parser();
		parser.parse(args);
	}

	@Test
	public void testSuccessfulParsingOfBufferLength() throws Exception {		
		String[] args = {"-b 100"};
		ArgumentParser.Parser parser = new ArgumentParser.Parser();
		parser.parse(args);

		assertEquals(parser.getBufferLength(), 100);
	}
}
