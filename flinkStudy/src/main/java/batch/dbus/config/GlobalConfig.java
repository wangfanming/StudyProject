package batch.dbus.config;

public class GlobalConfig {
    /**
     * 数据库driver class
     */
//  mysql 8.0以下版本的连接方式
//    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
//    public static final String DB_URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";

//  mysql 8.0及以上版本的连接方式
    public static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC";

    /**
     * 数据库user name
     */
    public static final String USER_MAME = "wfm";
    /**
     * 数据库password
     */
    public static final String PASSWORD = "123456";
    /**
     * 批量提交size
     */
    public static final int BATCH_SIZE = 2;

    //HBase相关配置
    public static final String HBASE_ZOOKEEPER_QUORUM = "centos7";
    public static final String HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT = "2181";
    public static final String ZOOKEEPER_ZNODE_PARENT = "/hbase";
}
