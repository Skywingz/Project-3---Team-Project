package immersive.android.assembly.general.yelpquest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Created by ubun17 on 8/19/16.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
   public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void testOne() throws Exception {
        onView(withId(R.id.undiscoveredButton)).perform(click());
        onView(withId(R.id.filterBarsButton)).perform(click());
        onView(withId(R.id.filterCoffeeButton)).perform(click());
        onView(withId(R.id.filterFoodButton)).perform(click());
        onView(withId(R.id.filterFoodButton)).check(matches(isDisplayed()));
        onView(withId(R.id.filterCoffeeButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testTwo() throws Exception {
       onView(withId(R.id.filterBarsButton)).check(matches(withText("Bars")));
       onView(withId(R.id.filterCoffeeButton)).check(matches(withText("Coffee")));
       onView(withId(R.id.filterFoodButton)).check(matches(withText("Food")));
    }

    @Test
    public void inputTest() throws Exception {
        onView(withId(R.id.questButton))
                .perform(click());

        onView(withId(R.id.fabPopupInterestEditText))
                .perform(typeText("I love yelp quest"))
                .check(matches(withText("I love yelp quest")));
    }

    @Test
    public void searchTest() throws Exception {
        onView(withId(R.id.searchOption))
                .perform(click());
    }
}
