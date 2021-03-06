/* Generated SBE (Simple Binary Encoding) message codec */
package com.github.binitabharati.arachne.sbe.stubs;

import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.github.binitabharati.arachne.sbe.stubs.VarDataEncodingDecoder"})
@SuppressWarnings("all")
public class VarDataEncodingDecoder
{
    public static final int ENCODED_LENGTH = -1;
    private DirectBuffer buffer;
    private int offset;

    public VarDataEncodingDecoder wrap(final DirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;

        return this;
    }

    public int encodedLength()
    {
        return ENCODED_LENGTH;
    }

    public static long lengthNullValue()
    {
        return 4294967294L;
    }

    public static long lengthMinValue()
    {
        return 0L;
    }

    public static long lengthMaxValue()
    {
        return 1073741824L;
    }

    public long length()
    {
        return (buffer.getInt(offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
    }


    public static long varDataNullValue()
    {
        return 4294967294L;
    }

    public static long varDataMinValue()
    {
        return 0L;
    }

    public static long varDataMaxValue()
    {
        return 4294967293L;
    }
    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        builder.append('(');
        //Token{signal=ENCODING, name='length', description='null', id=-1, version=0, encodedLength=4, offset=0, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT32, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=1073741824, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        builder.append("length=");
        builder.append(length());
        builder.append('|');
        //Token{signal=ENCODING, name='varData', description='null', id=-1, version=0, encodedLength=-1, offset=4, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT32, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        builder.append(')');

        return builder;
    }
}
