package com.zy.foundation.httpclient;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class CompletableFutureTest {

    private static final BiFunction<Integer, Integer, Integer> sumFun = Integer::sum;
    private static final BiFunction<Integer, Integer, Integer> minusFun = (integer, integer2) -> integer - integer2;
    private static final BiFunction<Integer, Integer, Integer> plusFun = (integer, integer2) -> integer * integer2;

    public static void main(String[] args) throws Exception {
        thenApply();
        thenCompose();
        thenCombine();
    }

    /**
     * thenApply（）转换的是泛型中的类型，是同一个CompletableFuture；
     * thenCompose（）用来连接两个CompletableFuture，是生成一个新的CompletableFuture。
     * <p>
     * 他们都是让CompletableFuture可以对返回的结果进行后续操作，就像流一样进行map和flatMap的装换。
     *
     * @throws Exception
     */
    private static void thenApply() throws Exception {
        int x = 5, y = 4, z = 3, m = 2;
        Integer result = CompletableFuture.supplyAsync(() -> sumFun.apply(x, y))
                .thenApply(sum -> minusFun.apply(sum, z))
                .thenApply(minus -> plusFun.apply(minus, m))
                .get();
        System.out.println("1.thenApply -----> " + result);
    }

    private static void thenCompose() throws Exception {
        int x = 5, y = 4, z = 3, m = 2;
        Integer result = CompletableFuture.supplyAsync(() -> sumFun.apply(x, y))
                .thenCompose(sum -> CompletableFuture.supplyAsync(() -> minusFun.apply(sum, z)))
                .thenCompose(minus -> CompletableFuture.supplyAsync(() -> plusFun.apply(minus, m)))
                .get();
        System.out.println("2.thenCompose -----> " + result);
    }

    /**
     * thenCombine会在两个任务都执行完成后，把两个任务的结果合并。
     * <p>
     * 注意：
     * 两个任务中只要有一个执行异常，则将该异常信息作为指定任务的执行结果。
     * 两个任务是并行执行的，它们之间并没有先后依赖顺序。
     *
     * @throws Exception
     */
    private static void thenCombine() throws Exception {
        CompletableFuture<Dept> deptFuture = CompletableFuture.supplyAsync(() -> {
                    // return deptService.getById(1);
                    return new Dept(1L, "研发部");
                }
        );

        // 第2个任务：获取id=1的人员
        CompletableFuture<User> userFuture = CompletableFuture.supplyAsync(() -> {
            // int a = 1 / 0; //出了异常就报错
            // return userService.getById(1);
            return new User(10001L, "tom");
        });

        // 将上面2个任务的返回结果 dept 和 user 合并，返回新的user
        CompletableFuture<User> resultFuture = deptFuture.thenCombine(userFuture,
                (dept, user) -> {
                    user.setDeptId(dept.getId());
                    user.setDeptName(dept.getName());
                    // return userService.save(user);
                    return user;
                }
        );

        System.out.println("线程：" + Thread.currentThread().getName() + " 结果：" + resultFuture.get());
    }

    @Data
    private static class User {
        private Long id;
        private String name;
        private Long deptId;
        private String deptName;

        public User(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @AllArgsConstructor
    @Data
    private static class Dept {
        private Long id;
        private String name;
    }

}
