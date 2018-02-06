package org.wpy.annotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wpy
 * @version V1.0
 * @Description: TODO
 * @Package org.wpy.annotation
 * @date 2017/12/3 22:27
 */
public class Main {

    @SonBean
    private String name;

    public static void main(String[] args) throws NoSuchFieldException {
        final SonBean name = Main.class.getDeclaredField("name").getAnnotation(SonBean.class);

        Class<? extends Annotation> annotationType = Main.class.getDeclaredField("name").getAnnotation(SonBean.class).annotationType();

        Arrays.stream(name.annotationType().getAnnotations()).forEach(System.out::println);
        System.out.println(annotationType.getAnnotation(ParentBean.class));
        System.out.println(annotationType);
        ArrayList<Class> objects = new ArrayList<>(9);


        printTree(SonBean2.class, objects);
        objects.forEach(System.err::println);
    }


    public static void printTree(Class annotationType, List<Class> list) {
        final Annotation[] annotations = annotationType.getAnnotations();
        for (Annotation annotation : annotations) {
            if (!annotation.annotationType().getName().contains("java.lang."))
                printTree(annotation.annotationType(), list);
            list.add(annotation.annotationType());
            //System.err.println("父注解"+annotation.annotationType().getName());
        }
    }


}
