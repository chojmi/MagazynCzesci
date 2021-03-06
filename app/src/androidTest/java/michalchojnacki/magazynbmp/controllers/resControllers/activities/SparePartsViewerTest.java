package michalchojnacki.magazynbmp.controllers.resControllers.activities;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import michalchojnacki.magazynbmp.R;
import michalchojnacki.magazynbmp.model.SparePart;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SparePartsViewerTest {

    @Rule
    public ActivityTestRule<SparePartsViewer> mSparePartsViewer = new ActivityTestRule(SparePartsViewer.class, false, false);
    private String numberText = "number";
    private String locationText = "location";
    private String descText = "desc";
    private String producerText = "producer";
    private String typeText = "type";
    private String supplierText = "supplier";
    private int size = 3;

    @Test
    public void activityStartedWithoutExtra() {
        mSparePartsViewer.launchActivity(new Intent());
    }

    @Test
    public void isShownProperly() {
        initSpareParts();
        for (int i = 0; i < size; i++) {

            Espresso.onView(nthChildOf(withId(R.id.SparePartsRecyclerView), i))
                    .check(matches(hasDescendant(withText(descText + i))))
                    .check(matches(hasDescendant(withText(typeText + i))))
                    .check(matches(hasDescendant(withText(producerText + i))))
                    .check(matches(hasDescendant(withText(supplierText + i))));
        }
    }

    private void initSpareParts() {

        SparePart[] spareParts = new SparePart[size];

        for (int i = 0; i < size; i++) {
            spareParts[i] = new SparePart.Builder().number(numberText + i)
                    .description(descText + i)
                    .type(typeText + i)
                    .producer(producerText + i)
                    .location(locationText + i)
                    .supplier(supplierText + i)
                    .build();
        }

        Intent intent = new Intent().putExtra(SparePartsViewer.SPARE_PARTS, spareParts);
        mSparePartsViewer.launchActivity(intent);
    }

    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with " + childPosition + " child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }

    @Test(expected = PerformException.class)
    public void properQuantityOfItems() {
        initSpareParts();
        Espresso.onView(withId(R.id.SparePartsRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(size + 1, ViewActions.click()));
    }

    @Test
    public void sparePartViewerOpensProperly() {
        initSpareParts();

        for (int i = 0; i < size; i++) {
            Espresso.onView(withId(R.id.SparePartsRecyclerView))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, ViewActions.click()));
            Espresso.onView(withId(R.id.SparePartNumberText))
                    .check(matches(withText(numberText + i)));
            Espresso.onView(withId(R.id.SparePartDescriptionText))
                    .check(matches(withText(descText + i)));
            Espresso.onView(withId(R.id.SparePartTypeText)).check(matches(withText(typeText + i)));
            Espresso.onView(withId(R.id.SparePartLocationText))
                    .check(matches(withText(locationText + i)));
            Espresso.onView(withId(R.id.SparePartProducerText))
                    .check(matches(withText(producerText + i)));
            Espresso.onView(withId(R.id.SparePartSupplierText))
                    .check(matches(withText(supplierText + i)));
            Espresso.pressBack();
        }
    }
}

