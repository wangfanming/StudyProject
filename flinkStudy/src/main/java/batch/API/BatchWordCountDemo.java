package batch.API;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class BatchWordCountDemo {
    public static void main(String[] args) throws Exception {
        //获取一个执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        //读取输入数据
        DataSource<String> dataSource = env.fromElements("flink flink flink", "spark spark spark");

        //单词词频统计
        AggregateOperator<Tuple2<String, Integer>> sum = dataSource.flatMap(new LineSplitter())
                .groupBy(0)
                .sum(1);

        sum.print();
    }

    public static class LineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {
        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
            for (String word : value.split(" ")) {
                out.collect(new Tuple2<>(word, 1));
            }
        }
    }
}
