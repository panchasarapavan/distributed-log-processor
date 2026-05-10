package info.laughingbuddha.utils;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoUlid {
    /**
     * Whether to use monotonic ULID generation
     * Monotonic ensures ordering within the same millisecond
     */
    boolean monotonic() default true;

    /**
     * Whether to generate only if the field is null
     */
    boolean onlyIfNull() default true;
}
