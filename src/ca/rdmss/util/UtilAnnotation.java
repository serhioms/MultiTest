package ca.rdmss.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class UtilAnnotation {

	static public List<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
		
		List<Method> methods = new ArrayList<Method>(12);
		
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(annotation)) {
				methods.add(method);
			}
		}
		
		return methods;
	}
	
	
	static public Method getAnnotatedMethod(Class<?> clazz, Class<? extends Annotation> annotation) {
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(annotation)) {
				return method;
			}
		}
		return null;
	}

}
