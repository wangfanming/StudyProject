package batch.API;

import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DistributeCacheDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.registerCachedFile("file:///D:\\Study\\workspace\\StudyProject\\flinkStudy\\src\\main\\resources\\user.txt", "localfile", true);

        //准备用户游戏充值数据
        ArrayList<Tuple2<String, Integer>> data = new ArrayList<>();
        data.add(new Tuple2<>("101", 2000000));
        data.add(new Tuple2<>("102", 190000));
        data.add(new Tuple2<>("103", 1090000));

        DataSet<Tuple2<String, Integer>> ds = env.fromCollection(data);

        DataSet<String> result = ds.map(new RichMapFunction<Tuple2<String, Integer>, String>() {

            HashMap<String, String> allMaps = new HashMap<String, String>();

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                File localfile = getRuntimeContext().getDistributedCache().getFile("localfile");

                List<String> lines = FileUtils.readLines(localfile);

                for (String line : lines) {
                    String[] split = line.split(",");
                    allMaps.put(split[0], split[1]);
                }

            }

            @Override
            public String map(Tuple2<String, Integer> tuple2) throws Exception {
                String money = allMaps.get(tuple2.f0);
                return tuple2.f0 + "," + tuple2.f1 + Integer.parseInt(money);
            }
        });

        result.print();

    }
}
