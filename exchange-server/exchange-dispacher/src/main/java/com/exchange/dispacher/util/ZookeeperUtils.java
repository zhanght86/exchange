package com.exchange.dispacher.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.api.DeleteBuilder;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exchange.common.util.JsonUtils;
import com.exchange.dispacher.ZooKeeperClientFactory;

public class ZookeeperUtils
{
    public static final Logger logger = LoggerFactory.getLogger(ZookeeperUtils.class);
    
    public static boolean forceSerializeToZookeeper(String zookeeperPath, Object target)
        throws Exception
    {
        return forceSerializeToZookeeper(zookeeperPath, CreateMode.PERSISTENT, target);
    }
    
    public static boolean forceSerializeToZookeeper(String zookeeperPath, CreateMode mode, Object target)
        throws Exception
    {
        if (!isExistZookeeperPath(zookeeperPath))
        {
            createZookeeperNode(zookeeperPath, mode, target);
        }
        else
        {
            serializeToZookeeper(zookeeperPath, target);
        }
        return true;
    }
    
    public static boolean createZookeeperNode(String zookeeperPath, Object target)
        throws Exception
    {
        return createZookeeperNode(zookeeperPath, CreateMode.PERSISTENT, target);
    }
    
    public static boolean createOrUpdateZookeeperNode(String zookeeperPath, Object target)
        throws Exception
    {
        if (isExistZookeeperPath(zookeeperPath))
        {
            serializeToZookeeper(zookeeperPath, target);
        }
        else
        {
            createZookeeperNode(zookeeperPath, CreateMode.PERSISTENT, target);
        }
        return true;
    }
    
    public static boolean createZookeeperNode(String zookeeperPath, CreateMode mode)
        throws Exception
    {
        return createZookeeperNode(zookeeperPath, mode, new byte[] {0});
    }
    
    public static boolean createZookeeperNode(String zookeeperPath, CreateMode mode, Object target)
        throws Exception
    {
        try
        {
            ZooKeeperClientFactory.getInstance().create().creatingParentsIfNeeded().withMode(mode).forPath(
                zookeeperPath, JsonUtils.serialize(target).getBytes());
            logger.debug("创建节点：" + zookeeperPath + "===" + JsonUtils.ObjectToJsonString(target));
        }
        catch (Exception e)
        {
            throw new Exception("创建zookeeper节点失败！" + zookeeperPath, e);
        }
        return true;
    }
    
    public static boolean serializeToZookeeper(String zookeeperPath, Object target)
        throws Exception
    {
        try
        {
            Stat stat = ZooKeeperClientFactory.getInstance().setData().forPath(zookeeperPath,
                JsonUtils.serialize(target).getBytes());
            logger.debug("序列化节点：" + zookeeperPath + "===" + JsonUtils.ObjectToJsonString(target) + "/Mtime:"
                + stat.getMtime() + "/Version:" + stat.getVersion());
        }
        catch (Exception e)
        {
            throw new Exception("序列化到zookeeper失败！" + zookeeperPath, e);
        }
        return true;
    }
    
    public static <T> T deserializeFromZookeeper(String zookeeperPath, Class<T> clazz)
        throws Exception
    {
        try
        {
            byte[] data = ZooKeeperClientFactory.getInstance().getData().forPath(zookeeperPath);
            return JsonUtils.deserialize(data, clazz);
        }
        catch (Exception e)
        {
            throw new Exception("从zookeeper反序列化失败！" + zookeeperPath, e);
        }
    }
    
    public static boolean isExistZookeeperPath(String path)
        throws Exception
    {
        try
        {
            return ZooKeeperClientFactory.getInstance().checkExists().forPath(path) != null;
        }
        catch (Exception e)
        {
            throw new Exception("检查path是否存在，执行失败！" + path, e);
        }
    }
    
    public static boolean delZooKeeperPath(String zookeeperPath, boolean deletingChildrenIfNeeded)
        throws Exception
    {
        try
        {
            DeleteBuilder delBuilder = ZooKeeperClientFactory.getInstance().delete();
            if (deletingChildrenIfNeeded)
            {
                delBuilder.deletingChildrenIfNeeded().forPath(zookeeperPath);
            }
            else
            {
                delBuilder.forPath(zookeeperPath);
            }
            logger.debug("删除节点：" + zookeeperPath);
        }
        catch (Exception e)
        {
            throw new Exception("删除路径失败！path：" + zookeeperPath, e);
        }
        return true;
    }
    
    public static List<String> getChildZookeeperPath(String path)
        throws Exception
    {
        List<String> rs = new ArrayList<String>();
        try
        {
            List<String> ls = ZooKeeperClientFactory.getInstance().getChildren().forPath(path);
            for (String p : ls)
            {
                rs.add(path + "/" + p);
            }
            return rs;
        }
        catch (Exception e)
        {
            throw new Exception("获取子路径失败！" + path, e);
        }
    }
}
