package com.exchange.dispacher;

import java.util.Properties;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exchange.common.constan.DispatcherConstants;
import com.exchange.dispacher.util.ZookeeperUtils;

public class ZooKeeperClientFactory
{
    private static CuratorFramework client;
    
    private static Properties proterties;
    
    public static Logger logger = LoggerFactory.getLogger(ZooKeeperClientFactory.class);
    
    private static ConnectionStateListener serverConnectionStateListener;
    
    private volatile static boolean isServerListenerLoaded = false;
    
    private static ConnectionStateListener notifyConnectionStateListener;
    
    private volatile static boolean isNotifyListenerLoaded = false;
    
    private ZooKeeperClientFactory()
    {
    }// 不允许默认构造器
    
    public static CuratorFramework getInstance()
    {
        if (null == client)
        {
            synchronized (ZooKeeperClientFactory.class)
            {
                if (client == null)
                {
                    logger.debug("initial ZooKeeperClientFactory.current listener :{}.", serverConnectionStateListener);
                    ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
                    client = CuratorFrameworkFactory.builder()
                        .connectString(getConnectString())
                        .retryPolicy(retryPolicy)
                        .build();
                    
                    if (null != serverConnectionStateListener)
                    {
                        client.getConnectionStateListenable().addListener(serverConnectionStateListener);
                        isServerListenerLoaded = true;
                        logger.debug("ServiceConnectionStateListener added.");
                    }
                    
                    if (null != notifyConnectionStateListener)
                    {
                        client.getConnectionStateListenable().addListener(notifyConnectionStateListener);
                        isNotifyListenerLoaded = true;
                        logger.debug("NotifyConnectionStateListener added.");
                    }
                    
                    client.start();
                }
            }
        }
        
        if (!isNotifyListenerLoaded)
        {
            synchronized (ZooKeeperClientFactory.class)
            {
                if (null != notifyConnectionStateListener)
                {
                    client.getConnectionStateListenable().addListener(notifyConnectionStateListener);
                    isNotifyListenerLoaded = true;
                    logger.debug("NotifyConnectionStateListener added.");
                }
            }
        }
        
        if (!isServerListenerLoaded)
        {
            synchronized (ZooKeeperClientFactory.class)
            {
                if (null != serverConnectionStateListener)
                {
                    client.getConnectionStateListenable().addListener(serverConnectionStateListener);
                    isServerListenerLoaded = true;
                    logger.debug("ServiceConnectionStateListener added.");
                }
            }
        }
        
        return client;
    }
    
    public static void destory()
    {
        CloseableUtils.closeQuietly(client);
    }
    
    /**
     * 监听zookeeper的连接状态,并处理server的重新注册.
     * 
     * @param serverConnectionStateListener
     */
    public synchronized static void setServerConnectionStateListener(
        ConnectionStateListener serverConnectionStateListener)
    {
        if (null == ZooKeeperClientFactory.serverConnectionStateListener && null != serverConnectionStateListener)
        {
            ZooKeeperClientFactory.serverConnectionStateListener = serverConnectionStateListener;
        }
    }
    
    /**
     * 监听zookeeper的连接状态,并处理notify的重新注册.
     * 
     * @param notifyConnectionStateListener
     */
    public synchronized static void setNotifyConnectionStateListener(
        ConnectionStateListener notifyConnectionStateListener)
    {
        if (null == ZooKeeperClientFactory.notifyConnectionStateListener && null != notifyConnectionStateListener)
        {
            ZooKeeperClientFactory.notifyConnectionStateListener = notifyConnectionStateListener;
        }
    }
    
    public static Properties getProterties()
    {
        return proterties;
    }
    
    public static void setProterties(Properties proterties)
    {
        ZooKeeperClientFactory.proterties = proterties;
    }
    
    public static String getConnectString()
    {
        if (proterties != null)
        {
            return proterties.getProperty(DispatcherConstants.ZOOKEEPER_CONNECT_KEY);
        }
        return null;
    }
    
    public static void main(String[] args)
    {
        Properties proterties = new Properties();
        proterties.setProperty(DispatcherConstants.ZOOKEEPER_CONNECT_KEY, "192.168.90.121:2181");
        ZooKeeperClientFactory.setProterties(proterties);
        try
        {
            ZookeeperUtils.createZookeeperNode("/test", CreateMode.EPHEMERAL,"test");
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
