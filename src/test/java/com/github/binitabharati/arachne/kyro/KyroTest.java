package com.github.binitabharati.arachne.kyro;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.binitabharati.arachne.service.model.RouteEntry;

public class KyroTest {

    @Test
    public void testRouteEntry() {
        RouteEntry re1 = new RouteEntry("1.2.3.4", "1.2.3.78", "255.255.255.0", "33", "eth0");
     
        Kryo kryo = new Kryo();
        
        //serialize
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Output output = new Output(stream);        
        kryo.writeObject(output, re1);
        output.close(); // Also calls output.flush()
        byte[] buffer = stream.toByteArray(); // Serialization done, get bytes
        
       //de-serialize
        RouteEntry re2 = kryo.readObject(new Input(new ByteArrayInputStream(buffer)), RouteEntry.class);
        if(!re1.equals(re2)) {
            throw new AssertionError("Serialized route entry " + re1 + " not equal deserilaized route entry " + re2);
        }
        
    }
    
    @Test
    public void testRouteEntryList() {
        List<RouteEntry> routes1 = new ArrayList<RouteEntry>();
        RouteEntry re = new RouteEntry("1.2.3.4", "1.2.3.78", "255.255.255.0", "33", "eth0");
        routes1.add(re);
        re = new RouteEntry("1.5.6.7", "1.5.6.89", "255.255.255.0", "12", "eth1");
        routes1.add(re);
        re = new RouteEntry("10.12.34.56", "10.12.34.67", "255.255.255.0", "23", "eth0");
        routes1.add(re);
        re = new RouteEntry("10.12.78.56", "10.12.78.67", "255.255.255.0", "4", "eth1");
        routes1.add(re);
        
        Kryo kryo = new Kryo();
        
        //serialize
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Output output = new Output(stream);        
        kryo.writeObject(output, routes1);
        output.close(); // Also calls output.flush()
        byte[] buffer = stream.toByteArray(); // Serialization done, get bytes
        
        //de-serialize
        List<RouteEntry> routes2 = (List<RouteEntry>)kryo.readObject(new Input(new ByteArrayInputStream(buffer)), ArrayList.class);
        if(!(routes1.containsAll(routes2) && routes2.containsAll(routes1))) {
            throw new AssertionError("Serialized route entry \n" + routes1 + "\n not equal deserilaized route entry \n" + routes2);
        }
        
    }
    
    @Test
    public void testRouteEntryList1() {
        List<RouteEntry> routes1 = new ArrayList<RouteEntry>();
        RouteEntry re = new RouteEntry("1.2.3.4", "1.2.3.78", "255.255.255.0", "33", "eth0");
        routes1.add(re);
        re = new RouteEntry("1.5.6.7", "1.5.6.89", "255.255.255.0", "12", "eth1");
        routes1.add(re);
        re = new RouteEntry("10.12.34.56", "10.12.34.67", "255.255.255.0", "23", "eth0");
        routes1.add(re);
        re = new RouteEntry("10.12.78.56", "10.12.78.67", "255.255.255.0", "4", "eth1");
        routes1.add(re);
        
        Kryo kryo = new Kryo();
        //serialize
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Output output = new Output(stream);        
        
        for (RouteEntry each : routes1){
            kryo.writeObject(output, each);
            kryo.writeObject(output, "eoe");
        }
        
        kryo.writeObject(output, "eof");
        output.close(); // Also calls output.flush()
        byte[] buffer = stream.toByteArray(); // Serialization done, get bytes
        
        //de-serialize
        Object dObj = kryo.readObject(new Input(new ByteArrayInputStream(buffer)), String.class);
        System.out.println(dObj.getClass().getName());
        Object dObj1 = kryo.readObject(new Input(new ByteArrayInputStream(buffer)), RouteEntry.class);
        System.out.println(dObj1.getClass().getName());
        Object dObj2 = kryo.readObject(new Input(new ByteArrayInputStream(buffer)), Integer.class);
        System.out.println(dObj2.getClass().getName());
        /*if(!(routes1.containsAll(routes2) && routes2.containsAll(routes1))) {
            throw new AssertionError("Serialized route entry \n" + routes1 + "\n not equal deserilaized route entry \n" + routes2);
        }*/
        
    }

}
