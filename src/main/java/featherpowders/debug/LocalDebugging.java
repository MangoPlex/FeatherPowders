package featherpowders.debug;

public class LocalDebugging {
    
    private static final int BYTES_PER_LINE = 16;
    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    
    public static void printBytes(byte[] bs) {
        String line = "00: ";
        
        for (int i = 0; i < bs.length; i++) {
            line += hexByte(bs[i]) + " ";
            if ((i % BYTES_PER_LINE) == BYTES_PER_LINE - 1) {
                System.out.println(line);
                line = hexByte((byte) (i / BYTES_PER_LINE)) + ": ";
            }
        }
        System.out.println(line);
    }
    
    private static String hexByte(byte b) {
        int i = Byte.toUnsignedInt(b);
        return HEX[(i & 0xF0) >> 4] + "" + HEX[i & 0x0F];
    }
    
}
