package com.github.binitabharati.arachne.random.test;

import java.io.*;

public class RandomAccessFileDemo {

   public static void main(String[] args) {
      try {
          
          String line_sep = System.getProperty("line.separator");;
          System.out.println(line_sep);
         // create a new RandomAccessFile with filename test
         RandomAccessFile raf = new RandomAccessFile("c://work//test.txt", "rw");

         // write something in the file
         raf.writeBytes("Hello ABBA" + line_sep);
         raf.writeBytes("Hello dabba" + line_sep);
         raf.writeBytes("Hello babaji" + line_sep);
         raf.writeBytes("Hello gattuji" + line_sep);

         // set the file pointer at 0 position
         raf.seek(0);

         long fp = raf.getFilePointer();
         long fileLength = raf.length();
         long preReadFP = fp;
         System.out.println("orig fp = " + fp + ", length = " + fileLength);
         while (fp < fileLength) {
             preReadFP = raf.getFilePointer();
             String curLine = raf.readLine();
             System.out.println(curLine);
             if (curLine.indexOf("baba") != -1) {
                 byte[] byteContent = curLine.getBytes();
                 raf.seek(preReadFP); 
                 String test = "DUMBO";
                 byte[] newByte = test.getBytes();
                 if (byteContent.length > newByte.length) {
                     int diff = byteContent.length - newByte.length;
                     StringBuffer postFix = new StringBuffer();
                     int count = 0;
                     if (diff > 0) {
                         while (count < diff) {
                             postFix.append(" ");
                             count++;
                         } 
                     }
                    
                     raf.writeBytes(test + postFix.toString());
                 }
                 
             }
             fp = raf.getFilePointer();//progress file pointer.
             System.out.println("after reading fp = " + fp);
         }
         
         //replace line.
         
      } catch (IOException ex) {
         ex.printStackTrace();
      }

   }
}
