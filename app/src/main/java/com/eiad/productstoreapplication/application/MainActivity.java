package com.eiad.productstoreapplication.application;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.eiad.productstoreapplication.R;
import com.eiad.productstoreapplication.adapter.ContentRecycleViewAdapter;
import com.eiad.productstoreapplication.adapter.HistoryAdapter;
import com.eiad.productstoreapplication.repository.HistoryRepository;
import com.eiad.productstoreapplication.repository.IHistoryRepository;
import com.eiad.productstoreapplication.util.IJsonReader;
import com.eiad.productstoreapplication.util.ListItem;
import com.eiad.productstoreapplication.util.RecyclerItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, ContentRecycleViewAdapter.RecycleViewOnClickListner {
    private static final int CHILD_HISTORY = 1;
    private static final int CHILD_CONTENT = 2;

    List<ListItem> items;
    RecyclerView contentRecyclerView,historyRecyclerView;
    MenuItem searchMenuItem;
    ContentRecycleViewAdapter contentRecycleViewAdapter;
    IHistoryRepository historyRepository;
    HistoryAdapter historyAdapter;
    ViewSwitcher viewSwitcher;
    IJsonReader api;
    SearchView searchView;
    FloatingActionButton loadFabBtn,gridFabbtn,listFabBtn;
    LinearLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initilaize
        loadFabBtn = (FloatingActionButton) findViewById(R.id.myFAB);
        loadFabBtn.setOnClickListener(this);
        gridFabbtn = (FloatingActionButton) findViewById(R.id.gridFab);
        gridFabbtn.setOnClickListener(this);
        listFabBtn = (FloatingActionButton) findViewById(R.id.listFab);
        listFabBtn.setOnClickListener(this);
        coordinatorLayout = (LinearLayout) findViewById(R.id.coordinator_layout);
        enableListButton(false);

        contentRecyclerView = (RecyclerView) findViewById(R.id.contentRecyclerView);
        historyRecyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);

        contentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        contentRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        this.api = new IJsonReader(getApplicationContext());
        items = new ArrayList<>();

        historyAdapter = new HistoryAdapter();
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(historyAdapter);

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(R.string.dialog_title);
        alertDialog.setMessage("Please press on the load button to load product list");
        alertDialog.setIcon(R.drawable.google);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {

                Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(contentRecyclerView);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder instanceof ContentRecycleViewAdapter.ViewHolder) {

                    String name = items.get(viewHolder.getAdapterPosition()).getName();


                    final ListItem deletedItem = items.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();


                    contentRecycleViewAdapter.removeItem(viewHolder.getAdapterPosition());
                    Toast.makeText(getApplicationContext(), "Product Was deleted", Toast.LENGTH_SHORT).show();

                    Snackbar snackbar = Snackbar.make(coordinatorLayout, name + " removed from list!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            contentRecycleViewAdapter.restoreItem(deletedItem, deletedIndex);
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, contentRecyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(contentRecyclerView);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        historyRepository = new HistoryRepository(getApplicationContext());
        historyAdapter.setItems(historyRepository.getSearchHistory());
        historyAdapter.setCallback(new HistoryAdapter.HistoryAdapterCallback()
        {
            @Override
            public void onItemSelected(String item) {
                searchView.setQuery(item, true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(getApplicationContext().SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setMaxWidth(Integer.MAX_VALUE);

        if (searchView != null)
        {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
            {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    viewSwitcher.setDisplayedChild(CHILD_CONTENT);
                    historyRepository.addSearchQuery(query);
                    historyAdapter.setItems(historyRepository.getSearchHistory());
                    contentRecycleViewAdapter.getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query)
                {
                    if (!TextUtils.isEmpty(query)) {
                        viewSwitcher.setDisplayedChild(CHILD_HISTORY);
                    }
                    contentRecycleViewAdapter.getFilter().filter(query);
                    return false;
                }
            });
        }
        return true;
    }

    public void setData(ArrayList<ListItem> items)
    {
        this.items.addAll(items);
        this.contentRecycleViewAdapter.notifyDataSetChanged();
    }

    private void show()
    {
        if (contentRecycleViewAdapter == null) {
            contentRecycleViewAdapter = new ContentRecycleViewAdapter(this.items, getApplicationContext(), this);
            contentRecyclerView.setAdapter(contentRecycleViewAdapter);
            contentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        } else {
            contentRecycleViewAdapter.setData(this.items);
            contentRecycleViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v)
    {
        ArrayList<ListItem> results = null;

        if(R.id.myFAB == v.getId())
        {
            results = api.parseJson(api.parse());
            viewSwitcher.setDisplayedChild(CHILD_CONTENT);
            this.items.addAll(results);
            show();
        }
        
        if(results != null) {
            enableListButton(true);
        }

        switch (v.getId())
        {
            case R.id.listFab:
                contentRecyclerView.setLayoutManager(true ? new LinearLayoutManager(this) : new GridLayoutManager(this, 2));
                contentRecycleViewAdapter.notifyDataSetChanged();
                break;
            case R.id.gridFab:
                contentRecyclerView.setLayoutManager(false ? new LinearLayoutManager(this) : new GridLayoutManager(this, 2));
                contentRecycleViewAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void enableListButton(boolean isListEnabled)
    {
        listFabBtn.setEnabled(isListEnabled);
        gridFabbtn.setEnabled(isListEnabled);
    }

    @Override
    public void onItemSelected(ListItem listItem, int position)
    {
        Intent nextPageIntent = new Intent(getApplicationContext(), SingleItemActivity.class);
        ListItem product = items.get(position);
        nextPageIntent.putExtra("product", product);
        startActivity(nextPageIntent);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position)
    {

    }
}