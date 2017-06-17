package com.github.binitabharati.arachne.random.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.github.binitabharati.arachne.util.ArachU;

public class RandomFileAccessConcurrentWritesTest{
    private static String line_sep = System.getProperty("line.separator");
    private static void init() throws Exception {
      //write freshly to file
        File file = new File("c://work//test1.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file, true);
        String temp = "name:tabitha;age:25;sex:F" + line_sep;
        fos.write(temp.getBytes());
        temp = "name:jubin;age:42;sex:M" + line_sep;
        fos.write(temp.getBytes());
        temp = "name:holly;age:18;sex:F" + line_sep;
        fos.write(temp.getBytes());
        temp = "name:tutemon;age:45;sex:T" + line_sep;
        fos.write(temp.getBytes());
        temp = "name:astha;age:27;sex:F" + line_sep;
        fos.write(temp.getBytes());
        temp = "name:bob;age:34;sex:M" + line_sep;
        fos.write(temp.getBytes());
        
        fos.flush();
        fos.close();
    }
    
class ConcurrentWriter implements Runnable {
        
        private String name;
        private String newName;
        
        public ConcurrentWriter(String name, String newName) {
            this.name = name;
            this.newName = newName;
        }
        
        @Override
        public void run() {
            try {
                String tname = Thread.currentThread().getName();
                RandomAccessFile raf = new RandomAccessFile(new File("c://work//test1.txt"), "rw");
                
             // set the file pointer at 0 position
                raf.seek(0);

                long fp = raf.getFilePointer();
                long fileLength = raf.length();
                long preReadFP = fp;
                System.out.println(tname+ " -> orig fp = " + fp + ", length = " + fileLength);
                while (fp < fileLength) {
                    preReadFP = raf.getFilePointer();
                    String curLine = raf.readLine();
                    System.out.println(tname + " -> curLine = " + curLine);
                    if (curLine.indexOf(name) != -1) {
                        byte[] byteContent = curLine.getBytes();
                        raf.seek(preReadFP); 
                        String test = curLine.replace(name, newName);
                        System.out.println(tname + " -> updatedLine = " + test);
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
                            System.out.println(tname + " -> writing1 ---"+test + postFix.toString()+"--------------");
                            raf.writeBytes(test + postFix.toString()+ line_sep );
                        } else {
                            System.out.println(tname+ " -> writing2 ---"+test+"--------------");
                            raf.writeBytes(test+line_sep);
                        }
                        
                    }
                    fp = raf.getFilePointer();//progress file pointer.
                    System.out.println(tname+ " -> after reading fp = " + fp);
                }
                
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        
    }
    
    public static void main(String[] args) throws Exception {
        init();
        
        RandomFileAccessConcurrentWritesTest junk = new RandomFileAccessConcurrentWritesTest();
        RandomFileAccessConcurrentWritesTest.ConcurrentWriter w1 = junk.new ConcurrentWriter("tabitha", "tabitha khan");
        Thread t1 = new Thread(w1);
        t1.start();
        
        RandomFileAccessConcurrentWritesTest.ConcurrentWriter w2 = junk.new ConcurrentWriter("jubin", "joe");
        Thread t2 = new Thread(w2);
        t2.start();
        
        RandomFileAccessConcurrentWritesTest.ConcurrentWriter w3 = junk.new ConcurrentWriter("holly", "betty cooper");
        Thread t3 = new Thread(w3);
        t3.start();
        
        
        
        
    }
    
    


}
