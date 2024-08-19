package assignment;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public interface QueryInterface {
    public static enum QueryType{
        SIMPLE,
        OR,
        AND,
        PHRASE,
        NOT
    }

    public boolean isSatisfied(String url);
    public HashMap<String, Page> getSatisfied();
    public int getType();
}
