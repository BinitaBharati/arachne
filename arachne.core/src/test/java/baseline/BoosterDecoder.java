/* Generated SBE (Simple Binary Encoding) message codec */
package baseline;

import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"baseline.BoosterDecoder"})
@SuppressWarnings("all")
public class BoosterDecoder
{
    public static final int ENCODED_LENGTH = 2;
    private DirectBuffer buffer;
    private int offset;

    public BoosterDecoder wrap(final DirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;

        return this;
    }

    public int encodedLength()
    {
        return ENCODED_LENGTH;
    }

    public BoostType boostType()
    {
        return BoostType.get(buffer.getByte(offset + 0));
    }


    public static short horsePowerNullValue()
    {
        return (short)255;
    }

    public static short horsePowerMinValue()
    {
        return (short)0;
    }

    public static short horsePowerMaxValue()
    {
        return (short)254;
    }

    public short horsePower()
    {
        return ((short)(buffer.getByte(offset + 1) & 0xFF));
    }

    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        builder.append('(');
        //Token{signal=BEGIN_ENUM, name='BoostType', description='null', id=-1, version=0, encodedLength=1, offset=0, componentTokenCount=6, encoding=Encoding{presence=REQUIRED, primitiveType=CHAR, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("boostType=");
        builder.append(boostType());
        builder.append('|');
        //Token{signal=ENCODING, name='horsePower', description='null', id=-1, version=0, encodedLength=1, offset=1, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        builder.append("horsePower=");
        builder.append(horsePower());
        builder.append(')');

        return builder;
    }
}
