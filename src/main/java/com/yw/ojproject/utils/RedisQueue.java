package com.yw.ojproject.utils;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-31 22:22
**/
public class RedisQueue<T> {
    private BoundListOperations<String, T> listOperations;//noblocking

    private static Lock lock = new ReentrantLock();//基于底层IO阻塞考虑

    private RedisTemplate redisTemplate;

    private byte[] rawKey;

    public RedisQueue(RedisTemplate redisTemplate, String key){
        this.redisTemplate = redisTemplate;
        rawKey = redisTemplate.getKeySerializer().serialize(key);
        listOperations = redisTemplate.boundListOps(key);
    }

    /**
     * blocking 一直阻塞直到队列里边有数据
     * remove and get last item from queue:BRPOP
     * @return
     */
    public T takeFromTail(int timeout) throws InterruptedException{
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        RedisConnection connection = connectionFactory.getConnection();
        lock.lockInterruptibly();
        try{
            List<byte[]> results = connection.bRPop(timeout, rawKey);
            if(CollectionUtils.isEmpty(results)){
                return null;
            }
            return (T)redisTemplate.getValueSerializer().deserialize(results.get(1));
        }finally{
            lock.unlock();
            RedisConnectionUtils.releaseConnection(connection, connectionFactory);
        }
    }

    public T takeFromTail() throws InterruptedException{
        return takeFromTail(0);
    }

    /**
     * 从队列的头，插入
     */
    public void pushFromHead(T value){
        listOperations.leftPush(value);
    }

    public void pushFromTail(T value){
        listOperations.rightPush(value);
    }

    /**
     * noblocking
     * @return null if no item in queue
     */
    public T removeFromHead(){
        return listOperations.leftPop();
    }

    public T removeFromTail(){
        return listOperations.rightPop();
    }

    /**
     * blocking 一直阻塞直到队列里边有数据
     * remove and get first item from queue:BLPOP
     * @return
     */
    public T takeFromHead(int timeout) throws InterruptedException{
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        RedisConnection connection = connectionFactory.getConnection();
        lock.lockInterruptibly();
        try{
            List<byte[]> results = connection.bLPop(timeout, rawKey);
            if(CollectionUtils.isEmpty(results)){
                return null;
            }
            return (T)redisTemplate.getValueSerializer().deserialize(results.get(1));
        }finally{
            lock.unlock();
            RedisConnectionUtils.releaseConnection(connection, connectionFactory);
        }
    }

    public T takeFromHead() throws InterruptedException{
        return takeFromHead(0);
    }
}
