/* Generated SBE (Simple Binary Encoding) message codec */
package baseline;

import org.agrona.MutableDirectBuffer;

@javax.annotation.Generated(value = {"baseline.EngineEncoder"})
@SuppressWarnings("all")
public class EngineEncoder
{
    public static final int ENCODED_LENGTH = 8;
    private MutableDirectBuffer buffer;
    private int offset;

    public EngineEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;

        return this;
    }

    public int encodedLength()
    {
        return ENCODED_LENGTH;
    }

    public static int capacityNullValue()
    {
        return 65535;
    }

    public static int capacityMinValue()
    {
        return 0;
    }

    public static int capacityMaxValue()
    {
        return 65534;
    }

    public EngineEncoder capacity(final int value)
    {
        buffer.putShort(offset + 0, (short)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static short numCylindersNullValue()
    {
        return (short)255;
    }

    public static short numCylindersMinValue()
    {
        return (short)0;
    }

    public static short numCylindersMaxValue()
    {
        return (short)254;
    }

    public EngineEncoder numCylinders(final short value)
    {
        buffer.putByte(offset + 2, (byte)value);
        return this;
    }


    public static int maxRpmNullValue()
    {
        return 65535;
    }

    public static int maxRpmMinValue()
    {
        return 0;
    }

    public static int maxRpmMaxValue()
    {
        return 65534;
    }

    public int maxRpm()
    {
        return 9000;
    }

    public static byte manufacturerCodeNullValue()
    {
        return (byte)0;
    }

    public static byte manufacturerCodeMinValue()
    {
        return (byte)32;
    }

    public static byte manufacturerCodeMaxValue()
    {
        return (byte)126;
    }

    public static int manufacturerCodeLength()
    {
        return 3;
    }

    public void manufacturerCode(final int index, final byte value)
    {
        if (index < 0 || index >= 3)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 3 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String manufacturerCodeCharacterEncoding()
    {
        return "UTF-8";
    }

    public EngineEncoder putManufacturerCode(final byte[] src, final int srcOffset)
    {
        final int length = 3;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 3, src, srcOffset, length);

        return this;
    }

    public EngineEncoder manufacturerCode(final String src)
    {
        final int length = 3;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 3, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 3 + start, (byte)0);
        }

        return this;
    }

    public static byte fuelNullValue()
    {
        return (byte)0;
    }

    public static byte fuelMinValue()
    {
        return (byte)32;
    }

    public static byte fuelMaxValue()
    {
        return (byte)126;
    }

    private static final byte[] FUEL_VALUE = {80, 101, 116, 114, 111, 108};

    public static int fuelLength()
    {
        return 6;
    }

    public byte fuel(final int index)
    {
        return FUEL_VALUE[index];
    }

    public int getFuel(final byte[] dst, final int offset, final int length)
    {
        final int bytesCopied = Math.min(length, 6);
        System.arraycopy(FUEL_VALUE, 0, dst, offset, bytesCopied);

        return bytesCopied;
    }

    private final BoosterEncoder booster = new BoosterEncoder();

    public BoosterEncoder booster()
    {
        booster.wrap(buffer, offset + 6);
        return booster;
    }
    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        EngineDecoder writer = new EngineDecoder();
        writer.wrap(buffer, offset);

        return writer.appendTo(builder);
    }
}
