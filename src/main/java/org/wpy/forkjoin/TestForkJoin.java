package org.wpy.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Created by wpy on 2017/3/23.
 */
public class TestForkJoin extends RecursiveTask<Integer> {

    private Integer canFork = 3;

    private Integer start;
    private Integer end;

    public TestForkJoin(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }

    public static void main(String[] args) throws Exception {
        ForkJoinPool forkJoinTask = new ForkJoinPool();

        TestForkJoin testForkJoin = new TestForkJoin(2, 30);

        Future<Integer> future = forkJoinTask.submit(testForkJoin);

        System.out.println(future.get());


    }

    @Override
    protected Integer compute() {
        int sum = 0;

        /**
         * 真正计算任务执行部分
         */
        if ((end - start) < canFork)
            for (int i = start; i < end; i++)
                sum += i;
        else {
            int mid = (start + end) >> 2;

            TestForkJoin testForkJoin1 = new TestForkJoin(start, mid);

            TestForkJoin testForkJoin2 = new TestForkJoin(mid + 1, end);


            /**
             * fork 、join 来实现计算的拆分
             */
            testForkJoin1.fork();
            testForkJoin2.fork();

            int result1 = testForkJoin1.join();
            int result2 = testForkJoin2.join();

            sum = result1 + result2;
        }
        return sum;
    }
}
