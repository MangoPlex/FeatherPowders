package featherpowders.binary;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

class BinaryRWTest {

    @Test
    void testVarUint() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new BinaryWriter(out).writeVarUint(12345);
        
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        assertEquals(12345, new BinaryReader(in).readVarUint());
    }

    @Test
    void testInt() throws IOException {
        int v = 0x7FACF2DF;
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new BinaryWriter(out).writeInt(v);
        
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        assertEquals(v, new BinaryReader(in).readInt());
    }

    @Test
    void testFloat() throws IOException {
        float v = 69.42F;
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new BinaryWriter(out).writeFloat(v);
        
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        assertEquals(v, new BinaryReader(in).readFloat());
    }

    @Test
    void testDouble() throws IOException {
        double v = 69.42D;
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new BinaryWriter(out).writeDouble(v);
        
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        assertEquals(v, new BinaryReader(in).readDouble());
    }

}
