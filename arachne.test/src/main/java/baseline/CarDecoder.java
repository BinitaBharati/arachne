/* Generated SBE (Simple Binary Encoding) message codec */
package baseline;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"baseline.CarDecoder"})
@SuppressWarnings("all")
public class CarDecoder
{
    public static final int BLOCK_LENGTH = 47;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final CarDecoder parentMessage = this;
    private DirectBuffer buffer;
    protected int offset;
    protected int limit;
    protected int actingBlockLength;
    protected int actingVersion;

    public int sbeBlockLength()
    {
        return BLOCK_LENGTH;
    }

    public int sbeTemplateId()
    {
        return TEMPLATE_ID;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public String sbeSemanticType()
    {
        return "";
    }

    public int offset()
    {
        return offset;
    }

    public CarDecoder wrap(
        final DirectBuffer buffer, final int offset, final int actingBlockLength, final int actingVersion)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        limit(offset + actingBlockLength);

        return this;
    }

    public int encodedLength()
    {
        return limit - offset;
    }

    public int limit()
    {
        return limit;
    }

    public void limit(final int limit)
    {
        this.limit = limit;
    }

    public static int serialNumberId()
    {
        return 1;
    }

    public static int serialNumberSinceVersion()
    {
        return 0;
    }

    public static String serialNumberMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long serialNumberNullValue()
    {
        return 0xffffffffffffffffL;
    }

    public static long serialNumberMinValue()
    {
        return 0x0L;
    }

    public static long serialNumberMaxValue()
    {
        return 0xfffffffffffffffeL;
    }

    public long serialNumber()
    {
        return buffer.getLong(offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int modelYearId()
    {
        return 2;
    }

    public static int modelYearSinceVersion()
    {
        return 0;
    }

    public static String modelYearMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static int modelYearNullValue()
    {
        return 65535;
    }

    public static int modelYearMinValue()
    {
        return 0;
    }

    public static int modelYearMaxValue()
    {
        return 65534;
    }

    public int modelYear()
    {
        return (buffer.getShort(offset + 8, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF);
    }


    public static int availableId()
    {
        return 3;
    }

    public static int availableSinceVersion()
    {
        return 0;
    }

    public static String availableMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public BooleanType available()
    {
        return BooleanType.get(((short)(buffer.getByte(offset + 10) & 0xFF)));
    }


    public static int codeId()
    {
        return 4;
    }

    public static int codeSinceVersion()
    {
        return 0;
    }

    public static String codeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public Model code()
    {
        return Model.get(buffer.getByte(offset + 11));
    }


    public static int someNumbersId()
    {
        return 5;
    }

    public static int someNumbersSinceVersion()
    {
        return 0;
    }

    public static String someNumbersMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long someNumbersNullValue()
    {
        return 4294967294L;
    }

    public static long someNumbersMinValue()
    {
        return 0L;
    }

    public static long someNumbersMaxValue()
    {
        return 4294967293L;
    }

    public static int someNumbersLength()
    {
        return 5;
    }

    public long someNumbers(final int index)
    {
        if (index < 0 || index >= 5)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 12 + (index * 4);

        return (buffer.getInt(pos, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
    }


    public static int vehicleCodeId()
    {
        return 6;
    }

    public static int vehicleCodeSinceVersion()
    {
        return 0;
    }

    public static String vehicleCodeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static byte vehicleCodeNullValue()
    {
        return (byte)0;
    }

    public static byte vehicleCodeMinValue()
    {
        return (byte)32;
    }

    public static byte vehicleCodeMaxValue()
    {
        return (byte)126;
    }

    public static int vehicleCodeLength()
    {
        return 6;
    }

    public byte vehicleCode(final int index)
    {
        if (index < 0 || index >= 6)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 32 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String vehicleCodeCharacterEncoding()
    {
        return "ASCII";
    }

    public int getVehicleCode(final byte[] dst, final int dstOffset)
    {
        final int length = 6;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        buffer.getBytes(this.offset + 32, dst, dstOffset, length);

        return length;
    }

    public String vehicleCode()
    {
        final byte[] dst = new byte[6];
        buffer.getBytes(this.offset + 32, dst, 0, 6);

        int end = 0;
        for (; end < 6 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public static int extrasId()
    {
        return 7;
    }

    public static int extrasSinceVersion()
    {
        return 0;
    }

    public static String extrasMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    private final OptionalExtrasDecoder extras = new OptionalExtrasDecoder();

    public OptionalExtrasDecoder extras()
    {
        extras.wrap(buffer, offset + 38);
        return extras;
    }

    public static int discountedModelId()
    {
        return 8;
    }

    public static int discountedModelSinceVersion()
    {
        return 0;
    }

    public static String discountedModelMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public Model discountedModel()
    {
        return Model.C;
    }


    public static int engineId()
    {
        return 9;
    }

    public static int engineSinceVersion()
    {
        return 0;
    }

    public static String engineMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    private final EngineDecoder engine = new EngineDecoder();

    public EngineDecoder engine()
    {
        engine.wrap(buffer, offset + 39);
        return engine;
    }

    private final FuelFiguresDecoder fuelFigures = new FuelFiguresDecoder();

    public static long fuelFiguresDecoderId()
    {
        return 10;
    }

    public static int fuelFiguresDecoderSinceVersion()
    {
        return 0;
    }

    public FuelFiguresDecoder fuelFigures()
    {
        fuelFigures.wrap(parentMessage, buffer);
        return fuelFigures;
    }

    public static class FuelFiguresDecoder
        implements Iterable<FuelFiguresDecoder>, java.util.Iterator<FuelFiguresDecoder>
    {
        private static final int HEADER_SIZE = 4;
        private final GroupSizeEncodingDecoder dimensions = new GroupSizeEncodingDecoder();
        private CarDecoder parentMessage;
        private DirectBuffer buffer;
        private int blockLength;
        private int actingVersion;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final CarDecoder parentMessage, final DirectBuffer buffer)
        {
            this.parentMessage = parentMessage;
            this.buffer = buffer;
            dimensions.wrap(buffer, parentMessage.limit());
            blockLength = dimensions.blockLength();
            count = dimensions.numInGroup();
            index = -1;
            parentMessage.limit(parentMessage.limit() + HEADER_SIZE);
        }

        public static int sbeHeaderSize()
        {
            return HEADER_SIZE;
        }

        public static int sbeBlockLength()
        {
            return 6;
        }

        public int actingBlockLength()
        {
            return blockLength;
        }

        public int count()
        {
            return count;
        }

        public java.util.Iterator<FuelFiguresDecoder> iterator()
        {
            return this;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext()
        {
            return (index + 1) < count;
        }

        public FuelFiguresDecoder next()
        {
            if (index + 1 >= count)
            {
                throw new java.util.NoSuchElementException();
            }

            offset = parentMessage.limit();
            parentMessage.limit(offset + blockLength);
            ++index;

            return this;
        }

        public static int speedId()
        {
            return 11;
        }

        public static int speedSinceVersion()
        {
            return 0;
        }

        public static String speedMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
        }

        public static int speedNullValue()
        {
            return 65535;
        }

        public static int speedMinValue()
        {
            return 0;
        }

        public static int speedMaxValue()
        {
            return 65534;
        }

        public int speed()
        {
            return (buffer.getShort(offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF);
        }


        public static int mpgId()
        {
            return 12;
        }

        public static int mpgSinceVersion()
        {
            return 0;
        }

        public static String mpgMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
        }

        public static float mpgNullValue()
        {
            return Float.NaN;
        }

        public static float mpgMinValue()
        {
            return 1.401298464324817E-45f;
        }

        public static float mpgMaxValue()
        {
            return 3.4028234663852886E38f;
        }

        public float mpg()
        {
            return buffer.getFloat(offset + 2, java.nio.ByteOrder.LITTLE_ENDIAN);
        }


        public static int usageDescriptionId()
        {
            return 200;
        }

        public static int usageDescriptionSinceVersion()
        {
            return 0;
        }

        public static String usageDescriptionCharacterEncoding()
        {
            return "UTF-8";
        }

        public static String usageDescriptionMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
        }

        public static int usageDescriptionHeaderLength()
        {
            return 4;
        }

        public int usageDescriptionLength()
        {
            final int limit = parentMessage.limit();
            return (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
        }

        public int getUsageDescription(final MutableDirectBuffer dst, final int dstOffset, final int length)
        {
            final int headerLength = 4;
            final int limit = parentMessage.limit();
            final int dataLength = (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
            final int bytesCopied = Math.min(length, dataLength);
            parentMessage.limit(limit + headerLength + dataLength);
            buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

            return bytesCopied;
        }

        public int getUsageDescription(final byte[] dst, final int dstOffset, final int length)
        {
            final int headerLength = 4;
            final int limit = parentMessage.limit();
            final int dataLength = (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
            final int bytesCopied = Math.min(length, dataLength);
            parentMessage.limit(limit + headerLength + dataLength);
            buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

            return bytesCopied;
        }

        public String usageDescription()
        {
            final int headerLength = 4;
            final int limit = parentMessage.limit();
            final int dataLength = (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
            parentMessage.limit(limit + headerLength + dataLength);
            final byte[] tmp = new byte[dataLength];
            buffer.getBytes(limit + headerLength, tmp, 0, dataLength);

            final String value;
            try
            {
                value = new String(tmp, "UTF-8");
            }
            catch (final java.io.UnsupportedEncodingException ex)
            {
                throw new RuntimeException(ex);
            }

            return value;
        }

        public String toString()
        {
            return appendTo(new StringBuilder(100)).toString();
        }

        public StringBuilder appendTo(final StringBuilder builder)
        {
            builder.append('(');
            //Token{signal=BEGIN_FIELD, name='speed', description='null', id=11, version=0, encodedLength=0, offset=0, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
            //Token{signal=ENCODING, name='uint16', description='null', id=-1, version=0, encodedLength=2, offset=0, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT16, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
            builder.append("speed=");
            builder.append(speed());
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='mpg', description='null', id=12, version=0, encodedLength=0, offset=2, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
            //Token{signal=ENCODING, name='float', description='null', id=-1, version=0, encodedLength=4, offset=2, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=FLOAT, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
            builder.append("mpg=");
            builder.append(mpg());
            builder.append('|');
            //Token{signal=BEGIN_VAR_DATA, name='usageDescription', description='null', id=200, version=0, encodedLength=0, offset=6, componentTokenCount=6, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
            builder.append("usageDescription=");
            builder.append(usageDescription());
            builder.append(')');
            return builder;
        }
    }

    private final PerformanceFiguresDecoder performanceFigures = new PerformanceFiguresDecoder();

    public static long performanceFiguresDecoderId()
    {
        return 13;
    }

    public static int performanceFiguresDecoderSinceVersion()
    {
        return 0;
    }

    public PerformanceFiguresDecoder performanceFigures()
    {
        performanceFigures.wrap(parentMessage, buffer);
        return performanceFigures;
    }

    public static class PerformanceFiguresDecoder
        implements Iterable<PerformanceFiguresDecoder>, java.util.Iterator<PerformanceFiguresDecoder>
    {
        private static final int HEADER_SIZE = 4;
        private final GroupSizeEncodingDecoder dimensions = new GroupSizeEncodingDecoder();
        private CarDecoder parentMessage;
        private DirectBuffer buffer;
        private int blockLength;
        private int actingVersion;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final CarDecoder parentMessage, final DirectBuffer buffer)
        {
            this.parentMessage = parentMessage;
            this.buffer = buffer;
            dimensions.wrap(buffer, parentMessage.limit());
            blockLength = dimensions.blockLength();
            count = dimensions.numInGroup();
            index = -1;
            parentMessage.limit(parentMessage.limit() + HEADER_SIZE);
        }

        public static int sbeHeaderSize()
        {
            return HEADER_SIZE;
        }

        public static int sbeBlockLength()
        {
            return 1;
        }

        public int actingBlockLength()
        {
            return blockLength;
        }

        public int count()
        {
            return count;
        }

        public java.util.Iterator<PerformanceFiguresDecoder> iterator()
        {
            return this;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext()
        {
            return (index + 1) < count;
        }

        public PerformanceFiguresDecoder next()
        {
            if (index + 1 >= count)
            {
                throw new java.util.NoSuchElementException();
            }

            offset = parentMessage.limit();
            parentMessage.limit(offset + blockLength);
            ++index;

            return this;
        }

        public static int octaneRatingId()
        {
            return 14;
        }

        public static int octaneRatingSinceVersion()
        {
            return 0;
        }

        public static String octaneRatingMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
        }

        public static short octaneRatingNullValue()
        {
            return (short)255;
        }

        public static short octaneRatingMinValue()
        {
            return (short)90;
        }

        public static short octaneRatingMaxValue()
        {
            return (short)110;
        }

        public short octaneRating()
        {
            return ((short)(buffer.getByte(offset + 0) & 0xFF));
        }


        private final AccelerationDecoder acceleration = new AccelerationDecoder();

        public static long accelerationDecoderId()
        {
            return 15;
        }

        public static int accelerationDecoderSinceVersion()
        {
            return 0;
        }

        public AccelerationDecoder acceleration()
        {
            acceleration.wrap(parentMessage, buffer);
            return acceleration;
        }

        public static class AccelerationDecoder
            implements Iterable<AccelerationDecoder>, java.util.Iterator<AccelerationDecoder>
        {
            private static final int HEADER_SIZE = 4;
            private final GroupSizeEncodingDecoder dimensions = new GroupSizeEncodingDecoder();
            private CarDecoder parentMessage;
            private DirectBuffer buffer;
            private int blockLength;
            private int actingVersion;
            private int count;
            private int index;
            private int offset;

            public void wrap(
                final CarDecoder parentMessage, final DirectBuffer buffer)
            {
                this.parentMessage = parentMessage;
                this.buffer = buffer;
                dimensions.wrap(buffer, parentMessage.limit());
                blockLength = dimensions.blockLength();
                count = dimensions.numInGroup();
                index = -1;
                parentMessage.limit(parentMessage.limit() + HEADER_SIZE);
            }

            public static int sbeHeaderSize()
            {
                return HEADER_SIZE;
            }

            public static int sbeBlockLength()
            {
                return 6;
            }

            public int actingBlockLength()
            {
                return blockLength;
            }

            public int count()
            {
                return count;
            }

            public java.util.Iterator<AccelerationDecoder> iterator()
            {
                return this;
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }

            public boolean hasNext()
            {
                return (index + 1) < count;
            }

            public AccelerationDecoder next()
            {
                if (index + 1 >= count)
                {
                    throw new java.util.NoSuchElementException();
                }

                offset = parentMessage.limit();
                parentMessage.limit(offset + blockLength);
                ++index;

                return this;
            }

            public static int mphId()
            {
                return 16;
            }

            public static int mphSinceVersion()
            {
                return 0;
            }

            public static String mphMetaAttribute(final MetaAttribute metaAttribute)
            {
                switch (metaAttribute)
                {
                    case EPOCH: return "unix";
                    case TIME_UNIT: return "nanosecond";
                    case SEMANTIC_TYPE: return "";
                }

                return "";
            }

            public static int mphNullValue()
            {
                return 65535;
            }

            public static int mphMinValue()
            {
                return 0;
            }

            public static int mphMaxValue()
            {
                return 65534;
            }

            public int mph()
            {
                return (buffer.getShort(offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF);
            }


            public static int secondsId()
            {
                return 17;
            }

            public static int secondsSinceVersion()
            {
                return 0;
            }

            public static String secondsMetaAttribute(final MetaAttribute metaAttribute)
            {
                switch (metaAttribute)
                {
                    case EPOCH: return "unix";
                    case TIME_UNIT: return "nanosecond";
                    case SEMANTIC_TYPE: return "";
                }

                return "";
            }

            public static float secondsNullValue()
            {
                return Float.NaN;
            }

            public static float secondsMinValue()
            {
                return 1.401298464324817E-45f;
            }

            public static float secondsMaxValue()
            {
                return 3.4028234663852886E38f;
            }

            public float seconds()
            {
                return buffer.getFloat(offset + 2, java.nio.ByteOrder.LITTLE_ENDIAN);
            }


            public String toString()
            {
                return appendTo(new StringBuilder(100)).toString();
            }

            public StringBuilder appendTo(final StringBuilder builder)
            {
                builder.append('(');
                //Token{signal=BEGIN_FIELD, name='mph', description='null', id=16, version=0, encodedLength=0, offset=0, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
                //Token{signal=ENCODING, name='uint16', description='null', id=-1, version=0, encodedLength=2, offset=0, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT16, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
                builder.append("mph=");
                builder.append(mph());
                builder.append('|');
                //Token{signal=BEGIN_FIELD, name='seconds', description='null', id=17, version=0, encodedLength=0, offset=2, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
                //Token{signal=ENCODING, name='float', description='null', id=-1, version=0, encodedLength=4, offset=2, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=FLOAT, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
                builder.append("seconds=");
                builder.append(seconds());
                builder.append(')');
                return builder;
            }
        }

        public String toString()
        {
            return appendTo(new StringBuilder(100)).toString();
        }

        public StringBuilder appendTo(final StringBuilder builder)
        {
            builder.append('(');
            //Token{signal=BEGIN_FIELD, name='octaneRating', description='null', id=14, version=0, encodedLength=0, offset=0, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
            //Token{signal=ENCODING, name='Ron', description='null', id=-1, version=0, encodedLength=1, offset=0, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT8, byteOrder=LITTLE_ENDIAN, minValue=90, maxValue=110, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
            builder.append("octaneRating=");
            builder.append(octaneRating());
            builder.append('|');
            //Token{signal=BEGIN_GROUP, name='acceleration', description='null', id=15, version=0, encodedLength=6, offset=1, componentTokenCount=12, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='null'}}
            builder.append("acceleration=[");
            AccelerationDecoder acceleration = acceleration();
            if (acceleration.count() > 0)
            {
                while (acceleration.hasNext())
                {
                    acceleration.next().appendTo(builder);
                    builder.append(',');
                }
                builder.setLength(builder.length() - 1);
            }
            builder.append(']');
            builder.append(')');
            return builder;
        }
    }

    public static int makeId()
    {
        return 18;
    }

    public static int makeSinceVersion()
    {
        return 0;
    }

    public static String makeCharacterEncoding()
    {
        return "UTF-8";
    }

    public static String makeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static int makeHeaderLength()
    {
        return 4;
    }

    public int makeLength()
    {
        final int limit = parentMessage.limit();
        return (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
    }

    public int getMake(final MutableDirectBuffer dst, final int dstOffset, final int length)
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
        final int bytesCopied = Math.min(length, dataLength);
        parentMessage.limit(limit + headerLength + dataLength);
        buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

        return bytesCopied;
    }

    public int getMake(final byte[] dst, final int dstOffset, final int length)
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
        final int bytesCopied = Math.min(length, dataLength);
        parentMessage.limit(limit + headerLength + dataLength);
        buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

        return bytesCopied;
    }

    public String make()
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
        parentMessage.limit(limit + headerLength + dataLength);
        final byte[] tmp = new byte[dataLength];
        buffer.getBytes(limit + headerLength, tmp, 0, dataLength);

        final String value;
        try
        {
            value = new String(tmp, "UTF-8");
        }
        catch (final java.io.UnsupportedEncodingException ex)
        {
            throw new RuntimeException(ex);
        }

        return value;
    }

    public static int modelId()
    {
        return 19;
    }

    public static int modelSinceVersion()
    {
        return 0;
    }

    public static String modelCharacterEncoding()
    {
        return "UTF-8";
    }

    public static String modelMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static int modelHeaderLength()
    {
        return 4;
    }

    public int modelLength()
    {
        final int limit = parentMessage.limit();
        return (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
    }

    public int getModel(final MutableDirectBuffer dst, final int dstOffset, final int length)
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
        final int bytesCopied = Math.min(length, dataLength);
        parentMessage.limit(limit + headerLength + dataLength);
        buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

        return bytesCopied;
    }

    public int getModel(final byte[] dst, final int dstOffset, final int length)
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
        final int bytesCopied = Math.min(length, dataLength);
        parentMessage.limit(limit + headerLength + dataLength);
        buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

        return bytesCopied;
    }

    public String model()
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
        parentMessage.limit(limit + headerLength + dataLength);
        final byte[] tmp = new byte[dataLength];
        buffer.getBytes(limit + headerLength, tmp, 0, dataLength);

        final String value;
        try
        {
            value = new String(tmp, "UTF-8");
        }
        catch (final java.io.UnsupportedEncodingException ex)
        {
            throw new RuntimeException(ex);
        }

        return value;
    }

    public static int activationCodeId()
    {
        return 20;
    }

    public static int activationCodeSinceVersion()
    {
        return 0;
    }

    public static String activationCodeCharacterEncoding()
    {
        return "UTF-8";
    }

    public static String activationCodeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static int activationCodeHeaderLength()
    {
        return 4;
    }

    public int activationCodeLength()
    {
        final int limit = parentMessage.limit();
        return (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
    }

    public int getActivationCode(final MutableDirectBuffer dst, final int dstOffset, final int length)
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
        final int bytesCopied = Math.min(length, dataLength);
        parentMessage.limit(limit + headerLength + dataLength);
        buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

        return bytesCopied;
    }

    public int getActivationCode(final byte[] dst, final int dstOffset, final int length)
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
        final int bytesCopied = Math.min(length, dataLength);
        parentMessage.limit(limit + headerLength + dataLength);
        buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

        return bytesCopied;
    }

    public String activationCode()
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
        parentMessage.limit(limit + headerLength + dataLength);
        final byte[] tmp = new byte[dataLength];
        buffer.getBytes(limit + headerLength, tmp, 0, dataLength);

        final String value;
        try
        {
            value = new String(tmp, "UTF-8");
        }
        catch (final java.io.UnsupportedEncodingException ex)
        {
            throw new RuntimeException(ex);
        }

        return value;
    }

    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        final int originalLimit = limit();
        limit(offset + actingBlockLength);
        builder.append("[Car](sbeTemplateId=");
        builder.append(TEMPLATE_ID);
        builder.append("|sbeSchemaId=");
        builder.append(SCHEMA_ID);
        builder.append("|sbeSchemaVersion=");
        if (actingVersion != SCHEMA_VERSION)
        {
            builder.append(actingVersion);
            builder.append('/');
        }
        builder.append(SCHEMA_VERSION);
        builder.append("|sbeBlockLength=");
        if (actingBlockLength != BLOCK_LENGTH)
        {
            builder.append(actingBlockLength);
            builder.append('/');
        }
        builder.append(BLOCK_LENGTH);
        builder.append("):");
        //Token{signal=BEGIN_FIELD, name='serialNumber', description='null', id=1, version=0, encodedLength=0, offset=0, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        //Token{signal=ENCODING, name='uint64', description='null', id=-1, version=0, encodedLength=8, offset=0, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT64, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        builder.append("serialNumber=");
        builder.append(serialNumber());
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='modelYear', description='null', id=2, version=0, encodedLength=0, offset=8, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        //Token{signal=ENCODING, name='ModelYear', description='null', id=-1, version=0, encodedLength=2, offset=8, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT16, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        builder.append("modelYear=");
        builder.append(modelYear());
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='available', description='null', id=3, version=0, encodedLength=0, offset=10, componentTokenCount=6, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        //Token{signal=BEGIN_ENUM, name='BooleanType', description='null', id=-1, version=0, encodedLength=1, offset=10, componentTokenCount=4, encoding=Encoding{presence=REQUIRED, primitiveType=UINT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("available=");
        builder.append(available());
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='code', description='null', id=4, version=0, encodedLength=0, offset=11, componentTokenCount=7, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        //Token{signal=BEGIN_ENUM, name='Model', description='null', id=-1, version=0, encodedLength=1, offset=11, componentTokenCount=5, encoding=Encoding{presence=REQUIRED, primitiveType=CHAR, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("code=");
        builder.append(code());
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='someNumbers', description='null', id=5, version=0, encodedLength=0, offset=12, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        //Token{signal=ENCODING, name='someNumbers', description='null', id=-1, version=0, encodedLength=20, offset=12, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT32, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        builder.append("someNumbers=");
        builder.append('[');
        if (someNumbersLength() > 0)
        {
            for (int i = 0; i < someNumbersLength(); i++)
            {
                builder.append(someNumbers(i));
                builder.append(',');
            }
            builder.setLength(builder.length() - 1);
        }
        builder.append(']');
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='vehicleCode', description='null', id=6, version=0, encodedLength=0, offset=32, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        //Token{signal=ENCODING, name='VehicleCode', description='null', id=-1, version=0, encodedLength=6, offset=32, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=CHAR, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='ASCII', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        builder.append("vehicleCode=");
        for (int i = 0; i < vehicleCodeLength() && vehicleCode(i) > 0; i++)
        {
            builder.append((char)vehicleCode(i));
        }
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='extras', description='null', id=7, version=0, encodedLength=0, offset=38, componentTokenCount=7, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        //Token{signal=BEGIN_SET, name='OptionalExtras', description='null', id=-1, version=0, encodedLength=1, offset=38, componentTokenCount=5, encoding=Encoding{presence=REQUIRED, primitiveType=UINT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("extras=");
        builder.append(extras());
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='discountedModel', description='null', id=8, version=0, encodedLength=0, offset=39, componentTokenCount=5, encoding=Encoding{presence=CONSTANT, primitiveType=CHAR, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=Model.C, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        //Token{signal=BEGIN_ENUM, name='Model', description='null', id=-1, version=0, encodedLength=0, offset=39, componentTokenCount=3, encoding=Encoding{presence=CONSTANT, primitiveType=CHAR, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='null'}}
        //Token{signal=BEGIN_FIELD, name='engine', description='null', id=9, version=0, encodedLength=0, offset=39, componentTokenCount=18, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        //Token{signal=BEGIN_COMPOSITE, name='Engine', description='null', id=-1, version=0, encodedLength=8, offset=39, componentTokenCount=16, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("engine=");
        engine().appendTo(builder);
        builder.append('|');
        //Token{signal=BEGIN_GROUP, name='fuelFigures', description='null', id=10, version=0, encodedLength=6, offset=47, componentTokenCount=18, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("fuelFigures=[");
        FuelFiguresDecoder fuelFigures = fuelFigures();
        if (fuelFigures.count() > 0)
        {
            while (fuelFigures.hasNext())
            {
                fuelFigures.next().appendTo(builder);
                builder.append(',');
            }
            builder.setLength(builder.length() - 1);
        }
        builder.append(']');
        builder.append('|');
        //Token{signal=BEGIN_GROUP, name='performanceFigures', description='null', id=13, version=0, encodedLength=1, offset=-1, componentTokenCount=21, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("performanceFigures=[");
        PerformanceFiguresDecoder performanceFigures = performanceFigures();
        if (performanceFigures.count() > 0)
        {
            while (performanceFigures.hasNext())
            {
                performanceFigures.next().appendTo(builder);
                builder.append(',');
            }
            builder.setLength(builder.length() - 1);
        }
        builder.append(']');
        builder.append('|');
        //Token{signal=BEGIN_VAR_DATA, name='make', description='null', id=18, version=0, encodedLength=0, offset=-1, componentTokenCount=6, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        builder.append("make=");
        builder.append(make());
        builder.append('|');
        //Token{signal=BEGIN_VAR_DATA, name='model', description='null', id=19, version=0, encodedLength=0, offset=-1, componentTokenCount=6, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        builder.append("model=");
        builder.append(model());
        builder.append('|');
        //Token{signal=BEGIN_VAR_DATA, name='activationCode', description='null', id=20, version=0, encodedLength=0, offset=-1, componentTokenCount=6, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='null'}}
        builder.append("activationCode=");
        builder.append(activationCode());

        limit(originalLimit);

        return builder;
    }
}
