package de.htw.ai.kbe.runmerunner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;

/**
 * Annotation RunMe for testing.
 * @author Dustin
 *
 */
@Retention(RetentionPolicy.RUNTIME) // zur Laufzeit
@Target(ElementType.METHOD)			// nur fuer Methoden
@Documented
public @interface RunMe {
	String issueId() default "No issueID";
	String description() default "No Description";
	String author() default "Dustin";
	String date() default "April 08, 2018";
}
