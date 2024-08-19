package assignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PhraseQuery implements QueryInterface{
    public static QueryType type = QueryType.PHRASE;
    SimpleQuery[] queries;
    WebIndex index;
    public PhraseQuery(ArrayList<String> words, WebIndex index){
        queries = new SimpleQuery[words.size()];
        this.index = index;
        for(int i = 0; i < words.size(); i++){
            queries[i] = new SimpleQuery(words.get(i), index);
        }
    }

    //TODO: Finish
    @Override
    public boolean isSatisfied(String url) {
        if(queries.length == 0){
            return false;
        }
        HashSet<Integer> indecies = queries[0].getIndecies(url);
        for(int i = 1; i < queries.length; i++){
            Object[] compare = queries[i].getIndecies(url).toArray();
            HashSet<Integer> newList = new HashSet<>();
            for(int j = 0; j < compare.length; j++){
                if(indecies.contains(((Integer)compare[j]) - 1)){
                    newList.add(((Integer)compare[j]));
                }
            }

            indecies = newList;
        }
        return indecies.size() > 0;
    }

    @Override
    public HashMap<String, Page> getSatisfied() {
        HashMap<String, Page> ret = new HashMap<>();
        Object[] possibleWords = getContainsWords().values().toArray();

        for(Object o : possibleWords){
            if(isSatisfied(((Page)o).toString())){
                ret.put(((Page)o).toString(), (Page)o);
            }
        }

        return ret;
    }

    public HashMap<String, Page> getContainsWords() {
        if(queries.length == 0){
            return new HashMap<>();
        }

        HashMap<String, Page> ret = queries[0].getSatisfied();
        for(int i = 1; i < queries.length; i++){
            ret.keySet().retainAll(queries[i].getSatisfied().keySet());
        }
        return ret;
    }

    @Override
    public int getType() {
        return type.ordinal();
    }
}
