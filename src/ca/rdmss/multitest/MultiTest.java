package ca.rdmss.multitest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MultiTest {
	
	public int repeatNo() default 0;

	public String threadSet() default "";
	
	public NewInstance newInstance() default NewInstance.False;
	
	public ExecutionOrder execute() default ExecutionOrder.Sequentially;
}
