package de.htw.ai.kbe.runmerunner;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.cli.*;
import de.htw.ai.kbe.runmerunner.SystemExitException;

public class Runner {
	
	private static Object classobj;
	private static final String FILENAME = "report.txt";
	
	
	public static void main(String[] args) {
	
		Options runoption = new Options();
		
		// class to be used
		Option inputclass = new Option("c", "input", true, "class to be used");
		inputclass.setRequired(true);
		
		// name of log file
		Option logname = new Option("o", "output", true, "logfile name");
		inputclass.setRequired(true);
		
		runoption.addOption(inputclass);
		runoption.addOption(logname);
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;
		
		try {
			cmd = parser.parse(runoption, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("runmerunner", runoption);
			throw new SystemExitException(1);
		}
		
		try {
			classobj = Runner.createObject(args[1]);
		} catch(ReflectiveOperationException e) {
			
		}
		
		Method[] methodsOfApp = classobj.getClass().getDeclaredMethods();
		String result = "";
		
		// Methoden aus Klasse, die per Argument uebergeben wurde
		for(Method m : methodsOfApp) {
			if(m.isAnnotationPresent(RunMe.class)) {
				result = result + "Methodenname: " + m.getName() + System.lineSeparator() + 
						"Modifizierer: " + Modifier.toString(m.getModifiers()) + System.lineSeparator() + 
								"Returntype: " + m.getReturnType() + System.lineSeparator();
				System.out.println(result);
			}
		}
		
		try {
			writeToLogfile(result);
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	
	}
	
    /**
     * erzeugt Object der im Argument definierten Klasse
     * @author E.Schueler
     * @param className
     * @return
     * @throws ReflectiveOperationException
     */
	public static Object createObject(String className) throws ReflectiveOperationException {
	
		if(className == null || className.trim().isEmpty()) {
		    className = "de.htw.ai.kbe.App"; //setze einen default-Wert
		    System.out.println("createObject mit Default Wert aufgerufen");
		}
		else className = "de.htw.ai.kbe.runmerunner." + className; //per Hand groupID setzen..?
				
		System.out.println("in create: " + className);
		Class<?> c = null;
		
        try {
            c = Class.forName(className);
            
        } catch (ClassNotFoundException e) {
            System.out.println("Class '" + className + "' not found, " + "using 'de.htw.ai.kbe.runmerunner.App");
            
            try {
                c = Class.forName("de.htw.ai.kbe.runmerunner.App");
                
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
                return null;
            }
        }
		
        // class.newInstance() depricated
        return c.getDeclaredConstructor().newInstance();
     
	}
	
	/**
	 * writes String to file 
	 * @param result
	 * @throws IOException
	 */
	private static void writeToLogfile(String result) throws IOException {
		try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILENAME), "utf-8"))) {
			writer.write(result);
		}
	}
}
	
	
