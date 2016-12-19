package ch.abacus.application.common.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ColumnDefinition {

	int order();

	String translationKey() default "";

	String sortProperty() default "";

	boolean filterable() default false;
}
