package com.openmind.zookeeper.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

/**
 * 继承ZkClient 重写watchForData方法
 * <p>
 * 注:如果有【监听节点时顺便验证节点是否存在】等需求的话，可以重写此方法
 * <p>
 * 注:watchForData 的参数final String path为被监听的节点的路径
 * 可以通过重写watchForData方法，来获取一些该节点的信息;
 * 我们当然也可以使用其他方式来获取该节点的信息。
 *
 * @author zhoujunwen
 * @date 2020-09-09
 * @time 10:16
 * @desc
 */
public class MyZkClient extends ZkClient {
    public MyZkClient(String zkServers, int sessionTimeout, int connectionTimeout, ZkSerializer zkSerializer) {
        super(zkServers, sessionTimeout, connectionTimeout, zkSerializer);
    }

    @Override
    public void watchForData(final String path) {
        retryUntilConnected(() -> {
            System.out.println("进入重写的watchForData方法了！要被监听的节点path是:" + path);
            Stat stat = new Stat();
            _connection.readData(path, stat, true);
            // 监听节点时，若节点不存在，则抛出异常
            if (!exists(path)) {
                throw new KeeperException.NoNodeException();
            }
            return null;
        });
    }
}
