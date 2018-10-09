package edu.app.bloodhub;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.app.bloodhub.login.activity.LoginActivity;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityInstrumentedTest {
    @RunWith(AndroidJUnit4.class)
    public static class LoginActivityInstrumentationTest {


        @Rule
        public ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

        private String username_tobe_typed = "admin";
        private String correct_password = "admin";
        private String wrong_password = "admin1";

        @Test
        public void login_success() {
            Log.e("@Test", "Performing login success test");
            Espresso.onView((withId(R.id.et_username)))
                    .perform(ViewActions.typeText(username_tobe_typed));

            Espresso.onView(withId(R.id.et_pass))
                    .perform(ViewActions.typeText(correct_password));

            Espresso.onView(withId(R.id.btn_login))
                    .perform(ViewActions.click());

            Espresso.onView(withText("Login Successful")).inRoot(withDecorView(not(is(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        }

        @Test
        public void login_failure() {
            Log.e("@Test", "Performing login failure test");
            Espresso.onView((withId(R.id.et_username)))
                    .perform(ViewActions.typeText(username_tobe_typed));

            Espresso.onView(withId(R.id.et_pass))
                    .perform(ViewActions.typeText(wrong_password));

            Espresso.onView(withId(R.id.btn_login))
                    .perform(ViewActions.click());

            Espresso.onView(withText(R.string.TOAST_STRING)).inRoot(withDecorView(not(is(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        }
    }
}
