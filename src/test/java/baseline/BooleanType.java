/* Generated SBE (Simple Binary Encoding) message codec */
package baseline;

@javax.annotation.Generated(value = {"baseline.BooleanType"})
public enum BooleanType
{
    F((short)0),
    T((short)1),
    NULL_VAL((short)255);

    private final short value;

    BooleanType(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static BooleanType get(final short value)
    {
        switch (value)
        {
            case 0: return F;
            case 1: return T;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
