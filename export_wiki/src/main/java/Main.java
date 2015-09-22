import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;


/**
 * Created by trung.do on 9/22/2015.
 */
public class Main {
    private static final String OUTPUT_DIRECTORY = "output";

    private static void println(String s) {
        System.out.println(s);
    }

    private static String getLastUrl(String url) {
        String[] splits = url.split("/");
        return splits[splits.length - 1];
    }

    private static void exportFromUrl(String url) throws Exception {
        println("url: " + url);
        println("starting to export");
        Connection connection = Jsoup.connect(url);
        Document doc = connection.get();

//        File input = new File("C:/Users/trung.do/Downloads/abc.html");
//        Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
        PrintWriter writer = new PrintWriter(OUTPUT_DIRECTORY + "/" + getLastUrl(url) + ".txt", "UTF-8");


        //Elements elements = doc.select("html body div#content.mw-body div#bodyContent.mw-body-content div#mw-content-text.mw-content-ltr ul li a[title]:not([class])");
        Elements elements = doc.select("html body div#content.mw-body div#bodyContent.mw-body-content div#mw-content-text.mw-content-ltr ul li");
        for (Element element : elements) {
            String text = element.text();
            if (text.contains(",")) {
                String[] split = text.split(",");
                writer.println(split[0]);
            }
        }
        writer.close();
        println("finished export");
        println("------------------------");
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0)
            args = new String[]{"link.txt"};

// if the directory does not exist, create it
        String directoryName = OUTPUT_DIRECTORY;
        File theDir = new File(directoryName);
        if (!theDir.exists()) {
            boolean result = false;
            theDir.mkdir();
            result = true;
            if (result) {
                println("DIR OUTPUT created");
            }
        }

        println("Output Directory: " + theDir.getAbsolutePath());


        String fileName = args[0];
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals(""))
                    exportFromUrl(line);
            }
        }

        //System.out.println(args[0]);
    }
}
