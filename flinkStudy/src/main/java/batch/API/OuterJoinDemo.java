package batch.API;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;

import java.util.ArrayList;

public class OuterJoinDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        ArrayList<Tuple2<Integer, String>> list1 = new ArrayList<>();
        list1.add(new Tuple2<>(1, "lily"));
        list1.add(new Tuple2<>(2, "lucy"));
        list1.add(new Tuple2<>(4, "jack"));

        ArrayList<Tuple2<Integer, String>> list2 = new ArrayList<>();
        list2.add(new Tuple2<>(1, "beijing"));
        list2.add(new Tuple2<>(2, "shanghai"));
        list2.add(new Tuple2<>(3, "guangzhou"));

        DataSet<Tuple2<Integer, String>> ds1 = env.fromCollection(list1);
        DataSet<Tuple2<Integer, String>> ds2 = env.fromCollection(list2);

        // 左外连接 ,ds1在ds2中没有join到的行，ds1的列都有值，ds2的列为null
//        ds1.leftOuterJoin(ds2)
//                .where(0)
//                .equalTo(0)
//                .with(new JoinFunction<Tuple2<Integer, String>, Tuple2<Integer, String>, Tuple3<Integer,String,String>>() {
//                    @Override
//                    public Tuple3<Integer, String, String> join(Tuple2<Integer, String> first, Tuple2<Integer, String> second) throws Exception {
//                        if (second == null){
//                            return new Tuple3<>(first.f0,first.f1,"null");
//                        }else{
//                            return new Tuple3<>(first.f0,first.f1,second.f1);
//                        }
//                    }
//                }).print();

        //右外连接 相当于ds的左外连接
//        ds1.rightOuterJoin(ds2)
//                .where(0)
//                .equalTo(0)
//                .with(new JoinFunction<Tuple2<Integer, String>, Tuple2<Integer, String>, Tuple3<Integer,String,String>>() {
//                    @Override
//                    public Tuple3<Integer, String, String> join(Tuple2<Integer, String> first, Tuple2<Integer, String> second) throws Exception {
//                        if (first == null){
//                            return new Tuple3<>(second.f0,"null",second.f1);
//                        }else{
//                            return new Tuple3<>(first.f0,first.f1,second.f1);
//                        }
//                    }
//                }).print();

        //全连接 ds1 ds2 都可能为null
        ds1.fullOuterJoin(ds2)
                .where(0)
                .equalTo(0)
                .with(new JoinFunction<Tuple2<Integer, String>, Tuple2<Integer, String>, Tuple3<Integer, String, String>>() {
                    @Override
                    public Tuple3<Integer, String, String> join(Tuple2<Integer, String> first, Tuple2<Integer, String> second) throws Exception {
                        if (first == null) {
                            return new Tuple3<>(second.f0, "null", second.f1);
                        } else if (second == null) {
                            return new Tuple3<>(first.f0, first.f1, "null");
                        } else {
                            return new Tuple3<>(first.f0, first.f1, second.f1);
                        }
                    }
                }).print();


    }
}
