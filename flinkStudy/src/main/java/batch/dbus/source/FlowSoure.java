package batch.dbus.source;

import batch.dbus.model.Flow;
import batch.dbus.utils.JdbcUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Duncan
 * @version 1.0
 * @ClassName FlowSoure
 * @Descripyion TODO
 * @date 2020 2020/2/5 23:56
 */
@Slf4j
public class FlowSoure extends RichSourceFunction {

    private static final long serialVersionUID = -4151455351863065475L;

    //状态位
    private volatile boolean isRunning = true; //值一旦改变，则全局都会更新该值
    private String query = "select * from test.dbus_flow";
    private Flow flow = new Flow();

    @Override
    public void run(SourceContext ctx) throws Exception {
        //定时读取数据库的flow表，生成Flow数据
        while (isRunning) {
            Connection connection = null;
            Statement statement = null;
            ResultSet rs = null;
            try {
                connection = JdbcUtil.getConnection();
                connection.createStatement();
                rs = statement.executeQuery(query);
                while (rs.next()) {
                    flow.setFlowId(rs.getInt("flowId"));
                    flow.setMode(rs.getInt("mode"));
                    flow.setDatabaseName(rs.getString("databaseName"));
                    flow.setTableName(rs.getString("tableName"));
                    flow.setHbaseTable(rs.getString("hbaseTable"));
                    flow.setFamily(rs.getString("family"));
                    flow.setUppercaseQualifier(rs.getBoolean("uppercaseQualifier"));
                    flow.setCommitBatch(rs.getInt("commitBatch"));
                    flow.setStatus(rs.getInt("status"));
                    flow.setRowKey(rs.getString("rowKey"));
                    log.info("load flow: " + flow.toString());
                    ctx.collect(flow);
                }
            } finally {
                JdbcUtil.close(rs, statement, connection);
            }
            //隔一段时间读取，可以使用更新的配置生效
            Thread.sleep(60 * 1000L);

        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
