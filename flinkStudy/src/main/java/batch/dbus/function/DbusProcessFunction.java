package batch.dbus.function;

import batch.dbus.enums.FlowStatusEnum;
import batch.dbus.incrementssync.IncrementSyncApp;
import batch.dbus.model.Flow;
import com.alibaba.otter.canal.protocol.FlatMessage;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;

public class DbusProcessFunction extends KeyedBroadcastProcessFunction<String, FlatMessage, Flow, Tuple2<FlatMessage, Flow>> {
    @Override
    public void processElement(FlatMessage value, ReadOnlyContext ctx, Collector<Tuple2<FlatMessage, Flow>> out) throws Exception {
        //获取配置流
        Flow flow = ctx.getBroadcastState(IncrementSyncApp.flowStateDescriptor).get(value.getDatabase() + value.getTable());

        if (null != flow && flow.getStatus() == FlowStatusEnum.FLOWSTATUS_RUNNING.getCode()) {
            out.collect(Tuple2.of(value, flow));
        }
    }

    @Override
    public void processBroadcastElement(Flow flow, Context ctx, Collector<Tuple2<FlatMessage, Flow>> out) throws Exception {
        //获取state状态
        BroadcastState<String, Flow> broadcastState = ctx.getBroadcastState(IncrementSyncApp.flowStateDescriptor);

        //更新状态
        broadcastState.put(flow.getDatabaseName() + flow.getTableName(), flow);
    }
}
