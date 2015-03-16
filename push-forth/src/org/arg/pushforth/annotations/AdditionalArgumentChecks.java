package org.arg.pushforth.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies which predicates to use for additional checking on the arguments. 
 * These checks will take place in addition to normal type checks. 
 * 
 * It is assumed that the String[] returns a fieldname for each argument of the function that this
 * annotation annotates -- the field is expect to be 'public final static'
 * 
 * You can use this for instance to demand that an argument is non-zero, or positive, etc.
 * 
 * See the 'TestDynamicPredicate' test for an example of how this is used
 * 
 * @author Maarten Keijzer
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdditionalArgumentChecks {
	public String[] predicates();
}
