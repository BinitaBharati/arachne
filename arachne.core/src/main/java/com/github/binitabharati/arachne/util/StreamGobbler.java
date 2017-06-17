package com.github.binitabharati.arachne.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author binita.bharati@gmail.com
 * java.lnag.Process's stream handler.
 *
 */

public class StreamGobbler extends Thread{
	
	private static final Logger logger = LoggerFactory.getLogger(StreamGobbler.class);
	
	private InputStream is;
	private String type;
	private String result;
    
    StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }
    
    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            StringBuffer tmp = new StringBuffer();

            while ( (line = br.readLine()) != null)
            {
                logger.debug("StreamGobbler: appending line = "+line);
                tmp.append(line);
                tmp.append("\n");
            }
            logger.debug("StreamGobbler: final appended result = "+tmp.toString());
            if (tmp != null && tmp.length() > 0) {
                result = tmp.toString().substring(0, tmp.toString().lastIndexOf("\n")); //Remove last new line character.
            }
            
            	  
            } catch (IOException ioe)
              {
                ioe.printStackTrace();  
              }
    }

	public String getResult() {
	    
		return this.result;
	}

	
}
