package immersive.android.assembly.general.yelpquest;

/**
 * Created by raymour on 8/17/16.
 */
public class SearchTerm {
    private static SearchTerm searchTerm = null;
    private static String mSearch;

    public SearchTerm(){
        mSearch = new String();
    }

    public static SearchTerm getInstance() {
        if (searchTerm == null) {
            searchTerm = new SearchTerm();
        }
        return searchTerm;
    }

    public void setSearchTerm(String st) {
        mSearch = st;
    }

    public String getmSearch() {
        return mSearch;
    }


}
