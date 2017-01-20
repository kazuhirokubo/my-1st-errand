package com.example.kazu.myapplication;

import android.app.Activity;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;

import android.support.test.runner.AndroidJUnit4;
import com.squareup.spoon.Spoon;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by kbx on 2017/01/20.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

    private Activity mActivity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
    }

    @Test // ユーザー名を入力できる
    public void canInputUserName() {
        onView(withId(R.id.textUserName)).perform(typeText("hoge@gmail.com"));
        Spoon.screenshot(mActivityRule.getActivity(), "InputTitle");
    }

    @Test // パスワードを入力できる
    public void canInputPassword(){
        onView(withId(R.id.textPasswd)).perform(typeText("1234"));
        Spoon.screenshot(mActivityRule.getActivity(), "InputPassword");
    }

    @Test // ログインボタンをタップできる
    public void canTapForLogin(){
        onView(withId(R.id.textUserName)).perform(typeText("hoge@gmail.com"));
        onView(withId(R.id.textPasswd)).perform(typeText("1234"));
        onView(withId(R.id.buttonLogin)).perform(click());
        Spoon.screenshot(mActivityRule.getActivity(), "TapLogin");
    }
}
