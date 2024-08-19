package assignment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class NotQuery implements QueryInterface{
    public static QueryType type = QueryType.NOT;
    private String word;
    private WebIndex index;

    public NotQuery(String word, WebIndex index){
        this.word = word;
        this.index = index;
    }

    @Override
    public boolean isSatisfied(String url) {
        return !index.getURLs(word).containsKey(url);
    }


    @Override
    public HashMap<String, Page> getSatisfied() {
        HashMap<String, Page> ret = new HashMap<>();
        HashMap<String, HashSet<Integer>> urlsTemp = index.getURLs(word);

        Set<String> urls = new HashSet<>(index.getAllURLs());

        if (urlsTemp != null){
            urls.removeAll(urlsTemp.keySet());

        }

        for(Object s : urls.toArray()){
            try {
                ret.put((String)s, new Page(new URL((String)s)));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return ret;
    }

    @Override
    public int getType() {
        return type.ordinal();
    }
}
