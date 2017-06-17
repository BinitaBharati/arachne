package com.github.binitabharati.arachne.random.test;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.Base64;

import org.junit.Test;

public class StringEncoding {
    
    @Test
    public void testStringEncoding() throws Exception {
        String input = "Hello World!";
        byte[] byteInput = input.getBytes("ISO_8859_1");
        
        String decodedStr = new String(byteInput, "UTF-8");
        System.out.println(decodedStr);
    }
    
    @Test
    public void testStringEncodingBase64()throws Exception {
        //http://stackoverflow.com/questions/19743851/base64-java-encode-and-decode-a-string
        
        // encode a String
        byte [] barr = Base64.getDecoder().decode("Hello World"); 
        
        // decode with padding
        String decoded = Base64.getEncoder().encodeToString(barr);
        System.out.println(decoded);

        // decode without padding
        String decoded2 = Base64.getEncoder().withoutPadding().encodeToString(barr);
        System.out.println(decoded2);

       
    }

}
