package hello.gtp;

import hello.Path;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GtpUtil {

    private void storePaths(Map<String, String> pathMap) {
        DatabaseFacade.get().putAll(pathMap);
    }

    private Path buildPath(String article) {
        StringBuilder sb = new StringBuilder(article);
        article = DatabaseFacade.get().get(article);
        int hopCount = 0;
        while (article != null) {
            sb.append(" ->\n");
            sb.append(article);
            hopCount++;
            article = DatabaseFacade.get().get(article);
        }
        return new Path(hopCount, sb.toString());
    }

    public Path findPhilosophy(String article) {

        Path resultPath = new Path(0, "todo insert error or info here");
        try {
            Map<String, String> pathMap = new HashMap<>();
            if (findPhilosophyRecursive(article, pathMap)) {
                storePaths(pathMap);
                resultPath = buildPath(article);
            } else {
                //handle errors and pages that do not reach philosophy
            }
        }
        finally {
            //todo: commit here, close on application shutdown
            DatabaseFacade.close();
        }
        return resultPath;
    }

    private boolean findPhilosophyRecursive(String article, Map<String, String> pathMap) {
        if (article == null || pathMap.size() > 100 || pathMap.containsKey(article)) {
            return false;
        } else if (DatabaseFacade.get().containsKey(article) || article.equalsIgnoreCase("http://en.wikipedia.org/wiki/philosophy")) {
            return true;
        } else {
            String nextLink = getNextLink(article);
            pathMap.put(article, nextLink);
            return findPhilosophyRecursive(nextLink, pathMap);
        }
    }

    private String getNextLink(String article) {
        String storedResult = DatabaseFacade.get().get(article);
        return storedResult == null ? getNextLinkOnline(article) : storedResult;
    }

    private String getNextLinkOnline(String article) {

        try {
            System.out.println("requesting: "  + article);
            URL url = new URL(article);
            Document doc = Jsoup.parse(url, 10000);

            //main text is a somewhat vague concept. since the page is not being rendered
            // and wikipedia does not have a clear or consistent styling for the concept...

            //make a bold assumption here to remove all tables
            //implied assumption is that this will break fewer pages than it fixes, based on a small random sample of cases.
            Elements elements = doc.getElementsByTag("table");
            for (Element table : elements) {
                table.remove();
            }

            // reverse-engineered css selector to attempt to grab approrpiate links for "Getting to Philosophy"
            Elements links = doc.select("p > a:not(.new):not(.external):not(.image)");


            //a second pass effort to skip links that might not match the spirit of "Getting to Philosophy"
            for (Element link : links) {
                String linkStr = link.toString();
                if (maybeGoodLink(linkStr)) {
                    return "http://en.wikipedia.org" + linkStr.substring(9, linkStr.indexOf("\"", 10));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean maybeGoodLink(String link) {
        return (link.contains("wiki") && !link.contains("Greek") && !link.contains("Latin") &&
                !link.contains("Help:") && !link.contains("#") && !link.contains("wiktionary"));
    }
}
