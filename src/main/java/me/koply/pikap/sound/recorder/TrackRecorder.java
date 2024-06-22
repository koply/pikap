package me.koply.pikap.sound.recorder;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.koply.pikap.Constants;
import me.koply.pikap.Main;
import me.koply.pikap.util.ByteUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TrackRecorder {

    private final AudioTrackInfo info;
    private final File file;

    public TrackRecorder(AudioTrackInfo info) {
        this.info = info;

        String recFolder = Main.CONFIG.get("recFolder");
        recFolder = recFolder.endsWith("/") ? recFolder : recFolder.concat("/");
        this.file = new File(recFolder + info.identifier);
    }

    private final List<byte[]> bytes = new ArrayList<>(130);

    public void writeHeader() {
        byte[] prefix = Constants.PTF_HEADER_PREFIX;
        byte[] btitle = info.title.getBytes(StandardCharsets.UTF_8);
        byte[] bauthor = info.author.getBytes(StandardCharsets.UTF_8);
        byte[] blength = ByteUtil.longToBytes(info.length);
        byte[] bid = info.identifier.getBytes(StandardCharsets.UTF_8);

        bytes.add(prefix);
        bytes.add(ByteUtil.surroundWith(btitle, SeperatorBytes.TitleStart, SeperatorBytes.TitleEnd));
        bytes.add(ByteUtil.surroundWith(bauthor, SeperatorBytes.AuthorStart, SeperatorBytes.AuthorEnd));
        bytes.add(ByteUtil.addPrefix(blength, SeperatorBytes.LengthStart));
        bytes.add(ByteUtil.surroundWith(bid, SeperatorBytes.IdentifierStart, SeperatorBytes.IdentifierEnd));

        //ByteBuffer buffer = ByteBuffer.allocate();
    }

    // TODO
    public void addArray(byte[] arr) {
        bytes.add(arr);
    }

    public void saveFile() {

    }

    public void close() {

    }

}