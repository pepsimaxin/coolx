package coolx.appcompat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Just for coolx internal code check mark.
 *
 * @author  Marco
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface CAD {
}
