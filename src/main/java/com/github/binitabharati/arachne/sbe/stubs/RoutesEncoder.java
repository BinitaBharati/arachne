/* Generated SBE (Simple Binary Encoding) message codec */
package com.github.binitabharati.arachne.sbe.stubs;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.github.binitabharati.arachne.sbe.stubs.RoutesEncoder"})
@SuppressWarnings("all")
public class RoutesEncoder
{
    /* MTU size used by RIP. 
     * Ref: http://networkengineering.stackexchange.com/questions/7583/why-rip-sending-only-maximum-of-512-bytes-of-data
            https://tools.ietf.org/html/rfc1058#page-19
    */
    public static final int BLOCK_LENGTH = 512; 
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final RoutesEncoder parentMessage = this;
    private MutableDirectBuffer buffer;
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

    public RoutesEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

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

    private final RouteEntriesEncoder routeEntries = new RouteEntriesEncoder();

    public static long routeEntriesId()
    {
        return 1;
    }

    public RouteEntriesEncoder routeEntriesCount(final int count)
    {
        routeEntries.wrap(parentMessage, buffer, count);
        return routeEntries;
    }

    public static class RouteEntriesEncoder
    {
        private static final int HEADER_SIZE = 4;
        private final GroupSizeEncodingEncoder dimensions = new GroupSizeEncodingEncoder();
        private RoutesEncoder parentMessage;
        private MutableDirectBuffer buffer;
        private int blockLength;
        private int actingVersion;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final RoutesEncoder parentMessage, final MutableDirectBuffer buffer, final int count)
        {
            if (count < 0 || count > 65534)
            {
                throw new IllegalArgumentException("count outside allowed range: count=" + count);
            }

            this.parentMessage = parentMessage;
            this.buffer = buffer;
            actingVersion = SCHEMA_VERSION;
            dimensions.wrap(buffer, parentMessage.limit());
            dimensions.blockLength((int)0);
            dimensions.numInGroup((int)count);
            index = -1;
            this.count = count;
            blockLength = 0;
            parentMessage.limit(parentMessage.limit() + HEADER_SIZE);
        }

        public static int sbeHeaderSize()
        {
            return HEADER_SIZE;
        }

        public static int sbeBlockLength()
        {
            return 0;
        }

        public RouteEntriesEncoder next()
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

        public static int destinationNwId()
        {
            return 1;
        }

        public static String destinationNwCharacterEncoding()
        {
            return "UTF-8";
        }

        public static String destinationNwMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
        }

        public static int destinationNwHeaderLength()
        {
            return 4;
        }

        public RouteEntriesEncoder putDestinationNw(final DirectBuffer src, final int srcOffset, final int length)
        {
            if (length > 1073741824)
            {
                throw new IllegalArgumentException("length > max value for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, (int)length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, src, srcOffset, length);

            return this;
        }

        public RouteEntriesEncoder putDestinationNw(final byte[] src, final int srcOffset, final int length)
        {
            if (length > 1073741824)
            {
                throw new IllegalArgumentException("length > max value for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, (int)length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, src, srcOffset, length);

            return this;
        }

        public RouteEntriesEncoder destinationNw(final String value)
        {
            final byte[] bytes;
            try
            {
                bytes = value.getBytes("UTF-8");
            }
            catch (final java.io.UnsupportedEncodingException ex)
            {
                throw new RuntimeException(ex);
            }

            final int length = bytes.length;
            if (length > 1073741824)
            {
                throw new IllegalArgumentException("length > max value for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, (int)length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, bytes, 0, length);

            return this;
        }

        public static int gatewayId()
        {
            return 2;
        }

        public static String gatewayCharacterEncoding()
        {
            return "UTF-8";
        }

        public static String gatewayMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
        }

        public static int gatewayHeaderLength()
        {
            return 4;
        }

        public RouteEntriesEncoder putGateway(final DirectBuffer src, final int srcOffset, final int length)
        {
            if (length > 1073741824)
            {
                throw new IllegalArgumentException("length > max value for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, (int)length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, src, srcOffset, length);

            return this;
        }

        public RouteEntriesEncoder putGateway(final byte[] src, final int srcOffset, final int length)
        {
            if (length > 1073741824)
            {
                throw new IllegalArgumentException("length > max value for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, (int)length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, src, srcOffset, length);

            return this;
        }

        public RouteEntriesEncoder gateway(final String value)
        {
            final byte[] bytes;
            try
            {
                bytes = value.getBytes("UTF-8");
            }
            catch (final java.io.UnsupportedEncodingException ex)
            {
                throw new RuntimeException(ex);
            }

            final int length = bytes.length;
            if (length > 1073741824)
            {
                throw new IllegalArgumentException("length > max value for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, (int)length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, bytes, 0, length);

            return this;
        }

        public static int netMaskId()
        {
            return 3;
        }

        public static String netMaskCharacterEncoding()
        {
            return "UTF-8";
        }

        public static String netMaskMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
        }

        public static int netMaskHeaderLength()
        {
            return 4;
        }

        public RouteEntriesEncoder putNetMask(final DirectBuffer src, final int srcOffset, final int length)
        {
            if (length > 1073741824)
            {
                throw new IllegalArgumentException("length > max value for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, (int)length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, src, srcOffset, length);

            return this;
        }

        public RouteEntriesEncoder putNetMask(final byte[] src, final int srcOffset, final int length)
        {
            if (length > 1073741824)
            {
                throw new IllegalArgumentException("length > max value for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, (int)length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, src, srcOffset, length);

            return this;
        }

        public RouteEntriesEncoder netMask(final String value)
        {
            final byte[] bytes;
            try
            {
                bytes = value.getBytes("UTF-8");
            }
            catch (final java.io.UnsupportedEncodingException ex)
            {
                throw new RuntimeException(ex);
            }

            final int length = bytes.length;
            if (length > 1073741824)
            {
                throw new IllegalArgumentException("length > max value for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, (int)length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, bytes, 0, length);

            return this;
        }

        public static int metricId()
        {
            return 4;
        }

        public static String metricCharacterEncoding()
        {
            return "UTF-8";
        }

        public static String metricMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
        }

        public static int metricHeaderLength()
        {
            return 4;
        }

        public RouteEntriesEncoder putMetric(final DirectBuffer src, final int srcOffset, final int length)
        {
            if (length > 1073741824)
            {
                throw new IllegalArgumentException("length > max value for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, (int)length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, src, srcOffset, length);

            return this;
        }

        public RouteEntriesEncoder putMetric(final byte[] src, final int srcOffset, final int length)
        {
            if (length > 1073741824)
            {
                throw new IllegalArgumentException("length > max value for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, (int)length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, src, srcOffset, length);

            return this;
        }

        public RouteEntriesEncoder metric(final String value)
        {
            final byte[] bytes;
            try
            {
                bytes = value.getBytes("UTF-8");
            }
            catch (final java.io.UnsupportedEncodingException ex)
            {
                throw new RuntimeException(ex);
            }

            final int length = bytes.length;
            if (length > 1073741824)
            {
                throw new IllegalArgumentException("length > max value for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, (int)length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, bytes, 0, length);

            return this;
        }

        public static int portId()
        {
            return 5;
        }

        public static String portCharacterEncoding()
        {
            return "UTF-8";
        }

        public static String portMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
        }

        public static int portHeaderLength()
        {
            return 4;
        }

        public RouteEntriesEncoder putPort(final DirectBuffer src, final int srcOffset, final int length)
        {
            if (length > 1073741824)
            {
                throw new IllegalArgumentException("length > max value for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, (int)length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, src, srcOffset, length);

            return this;
        }

        public RouteEntriesEncoder putPort(final byte[] src, final int srcOffset, final int length)
        {
            if (length > 1073741824)
            {
                throw new IllegalArgumentException("length > max value for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, (int)length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, src, srcOffset, length);

            return this;
        }

        public RouteEntriesEncoder port(final String value)
        {
            final byte[] bytes;
            try
            {
                bytes = value.getBytes("UTF-8");
            }
            catch (final java.io.UnsupportedEncodingException ex)
            {
                throw new RuntimeException(ex);
            }

            final int length = bytes.length;
            if (length > 1073741824)
            {
                throw new IllegalArgumentException("length > max value for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, (int)length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, bytes, 0, length);

            return this;
        }
    }
    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        RoutesDecoder writer = new RoutesDecoder();
        writer.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return writer.appendTo(builder);
    }
}
