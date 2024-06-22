package me.koply.pikap.util;

import com.github.tomaslanger.chalk.Chalk;
import me.koply.pikap.api.cli.Console;

import java.util.ArrayList;
import java.util.List;

public class OutputPager {

    private final List<String> pages;
    private int selectedPage;

    // selectedPage starts from 0, not 1
    public OutputPager(List<String> pages, int selectedPage) {
        this.pages = pages;
        this.selectedPage = selectedPage < pages.size() && selectedPage > 0 ? selectedPage : 0;
    }

    public void execute(String requestStr) {
        while (true) {
            Console.println(pages.get(selectedPage));
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i<=pages.size(); i++) {
                if (i-1 == selectedPage) {
                    sb.append(Chalk.on("[" + i + "] ").blue().bold());
                } else {
                    sb.append(Chalk.on("[" + i + "] ").yellow());
                }
            }
            Console.println(sb.toString());
            int readedPage = Util.readInteger(requestStr, 1, pages.size()) - 1;
            if (readedPage == -1) return;
            if (selectedPage == readedPage) continue;

            selectedPage = readedPage;
        }
    }

    public static OutputPager splitAndGenerate(String text, int linesPerPage) {
        String[] lines = text.split("\n");
        List<String> pages = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<lines.length; i++) {
            sb.append(lines[i]);
            if (i % linesPerPage == 0) {
                pages.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append("\n");
            }
        }
        if (sb.length() > 0) pages.add(sb.toString());
        return new OutputPager(pages, 0);
    }

}
