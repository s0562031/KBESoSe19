package de.htw.ai.kbe.runmerunner;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
	
	
	// Initialize
	
	
	// createObj

	@Test
	public void testCreateObjectWithApp() throws ReflectiveOperationException {
		assertTrue(Runner.createObject("App") instanceof App);
	}
	
	@Test
	public void testCreateObjectWithNoExistingClass() throws ReflectiveOperationException {
		assertTrue(Runner.createObject("irgendwas") instanceof App);
	}
	
	@Test
	public void testCreateObjectWithEmptyArgument() throws ReflectiveOperationException {
		assertTrue(Runner.createObject("") instanceof App);
	}
	
	@Test
	public void testCreateObjectWithNull() throws ReflectiveOperationException {
		assertTrue(Runner.createObject(null) instanceof App);
	}
	
	@Test
	public void testcreateObjectWODefaultWithApp() throws ReflectiveOperationException {
		assertTrue(Runner.createObject("App") instanceof App);
	}
}