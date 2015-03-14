package org.arg.pushforth.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// annotates that the return type of the method, assumed to be a Program, has multiple elements and 
// that they should be put on the stack one-by-one

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Unpack {

}
