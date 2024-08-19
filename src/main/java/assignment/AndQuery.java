package assignment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AndQuery implements  QueryInterface{
    public static QueryType type = QueryType.AND;

    QueryInterface left, right;
    public AndQuery(QueryInterface left, QueryInterface right){
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfied(String url) {
        return left.isSatisfied(url) && right.isSatisfied(url);
    }

    @Override
    public HashMap<String, Page> getSatisfied() {
        HashMap<String, Page> ret = left.getSatisfied();
        ret.keySet().retainAll(right.getSatisfied().keySet());
        return ret;
    }



    @Override
    public int getType() {
        return type.ordinal();
    }
}
