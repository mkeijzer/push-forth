package org.arg.pushforth.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the invariant for an instruction, something that must hold
 * @author keijm1
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Invariant {
	public Class<?> forall();
	public String program();
}
