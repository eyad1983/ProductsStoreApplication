package com.eiad.productstoreapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiad.productstoreapplication.R;
import com.eiad.productstoreapplication.util.ListItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ContentRecycleViewAdapter extends RecyclerView.Adapter<ContentRecycleViewAdapter.ViewHolder> implements Filterable
{
    List<ListItem> data;
    List<ListItem> filterData;

    RecycleViewOnClickListner rvc;
    Context context;

    public List<ListItem> getData() {
        return data;
    }

    public void setData(List<ListItem> data) {
        this.data = data;
    }

    public ContentRecycleViewAdapter(List<ListItem> data, Context applicationContext, RecycleViewOnClickListner rvc)
    {
        this.data = data;
        this.context = applicationContext;
        this.rvc = rvc;
        this.filterData = data;
    }

    public void removeItem(int position)
    {
        filterData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(ListItem item, int position)
    {
        filterData.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result,parent,false);
        return new ViewHolder(root, rvc);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return filterData == null ? 0 : filterData.size();
    }


    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charString = charSequence.toString();
                if (charString.isEmpty())
                {
                    filterData = data;
                } else
                  {
                      List<ListItem> filteredList = new ArrayList<>();
                        for (ListItem row : data)
                        {
                            if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getDescription().toLowerCase().contains(charString.toLowerCase()))
                            {
                                filteredList.add(row);
                            }
                        }
                    filterData = filteredList;
                  }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filterData;
                    return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                filterData = (ArrayList<ListItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView textView;
        TextView descView;
        ImageView imageView;
        RecycleViewOnClickListner recycleViewOnClickListner;
        public RelativeLayout viewForeground;

        public ViewHolder(View itemView, RecycleViewOnClickListner listner) {
            super(itemView);

            recycleViewOnClickListner = listner;
            itemView.setOnClickListener(this);

            textView = (TextView) itemView.findViewById(R.id.title);
            descView = (TextView) itemView.findViewById(R.id.description);
            imageView = (ImageView) itemView.findViewById(R.id.img);

            viewForeground = (RelativeLayout) itemView.findViewById(R.id.view_foreground);
        }

        public void bind(ListItem input)
        {
            textView.setText(input.getName());
            descView.setText(input.getDescription());
            Picasso.with(context).load(Uri.parse(input.getImg())).into(imageView);
        }

        @Override
        public void onClick(View v)
        {
            recycleViewOnClickListner.onItemSelected(filterData.get(getAdapterPosition()),getAdapterPosition());
        }
    }

    public interface RecycleViewOnClickListner
    {
        void onItemSelected(ListItem listItem, int pos);
    }
}