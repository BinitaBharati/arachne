/* Generated SBE (Simple Binary Encoding) message codec */
package com.github.binitabharati.arachne.sbe.stubs;

import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.github.binitabharati.arachne.sbe.stubs.GroupSizeEncodingDecoder"})
@SuppressWarnings("all")
public class GroupSizeEncodingDecoder
{
    public static final int ENCODED_LENGTH = 4;
    private DirectBuffer buffer;
    private int offset;

    public GroupSizeEncodingDecoder wrap(final DirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;

        return this;
    }

    public int encodedLength()
    {
        return ENCODED_LENGTH;
    }

    public static int blockLengthNullValue()
    {
        return 65535;
    }

    public static int blockLengthMinValue()
    {
        return 0;
    }

    public static int blockLengthMaxValue()
    {
        return 65534;
    }

    public int blockLength()
    {
        return (buffer.getShort(offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF);
    }


    public static int numInGroupNullValue()
    {
        return 65535;
    }

    public static int numInGroupMinValue()
    {
        return 0;
    }

    public static int numInGroupMaxValue()
    {
        return 65534;
    }

    public int numInGroup()
    {
        return (buffer.getShort(offset + 2, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF);
    }

    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        builder.append('(');
        //Token{signal=ENCODING, name='blockLength', description='null', id=-1, version=0, encodedLength=2, offset=0, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT16, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("blockLength=");
        builder.append(blockLength());
        builder.append('|');
        //Token{signal=ENCODING, name='numInGroup', description='null', id=-1, version=0, encodedLength=2, offset=2, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT16, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("numInGroup=");
        builder.append(numInGroup());
        builder.append(')');

        return builder;
    }
}
