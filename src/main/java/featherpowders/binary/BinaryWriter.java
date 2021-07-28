package featherpowders.binary;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BinaryWriter {
    
    private OutputStream stream;
    
    public BinaryWriter(OutputStream stream) { this.stream = stream; }
    
    public OutputStream stream() { return stream; }
    
    public void writeVarUint(int val) throws IOException {
        int b;
        do {
            b = val & 0x7F;
            val >>= 7;
            if (val != 0) b |= 0x80;
            stream.write(b);
        } while (val != 0);
    }
    
    public void writeString(String val) throws IOException {
        byte[] bytes = val.getBytes(StandardCharsets.UTF_8);
        writeVarUint(bytes.length);
        stream.write(bytes);
    }
    
    public void writeShort(short s) throws IOException {
        int i = Short.toUnsignedInt(s);
        stream.write(new byte[] {
                (byte) ((i & 0xFF00) >> 8),
                (byte) (i & 0x00FF)
        });
    }
    
    public void writeInt(int i) throws IOException {
        stream.write(new byte[] {
                (byte) ((i & 0xFF000000) >> 24),
                (byte) ((i & 0x00FF0000) >> 16),
                (byte) ((i & 0x0000FF00) >> 8),
                (byte) (i & 0x000000FF)
        });
    }
    
    public void writeLong(long l) throws IOException {
        stream.write(new byte[] {
                (byte) ((l & 0xFF000000_00000000L) >> 56),
                (byte) ((l & 0x00FF0000_00000000L) >> 48),
                (byte) ((l & 0x0000FF00_00000000L) >> 40),
                (byte) ((l & 0x000000FF_00000000L) >> 32),
                (byte) ((l & 0x00000000_FF000000L) >> 24),
                (byte) ((l & 0x00000000_00FF0000L) >> 16),
                (byte) ((l & 0x00000000_0000FF00L) >> 8),
                (byte) (l & 0x00000000_000000FFL)
        });
    }

    public void writeFloat(float f) throws IOException { writeInt(Float.floatToIntBits(f)); }
    public void writeDouble(double d) throws IOException { writeLong(Double.doubleToLongBits(d)); }
    
}
