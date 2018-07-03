/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topica.itlab.connectionpoll.connection;

import com.topica.itlab.connectionpoll.config.Config;
import com.topica.itlab.connectionpoll.logger.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

public class ConnectionPool {

    @SuppressWarnings("FieldMayBeFinal")
    private Logger logger = new Logger("ConnectionPool");  // Define a simple logger , just Print to console .
    @SuppressWarnings("FieldMayBeFinal")
    private LinkedList pool = new LinkedList();

    public final static int MAX_CONNECTIONS = 5;
    public final static int INIT_CONNECTIONS = 2;
    private int connectionAvailble;
    public final static int TIME_OUT = 10; // 10s time out
    private Lock lock;

    public void driverTest() throws ClassNotFoundException {
        Class.forName(Config.DRIVER);
        logger.log(" Load driver success. ");
    }

    public Connection createConnection() {
        Connection conn = null;
        try {
            driverTest();
        } catch (ClassNotFoundException ex) {
            logger.error(" Load driver failed");
        }

        try {
            conn = DriverManager.getConnection(Config.URL, Config.USER, Config.PASS);
            System.out.println("Connect success to database " + Config.URL);
        } catch (SQLException ex) {
            logger.error(" Connect Failed");
            java.util.logging.Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
        }

        return conn;

    }

    // Inititae ConnectionsPool ( with min Connections )
    public void initPool() {
        logger.log("Establishing " + INIT_CONNECTIONS + " connections...");
        @SuppressWarnings("UnusedAssignment")
        Connection conn = null;
        for (int i = 0; i < INIT_CONNECTIONS; i++) {
            conn = createConnection();
            if (conn != null) {
                pool.addLast(conn);
            }
        }
        connectionAvailble = pool.size();
        logger.log("Number of connection: " + pool.size());
        //release();
    }

    @SuppressWarnings("SynchronizeOnNonFinalField")
    public Connection getFirstConnectionInPool() {
        @SuppressWarnings("UnusedAssignment")
        Connection conn = null;
        conn = (Connection) pool.removeFirst();

        if (conn == null) {
            conn = createConnection();
        }
        try {
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
        }

        return conn;
    }

    /**
     * get a Connection when a user need ; get First connection from pool If
     * toal of Connection is max ; Try Request 1 time per second In Time_Out If
     * total of Connection not max
     *
     * @return
     * @throws java.lang.InterruptedException
     */
    @SuppressWarnings({"SynchronizeOnNonFinalField", "SleepWhileInLoop"})
    public Connection getConnection() throws InterruptedException {
        Connection conn = null;

        lock = new ReentrantLock();
        lock.lock();

        if (!pool.isEmpty()) {
            try {
                conn = getFirstConnectionInPool();
                logger.log(" get a Connection Success ! ");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        } else {

            if (connectionAvailble > MAX_CONNECTIONS) {

                logger.log(" All connections are busy ... Please wait.. ");
                for (int time = 0; time < TIME_OUT; time++) {
                    Thread.sleep(1000);
                    if (!pool.isEmpty()) {
                        try {
                            conn = getFirstConnectionInPool();
                            logger.log(" get a Connection Success ! ");
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        }
                        break;
                    }
                }
                logger.log(" Request Time Out ");

            } else {
                synchronized (pool) {
                    conn = createConnection();
                    if (conn != null) {
                        pool.addLast(conn);
                        connectionAvailble++;
                    }
                }

            }
        }
        lock.unlock();
        return conn;
    }

    // Return a connection to Pool when a user use done
    @SuppressWarnings("SynchronizeOnNonFinalField")
    public void putConnection(Connection conn) {
        lock = new ReentrantLock();
        lock.lock();
        try { // Ignore closed connection
            lock = new ReentrantLock();
            lock.lock();
            if (conn == null || conn.isClosed()) {
                logger.log("putConnection: conn is null or closed: " + conn);
                return;
            }
            if (pool.size() >= MAX_CONNECTIONS) {
                conn.close();
                connectionAvailble--;
                return;
            }
        } catch (SQLException ex) {
        }
        pool.addLast(conn);
        lock.unlock();
    }

    // Close and delete all connections in Pool
    @SuppressWarnings("SynchronizeOnNonFinalField")
    public void release() {
        logger.log("Closing connections in pool...");
        synchronized (pool) {
            for (Iterator it = pool.iterator(); it.hasNext();) {
                Connection conn = (Connection) it.next();
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("release: Cannot close connection! (maybe closed?)");
                }
            }
            pool.clear();
        }
        logger.log("Release connection OK");
    }

}
