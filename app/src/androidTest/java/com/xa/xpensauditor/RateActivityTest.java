package com.xa.xpensauditor;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.auth.FirebaseAuth;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RateActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @After
    public void signoutandclear()
    {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            auth.signOut();
        }
    }

    @Test
    public void rateActivityTest() {
//        SystemClock.sleep(5000);
//        ViewInteraction appCompatEditText = onView(
//                allOf(withId(R.id.emailIds),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
//                                        0),
//                                0),
//                        isDisplayed()));
//        appCompatEditText.perform(replaceText("drkanaki@ncsu.edu"), closeSoftKeyboard());
//        SystemClock.sleep(5000);
//        ViewInteraction appCompatEditText2 = onView(
//                allOf(withId(R.id.password),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
//                                        0),
//                                0),
//                        isDisplayed()));
//        appCompatEditText2.perform(replaceText("abcd1234"), closeSoftKeyboard());
//        SystemClock.sleep(5000);
//        ViewInteraction materialButton = onView(
//                allOf(withId(R.id.btn_login), withText("LOGIN"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
//                                        0),
//                                3),
//                        isDisplayed()));
//        materialButton.perform(click());
//        SystemClock.sleep(5000);
//        ViewInteraction imageButton = onView(
//                allOf(withContentDescription("Open navigation drawer"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
//                        isDisplayed()));
//        imageButton.check(matches(isDisplayed()));
//        SystemClock.sleep(5000);
//        ViewInteraction appCompatImageButton = onView(
//                allOf(withContentDescription("Open navigation drawer"),
//                        childAtPosition(
//                                allOf(withId(R.id.toolbar),
//                                        childAtPosition(
//                                                withClassName(is("com.google.android.material.appbar.AppBarLayout")),
//                                                0)),
//                                1),
//                        isDisplayed()));
//        appCompatImageButton.perform(click());
//        SystemClock.sleep(5000);
//        ViewInteraction checkedTextView = onView(
//                allOf(withId(com.google.android.material.R.id.design_menu_item_text), withText("Rate"),
//                        withParent(allOf(withId(R.id.nav_rate),
//                                withParent(withId(com.google.android.material.R.id.design_navigation_view)))),
//                        isDisplayed()));
//        checkedTextView.check(matches(isDisplayed()));
//        SystemClock.sleep(5000);
//        ViewInteraction navigationMenuItemView = onView(
//                allOf(withId(R.id.nav_rate),
//                        childAtPosition(
//                                allOf(withId(com.google.android.material.R.id.design_navigation_view),
//                                        childAtPosition(
//                                                withId(R.id.nav_view),
//                                                0)),
//                                13),
//                        isDisplayed()));
//        navigationMenuItemView.perform(click());
//        SystemClock.sleep(5000);
//        ViewInteraction ratingBar = onView(
//                allOf(withId(R.id.ratingBar),
//                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class))),
//                        isDisplayed()));
//        ratingBar.check(matches(isDisplayed()));
//        SystemClock.sleep(5000);
//        pressBack();
//        SystemClock.sleep(5000);
//        ViewInteraction overflowMenuButton = onView(
//                allOf(withContentDescription("More options"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.toolbar),
//                                        2),
//                                0),
//                        isDisplayed()));
//        overflowMenuButton.perform(click());
//        SystemClock.sleep(5000);
//        ViewInteraction materialTextView = onView(
//                allOf(withId(androidx.recyclerview.R.id.title), withText("Account Settings"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(androidx.constraintlayout.widget.R.id.content),
//                                        0),
//                                0),
//                        isDisplayed()));
//        materialTextView.perform(click());
//        SystemClock.sleep(5000);
//        ViewInteraction materialButton2 = onView(
//                allOf(withId(R.id.sign_out), withText("Sign Out"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
//                                        1),
//                                12),
//                        isDisplayed()));
//        materialButton2.perform(click());
//        SystemClock.sleep(5000);
//        ViewInteraction materialButton3 = onView(
//                allOf(withId(android.R.id.button1), withText("SignOut"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("android.widget.ScrollView")),
//                                        0),
//                                3)));
//        materialButton3.perform(scrollTo(), click());
//        SystemClock.sleep(5000);
//        ViewInteraction imageView = onView(
//                allOf(withContentDescription("XpensAuditor"),
//                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class))),
//                        isDisplayed()));
//        imageView.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
