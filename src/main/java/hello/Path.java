package hello;

import java.util.LinkedList;
import java.util.List;

public class Path {

    public List<String> wikiPages = new LinkedList<>();

    public long getHopCount() {
        return wikiPages.size();
    }

    public List<String> getWikiPages() {
        return wikiPages;
    }
}
