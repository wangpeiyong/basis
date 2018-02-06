package org.wpy.lambda;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * JDK Option操作：
 */
public class TestLambda2 {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> rangeArray(100, 100)).exceptionally(e -> {
            e.printStackTrace();
            return null;
        }).thenApply(a -> a.map(i -> (char) i.intValue())).thenAccept(a -> a.forEach(System.out::println));
        completableFuture.get();
    }


    @Test
    public void testRandom() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.println(random.nextInt(1000));
        }
    }


    /**
     * User::getName 直接对象中的非静态方法。
     */
    @Test
    public void test2() {
        List<User> data = IntStream.range(1, 10).mapToObj(a -> new User("wang_" + a, 23 + a)).collect(Collectors.toList());
        data.stream().map(User::getName).map(String::toUpperCase).sorted().peek(System.out::println).collect(Collectors.toList());
    }


    public Stream<Integer> rangeArray(int length, int bound) {
        List<Integer> data = new ArrayList<>(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            data.add(random.nextInt(bound));
        }
        return data.stream();
    }

}

class User {
    private String name;
    private Integer age;

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
