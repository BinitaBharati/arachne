package com.github.binitabharati.arachne.sbe;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.agrona.concurrent.UnsafeBuffer;


import com.github.binitabharati.arachne.sbe.stubs.MessageHeaderDecoder;
import com.github.binitabharati.arachne.sbe.stubs.MessageHeaderEncoder;
import com.github.binitabharati.arachne.sbe.stubs.RoutesDecoder;
import com.github.binitabharati.arachne.sbe.stubs.RoutesEncoder;
import com.github.binitabharati.arachne.sbe.stubs.RoutesEncoder.RouteEntriesEncoder;

public class SbeTest2 {
    private static final MessageHeaderDecoder MESSAGE_HEADER_DECODER = new MessageHeaderDecoder();
    private static final MessageHeaderEncoder MESSAGE_HEADER_ENCODER = new MessageHeaderEncoder();
    private static final RoutesEncoder ROUTES_ENCODER = new RoutesEncoder();
    private static final RoutesDecoder ROUTES_DECODER = new RoutesDecoder();
    
    public static void main(String[] args) throws Exception{
        
        //final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4096);
        final ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);
        int bufferOffset = 0;
        int encodingLength = 0;

        // Setup for encoding a message
        MESSAGE_HEADER_ENCODER
            .wrap(directBuffer, bufferOffset)
            .blockLength(ROUTES_ENCODER.sbeBlockLength())
            .templateId(ROUTES_ENCODER.sbeTemplateId())
            .schemaId(ROUTES_ENCODER.sbeSchemaId())
            .version(ROUTES_ENCODER.sbeSchemaVersion());

        bufferOffset += MESSAGE_HEADER_ENCODER.encodedLength();
        encodingLength += MESSAGE_HEADER_ENCODER.encodedLength();
        encodingLength += encode(ROUTES_ENCODER, directBuffer, bufferOffset);
        
        //get byte[] from UnsafeBuffer (direct)
        byte[] test = directBuffer.byteArray();
        System.out.println("byte array length = "+test.length);
        
        bufferOffset = 0;
        final UnsafeBuffer directBuffer2 = new UnsafeBuffer(directBuffer.byteBuffer());
        MESSAGE_HEADER_DECODER
        .wrap(directBuffer2, bufferOffset)
        ;
        
     // Lookup the applicable flyweight to decode this type of message based on templateId and version.
        final int templateId = MESSAGE_HEADER_DECODER.templateId();
        if (templateId != RoutesEncoder.TEMPLATE_ID)
        {
            throw new IllegalStateException("Template ids do not match");
        }

        final int actingBlockLength = MESSAGE_HEADER_DECODER.blockLength();
        final int schemaId = MESSAGE_HEADER_DECODER.schemaId();
        final int actingVersion = MESSAGE_HEADER_DECODER.version();

        bufferOffset += MESSAGE_HEADER_DECODER.encodedLength();
        decode(ROUTES_DECODER, directBuffer, bufferOffset, actingBlockLength, schemaId, actingVersion);
        
        

        }
    
    public static int encode(final RoutesEncoder routeEncoder, final UnsafeBuffer directBuffer, final int bufferOffset)
    {
        final int srcOffset = 0;

        routeEncoder.wrap(directBuffer, bufferOffset);
            

        // An exception will be raised if the string length is larger than can be encoded in the varDataEncoding length field
        // Please use a suitable schema type for varDataEncoding.length: uint8 <= 254, uint16 <= 65534
        RoutesEncoder.RouteEntriesEncoder re_enc = routeEncoder.routeEntriesCount(2);
        
        re_enc
        .next()
        .destinationNw("11.1.3.4")
        .gateway("11.1.3.10")
        .metric("3")
        .netMask("255.255.255.0")
        .port("eth0")
        .next()
        .destinationNw("11.1.6.4")
        .gateway("11.1.3.10")
        .metric("11")
        .netMask("255.255.255.0")
        .port("eth1");
        
        return routeEncoder.encodedLength();
    }
    
    public static void decode(
            final RoutesDecoder routesDecoder,
            final UnsafeBuffer directBuffer,
            final int bufferOffset,
            final int actingBlockLength,
            final int schemaId,
            final int actingVersion)
            throws Exception
        {
            final byte[] buffer = new byte[128];
            final StringBuilder sb = new StringBuilder();

            routesDecoder.wrap(directBuffer, bufferOffset, actingBlockLength, actingVersion);

            
            for (final RoutesDecoder.RouteEntriesDecoder re_dec : routesDecoder.routeEntries())
            {
                sb.append("\nRE.destinationNw=").append(re_dec.destinationNw());
                sb.append("\nRE.gateway=").append(re_dec.gateway());
                sb.append("\nRE.metric=").append(re_dec.metric());
                sb.append("\nRE.netmask=").append(re_dec.netMask());
                sb.append("\nRE.port=").append(re_dec.port());
            }
           
            sb.append("\nRE.encodedLength=").append(routesDecoder.encodedLength());

            System.out.println(sb);
        }

}
