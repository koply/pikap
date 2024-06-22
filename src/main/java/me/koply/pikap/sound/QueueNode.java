package me.koply.pikap.sound;

public class QueueNode {

    private int left;
    private boolean isPlaylist;

    public QueueNode(int left, boolean isPlaylist) {
        this.left = left;
        this.isPlaylist = isPlaylist;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public boolean isPlaylist() {
        return isPlaylist;
    }

    public void setPlaylist(boolean playlist) {
        isPlaylist = playlist;
    }
}
