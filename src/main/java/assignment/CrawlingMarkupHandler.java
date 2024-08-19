package assignment;

import java.util.*;
import java.net.*;
import org.attoparser.simple.*;

/**
 * A markup handler which is called by the Attoparser markup parser as it parses the input;
 * responsible for building the actual web index.
 *
 * TODO: Implement this!
 */
public class CrawlingMarkupHandler extends AbstractSimpleMarkupHandler {

    private WebIndex index;
    private LinkedList<URL> newURLs = new LinkedList<>();
    private URL currentURL;
    private int indexCounter = 0;
    private String currTag = "";

    public CrawlingMarkupHandler() {
        index = new WebIndex();
    }

    /**
    * This method returns the complete index that has been crawled thus far when called.
    */
    public WebIndex getIndex() {
        return index;
    }

    /**
    * This method returns any new URLs found to the Crawler; upon being called, the set of new URLs
    * should be cleared.
    */
    public List<URL> newURLs() {
        LinkedList<URL> ret = newURLs;
        newURLs = new LinkedList<>();
        return ret;
    }

    public void setCurrentURL(URL url){
        if(currentURL == null || !currentURL.toString().equals(url.toString())){
            indexCounter = 0;
            currentURL = url;

        }

    }

    /**
    * These are some of the methods from AbstractSimpleMarkupHandler.
    * All of its method implementations are NoOps, so we've added some things
    * to do; please remove all the extra printing before you turn in your code.
    *
    * Note: each of these methods defines a line and col param, but you probably
    * don't need those values. You can look at the documentation for the
    * superclass to see all of the handler methods.
    */

    /**
    * Called when the parser first starts reading a document.
    * @param startTimeNanos  the current time (in nanoseconds) when parsing starts
    * @param line            the line of the document where parsing starts
    * @param col             the column of the document where parsing starts
    */
    public void handleDocumentStart(long startTimeNanos, int line, int col) {

    }

    /**
    * Called when the parser finishes reading a document.
    * @param endTimeNanos    the current time (in nanoseconds) when parsing ends
    * @param totalTimeNanos  the difference between current times at the start
    *                        and end of parsing
    * @param line            the line of the document where parsing ends
    * @param col             the column of the document where the parsing ends
    */
    public void handleDocumentEnd(long endTimeNanos, long totalTimeNanos, int line, int col) {
        // TODO: Implement this.
    }

    /**
    * Called at the start of any tag.
    * @param elementName the element name (such as "div")
    * @param attributes  the element attributes map, or null if it has no attributes
    * @param line        the line in the document where this element appears
    * @param col         the column in the document where this element appears
    */
    public void handleOpenElement(String elementName, Map<String, String> attributes, int line, int col) {
        currTag = elementName;

        // checking if tag has URL
        if(elementName.equals("a")){
           String url = attributes.get("href");
           if(url == null){
               return;
           }
           URL newURL;

           // creates new url
           try {
                newURL = new URL(currentURL, url);
           } catch (MalformedURLException e){
                return;
           }

           // Checks if it is a valid url
           String[] splitURL = url.split("\\.");
           if(splitURL.length != 0 && (
                   splitURL[splitURL.length - 1].equals("html") ||
                   splitURL[splitURL.length - 1].equals("htm") ||
                   splitURL[splitURL.length - 1].equals("txt")) && !index.containsURL(newURL.toString())){

               // adds URL if valid
                   newURLs.add(newURL);
                   index.addURL(newURL.toString());

           }
       }
    }

    /**
    * Called at the end of any tag.
    * @param elementName the element name (such as "div").
    * @param line        the line in the document where this element appears.
    * @param col         the column in the document where this element appears.
    */
    public void handleCloseElement(String elementName, int line, int col) {
        // TODO: Implement this.
    }

    /**
    * Called whenever characters are found inside a tag. Note that the parser is not
    * required to return all characters in the tag in a single chunk. Whitespace is
    * also returned as characters.
    * @param ch      buffer containing characters; do not modify this buffer
    * @param start   location of 1st character in ch
    * @param length  number of characters in ch
    */
    public void handleText(char[] ch, int start, int length, int line, int col) {

        // checks if it is text to be ignored
        if(currTag.equals("script") || currTag.equals("style") || currTag.equals("meta")){
            return;
        }

        String currentWord = "";
        for(int i = start; i < start + length; i++){
            if(Character.isLetterOrDigit(ch[i])){
                currentWord += ch[i];
            } else if (!currentWord.isEmpty()){

                index.add(currentWord.toLowerCase(), currentURL.toString(), indexCounter);
                currentWord = "";
                indexCounter++;

            }
        }
        if (!currentWord.isEmpty()){

            index.add(currentWord.toLowerCase(), currentURL.toString(), indexCounter);
            indexCounter++;

        }

    }


}
