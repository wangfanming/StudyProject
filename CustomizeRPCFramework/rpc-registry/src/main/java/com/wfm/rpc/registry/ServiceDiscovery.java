package com.wfm.rpc.registry;

import io.netty.util.internal.ThreadLocalRandom;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 本类用于 client 发现 server 节点的变化，实现负载均衡
 */
public class ServiceDiscovery {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscovery.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> dataList = new ArrayList<String>();

    private String registryAddress;

    public ServiceDiscovery(String registryAddress) {
        this.registryAddress = registryAddress;

        ZooKeeper zk = connectServer();
        if (zk != null) {
            watchNode(zk);
        }
    }

    /**
     * 建立与 Zookeeper 的连接
     *
     * @return
     */
    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT,
                    new Watcher() {
                        public void process(WatchedEvent event) {
                            if (event.getState() == Event.KeeperState.SyncConnected) {
                                latch.countDown();  //阻塞到 连接建立完成
                            }
                        }
                    });
            latch.await();
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return zk;
    }

    /**
     * 监听子节点改变事件
     *
     * @param zk
     */
    private void watchNode(final ZooKeeper zk) {
        try {
            //获取所有子节点
            List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH,
                    new Watcher() {
                        public void process(WatchedEvent event) {
                            // 节点改变
                            if (event.getType() == Event.EventType.NodeChildrenChanged) {
                                watchNode(zk);
                            }
                        }
                    });
            ArrayList<String> dataList = new ArrayList<String>();

            // 循环字节点
            for (String node : nodeList) {
                //获取节点中的服务器地址
                byte[] bytes = zk.getData(Constant.ZK_REGISTRY_PATH + "/" + node, false, null);

                //存储到list中
                dataList.add(new String(bytes));
            }
            LOGGER.debug("node data: {}", dataList);

            this.dataList = dataList;
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    /**
     * 发现新节点
     *
     * @return
     */
    public String discover() {
        String data = null;
        int size = dataList.size();

        //存在新节点，直接使用
        if (size > 0) {
            if (size == 1) {
                data = dataList.get(0);
            } else {
                data = dataList.get(ThreadLocalRandom.current().nextInt(size));
                LOGGER.debug("using random data : {}", data);
            }
        }

        return data;
    }

}
