package com.pkulak.androidtest;

import android.support.test.InstrumentationRegistry;
import android.view.View;

import com.pkulak.androidtest.client.ClientModule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import okhttp3.mockwebserver.MockWebServer;

public class TestUtils {
    public static TestApplicationComponent setUpDependencyGraph(MockWebServer server) {
        // create a test dependency graph
        TestApplicationComponent component = DaggerTestApplicationComponent.builder()
                .clientModule(new ClientModule("superSekretKey", server.url("").toString()))
                .build();

        // and inject over top of what's already there (there has to be a cleaner way...)
        component.inject((TestApplication)
                InstrumentationRegistry.getTargetContext().getApplicationContext());

        return component;
    }

    // https://stackoverflow.com/a/39756832/131854
    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }
}
