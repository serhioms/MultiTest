package ca.mss.multitest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConcurrentTest {
	
	public int maxTry() default 1;
	
	public boolean newInstancePerTry() default false;

}
