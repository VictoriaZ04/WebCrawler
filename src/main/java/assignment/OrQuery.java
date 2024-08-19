package assignment;

import java.util.HashMap;
import java.util.Set;

public class OrQuery implements QueryInterface{
    public static QueryType type = QueryType.OR;

    QueryInterface left, right;
    public OrQuery(QueryInterface left, QueryInterface right){
        this.left = left;
        this.right = right;
    }
    @Override
    public boolean isSatisfied(String url) {
        return left.isSatisfied(url) || right.isSatisfied(url);
    }

    @Override
    public HashMap<String, Page> getSatisfied() {
        HashMap<String, Page> ret = left.getSatisfied();
        ret.putAll(right.getSatisfied());
        return ret;
    }

    @Override
    public int getType() {
        return type.ordinal();
    }
}
