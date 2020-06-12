package batch.dbus.sink;

import batch.dbus.config.GlobalConfig;
import batch.dbus.model.Flow;
import com.alibaba.otter.canal.protocol.FlatMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.hadoop.hbase.HBaseConfiguration;
import scala.Tuple2;

/**
 * @author Duncan
 * @version 1.0
 * @ClassName HbaseSyncSink
 * @Descripyion TODO
 * @date 2020 2020/2/5 23:24
 */
@Slf4j
public class HbaseSyncSink extends RichSinkFunction<Tuple2<FlatMessage, Flow>> {
    private HbaseSyncService hbaseSyncService;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        org.apache.hadoop.conf.Configuration hbaseConfig = HBaseConfiguration.create();
        hbaseConfig.set("hbase.zookeeper.quorum", GlobalConfig.HBASE_ZOOKEEPER_QUORUM);
        hbaseConfig.set("hbase.zookeeper.property.clientPort", GlobalConfig.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT);
        hbaseConfig.set("zookeeper.znode.parent", GlobalConfig.ZOOKEEPER_ZNODE_PARENT);

        HbaseTemplate hbaseTemplate = new HbaseTemplate(hbaseConfig);
        hbaseSyncService = new HbaseSyncService(hbaseTemplate);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void invoke(Tuple2<FlatMessage, Flow> value, Context context) throws Exception {
        hbaseSyncService.sync(value._2, value._1);
    }
}
