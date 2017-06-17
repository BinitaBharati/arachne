package com.github.binitabharati.arachne.multicast.nio2;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.util.Enumeration;

public class MulticastTest {
    
    private static int MULTICAST_PORT = 9000;
    private static String multicastGrpStr = "238.0.0.12";
    public static void main(String[] args) throws InterruptedException {
        
        Runnable multiCastServer = new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                MembershipKey key = null;
                DatagramChannel client = null;
                try {
                        client = DatagramChannel.open(StandardProtocolFamily.INET);              
                        client.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                        client.bind(new InetSocketAddress(MULTICAST_PORT));
                
                        InetAddress group = InetAddress.getByName(multicastGrpStr);
        
                        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
                        while (e.hasMoreElements()) {
                            
                            NetworkInterface ni = e.nextElement(); 
                            Enumeration<InetAddress> e1 = ni.getInetAddresses();
                            while (e1.hasMoreElements()) {
                                InetAddress inetAddr = e1.nextElement();                                                             
                                if (inetAddr instanceof Inet4Address) {
                                    client.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
                                    System.out.println("MulticastServer: joining grp on interface " + ni.getName());
                                    key = client.join(group, ni);
                                }
                                  
                            }
                                                            
                        }
                            
                 }
                 catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
                
                while (true) {
                    System.out.println("Joined the   multicast  group:" + key);
                    System.out.println("Waiting for a  message  from  the"
                        + "  multicast group....");

                    ByteBuffer buffer = ByteBuffer.allocate(1048);
                    try {
                        System.out.println("Blocking receive");
                        client.receive(buffer);
                        System.out.println("After receiving");

                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    buffer.flip();
                    int limits = buffer.limit();
                    byte bytes[] = new byte[limits];
                    buffer.get(bytes, 0, limits);
                    String msg = new String(bytes);
                    System.out.println("Received  -> " + msg);
                }

               
            }
        };
        
        //Multicast sender
         Thread t1 = new Thread(multiCastServer);
         t1.start();
        
         Runnable multicastClient = new Runnable() {
             
             @Override
             public void run() {
                 // TODO Auto-generated method stub
                 DatagramChannel server;
                 try {
                         server = DatagramChannel.open();               
                         server.bind(null);
                         Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
                         
                         while (e.hasMoreElements()) {                
                             NetworkInterface ni = e.nextElement(); 
                             
                             Enumeration<InetAddress> e1 = ni.getInetAddresses();
                             while (e1.hasMoreElements()) {
                                 InetAddress inetAddr = e1.nextElement();
                                 if (inetAddr instanceof Inet4Address) {
                                     server.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);   
                                     System.out.println("Going to sleep");
                                     Thread.sleep(300);
                                     System.out.println("Woke up from sleep");
                                     String msg = "Hello from " + ni.getName();
                                     ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                                     InetAddress group = InetAddress.getByName(multicastGrpStr);
                                     InetSocketAddress sockAddr = new InetSocketAddress(group,
                                         MULTICAST_PORT);        
                                     server.send(buffer, sockAddr);
                                     System.out.println("Sent the   multicast  message: " + msg);
                                 }
                             }
                             
                         }
                         
                 } catch (IOException e1) {
                     // TODO Auto-generated catch block
                     e1.printStackTrace();
                 } catch (InterruptedException e2) {
                     // TODO Auto-generated catch block
                     e2.printStackTrace();
                 }
                 

             }
         };
        //MulticastSender
        Thread t2 = new Thread(multicastClient);
        t2.start();
        
        t1.join();
        
        
    }

}
