package me.koply.pikap.util;

import me.koply.pikap.sound.recorder.SeperatorBytes;

import java.nio.ByteBuffer;

public class ByteUtil {

    public static byte[] longToBytes(long data) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

        buffer.putLong(data);
        buffer.flip();
        return buffer.array();
    }

    public static String bytesToString(byte[] data) {
        return new String(data);
    }

    public static byte[] addPrefix(byte[] data, SeperatorBytes prefix) {
        int length = data.length+1;
        byte[] arr = new byte[length];
        arr[0] = prefix.value;
        System.arraycopy(data, 0, arr, 1, data.length);
        return arr;
    }

    public static byte[] surroundWith(byte[] data, SeperatorBytes start, SeperatorBytes end) {
        int length = data.length+2;
        byte[] arr = new byte[length];
        arr[0] = start.value;
        System.arraycopy(data, 0, arr, 1, data.length);
        arr[length-1] = end.value;
        return arr;
    }
}