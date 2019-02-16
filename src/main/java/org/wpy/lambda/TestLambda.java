package org.wpy.lambda;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * JDK Option操作：
 */
public class TestLambda {

    public static void main(String[] args) {
        List<Integer> data = new ArrayList<>(12);
        for (int i = 0; i < 12; i++)
            data.add(i);

        Integer result = data.stream()
                .map(a -> a * a)
                .filter(a -> a > 20)
                .reduce((a, b) -> a + b)
                .flatMap(a -> Optional.of(a * 100))
                .orElseGet(null);
        System.out.println(result + " size:" + data.size());


        LongStream longStream = data.parallelStream()
                .map(a -> a * a)
                .filter(a -> a > 20)
                .distinct()
                .skip(1)
                .limit(5)
                .peek(a -> a += 20) //int为直接引用，则该操作无效
                .map(a -> a += 20)
                .mapToLong(a -> Long.valueOf(a.toString()));
        System.out.println(longStream.sum());


        Stream<Integer> result2 = data.stream()
                .map(a -> a * a)
                .filter(a -> a > 20);

        System.out.println(result2.count());
        //IllegalStateException : stream has already been operated upon or closed    Stream只能使用一次
        //System.out.println(result2.collect(Collectors.toSet()));


        /**
         * jdk 8 的多种stream表达式
         */
        String[] array = {"wang", "pei", "yong"};
        String data1 = Arrays.stream(array).map(String::toUpperCase).collect(Collectors.joining(",", "[", "]"));
        System.out.println(data1);

        System.out.println(Arrays.stream(array).map(String::toUpperCase).reduce(new StringBuffer(), (a, b) -> a.append(b), (a, b) -> a.append(b)).toString());

        System.out.println(Arrays.stream(array).map(String::toUpperCase).max(Comparator.naturalOrder()).orElseGet(null));

        int[] nums = {1, 2, 3, 34, 11, 1, 12, 2, 23};
        System.out.println(Arrays.stream(nums).average().orElseGet(null));
        System.out.println(Arrays.stream(array).collect(() -> new StringBuffer(), (a, b) -> a.append(b), (a, b) -> a.append(b)).insert(0, "[").append("]"));

        List<Albums> albums = new ArrayList<>();

        albums.add(new Albums("zhoujielun", "xunzhaozhoujielun", Arrays.asList("YIFUZHIMING", "SHUANGDAO", "BABAHUILAILE")));
        albums.add(new Albums("zhoujielun", "AIZAIXIYUANQIAN", Arrays.asList("LONGJUANFENG", "SANNIANERBAN", "QINGTIAN", "TADEJIEMAO")));

        albums.stream().collect(groupingBy(a -> a.getName(), counting())).forEach((k, v) -> System.out.println(k + "  " + v));
        albums.stream().collect(groupingBy(a -> a.getName())).forEach((k, v) -> System.out.println(k + "  " + v.stream().flatMap(a -> a.getSongs().stream()).collect(Collectors.joining(",", "[", "]"))));

        // TODO: 2017/7/11   groupingBy( averagingInt() ) :    select album ,avg(song) from albums
        albums.stream().collect(groupingBy(a -> a.getName(), averagingInt(a -> a.getSongs().size()))).forEach((k, v) -> System.out.println(k + "  " + v));

        // TODO: 2017/7/11   groupingBy( summingInt() ) :     select album ,count(song) from albums
        albums.stream().collect(groupingBy(a -> a.getName(), summingInt(a -> a.getSongs().size()))).forEach((k, v) -> System.out.println(k + "  " + v));

        // TODO: 2017/7/11   groupingBy( summarizingInt() ) :     select album ,summarizingInt(song) from albums     summarizingInt各种song的统计信息
        albums.stream().collect(groupingBy(a -> a.getName(), summarizingInt(a -> a.getSongs().size()))).forEach((k, v) -> System.out.println(k + "  " + v));

        // IntSummaryStatistics 得到int\double的统计数据
        IntSummaryStatistics intSummaryStatistics = albums.stream().mapToInt(a -> a.getSongs().size()).summaryStatistics();
        System.out.println(intSummaryStatistics.getAverage() + "  " + intSummaryStatistics.getCount() + "  " + intSummaryStatistics.getSum());

        // toCollection 收集到新的容器中。   parallelStream 只有在大数据的时候才能使用，不然线程同步会更慢。
        HashSet<Albums> set = albums.parallelStream().collect(toCollection(HashSet::new));
        set.forEach(System.out::println);
        IntStream.range(0, 1000).parallel().forEach(System.out::println);
        IntStream.range(0, 1001).parallel().mapToObj(a -> new TwoDice(a % 2, 1)).collect(groupingBy(a -> a.getCoin(), summingInt(a -> a.getNum())))
                .forEach((k, v) -> System.out.println(k + "  " + v));

    }

}

class TwoDice {
    private int coin;
    private int num;

    public TwoDice(int coin, int num) {
        this.coin = coin;
        this.num = num;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}

class Albums {
    private String singer;
    private String name;
    private List<String> songs;

    public Albums(String singer, String name, List<String> songs) {
        this.singer = singer;
        this.name = name;
        this.songs = songs;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSongs() {
        return songs;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }

    @Override
    public String toString() {
        return "Albums{" + "singer='" + singer + '\'' + ", name='" + name + '\'' + ", songs=" + songs + '}';
    }
}
