package batch.API;

import org.apache.flink.api.common.functions.MapPartitionFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.util.Collector;

public class MapPartitionDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<Long> ds = env.generateSequence(1, 20);

        ds.mapPartition(new MyMapPartitionFunc()).print();
    }

    private static class MyMapPartitionFunc implements MapPartitionFunction<Long, Long> {
        @Override
        public void mapPartition(Iterable<Long> iterable, Collector<Long> collector) throws Exception {
            long count = 0;
            for (long value : iterable) {
                count++;
            }
            collector.collect(count);
        }
    }
}
