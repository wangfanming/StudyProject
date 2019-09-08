package watcherStudy;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @Auther: wangfanming
 * @Date: 2019/9/8 15:10
 * @Description:
 */

public class SimpleZKClient {
    private static final String connectString = "10.10.3.210:2181";
    private static final int sessionTimeout = 2000;

    ZooKeeper zkClient = null;

    @Before
    public void init() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent event) {
                //收到事件通知后的回调函数（自定义事件处理逻辑）
                System.out.println(event.getType() + " ------- " + event.getPath());
                try {
                    zkClient.getChildren("/",true);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @Author wangfanming
     * @Description 数据的增删改查
     * @Date 15:52 2019/9/8
     * @Param
     * @return
     **/


    //在zk中创建数据节点
    @Test
    public void testCreate() throws KeeperException, InterruptedException {
        String nodeCreated  = zkClient.create("/test", "hello Zookeeper".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //上传数据可以是任何类型，但都必须转成byte[]
    }

    //判断znode是否存在
    @Test
    public void testExist() throws KeeperException, InterruptedException {
        Stat stat = zkClient.exists("/test", false);

        System.out.println(stat==null ? "not exist !" : "exist !");
    }

    //获取子节点
    @Test
    public void getChildren() throws KeeperException, InterruptedException {
        List<String> children = zkClient.getChildren("/", true);
        for (String child : children){
            System.out.println(child);
        }
        Thread.sleep(Long.MAX_VALUE);
    }

    //获取znode的数据
    @Test
    public void getData() throws KeeperException, InterruptedException {
        byte[] data = zkClient.getData("/test", false, null);
        System.out.println(new String(data));
    }

    //删除znode
    @Test
    public void deleteZnode() throws KeeperException, InterruptedException {
        //参数二：指定要删除的版本，-1表示所有版本
        zkClient.delete("/test",-1);
    }

    @Test
    public void setData() throws Exception {

        zkClient.setData("/app1", "imissyou angelababy".getBytes(), -1);

        byte[] data = zkClient.getData("/app1", false, null);
        System.out.println(new String(data));

    }

}
