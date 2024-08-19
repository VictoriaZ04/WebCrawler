package assignment;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SimpleQuery implements QueryInterface{

    public static QueryType type = QueryType.SIMPLE;
    private String word;
    private WebIndex index;

    public SimpleQuery(String word, WebIndex index){
        this.word = word;
        this.index = index;
    }

    @Override
    public boolean isSatisfied(String url) {
        return index.getURLs(word).containsKey(url);
    }

    @Override
    public HashMap<String, Page> getSatisfied() {
        System.out.println("Begin Search");

        HashMap<String, Page> ret = new HashMap<>();
        HashMap<String, HashSet<Integer>> urls = index.getURLs(word);
        if (urls == null){
            return new HashMap<>();
        }
        for(Object s : urls.keySet().toArray()){
            try {
                ret.put((String) s,  new Page(new URL((String)s)));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("End Search");

        return ret;
    }

    public HashSet<Integer> getIndecies(String url){return index.getURLs(word).get(url);}

    @Override
    public int getType(){
        return type.ordinal();
    }
}
