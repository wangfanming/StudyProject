package batch.API;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple3;

public class DistinctDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<Tuple3<Long, String, Integer>> ds = env.fromElements(Tuple3.of(1L, "zhangsan", 28),
                Tuple3.of(3L, "lisi", 34),
                Tuple3.of(3L, "wangwu", 23),
                Tuple3.of(3L, "zhaoliu", 34),
                Tuple3.of(3L, "lili", 25));

        ds.distinct(0).print();
    }
}
