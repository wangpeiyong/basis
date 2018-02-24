# CGLib、JDK代理的区别：
    1、JDK的代理实现需要接口，对接口做动态代理。
       CGLib的动态代理是代理类的实现的。CGLib采用了非常底层的字节码技术，其原理是通过字节码技术为一个类创建子类，并在子类中采用方法拦截的技术拦截所有父类方法的调用，顺势织入横切逻辑。
       JDK动态代理与CGLib动态代理均是实现Spring AOP的基础。（JDK 接口的代理，CGlib类的子类的动态代理）
       
    2、CGLib创建的动态代理对象性能比JDK创建的动态代理对象的性能高不少，但是CGLib在创建代理对象时所花费的时间却比JDK多得多，所以对于单例的对象，因为无需频繁创建对象，用CGLib合适，
      反之，使用JDK方式要更为合适一些。同时，由于CGLib由于是采用动态创建子类的方法，对于final方法，无法进行代理。
      
    3、单例的对象适合CGLib动态代理。final函数不能被代理，只能运行方法。（因为子类不能继承父类的final成员）
    
    
    总结：JDK实现接口来代理对象，CGlib子类继承代理的对象。 