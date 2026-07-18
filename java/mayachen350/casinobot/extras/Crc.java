package mayachen350.casinobot.extras;

// Adapated from the PNG specs (v1.2) Sample CRC Code
// https://www.libpng.org/pub/png/spec/1.2/PNG-CRCAppendix.html
// (Because I didn't want to use JNI and I didn't want to write that in clojure)
public class Crc {
    static long[] crcTable = new long[256];
    static boolean isCrcTableComputed = false;

    public static void makeCrcTable() {
        long c;
        int n, k;

        for (n = 0; n < 256; n++) {
            c = (long) n;
            for (k = 0; k < 8; k++) {
                if ((c & 1) == 1)
                    c = 0xedb88320L ^ (c >> 1);
                else
                    c = c >> 1;
            }
            crcTable[n] = c;
        }
        isCrcTableComputed = true;
    }

    public static long updateCrc(long crc, byte[] buf) {
        long c = crc;
        int len = buf.length;
        int n;

        if (!isCrcTableComputed)
            makeCrcTable();
        for (n = 0; n < len; n++) {
            c = crcTable[(int)((c ^ buf[n]) & 0xffL)] ^ (c >> 8);
        }
        return c;
    }

    public static int makeCrc(byte[] buf) {
        return (int) (updateCrc(0xffffffffL, buf) ^ 0xffffffffL); // the crc in a png file seems to be only 4 bytes long
    }
}
