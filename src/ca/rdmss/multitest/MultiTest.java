package ca.rdmss.multitest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MultiTest {
	
	public int repeatNo() default 1;

	public String threadSet() default "1";
	
	public boolean newInstance() default false;
}
