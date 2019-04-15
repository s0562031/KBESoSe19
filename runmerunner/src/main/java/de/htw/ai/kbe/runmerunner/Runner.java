package de.htw.ai.kbe.runmerunner;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.cli.*;
import de.htw.ai.kbe.runmerunner.SystemExitException;

/**
 * Loads class by name if present in package and writes all declared
 * methods into logfile named by user.
 * @author Dustin
 *
 */
public class Runner {
	
	/**
	 * name of class
	 */
	private static Object classobj;
	
	/**
	 * name of logfile
	 */
	private static String filename = "report.txt";
	
	
	public static void main(String[] args) throws SystemExitException {
	
		Options runoption = new Options();
		
		// class to be used
		Option inputclass = new Option("c", "input", true, "class to be used");
		inputclass.setRequired(true);
		
		// name of log file
		Option logname = new Option("o", "output", true, "logfile name");
		inputclass.setRequired(true);
		
		runoption.addOption(inputclass);
		runoption.addOption(logname);
		
		CommandLine cmd = null;
		
		
		cmd = parseArguments(runoption, args);		
		setClassObjAndFilename(args, cmd);		
		logAnnotatedMethods();		
			
	}
	
	/**
	 * Reads all declared methods with RunMe annotation and without and writes them to via arguments
	 * given file. 
	 */
	private static void logAnnotatedMethods() {
		
		Method[] methodsOfApp = classobj.getClass().getDeclaredMethods();
		String result = "Methods with @RunMe: " + System.lineSeparator();
		
		// annotated
		for(Method m : methodsOfApp) {
			
			if(m.isAnnotationPresent(RunMe.class)) {
				result = result + "Methodenname: " + m.getName() + System.lineSeparator() + 
						"Modifizierer: " + Modifier.toString(m.getModifiers()) + System.lineSeparator() + 
								"Returntype: " + m.getReturnType() + System.lineSeparator();
				try {
					m.invoke(classobj, new Object[] {});
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					String failer = System.lineSeparator() + "nicht invokierbare Methoden: " + m.getName() + System.lineSeparator();
					result = result + failer + e.getMessage() + System.lineSeparator();
					//System.out.println("Illegal Access");
					//e.printStackTrace();
				}
								
				
			}				
		}
		
		result = result + "Methods without @RunMe: " + System.lineSeparator();
		
		// not annotated
		for(Method m : methodsOfApp) {
			
			if(!m.isAnnotationPresent(RunMe.class)) {
				result = result + "Methodenname: " + m.getName() + System.lineSeparator() + 
						"Modifizierer: " + Modifier.toString(m.getModifiers()) + System.lineSeparator() + 
								"Returntype: " + m.getReturnType() + System.lineSeparator();
				try {
					m.invoke(classobj, new Object[] {});
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					String failer = System.lineSeparator() + "nicht invokierbare Methoden: " + m.getName() + System.lineSeparator();
					result = result + failer + e.getMessage() + System.lineSeparator();
					//System.out.println("Illegal Access");
					//e.printStackTrace();
				}
			}				
		}
		
		System.out.println(result);
						
		try {
			writeToLogfile(result);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets classObj and filename from Arguments, checks if any Arguments are given.
	 * If not catches ReflectiveOperationException or NullPointerException and exits.
	 * @param args
	 */
	 static void setClassObjAndFilename(String[] args, CommandLine cmd) throws SystemExitException{
		
		try {
			classobj = Runner.createObject(cmd.getOptionValue("c"));
			filename = cmd.getOptionValue("o");
		} catch(ReflectiveOperationException e) {
			//e.printStackTrace();
			throw new SystemExitException(1);
		} catch(NullPointerException e) {
			//e.printStackTrace();
			System.out.println("CommandLine Object not properly initialized.");
			throw new SystemExitException(1);
		}
		
	}
	
	/**
	 * Parses Arguments to CommandLine Object. Validates if format matches in main defined
	 * runoption.
	 * @param 	runoption				Required format of arguments.
	 * @param 	args					Given arguments.
	 * @return
	 * @throws 	SystemExitException		If required does not matches given.
	 */
	static CommandLine parseArguments(Options runoption, String[] args) throws SystemExitException {
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;
		
		try {
			cmd = parser.parse(runoption, args);
		} catch (ParseException e) {
			//e.printStackTrace();
			formatter.printHelp("runmerunner", runoption);
			//System.exit(1);
			throw new SystemExitException(1);
		} 
		
		if(cmd.hasOption("c") && cmd.hasOption("o")) return cmd;
		else throw new SystemExitException(1);
	}
	
    /**
     * Creates object of given classname if possible.
     * @param className							Given Classname.
     * @return									New Instance of declaredConstructor.
     * @throws ReflectiveOperationException		
     */
	public static Object createObject(String className) throws ReflectiveOperationException {
	
		if(className == null || className.trim().isEmpty()) {
		    className = "de.htw.ai.kbe.runmerunner.App"; //setze default-Wert
		    System.out.println("createObject mit Default Wert aufgerufen");
		}
				
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
	
	/*
	public static Object createObjectFromSysProp() throws ReflectiveOperationException {
		
		String className = System.getProperty("App.implementation.class", "de.htw.ai.kbe.runmerunner.App");
				
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
	*/
	
	/**
	 * writes String to file 
	 * @param result
	 * @throws IOException
	 */
	private static void writeToLogfile(String result) throws IOException {
		try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"))) {
			writer.write(result);
		}
	}
}
	
	
