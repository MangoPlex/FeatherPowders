package featherpowders.binary;

import java.io.IOException;
import java.io.InputStream;

public class BinaryReader {
    
    private InputStream stream;
    
    public BinaryReader(InputStream stream) {
        this.stream = stream;
    }
    
    public InputStream stream() { return stream; }
    
    public int readVarUint() throws IOException {
        int b, v = 0, shift = 0;
        do {
            b = stream.read();
            if (b == -1) return 0;
            v |= (b & 0x7F) << shift;
            shift += 7;
        } while ((b & 0x80) > 0);
        return v;
    }
    
    public String readString() throws IOException {
        int bytes = readVarUint();
        return new String(stream.readNBytes(bytes));
    }
    
    public short readShort() throws IOException {
        byte[] bs = stream.readNBytes(2);
        return (short) ((bs[0] << 8) | bs[1]);
    }
    
    public int readInt() throws IOException {
        byte[] bs = stream.readNBytes(4);
        return (Byte.toUnsignedInt(bs[0]) << 24) |
                (Byte.toUnsignedInt(bs[1]) << 16) |
                (Byte.toUnsignedInt(bs[2]) << 8) |
                Byte.toUnsignedInt(bs[3]);
    }
    
    public long readLong() throws IOException {
        byte[] bs = stream.readNBytes(8);
        long l = (Byte.toUnsignedInt(bs[0]) << 24) |
                (Byte.toUnsignedInt(bs[1]) << 16) |
                (Byte.toUnsignedInt(bs[2]) << 8) |
                Byte.toUnsignedInt(bs[3]);
        l <<= 32;
        l |= (Byte.toUnsignedInt(bs[4]) << 24) |
                (Byte.toUnsignedInt(bs[5]) << 16) |
                (Byte.toUnsignedInt(bs[6]) << 8) |
                Byte.toUnsignedInt(bs[7]);
        return l;
    }
    
    public float readFloat() throws IOException { return Float.intBitsToFloat(readInt()); }
    public double readDouble() throws IOException { return Double.longBitsToDouble(readLong()); }
    
}
