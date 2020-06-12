package com.yarnRpc;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.Server;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.ipc.YarnRPC;
import org.apache.hadoop.yarn.server.api.ResourceTracker;
import org.apache.hadoop.yarn.server.api.protocolrecords.NodeHeartbeatRequest;
import org.apache.hadoop.yarn.server.api.protocolrecords.NodeHeartbeatResponse;
import org.apache.hadoop.yarn.server.api.protocolrecords.RegisterNodeManagerRequest;
import org.apache.hadoop.yarn.server.api.protocolrecords.RegisterNodeManagerResponse;
import org.apache.hadoop.service.AbstractService;


import java.net.InetSocketAddress;

public class ResourceTrackerService extends AbstractService implements ResourceTracker {

    private Server server;

    /**
     * Construct the service.
     *
     * @param name service name
     */
    public ResourceTrackerService(String name) {
        super(name);
    }

    protected void serviceStart() throws Exception {
        super.serviceStart();
        Configuration conf = getConfig();
        YarnRPC rpc = YarnRPC.create(conf);

        this.server = rpc.getServer(ResourceTracker.class, this, new InetSocketAddress("127.0.0.1", 8990), conf, null, conf.getInt(YarnConfiguration.RM_RESOURCE_TRACKER_CLIENT_THREAD_COUNT, YarnConfiguration.DEFAULT_RM_RESOURCE_TRACKER_CLIENT_THREAD_COUNT));
        this.server.start();
    }

    public RegisterNodeManagerResponse registerNodeManager(RegisterNodeManagerRequest registerNodeManagerRequest) {
        return null;
    }

    public NodeHeartbeatResponse nodeHeartbeat(NodeHeartbeatRequest nodeHeartbeatRequest) {
        return null;
    }
}
