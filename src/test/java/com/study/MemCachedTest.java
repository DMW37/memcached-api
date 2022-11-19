package com.study;

import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: 邓明维
 * @date: 2022/11/19
 * @description: memcached api的使用
 */
public class MemCachedTest {
    private MemcachedClient client = null;

    @Before
    public void before() throws IOException {
        XMemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses("43.139.105.251:11211"));
        client = builder.build();
    }

    @After
    public void after() throws Exception{
        client.shutdown();
    }

    @Test
    public void testSet() throws IOException, InterruptedException, TimeoutException, MemcachedException {
        boolean set = client.set("mykey", 100, "hello world client");
        System.out.println(set);
    }

    @Test
    public void testGet() throws InterruptedException, TimeoutException, MemcachedException {
        Object mykey = client.get("mykey");
        System.out.println(mykey);
    }

    @Test
    public void testAdd() throws Exception{
        boolean bool = client.add("mykey", 200, 2);
        System.out.println(bool);
        boolean newKey = client.add("newkey", 200, "hello world");
        System.out.println(newKey);
    }

    @Test
    public void testReplace() throws Exception{
        System.out.println(client.replace("mykey", 0, 2990));
    }

    /**
     * cas操作
     * @throws Exception
     */
    @Test
    public void testCas() throws Exception{
        boolean bool = client.set("key", 20, "hello world 你好!");
        System.out.println(bool);
        GetsResponse<Object> key = client.gets("key");
        long cas = key.getCas();
        System.out.println(cas);
        boolean casBool = client.cas("key", 200, "我爱你中国", cas);
        System.out.println(casBool);
        GetsResponse<Object> keys = client.gets("key");
        Object value = keys.getValue();
        System.out.println(value);
    }

    @Test
    public void testIncrAndDecr() throws Exception{
        // 注意，需要递增或者递减，需要将set 的值设置为字符串
        boolean flag = client.set("incrKeyAndDecrKey", 0, "20");
        System.out.println(flag);
        // 增1
        long incrValue = client.incr("incrKeyAndDecrKey", 20);
        System.out.println(incrValue);
        long decrValue = client.decr("incrKeyAndDecrKey", 10);
        System.out.println(decrValue);
    }


}
