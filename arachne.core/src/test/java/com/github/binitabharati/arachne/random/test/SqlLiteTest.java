package com.github.binitabharati.arachne.random.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

//http://www.tutorialspoint.com/sqlite/sqlite_java.htm
public class SqlLiteTest {
    
    private static BlockingQueue<Connection> connectionPool = new ArrayBlockingQueue<Connection>(4);
    
    
    private static void initPool() {
        for (int i = 0 ; i < 4 ; i++) {
            Connection c = null;
            try {
              Class.forName("org.sqlite.JDBC");
              c = DriverManager.getConnection("jdbc:sqlite:test.db");
              connectionPool.add(c);
            } catch ( Exception e ) {
              System.err.println( e.getClass().getName() + ": " + e.getMessage() );
              System.exit(0);
            }
            System.out.println("Opened database successfully");
        }
    }
   
    
    private static Connection getConnection() {
        Connection ret = null;
        String curThreadName = Thread.currentThread().getName();        
        System.out.println(curThreadName + " getConnection : entered");
        try {
            ret = connectionPool.take();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(curThreadName + " getConnection : exiting");
        return ret;
    }
    
    private static void returnConnection(Connection c) {
        try {
            connectionPool.put(c);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    //http://stackoverflow.com/questions/14950482/creating-database-in-jdbc-sqlite
    //https://www.sqlite.org/autoinc.html
    private static void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
          Class.forName("org.sqlite.JDBC");
          c = getConnection();
          System.out.println("Opened database successfully");

          stmt = c.createStatement();
          String sql = "CREATE TABLE COMPANY " +
                       "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                       " NAME           TEXT    NOT NULL, " + 
                       " AGE            INT     NOT NULL, " + 
                       " ADDRESS        CHAR(50), " + 
                       " SALARY         REAL)"; 
          stmt.executeUpdate(sql);
          stmt.close();
          //c.close();
        } catch ( Exception e ) {
          System.err.println( e.getClass().getName() + ": " + e.getMessage() );
          System.exit(0);
        }
        finally {
            if (c != null) {
                returnConnection(c);
            }
        }
        System.out.println("Table created successfully");
    }
    
    private static void insert(Map<String, String> valueMap) {
        String curThreadName = Thread.currentThread().getName();        
        System.out.println(curThreadName + " insert: entered with "+valueMap);

        Connection c = null;
        PreparedStatement  stmt = null;
        try {
          Class.forName("org.sqlite.JDBC");
          c = getConnection();
          System.out.println(curThreadName + " insert: conection = "+c);
          //c.setAutoCommit(false);
          System.out.println("Opened database successfully");

          String sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                  "VALUES (?, ?, ?, ?, ? );"; 
          stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
          //stmt.setInt(1, Integer.parseInt(valueMap.get("ID")));
          stmt.setString(2, valueMap.get("NAME"));
          stmt.setInt(3, Integer.parseInt(valueMap.get("AGE")));
          stmt.setString(4, valueMap.get("ADDRESS"));
          stmt.setFloat(5, Float.parseFloat(valueMap.get("SALARY")));
                    
          stmt.executeUpdate();
          
          ResultSet rs = stmt.getGeneratedKeys();
          rs.next();
          int auto_id = rs.getInt(1);
          System.out.println("Generated key = "+auto_id);

          stmt.close();
          //c.commit();
          //c.close();
        } catch ( Exception e ) {
          System.out.println( "OOPSSSSS-"+curThreadName + "---" + e.getClass().getName() + "---" + e.getMessage() );
          e.printStackTrace();
          //System.exit(0);
        }
        finally {
            if (c != null) {
                returnConnection(c);
            }
        }
        System.out.println(curThreadName + " insert: exiting for "+valueMap);
        System.out.println("Records created successfully");
    }
    
    private static void edit(Map<String, String> valueMap) {
        String curThreadName = Thread.currentThread().getName();        
        System.out.println(curThreadName + " insert: entered with "+valueMap);

        Connection c = null;
        PreparedStatement  stmt = null;
        try {
          Class.forName("org.sqlite.JDBC");
          c = getConnection();
          System.out.println(curThreadName + " insert: conection = "+c);
          //c.setAutoCommit(false);
          System.out.println("Opened database successfully");

          String sql = "UPDATE COMPANY SET NAME = ? where ID = ?";
          stmt = c.prepareStatement(sql);
          stmt.setString(1, valueMap.get("NAME"));         
          stmt.setInt(2, Integer.parseInt(valueMap.get("ID")));
                  
          stmt.executeUpdate();

          stmt.close();
          //c.commit();
          //c.close();
        } catch ( Exception e ) {
          System.out.println( "OOPSSSSS-"+curThreadName + "---" + e.getClass().getName() + "---" + e.getMessage() );
          e.printStackTrace();
          //System.exit(0);
        }
        finally {
            if (c != null) {
                returnConnection(c);
            }
        }
        System.out.println(curThreadName + " insert: exiting for "+valueMap);
        System.out.println("Records created successfully");
    }
    
    public static void main(String[] args) {
        initPool();
        createTable();
        
        insertRows();
        
        //editRows();
        
        
    }
    
    private static void insertRows() {
        Map<String, String> valueMap1 = new HashMap<String, String>();
        valueMap1.put("ID", "1");
        valueMap1.put("NAME", "Binita");
        valueMap1.put("AGE", "34");
        valueMap1.put("ADDRESS", "#101, Spectrum Ambara, Prasanth Layout extn., Near Hope Farm circle, Whitefield, Bangalore - 560066");
        valueMap1.put("SALARY", "30000");
        
        Map<String, String> valueMap2 = new HashMap<String, String>();
        valueMap2.put("ID", "2");
        valueMap2.put("NAME", "Aisha");
        valueMap2.put("AGE", "23");
        valueMap2.put("ADDRESS", "#001, Swarna Heavens, Prasanth Layout extn., Near Hope Farm circle, Whitefield, Bangalore - 560066");
        valueMap2.put("SALARY", "10000");
        
        Map<String, String> valueMap3 = new HashMap<String, String>();
        valueMap3.put("ID", "3");
        valueMap3.put("NAME", "Lobo Fernandes");
        valueMap3.put("AGE", "45");
        valueMap3.put("ADDRESS", "#34, Swarna Heavens, Prasanth Layout extn., Near Hope Farm circle, Whitefield, Bangalore - 560066");
        valueMap3.put("SALARY", "1000000");
        
        Map<String, String> valueMap4 = new HashMap<String, String>();
        valueMap4.put("ID", "4");
        valueMap4.put("NAME", "Lobo Inchira");
        valueMap4.put("AGE", "20");
        valueMap4.put("ADDRESS", "#78, Suparnika Sanvi, Prasanth Layout extn., Near Hope Farm circle, Whitefield, Bangalore - 560066");
        valueMap4.put("SALARY", "8000");
        
        Map<String, String> valueMap5 = new HashMap<String, String>();
        valueMap5.put("ID", "5");
        valueMap5.put("NAME", "Kaska  Bhushan");
        valueMap5.put("AGE", "32");
        valueMap5.put("ADDRESS", "#10, Suparnika Sanvi, Prasanth Layout extn., Near Hope Farm circle, Whitefield, Bangalore - 560066");
        valueMap5.put("SALARY", "200000");
        
        Map<String, String> valueMap6 = new HashMap<String, String>();
        valueMap6.put("ID", "6");
        valueMap6.put("NAME", "Misha Lyns");
        valueMap6.put("AGE", "27");
        valueMap6.put("ADDRESS", "#10, Suparnika Sanvi, Prasanth Layout extn., Near Hope Farm circle, Whitefield, Bangalore - 560066");
        valueMap6.put("SALARY", "600000");
        
        Runnable r1 = new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                SqlLiteTest.insert(valueMap1);
            }
        };
        
        Runnable r2 = new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                SqlLiteTest.insert(valueMap2);
            }
        };
        
        Runnable r3 = new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                SqlLiteTest.insert(valueMap3);
            }
        };
        
        Runnable r4 = new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                SqlLiteTest.insert(valueMap4);
            }
        };
        
        Runnable r5 = new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                SqlLiteTest.insert(valueMap5);
            }
        };
        
        Runnable r6 = new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                SqlLiteTest.insert(valueMap6);
            }
        };
        
        Thread t1 = new Thread(r1); Thread t2 = new Thread(r2);Thread t3 = new Thread(r3);Thread t4 = new Thread(r4);Thread t5 = new Thread(r5);
        Thread t6 = new Thread(r6);
        t1.start();t2.start();t3.start();t4.start();t5.start();t6.start();
    }
    
    private static void editRows() {
        Map<String, String> valueMap1 = new HashMap<String, String>();
        valueMap1.put("ID", "1");
        valueMap1.put("NAME", "Binita");
        valueMap1.put("AGE", "34");
        valueMap1.put("ADDRESS", "#101, Spectrum Ambara, Prasanth Layout extn., Near Hope Farm circle, Whitefield, Bangalore - 560066");
        valueMap1.put("SALARY", "30000");
        
        Map<String, String> valueMap2 = new HashMap<String, String>();
        valueMap2.put("ID", "1");
        valueMap2.put("NAME", "Syonas mom");
        valueMap2.put("AGE", "23");
        valueMap2.put("ADDRESS", "#001, Swarna Heavens, Prasanth Layout extn., Near Hope Farm circle, Whitefield, Bangalore - 560066");
        valueMap2.put("SALARY", "10000");
        
        Map<String, String> valueMap3 = new HashMap<String, String>();
        valueMap3.put("ID", "1");
        valueMap3.put("NAME", "Bindiya Sharma");
        valueMap3.put("AGE", "45");
        valueMap3.put("ADDRESS", "#34, Swarna Heavens, Prasanth Layout extn., Near Hope Farm circle, Whitefield, Bangalore - 560066");
        valueMap3.put("SALARY", "1000000");
        
        Map<String, String> valueMap4 = new HashMap<String, String>();
        valueMap4.put("ID", "1");
        valueMap4.put("NAME", "Rakesh Roshan");
        valueMap4.put("AGE", "34");
        valueMap4.put("ADDRESS", "#101, Spectrum Ambara, Prasanth Layout extn., Near Hope Farm circle, Whitefield, Bangalore - 560066");
        valueMap4.put("SALARY", "30000");
        
        
        
        Runnable r1 = new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                SqlLiteTest.edit(valueMap1);
            }
        };
        
        Runnable r2 = new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                SqlLiteTest.edit(valueMap2);
            }
        };
        
        Runnable r3 = new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                SqlLiteTest.edit(valueMap3);
            }
        };
        
        Runnable r4 = new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                SqlLiteTest.edit(valueMap4);
            }
        };
        
        
        
        Thread t1 = new Thread(r1); Thread t2 = new Thread(r2);Thread t3 = new Thread(r3);Thread t4 = new Thread(r4);
        t1.start();t2.start();t3.start();t4.start();
    }

}
