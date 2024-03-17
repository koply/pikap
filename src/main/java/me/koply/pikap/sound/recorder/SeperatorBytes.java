package me.koply.pikap.sound.recorder;

public enum SeperatorBytes {
    TitleStart((byte) 0x01), TitleEnd((byte) 0x02),   // String
    AuthorStart((byte) 0x03), AuthorEnd((byte) 0x04), // String
    LengthStart((byte) 0x05),                         // Long
    IdentifierStart((byte) 0x06), IdentifierEnd((byte) 0x07); // String

    public final byte value;
    SeperatorBytes(byte value) {
        this.value = value;
    }
}