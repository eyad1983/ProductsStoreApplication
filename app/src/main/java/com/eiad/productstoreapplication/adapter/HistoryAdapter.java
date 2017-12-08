package com.eiad.productstoreapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eiad.productstoreapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Adapter to show elements in a list (strings)
 */

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_TITLE = 1;
    private static final int VIEW_TYPE_ITEM = 2;

    private List<String> items;
    private HistoryAdapterCallback callback;

    public interface HistoryAdapterCallback {
        void onItemSelected(String item);
    }

    public HistoryAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.isEmpty()) {
            return VIEW_TYPE_EMPTY;
        } else if (position == 0) {
            return VIEW_TYPE_TITLE;

        }
        return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_EMPTY:
                return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_empty_list_item, parent, false));
            case VIEW_TYPE_TITLE:
                return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_title_list_item, parent, false));
            default:
                return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false), callback);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).setText(items.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return items.isEmpty() ? 1 : items.size() + 1;
    }

    public void setCallback(HistoryAdapterCallback callback) {
        this.callback = callback;
    }

    /**
     * @param items
     */
    public void setItems(Set<String> items)
    {
        this.items.clear();

        if (items != null)
        {
            this.items.addAll(items);
        }
        notifyDataSetChanged();
    }

    /**
     *
     */
    private static class EmptyViewHolder extends RecyclerView.ViewHolder
    {
        private EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     *
     */
    private static class ItemViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textView;

        private ItemViewHolder(View itemView, final HistoryAdapterCallback callback) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onItemSelected(textView.getText().toString());
                    }
                }
            });
        }

        private void setText(final String text) {
            textView.setText(text);
        }
    }
}
