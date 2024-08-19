package assignment;
import java.net.URL;
import java.util.*;

/**
 * A query engine which holds an underlying web index and can answer textual queries with a
 * collection of relevant pages.
 *
 * TODO: Implement this!
 */
public class WebQueryEngine {

    private WebIndex index;
    /**
     * Returns a WebQueryEngine that uses the given Index to construct answers to queries.
     *
     * @param index The WebIndex this WebQueryEngine should use.
     * @return A WebQueryEngine ready to be queried.
     */
    public static WebQueryEngine fromIndex(WebIndex index) {

        return new WebQueryEngine(index);
    }

    public WebQueryEngine(WebIndex index){
        this.index = index;
    }

    /**
     * Returns a Collection of URLs (as Strings) of web pages satisfying the query expression.
     *
     * @param query A query expression.
     * @return A collection of web pages satisfying the query.
     */
    public Collection<Page> query(String query) {
        System.out.println("Begin Query");
        ArrayList<Character> queryList = new ArrayList<>();

        // Checking edge cases
        if(query.equals("")|| query == null){
            return null;
        }

        // Creating Character ArrayList for buildQueryTree()
        for(char c : (query.toLowerCase()).toCharArray()){
            queryList.add(c);
        }
        QueryInterface q = buildQueryTree(queryList);

        // Checking error states
        if(q == null || !queryList.isEmpty()){
            return null;
        }

        return q.getSatisfied().values();
    }

    /**
     * Builds the Query Tree
     * @param q
     * @return
     */
    public QueryInterface buildQueryTree(ArrayList<Character> q){
        QueryInterface.QueryType type = null;
        QueryInterface[] subQueries = new QueryInterface[2];
        String currentWord = "";
        boolean negate = false;

        while(q.size() > 0){ // Iterate through all the characters
            if(q.get(0) == '('){
                // Opening a new subQuery
                q.remove(0);
                if(negate){
                    return null;
                }
                if(!addSubQuery(subQueries, type != null, buildQueryTree(q))){
                    return null;
                }

            }  else if(q.get(0) == '"'){
                // Creating a phraseQuery
                q.remove(0);
                if(negate){
                    return null;
                }
                QueryInterface temp = buildPhraseQuery(q);

                //Checking for error states
                if(temp == null){
                    return null;
                }
                addSubQuery(subQueries, type != null, temp);

            } else if(Character.isLetterOrDigit(q.get(0))){
                // Checks if it's part of a word
                currentWord += q.get(0);

            } else if (q.get(0) == '!') {
                //Checks if it is negating
                negate = !negate;
                if(!currentWord.equals("")){
                    return null;
                }

            } else if(q.get(0) == '&'){
                // AndQuery

                // Checking for error states
                if(type != null){
                    return null;
                }
                type = QueryInterface.QueryType.AND;

                // checking if there is a word to be added to the query
                if(!currentWord.equals("")){
                    addSubQuery(subQueries, false, currentWord, negate);
                    negate = false;
                }
                currentWord = "";
            } else if(q.get(0) == '|'){
                // OrQuery

                //Checking for error states
                if(type != null){
                    return null;
                }

                type = QueryInterface.QueryType.OR;

                // Checking if there is a word to be added to the query
                if(!currentWord.equals("")){
                    addSubQuery(subQueries, false, currentWord, negate);
                    negate = false;
                }
                currentWord = "";
            }
            else if(q.get(0) == ')'){
                // Closing subQuery
                if(!currentWord.equals("")){
                    // There is a current word
                    addSubQuery(subQueries, type != null, currentWord, negate);
                    negate = false;
                }
                return buildQuery(subQueries, type);

            } else {
                // end of word
                if(!currentWord.equals("")){
                    addSubQuery(subQueries, type != null, currentWord, negate);
                    negate = false;
                }
                currentWord = "";

            }
            if(q.size() > 0) {
                q.remove(0);
            }
        }

        // Checking if there is a word to be added to the query
        if(!currentWord.equals("")){
            addSubQuery(subQueries, type != null, currentWord, negate);
        }

        return buildQuery(subQueries, type);

    }

    /**
     * Creates query using word and calls other addSubQuery
     * @param subQueries
     * @param hasType
     * @param word
     * @param negate
     * @return
     */
    private boolean addSubQuery(QueryInterface[] subQueries, boolean hasType, String word, boolean negate){
        int i = 0;
        if(hasType){
            i = 1;
        }

        // Making query based on negate
        QueryInterface query;
        if(negate) {
            query = new NotQuery(word, index);
            negate = false;
        } else {
            query = new SimpleQuery(word, index);
        }

        return addSubQuery(subQueries, hasType, query);
    }

    /**
     * Adding new Query to subQueries array
     * @param subQueries
     * @param hasType
     * @param query
     * @return
     */
    private boolean addSubQuery(QueryInterface[] subQueries, boolean hasType, QueryInterface query){

        // deciding which index to add it to
        int i = 0;
        if(hasType){
            i = 1;
        }

        if(query == null){
            return false;
        }


        // checking for implicit and
        if(subQueries[i] == null){
            subQueries[i] = query;
        } else{
            subQueries[i] = new AndQuery(subQueries[i], query);
        }
        return true;
    }

    /**
     * Building subQuery
     * @param subQueries
     * @param type
     * @return
     */
    public QueryInterface buildQuery(QueryInterface[] subQueries, QueryInterface.QueryType type){
        if(subQueries[0] == null){
            // no Query
            return null;
        } else if(subQueries[1] == null){
            // no query type
            return subQueries[0];
        } else {
            // checking which query type
            if(type == QueryInterface.QueryType.AND){
                return new AndQuery(subQueries[0], subQueries[1]);
            }else if(type == QueryInterface.QueryType.OR){
                return new OrQuery(subQueries[0], subQueries[1]);
            }
        }
        return null;
    }

    /**
     * Building phrase query
     * @param q
     * @return
     */
    public QueryInterface buildPhraseQuery(ArrayList<Character> q){
        ArrayList<String> words = new ArrayList<>();
        String currentWord = "";

        // Parsing for words
        while(q.get(0) != '"'){
            if(Character.isLetterOrDigit(q.get(0))){
                currentWord += q.get(0);
            } else if(!q.equals("")){

                // end of a word
                words.add(currentWord);
                currentWord = "";
            }
            q.remove(0);

            // Checking for Error states
            if(q.size() == 0){
                return null;
            }
        }

        // checking if there is still a word to be added
        if(!q.equals("")){
            words.add(currentWord);
            currentWord = "";
        }
        q.remove(0);
        return new PhraseQuery(words, index);
    }
}
