package batch.dbus.sink;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class HbaseTemplate implements Serializable {
    private Configuration hbaseConfig;
    private Connection conn;

    public HbaseTemplate(Configuration hbaseConfig) {
        this.hbaseConfig = hbaseConfig;
        initConn();
    }

    private void initConn() {
        try {
            this.conn = ConnectionFactory.createConnection(hbaseConfig);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        if (conn == null || conn.isAborted() || conn.isClosed()) {
            initConn();
        }
        return this.conn;
    }

    public boolean tableExists(String tableName) {
        try (HBaseAdmin admin = (HBaseAdmin) getConnection().getAdmin()) {
            return admin.tableExists(TableName.valueOf(tableName));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void createTable(String tableName, String... familyNames) {
        try (HBaseAdmin admin = (HBaseAdmin) getConnection().getAdmin()) {
            HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(tableName));
            //添加列簇
            if (familyNames != null) {
                for (String familyName : familyNames) {
                    HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(familyName);
                    desc.addFamily(hColumnDescriptor);
                }
            }
            admin.createTable(desc);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void disableTable(String tableName) {
        try (HBaseAdmin admin = (HBaseAdmin) getConnection().getAdmin()) {
            if (admin.isTableEnabled(tableName)) {
                admin.disableTable(tableName);
            }
            admin.deleteTable(tableName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    public void deleteTable(String tableName) {
        try (HBaseAdmin admin = (HBaseAdmin) getConnection().getAdmin()) {
            admin.disableTable(tableName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    /**
     * @param tableName, hRow
     * @return java.lang.Boolean
     * @Author wangfanming
     * @Description 插入一行数据
     * @Date 22:28 2020/2/5
     */
    public Boolean put(String tableName, HRow hRow) {
        boolean flag = false;
        try {
            HTable table = (HTable) getConnection().getTable(TableName.valueOf(tableName));
            Put put = new Put(hRow.getRowKey());
            for (HRow.HCell hCell : hRow.getCells()) {
                put.addColumn(Bytes.toBytes(hCell.getFamily()), Bytes.toBytes(hCell.getQualifier()), hCell.getValue());
            }
            table.put(put);
            flag = true;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        }
        return flag;
    }

    /**
     * @param tableName, rows
     * @return java.lang.Boolean
     * @Author wangfanming
     * @Description 批量插入数据
     * @Date 23:21 2020/2/5
     */
    public Boolean puts(String tableName, List<HRow> rows) {
        boolean flag = false;
        try {
            HTable table = (HTable) getConnection().getTable(TableName.valueOf(tableName));
            List<Put> puts = new ArrayList<>();
            System.out.println(tableName + "------------------------------------------------");

            for (HRow hRow : rows) {
                Put put = new Put(hRow.getRowKey());
                for (HRow.HCell hCell : hRow.getCells()) {
                    put.addColumn(Bytes.toBytes(hCell.getFamily()), Bytes.toBytes(hCell.getQualifier()), hCell.getValue());
                }
                puts.add(put);
                if (!puts.isEmpty()) {
                    table.put(puts);
                }
                flag = true;
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return flag;
    }

    /**
     * @param tableName, rowKeys
     * @return java.lang.Boolean
     * @Author wangfanming
     * @Description 批量删除数据
     * @Date 23:22 2020/2/5
     */
    public Boolean deletes(String tableName, Set<byte[]> rowKeys) {
        boolean flag = false;
        try {
            HTable table = (HTable) getConnection().getTable(TableName.valueOf(tableName));
            List<Delete> deletes = new ArrayList<>();
            for (byte[] rowKey : rowKeys) {
                Delete delete = new Delete(rowKey);
                deletes.add(delete);
            }
            if (!deletes.isEmpty()) {
                table.delete(deletes);
            }
            flag = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return flag;
    }

    /**
     * @param
     * @return void
     * @Author wangfanming
     * @Description 关闭连接
     * @Date 23:22 2020/2/5
     */
    public void close() throws IOException {
        if (conn != null) {
            conn.close();
        }
    }
}
