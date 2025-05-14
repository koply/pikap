package me.koply.pikap.shell;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jline.reader.LineReader;

import java.io.PrintStream;
import java.util.Arrays;

public class PrintStreamWrapper extends PrintStream {

    private final LineReader reader;

    public PrintStreamWrapper(PrintStream original, LineReader reader) {
        super(original);
        this.reader = reader;
    }

    @Override
    public void println(boolean x) {
        reader.printAbove(x+"");
    }

    @Override
    public void println(char x) {
        reader.printAbove(x+"");
    }

    @Override
    public void println(int x) {
        reader.printAbove(x+"");
    }

    @Override
    public void println(long x) {
        reader.printAbove(x+"");
    }

    @Override
    public void println(float x) {
        reader.printAbove(x+"");
    }

    @Override
    public void println(double x) {
        reader.printAbove(x+"");
    }

    @Override
    public void println(@NotNull char[] x) {
        reader.printAbove(Arrays.toString(x));
    }

    @Override
    public void println(@Nullable String x) {
        reader.printAbove(x);
    }

    @Override
    public void println(Object x) {
        if (x == null) return;
        reader.printAbove(x.toString());
    }
}