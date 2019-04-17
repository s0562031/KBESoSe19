package de.htw.ai.kbe.runmerunner;

public class SystemExitException extends SecurityException {
    	
        public final int status;
        
        public SystemExitException(int status) {
            this.status = status;
        }
}
