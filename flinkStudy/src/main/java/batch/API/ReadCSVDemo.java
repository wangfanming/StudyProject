package batch.API;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple3;

public class ReadCSVDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<Tuple3<Integer, Integer, String>> cvsDS = env.readCsvFile("D:\\Study\\workspace\\StudyProject\\flinkStudy\\src\\main\\resources\\user.txt")
                .includeFields("11100")
                .ignoreFirstLine()
                .ignoreComments("##")
                .lineDelimiter("\n")
                .fieldDelimiter(",")
                .types(Integer.class, Integer.class, String.class);

        cvsDS.print();
    }
}
