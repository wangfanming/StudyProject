package batch.API;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.configuration.Configuration;

public class ParameterDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        //读取数据
        DataSet<Integer> data = env.fromElements(1, 2, 3, 4, 5);

        // 构造函数传递参数
//        data.filter(new MyFilter(3)).print();

        // Configuration传递参数
        Configuration conf = new Configuration();
        conf.setInteger("limit", 3);

        data.filter(new RichFilterFunction<Integer>() {
            private int limit;

            @Override
            public void open(Configuration parameters) throws Exception {
                limit = parameters.getInteger("limit", 0);
            }

            //读取数据
            DataSet<Integer> data = env.fromElements(1, 2, 3, 4, 5);

            @Override
            public boolean filter(Integer value) throws Exception {
                return value > limit;
            }
        }).print();

    }

    private static class MyFilter implements FilterFunction<Integer> {
        private int limit;

        public MyFilter(int i) {
            this.limit = i;
        }

        @Override
        public boolean filter(Integer value) throws Exception {
            return value > limit;
        }
    }
}
