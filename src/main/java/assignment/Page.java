package assignment;
import java.net.URL;

/**
 * The Page class holds anything that the QueryEngine returns to the server.  The field and method
 * we provided here is the bare minimum requirement to be a Page - feel free to add anything you
 * want as long as you don't break the getURL method.
 *
 * TODO: Implement this!
 */
public class Page {
    // The URL the page was located at.
    private URL url;

    /**
     * Creates a Page with a given URL.
     * @param url The url of the page.
     */
    public Page(URL url) {
        this.url = url;
    }

    /**
     * @return the URL of the page.
     */
    public URL getURL() { return url; }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Page){
            return url.toString().equals(((Page)obj).getURL().toString());

        }
        return false;
    }

    @Override
    public String toString(){
        return url.toString();
    }
}
