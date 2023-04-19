import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WebScraper {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<String> internalUrls = new ArrayList<>();
    private static final List<String> externalUrls = new ArrayList<>();
    private static final List<String> imagesUrls = new ArrayList<>();
    private static int decision = 0;

    private static void scanItems(String url) {
        Document document;
        File input = new File(url);
        try {
            document = Jsoup.parse(input, "UTF-8");
        } catch (IOException e) {
            System.out.println("Could not scan items ");
            decision = 4;
            return;
        }

        Elements elements = document.select("a[href]");
        Elements elements1 = document.select("img");

        for (Element element : elements) {
            String link = element.attributes().get("href");
            if (link.equals("")) {
                continue;
            }
            if (link.startsWith("http")) {
                externalUrls.add(link);
            } else {
                internalUrls.add(link);
            }
        }

        for (Element element : elements1) {
            String link = element.attr("src");
            if (link.equals("")) {
                continue;
            }
            imagesUrls.add(link);
        }
    }

    public static void start(String filename) {
        scanItems(filename);
        while (decision != 4) {
            System.out.println("The index.html file includes the following data:\n" +
                    "- Title: " + filename +
                    "\n- Internal hyperlinks: " + internalUrls.size() +
                    "\n- External hyperlinks: " + externalUrls.size() +
                    "\n- Images: " + imagesUrls.size());
            System.out.println("\nWould you like to get the URL of an internal hyperlink (1), an external hyperlink (2), an image (3)" +
                    " or (4) to quit? Please enter a choice [1, 2, 3, 4]:");
            decision = scanner.nextInt();
            decisionMaking(decision);
        }
    }

    private static void decisionMaking(int decision) {
        if (decision == 4)
            return;
        int index;
        switch (decision) {
            case 1:
                if (internalUrls.size() == 0) {
                    System.out.println("No links were found");
                    break;
                }
                System.out.println("Please enter the index of the internal hyperlink you want to know the URL of [1-" + internalUrls.size() + "]");
                index = scanner.nextInt() - 1;
                try {
                    System.out.println("The internal link of index " + (index + 1) + " is: " + internalUrls.get(index));
                } catch (Exception e) {
                    System.out.println("The given index is not within the bounds");
                }
                break;
            case 2:
                if (externalUrls.size() == 0) {
                    System.out.println("No links were found");
                    break;
                }
                System.out.println("Please enter the index of the external hyperlink you want to know the URL of [1-" + externalUrls.size() + "]");
                index = scanner.nextInt() - 1;
                try {
                    System.out.println("The external link of index " + (index + 1) + " is: " + externalUrls.get(index));
                } catch (Exception e) {
                    System.out.println("The given index is not within the bounds");
                }
                break;
            case 3:
                if (imagesUrls.size() == 0) {
                    System.out.println("No images were found");
                    break;
                }
                System.out.println("Please enter the index of the image you want to know the source of [1-" + imagesUrls.size() + "]");
                index = scanner.nextInt() - 1;
                try {
                    System.out.println("The image source of index " + (index + 1) + " is: " + imagesUrls.get(index));
                } catch (Exception e) {
                    System.out.println("The given index is not within the bounds");
                }
                break;
            default:
                System.out.println("The given input is not valid");
        }
    }
}
