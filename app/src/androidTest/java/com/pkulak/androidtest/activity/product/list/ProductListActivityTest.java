package com.pkulak.androidtest.activity.product.list;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.pkulak.androidtest.ProductListResponse;
import com.pkulak.androidtest.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.pkulak.androidtest.TestUtils.setUpDependencyGraph;
import static com.pkulak.androidtest.TestUtils.withIndex;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ProductListActivityTest {
    @Rule
    public ActivityTestRule<ProductListActivity> activityRule = new ActivityTestRule<>(
            ProductListActivity.class, true, false);

    @Test
    public void productListingWorks() throws Exception {
        MockWebServer server = new MockWebServer();

        // queue up a response
        server.enqueue(new MockResponse().setBody(ProductListResponse.generate(0, 25, 25)));

        server.start();
        setUpDependencyGraph(server);

        activityRule.launchActivity(new Intent(
                InstrumentationRegistry.getTargetContext(), ProductListActivity.class));

        // busy wait for the request to come in
        while (activityRule.getActivity().fragment.adapter.getItemCount() == 0) {
            Thread.sleep(10);
        }

        // just make sure the first item came all the way through to the views
        onView(withIndex(withId(R.id.name_text), 0))
                .check(matches(withText("Super Cool Product")));

        server.shutdown();
    }
}
