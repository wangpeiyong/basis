package org.wpy.annotation;

import java.lang.annotation.*;

/**
 * @author wpy
 * @version V1.0
 * @Description: TODO
 * @Package org.wpy.annotation
 * @date 2017/12/3 22:24
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Inherited
@ParentBean
public @interface SonBean {
}
