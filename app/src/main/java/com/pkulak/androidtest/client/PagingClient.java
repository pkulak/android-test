package com.pkulak.androidtest.client;

import android.util.Log;

import com.google.common.collect.ImmutableList;
import com.pkulak.androidtest.client.model.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * A client wrapper that handles the paging. True random access isn't supported; to start a new
 * page fetch, request an item near the end of the list.
 *
 * TODO: window this front AND back, ie, have pages fall off the front after some limit
 *
 * @param <T> the type of the items to page through
 */
public class PagingClient<T> {
    private static final String TAG = "PagingClient";

    private final int perPage;
    private final PageFetcher<T> pageFetcher;
    private final Consumer<Range> rangeAddConsumer;
    private final List<T> items;
    private final Set<Integer> pagesFetched;
    private boolean complete;

    // all fetched pages go into a priority queue, in case they come in out of order
    private final PriorityQueue<Page<T>> pageBuffer = new PriorityQueue<>();

    public PagingClient(int perPage, PageFetcher<T> pageFetcher, Consumer<Range> rangeAddConsumer) {
        this.perPage = perPage;
        this.pageFetcher = pageFetcher;
        this.rangeAddConsumer = rangeAddConsumer;
        this.items = new ArrayList<>();
        this.pagesFetched = new HashSet<>();

        fetchPage(0);
    }

    public PagingClient(int perPage, Collection<T> items, PageFetcher<T> pageFetcher,
                        Consumer<Range> rangeAddConsumer) {
        this.perPage = perPage;
        this.pageFetcher = pageFetcher;
        this.rangeAddConsumer = rangeAddConsumer;
        this.items = new ArrayList<>(items);
        this.pagesFetched = calculatePagesFetched();
        this.complete = calculateComplete();
    }

    private Set<Integer> calculatePagesFetched() {
        Set<Integer> ret = new HashSet<>();

        for (int i = perPage; i <= items.size(); i += perPage) {
            ret.add((i / perPage) - 1);
        }

        return ret;
    }

    private boolean calculateComplete() {
        return items.size() % perPage > 0;
    }

    public List<T> getItems() {
        return ImmutableList.copyOf(items);
    }

    public int getPerPage() {
        return perPage;
    }

    public T getItem(long index) {
        // if we're halfway into the current page, get the next one
        if (index > items.size() - perPage / 2) {
            fetchPage(getNextPageExpected());
        }

        return items.get((int) index);
    }

    public int getItemCount() {
        return items.size();
    }

    private void fetchPage(int index) {
        if (pagesFetched.contains(index) || complete) return;

        pagesFetched.add(index);

        pageFetcher.getPage(index, perPage)
                .thenAccept(this::addPage)
                .exceptionally(throwable -> {
                    Log.e(TAG, throwable.getMessage());
                    pagesFetched.remove(index); // try again
                    return null;
                });
    }

    private void addPage(Page<T> objectList) {
        pageBuffer.add(objectList);
        int totalAdded = 0;

        // empty the buffer as long as we can
        while (pageBuffer.size() > 0 && pageBuffer.peek().getPageIndex() == getNextPageExpected()) {
            Page<T> nextPage = pageBuffer.poll();
            totalAdded += nextPage.getItems().size();
            items.addAll(nextPage.getItems());
            Log.i(TAG, "added items");

            if (nextPage.getItems().size() < nextPage.getPerPage()) {
                Log.i(TAG, "all products downloaded");
                complete = true;
                break;
            }
        }

        if (totalAdded > 0) {
            rangeAddConsumer.accept(new Range(items.size() - totalAdded, totalAdded));
        } else {
            Log.i(TAG, "results out of order");
        }
    }

    private int getNextPageExpected() {
        return items.size() / perPage;
    }

    public interface PageFetcher<T> {
        CompletableFuture<Page<T>> getPage(int pageIndex, int perPage);
    }

    public static class Range {
        public final int start;
        public final int count;

        private Range(int start, int count) {
            this.start = start;
            this.count = count;
        }
    }
}
