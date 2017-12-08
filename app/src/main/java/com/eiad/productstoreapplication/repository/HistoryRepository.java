package com.eiad.productstoreapplication.repository;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class HistoryRepository implements IHistoryRepository {

    private static final String FILE_NAME = "YourApp";
    private static final String HISTORY_LIST = "history_list";

    private final SharedPreferences prefs;

    public HistoryRepository(Context context) {
        prefs = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void addSearchQuery(String query) {
        HashSet<String> newHistory = new HashSet<>();
        newHistory.add(query);

        Set<String> history = prefs.getStringSet(HISTORY_LIST, new HashSet<String>());
        if(!history.isEmpty()) {
            newHistory.addAll(history);
        }
        prefs.edit().putStringSet(HISTORY_LIST, newHistory).apply();
    }

    @Override
    public Set<String> getSearchHistory() {
        return prefs.getStringSet(HISTORY_LIST, new HashSet<String>());
    }
}
