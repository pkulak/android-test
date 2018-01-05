package com.pkulak.androidtest.activity.product.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pkulak.androidtest.ProductListResponse;
import com.pkulak.androidtest.R;
import com.pkulak.androidtest.client.model.ProductList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.pkulak.androidtest.TestUtils.withIndex;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ProductViewActivityTest {
    @Rule
    public ActivityTestRule<ProductViewActivity> activityRule = new ActivityTestRule<>(
            ProductViewActivity.class, true, false);

    @Test
    public void productViewingWorks() throws Exception {
        ProductList productList = new ObjectMapper().readValue(
                ProductListResponse.generate(0, 25, 25), ProductList.class);

        // start 'er up!
        Bundle bundle = new Bundle();
        bundle.putInt("index", 0);
        bundle.putInt("perPage", 25);
        bundle.putParcelableArrayList("items", new ArrayList<>(productList.products));

        Intent intent = new Intent(
                InstrumentationRegistry.getTargetContext(), ProductViewActivity.class);
        intent.putExtras(bundle);

        activityRule.launchActivity(intent);

        // just make sure the first item came all the way through to the views
        onView(withIndex(withId(R.id.name_text), 0))
                .check(matches(withText("Super Cool Product")));
    }
}
