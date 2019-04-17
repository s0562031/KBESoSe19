package de.htw.ai.kbe.runmerunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;


/**
 * Unit test for simple App.
 */
public class AppTest {
	
	
	//Methode mit Argumenten aufrufen:
	//Object result = m.invoke(obj, new Object[] {"x","y"});
	
	// whole program test
		
	@Test(expected = SystemExitException.class)
	public void runnerWithoutFilenameDeclaration() {
		Runner.main(new String[] {"-c de.htw.ai.kbe.runmerunner.App"});
	}
	
	@Test(expected = SystemExitException.class)
	public void runnerWithRunner() {
		Runner.main(new String[] {"-c de.htw.ai.kbe.runmerunner.Runner -o report.txt"});
	}
	
	@Test(expected = SystemExitException.class)
	public void runnerWithoutAppnameDeclaration() {
		Runner.main(new String[] {"-o report.txt"});
	}
	
	@Test(expected = SystemExitException.class)
	public void runnerWithouAnyParamDeclaration() {
		Runner.main(new String[] {""});
	}
	
	
	// separated setClassObjAndFilename
	
	@Test(expected = SystemExitException.class)
	public void setClassObjAndFilenameWithNullInCommandLineObject() {
		
		Runner.setClassObjAndFilename(new String[] {"-c de.htw.ai.kbe.runmerunner.App -o report.txt"}, null);
	}
	
	
	// separated createObj

	@Test
	public void testCreateObjectWithAppShouldReturnAnApp() throws ReflectiveOperationException {
		assertTrue(Runner.createObject("de.htw.ai.kbe.runmerunner.App") instanceof App);
	}
	
	@Test
	public void testCreateObjectWithAppWOFullPathShouldReturnAnApp() throws ReflectiveOperationException {
		assertTrue(Runner.createObject("App") instanceof App);
	}
	
	@Test
	public void testCreateObjectWithNoExistingClassShouldReturnAnApp() throws ReflectiveOperationException {
		assertTrue(Runner.createObject("irgendwas") instanceof App);
	}
	
	@Test
	public void testCreateObjectWithEmptyArgumentShouldReturnAnApp() throws ReflectiveOperationException {
		assertTrue(Runner.createObject("") instanceof App);
	}
	
	@Test
	public void testCreateObjectWithNullShouldReturnAnApp() throws ReflectiveOperationException {
		assertTrue(Runner.createObject(null) instanceof App);
	}
	
	@Test
	public void testcreateObjectWODefaultWithAppShouldReturnAnApp() throws ReflectiveOperationException {
		assertTrue(Runner.createObject("App") instanceof App);
	}
	
	
	// separated parseArguments
	
	@Test
	public void parseArgumentsWithCorrectFormat() throws SystemExitException{
		Options runoption = new Options();
		
		// class to be used
		Option inputclass = new Option("c", "input", true, "class to be used");
		inputclass.setRequired(true);
		
		// name of log file
		Option logname = new Option("o", "output", true, "logfile name");
		inputclass.setRequired(true);
		
		runoption.addOption(inputclass);
		runoption.addOption(logname);
		
		String[] args = new String[4];
		args[0] = "-c";
		args[1] = "de.htw.ai.kbe.runmerunner.App";
		args[2] = "-o";
		args[3] = "report.txt";
		
		CommandLine cmd = Runner.parseArguments(runoption, args);
		assertTrue(cmd.hasOption("c") == true && cmd.hasOption("o") == true);
	}
	
	@Test(expected = SystemExitException.class)
	public void parseArgumentsWithIncorrectFirst() throws SystemExitException{
		Options runoption = new Options();
		
		// class to be used
		Option inputclass = new Option("c", "input", true, "class to be used");
		inputclass.setRequired(true);
		
		// name of log file
		Option logname = new Option("o", "output", true, "logfile name");
		inputclass.setRequired(true);
		
		runoption.addOption(inputclass);
		runoption.addOption(logname);
		
		String[] args = new String[4];
		args[0] = "";
		args[1] = "de.htw.ai.kbe.runmerunner.App";
		args[2] = "-o";
		args[3] = "report.txt";
		
		CommandLine cmd = Runner.parseArguments(runoption, args);
	}
	
	@Test
	public void parseArgumentsWithIncorrectSecoundUsesDefault() throws SystemExitException{
		Options runoption = new Options();
		
		// class to be used
		Option inputclass = new Option("c", "input", true, "class to be used");
		inputclass.setRequired(true);
		
		// name of log file
		Option logname = new Option("o", "output", true, "logfile name");
		inputclass.setRequired(true);
		
		runoption.addOption(inputclass);
		runoption.addOption(logname);
		
		String[] args = new String[4];
		args[0] = "-c";
		args[1] = "";
		args[2] = "-o";
		args[3] = "report.txt";
		
		CommandLine cmd = Runner.parseArguments(runoption, args);
		assertTrue(cmd.hasOption("c") == true && cmd.hasOption("o") == true);
	}
	
	@Test(expected = SystemExitException.class)
	public void parseArgumentsWithIncorrectThird() throws SystemExitException{
		Options runoption = new Options();
		
		// class to be used
		Option inputclass = new Option("c", "input", true, "class to be used");
		inputclass.setRequired(true);
		
		// name of log file
		Option logname = new Option("o", "output", true, "logfile name");
		inputclass.setRequired(true);
		
		runoption.addOption(inputclass);
		runoption.addOption(logname);
		
		String[] args = new String[4];
		args[0] = "-c";
		args[1] = "de.htw.ai.kbe.runmerunner.App";
		args[2] = "";
		args[3] = "report.txt";
		
		CommandLine cmd = Runner.parseArguments(runoption, args);
		assertTrue(cmd.hasOption("c") == true && cmd.hasOption("o") == true);
	}
	
	@Test
	public void parseArgumentsWithIncorrectFourthUsesDefault() throws SystemExitException{
		Options runoption = new Options();
		
		// class to be used
		Option inputclass = new Option("c", "input", true, "class to be used");
		inputclass.setRequired(true);
		
		// name of log file
		Option logname = new Option("o", "output", true, "logfile name");
		inputclass.setRequired(true);
		
		runoption.addOption(inputclass);
		runoption.addOption(logname);
		
		String[] args = new String[4];
		args[0] = "-c";
		args[1] = "de.htw.ai.kbe.runmerunner.App";
		args[2] = "-o";
		args[3] = "";
		
		CommandLine cmd = Runner.parseArguments(runoption, args);
		assertTrue(cmd.hasOption("c") == true && cmd.hasOption("o") == true);
	}
	
	// separated writeToLogFile
	
	@Test
	public void writingTestStringToFileShouldBePresentInFile() {
		
		String teststring = "This is a test string.";
		String filename = "test_report.txt";
		String result = null;
		
		try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"))) {
			writer.write(teststring);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try(FileInputStream inputStream = new FileInputStream("test_report.txt")) {     
		    result = IOUtils.toString(inputStream, "UTF-8");
		    // do something with everything string
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(result);
		assertEquals(result.toString(), "This is a test string.");
	}
	
	
	@Test
	public void runItWithProperArgumentsShouldGivePeferctStringInFile() throws SystemExitException {
		
		String[] args = new String[4];
		args[0] = "-c";
		args[1] = "de.htw.ai.kbe.runmerunner.App";
		args[2] = "-o";
		args[3] = "report.txt";
		
		Runner.main(args);
		
		String result = null;
		try(FileInputStream inputStream = new FileInputStream("report.txt")) {     
		    result = IOUtils.toString(inputStream, "UTF-8");
		    // do something with everything string
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("result++++++++++++++++++++++++++ " + result);
		
		// chronology of entries differs
		assertTrue(result.toString().contains("Methods with @RunMe: \r\n" + 
				"Methodenname: findMe3\r\n" + 
				"Modifizierer: private\r\n" + 
				"Returntype: void\r\n" + 
				"\r\n" + 
				"nicht invokierbare Methoden: findMe3\r\n" + 
				"class de.htw.ai.kbe.runmerunner.Runner cannot access a member of class de.htw.ai.kbe.runmerunner.App with modifiers \"private\"\r\n" + 
				"Methodenname: findMe4\r\n" + 
				"Modifizierer: public\r\n" + 
				"Returntype: void\r\n" + 
				"Methodenname: findMe2\r\n" + 
				"Modifizierer: public\r\n" + 
				"Returntype: void\r\n" + 
				"Methodenname: findMe1\r\n" + 
				"Modifizierer: public\r\n" + 
				"Returntype: void\r\n" + 
				"Methods without @RunMe: \r\n" + 
				"Methodenname: testWithoutRM\r\n" + 
				"Modifizierer: public\r\n" + 
				"Returntype: void\r\n" + 
				"Methodenname: testNoRM22\r\n" + 
				"Modifizierer: public\r\n" + 
				"Returntype: void"));
	}
	
}


 