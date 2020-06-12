package batch.dbus.sink;

import batch.dbus.model.Flow;
import batch.dbus.utils.Md5Utils;
import com.alibaba.otter.canal.protocol.FlatMessage;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HBase同步操作业务
 */
public class HbaseSyncService implements Serializable {
    private HbaseTemplate hbaseTemplate;

    public HbaseSyncService(HbaseTemplate hbaseTemplate) {
        this.hbaseTemplate = hbaseTemplate;
    }

    public void sync(Flow flow, FlatMessage dml) {
        if (flow != null) {
            String type = dml.getType();
            if (type != null && type.equalsIgnoreCase("INSERT")) {

            }
        }
    }

    private void insert(Flow flow, FlatMessage dml) {
        List<Map<String, String>> data = dml.getData();
        if (data == null || data.isEmpty()) {
            return;
        }
        int i = 1;
        boolean complete = false;
        List rows = new ArrayList<>();
        for (Map<String, String> r : data) {
            HRow hRow = new HRow();

            //拼接复合rowkey
            if (flow.getRowKey() != null) {
                String[] rowKeyColumns = flow.getRowKey().trim().split(",");
                String rowKeyValue = getRowKey(rowKeyColumns, r);
                hRow.setRowKey(Bytes.toBytes(rowKeyValue));
            }
            convertDate2Row(flow, hRow, r);

            if (hRow.getRowKey() == null) {
                throw new RuntimeException("empty rowKey: " + hRow.toString() + ",Flow: " + flow.toString());
            }
            rows.add(hRow);
            complete = true;

            if (i % flow.getCommitBatch() == 0 && !rows.isEmpty()) {
                hbaseTemplate.put(flow.getHbaseTable(), hRow);
                rows.clear();
                complete = true;
            }
            i++;
        }
        if (!complete && !rows.isEmpty()) {
            hbaseTemplate.puts(flow.getHbaseTable(), rows);
        }
    }

    /**
     * @param rowKeyColumns, data
     * @return java.lang.String
     * @Author wangfanming
     * @Description 获取复合字段作为rowkey的拼接
     * @Date 22:59 2020/2/5
     */
    private static String getRowKey(String[] rowKeyColumns, Map<String, String> data) {
        StringBuilder rowKeyValue = new StringBuilder();
        for (String rowKeyColumnName : rowKeyColumns) {
            Object obj = data.get(rowKeyColumnName);
            if (obj != null) {
                rowKeyValue.append(obj.toString());
            }
            rowKeyValue.append("|");
        }
        int len = rowKeyValue.length();
        if (len > 0) {
            rowKeyValue.delete(len - 1, len);
        }

        //可自行扩展支持多种rowkey生成策略，这里使用MD5前缀
        return Md5Utils.getMD5String(rowKeyValue.toString()).substring(0, 8) + "_" + rowKeyValue.toString();
    }

    /**
     * @param flow, hRow, data
     * @return
     * @Author wangfanming
     * @Description 将Map数据转换为HRow数据
     * @Date 23:08 2020/2/5
     */
    private static void convertDate2Row(Flow flow, HRow hRow, Map<String, String> data) {
        String familyName = flow.getFamily();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (entry.getValue() != null) {
                byte[] bytes = Bytes.toBytes(entry.getValue().toString());

                String qualifier = entry.getKey();
                if (flow.isUppercaseQualifier()) {
                    qualifier = qualifier.toUpperCase();
                }
                hRow.addCell(familyName, qualifier, bytes);
            }
        }
    }

}
