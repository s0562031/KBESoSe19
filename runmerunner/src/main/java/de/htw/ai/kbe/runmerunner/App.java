package de.htw.ai.kbe.runmerunner;

/**
 * Annotated Class to be found with Runner.
 * @author Dustin
 *
 */
public class App 
{
	
	// Methoden ohne @RunMe
    public void testWithoutRM() {
    	
    }
    
    public void testNoRM22(){
    	
    }
    
    // Methoden mit @RunMe
    @RunMe
    public void findMe1() {
    	
    }
    
    @RunMe
    public void findMe2() {
    	
    }
    
    // private, daher nicht invokierbar
    @RunMe
    private void findMe3(String test) {
    	
    }
    
    @RunMe
    public void findMe4() {
    	
    }
    
}
