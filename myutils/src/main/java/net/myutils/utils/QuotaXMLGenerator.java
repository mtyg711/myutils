package net.myutils.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.File;


/**
 * Created by admin on 11/8/18.
 */
public class QuotaXMLGenerator {


    private static final String XML_BEGIN = "<qa:browsers xmlns:qa=\"urn:config.gridrouter.qatools.ru\">\n";
    private static final String XML_END ="</qa:browsers>";
    private static final int NODE_COUNT= 7;
    private static final String[] browsers = {"firefox","chrome"};
    private static final String REGION_START ="\t\t\t<region name=\"1\">\n";
    private static final String REGION_END ="\t\t\t</region>\n";

    private static final String[] FIREFOX_VERSIONS = {"48.0", "49.0", "50.0", "51.0", "52.0", "53.0", "54.0", "55.0", "56.0", "57.0", "58.0", "59.0", "60.0"};
    private static final String   FIREFOX_DEFAULT_VERSION = "60.0";

    private static final String[] CHROME_VERSIONS = {"55.0", "56.0", "57.0", "58.0", "59.0", "60.0", "61.0", "62.0", "63.0", "64.0", "65.0", "66.0", "67.0", "68.0"};
    private static final String   CHROME_DEFAULT_VERSION = "68.0";

    private static final String[] IE_VERSIONS = {"11.0"};
    private static final String   IE_DEFAULT_VERSION = "11.0";

    private static final String[] nodes ={"10.0.1.220","10.0.1.221","10.0.1.222"};



    private static String buildXML(){

        String content =  Arrays.stream(Browser.values()).map(br -> buildBrowserSection(br.getName(),br.getDefaultVersion())).collect(Collectors.joining("\n"));

        return XML_BEGIN.concat(content).concat(XML_END);

    }




    private static String buildBrowserSection(String browser, String defaultVersion){

        String browserStart =  "\t<browser name=\""+browser+"\" defaultVersion=\""+defaultVersion+"\">\n";
        String browserEnd = "\t</browser>";

        String browserNodes = determineVersion(browser).map(v -> buildBrowser(v)).collect(Collectors.joining("\n"));

        return browserStart.concat(browserNodes).concat(browserEnd).concat("\n");

    }




    private static String buildBrowser(String version){
        String versionStart = "\t\t<version number=\""+version+"\">\n" ;
        String versionEnd ="\t\t</version>\n";

        return versionStart.concat(REGION_START).concat(buildAllNodes()).concat(REGION_END).concat(versionEnd);
    }
    private static String buildNodeStatement(String nodeIp, int count) {

        return "\t\t\t\t<host name=\"" + nodeIp + "\" port=\"4444\" count=\"" + count + "\"/>";
    }

    private static String buildAllNodes()  {

       //return  Arrays.stream(nodes).map(node -> buildNodeStatement(node,NODE_COUNT)).collect(Collectors.joining("\n")).concat("\n");
        return  nodes().stream().map(node -> buildNodeStatement(node,NODE_COUNT)).collect(Collectors.joining("\n")).concat("\n");
    }

    private static Collection<String> nodes()  {

        try {
            return Files.lines(new File("/Users/admin/Documents/workspace/myutils/src/main/rsources/nodes.txt").toPath()) .map(s -> s.trim()) .filter(s -> !s.isEmpty()).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    private static final Stream<String> determineVersion(String browser){

        switch (browser){
            case "chrome":
               return  Arrays.stream(CHROME_VERSIONS);
            case "firefox":
                return  Arrays.stream(FIREFOX_VERSIONS);
            case "internet explorer":
                return  Arrays.stream(IE_VERSIONS);
            default:
                throw new UnsupportedOperationException("Invalid browser: "+browser);
        }
    }

public static void main(String[] s) throws IOException {

    //Arrays.stream(nodes).forEach(n -> System.out.println(buildNodeStatement(n,NODE_COUNT)));
    ///System.out.println(buildBrowserSection("firefox","12.0"));


    String tt = "firefox";

/*    isChrome(tt)
            .map(d -> {System.out.println("chrome"); return d;})
            .map(Optional::of)
            .orElseGet(() -> isFirefox(tt))
            .map(d -> {System.out.println("firefox"); return d;})
            .orElseGet(() -> isIE(tt))
            .map(d -> {System.out.println(tt); return Optional.of(d);})
            .ifPresent(result  -> System.out.println(result));*/


    System.out.println(buildXML());
    //nodes();

    for (double i=48.0 ;  i<=60.0 ; i++){
        System.out.print("\""+i+"\", ");
    }

}

    private static Boolean isChrome(String browser){
        System.out.println(browser);
        return Browser.CHROME.getName().equals(browser);
    }
    private static Boolean  isFirefox(String browser){
        System.out.println(browser);
        return Browser.FIREFOX.getName().equals(browser);
    }
    private static Boolean  isIE(String browser){
        System.out.println(browser);
        return Browser.IE.getName().equals(browser);
    }

/*
private static Optional<Boolean> isChrome(String browser){
    System.out.println(browser);
    return Optional.of(browser).filter(b-> "chrome".equals(b)).orElseGet(() -> Optional.empty());
}
    private static String  isFirefox(String browser){
        System.out.println(browser);
        return Optional.of(browser).filter(b-> "firefox".equals(b)).orElseGet(() -> Optional.empty());
    }
    private static Optional<String>  isIE(String browser){
        System.out.println(browser);
        return Optional.of(browser).filter(b-> "intener explorer".equals(b)).orElseGet(() -> Optional.empty());
    }
*/

}

enum Browser{
    CHROME("chrome", "68.0"), FIREFOX("firefox","58.0"), IE("internet explorer","11.0");

    private String defaultVersion;
    private String name;

    Browser(String name, String defaultVersion) {
        this.name = name;
        this.defaultVersion =defaultVersion;
    }

    public String getName(){
        return this.name;
    }
    public String getDefaultVersion(){
        return this.defaultVersion;
    }
}