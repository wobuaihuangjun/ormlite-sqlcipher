package com.example.huangzj.dbencrypt.dao.ormlite;


public enum DaoThreadMode {

    /**
     * 同一个线程
     */
    PostThread,

    /**
     * 主线程
     */
    MainThread,

    /**
     * 后台线程
     */
    BackgroundThread,

    /**
     * 单独开一个线程
     */
    Async
}
