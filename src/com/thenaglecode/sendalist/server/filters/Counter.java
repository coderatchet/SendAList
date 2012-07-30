package com.thenaglecode.sendalist.server.filters;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 25/07/12
 * Time: 11:12 PM
 */
public class Counter {
    private long counter = 0;

    public synchronized long incCounter(){
        return ++counter;
    }

    public synchronized long count(){
        return counter;
    }
}
