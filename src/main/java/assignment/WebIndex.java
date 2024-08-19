package assignment;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A web-index which efficiently stores information about pages. Serialization is done automatically
 * via the superclass "Index" and Java's Serializable interface.
 *
 * TODO: Implement this!
 */
public class WebIndex extends Index {
    /**
     * Needed for Serialization (provided by Index) - don't remove this!
     */
    private static final long serialVersionUID = 1L;

    private HashMap<String, HashMap<String, HashSet<Integer>>> wordDictionary = new HashMap<>();
    private HashSet<String> urls = new HashSet<>();


    public void add(String word, String url, int index){
        if(!wordDictionary.containsKey(word)){
            wordDictionary.put(word, new HashMap<>());
        }

        if(!wordDictionary.get(word).containsKey(url)){
            wordDictionary.get(word).put(url, new HashSet<>());
        }

        wordDictionary.get(word).get(url).add(index);

    }

    public HashMap<String, HashSet<Integer>> getURLs(String word){
        return wordDictionary.get(word);
    }

    public HashSet<String> getAllURLs() {return urls;}

    public void addURL(String url) {urls.add(url);}
    public boolean containsURL(String url) {return urls.contains(url);}

}

