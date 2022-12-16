package jwang;

import jwang.utils.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDate;

public class RunFirst {
  public static final String INPUT_URL_PATH_PATTERN = "https://adventofcode.com/%d/day/%d/input";

  public static void main(String... args) throws IOException {
    int year = LocalDate.now().getYear();
    int day = LocalDate.now().getDayOfMonth();
    downloadUsingNIO(String.format(INPUT_URL_PATH_PATTERN, year, day), String.format(Utils.INPUT_PATH_PATTERN, year, day));
  }

  private static void downloadUsingNIO(String urlStr, String file) throws MalformedURLException {
    URL url = new URL(urlStr);
    try (ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file)) {
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    } catch (IOException e) {
      e.getStackTrace();
    }
  }
}
