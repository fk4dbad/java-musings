package com.dasburo.fk.javatest.dataexcerpt;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataExcerptCrawler {

  private static final Logger LOG = LoggerFactory.getLogger(DataExcerptCrawler.class);

  private static final Pattern ID_URL_PATTERN = Pattern.compile("<td class=\"pre\">(.+) <a href=\"([^\\\"]+)\"");
  private static final Pattern TEXT_PATTERN = Pattern.compile("<td>([^<]+)</td>");

  private static final int MAX_DEPTH = 4;

  private static class UrlWithDepth {
    private final String url;
    private final int depth;

    public UrlWithDepth(String url, int depth) {
      super();
      this.url = url;
      this.depth = depth;
    }

    public Optional<UrlWithDepth> subUrl(String subUrl) {
      int subDepth = depth + 1;
      if (subDepth <= MAX_DEPTH) {
        return Optional.of(new UrlWithDepth(subUrl, subDepth));
      }
      return Optional.empty();
    }

    @Override
    public String toString() {
      return url + " (depth " + depth + ')';
    }
  }

  public static void main(String[] args) throws Exception {
    String mandant = "fong-haendler-vwn-germany-preview";

    if (args.length < 2) {
      LOG.error("missing login information: java DataExcerptCrawler <username> <password>");
      return;
    }

    Authenticator.setDefault(new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(args[0], args[1].toCharArray());
      }
    });

    String relativeUrl = "?mandant=" + mandant;

    Set<String> seenIds = new HashSet<>();
    Set<String> seenTexts = new HashSet<>();
    try (FileWriter idw = new FileWriter("/tmp/ids.txt"); FileWriter textw = new FileWriter("/tmp/texts.txt")) {
      Deque<UrlWithDepth> urls = new ArrayDeque<>();
      urls.addLast(new UrlWithDepth(relativeUrl, 0));

      while (!urls.isEmpty()) {
        UrlWithDepth nextUrl = urls.removeFirst();
        LOG.info("got {} url(s) left, next: {}", urls.size(), nextUrl);
        URL url = new URL("https://acc-prepare.audi.de/fong-0/pages/test/fong/data-excerpt.jsp" + nextUrl.url);
        List<String> tdLines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
          tdLines = reader.lines()
              .filter(line -> line.contains("<td"))
              .collect(Collectors.toList());
        }
        for (String line : tdLines) {
          Matcher idUrlMatcher = ID_URL_PATTERN.matcher(line);
          if (idUrlMatcher.find()) {
            String id = idUrlMatcher.group(1);
            String urlStr = idUrlMatcher.group(2);
            if (seenIds.add(id)) {
              idw.write(id + "\n");
            }
            nextUrl.subUrl(urlStr)
                .ifPresent(urls::addLast);
          } else {
            Matcher textMatcher = TEXT_PATTERN.matcher(line);
            if (textMatcher.find()) {
              String text = textMatcher.group(1);
              if (seenTexts.add(text)) {
                textw.write(text + "\n");
              }
            }
          }
        }
      }
    }
  }
}
