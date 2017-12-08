package com.eiad.productstoreapplication;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.eiad.productstoreapplication.application.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest
{
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void check_dialog_message()
    {
        onView(withText(R.string.dialog_title)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(R.string.welcome_message)).perform(click());
        onView(withText(R.string.welcome_message)).perform(pressBack());
    }

    @Test
    public void click_on_load_floating_button()
    {
        onView(withText(R.string.welcome_message)).perform(pressBack());
        onView(withId(R.id.myFAB)).perform(click());
    }

    @Test
    public void click_on_grid_floating_button()
    {
        click_on_load_floating_button();
        onView(withId(R.id.gridFab)).perform(click());
    }

    @Test
    public void click_on_list_floating_button()
    {
        click_on_load_floating_button();
        onView(withId(R.id.listFab)).perform(click());
    }
    
    @Test
    public void click_on_sigle_item()
    {
        click_on_load_floating_button();
        onView(withRecyclerView(R.id.contentRecyclerView).atPosition(3)).perform(click());
    }

//    @Test
//    public void search_on_sigle_item()
//    {
//        click_on_load_floating_button();
//        onView(withRecyclerView(R.id.contentRecyclerView).atPositionOnView(1, R.id.contentRecyclerView)).check(matches(withText("Eyad")));
//    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId)
    {
        return new RecyclerViewMatcher(recyclerViewId);
    }
}
