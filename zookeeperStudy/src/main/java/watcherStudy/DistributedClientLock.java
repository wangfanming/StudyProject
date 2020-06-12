package watcherStudy;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @ClassName: DistributedClientLock
 * @Auther: wangfanming
 * @Date: 2019/9/8
 * @Description: TODO
 * @Version 1.0
 */
public class DistributedClientLock {
    //会话超时
    private static final int SESSION_TIMEOUT = 200000;

    //zookeeper 集群地址
    private static final String hosts = "cluster:2181";
    private String groupNode = "locks";
    private String subNode = "sub";
    private boolean haveLock = false;

    private ZooKeeper zk;

    //记录自己创建的子节点路径
    private volatile String thisPath;

    public void connectZookeeper() throws Exception {
        zk = new ZooKeeper(hosts, SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent event) {
                try {
                    //判断事件类型，此处只处理子节点变化事件
                    if (event.getType() == Event.EventType.NodeChildrenChanged && event.getPath().equals("/" + groupNode)) {
                        System.out.println("1 执行了。。。。。。");
                        //获取子节点，并对父节点进行监听
                        List<String> childrenNodes = zk.getChildren("/" + groupNode, true);
                        String thisNode = thisPath.substring(("/" + groupNode + "/").length());

                        //去比较自己的 id 是否最小
                        Collections.sort(childrenNodes);
                        if (childrenNodes.indexOf(thisNode) == 0) {
                            //访问共享资源处理业务，并且在完成处理之后删除锁
                            doSomething();

                            //重新注册一把新锁
                            zk.create("/" + groupNode + "/" + subNode, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 1、程序一进来就先注册一把锁到zk
        thisPath = zk.create("/" + groupNode + "/" + subNode, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        // wait一小会，便于观察
        Thread.sleep(new Random().nextInt(1000));

        // 从ZK的锁父目录下，获取所有子节点，并且注册对父节点的监听
        List<String> childrenNodes = zk.getChildren("/" + groupNode, true);

        //如果挣钱资源的程序就只有自己，就可以直接去访问共享资源
        if (childrenNodes.size() == 1) {
            doSomething();

            thisPath = zk.create("/" + groupNode + "/" + subNode, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        }

    }

    /**
     * @return void
     * @Author wangfanming
     * @Param []
     * @Description 处理业务逻辑，并且在最后释放锁
     * @Date 2019/9/8 23:12
     **/
    private void doSomething() throws Exception {
        try {
            System.out.println("gain lock: " + thisPath);
            Thread.sleep(2000);
            // do something
        } finally {
            System.out.println("finished: " + thisPath);
            //释放锁
            zk.delete(this.thisPath, -1);
        }
    }


    public static void main(String[] args) throws Exception {
        DistributedClientLock distributedClientLock = new DistributedClientLock();
        distributedClientLock.connectZookeeper();
        Thread.sleep(10000);
        DistributedClientLock distributedClientLock1 = new DistributedClientLock();
        distributedClientLock1.connectZookeeper();
        Thread.sleep(10000);
        DistributedClientLock distributedClientLock2 = new DistributedClientLock();
        distributedClientLock2.connectZookeeper();

        Thread.sleep(100000000);

    }
}
