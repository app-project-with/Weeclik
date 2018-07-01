package com.grace.weeclik;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.grace.weeclik.adapter.CommerceAdapter;
import com.grace.weeclik.model.Commerce;
import com.grace.weeclik.utils.PaginationScrollListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Commerce> commerces;
    private CommerceAdapter adapter;
    private Spinner spinner;

    ImageButton imageButton;
    ImageButton imageButtonFilter;

    LinearLayout layoutEmpty;

    LinearLayoutManager linearLayoutManager;

    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 6;
    private int currentPage = PAGE_START;
    private int limit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
        initSpinner();

        imageButton = findViewById(R.id.main_image_button_account);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                Log.e("azerty", ""+currentUser);
                if (currentUser != null) {
                    startActivity(new Intent(MainActivity.this, Profile.class));
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });

        imageButtonFilter = findViewById(R.id.main_image_button_filter);
        imageButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FilterActivity.class));
            }
        });

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Loading more data
                        //loadNextPage("Restauration");
                    }
                }, 1000);
            }

            @Override
            protected int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            protected boolean isLastPage() {
                return isLastPage;
            }

            @Override
            protected boolean isLoading() {
                return isLoading;
            }
        });

//        loadFirstPage("Artisanat");

//        isEmptyCommerce();

        try {
            Glide.with(MainActivity.this).load(R.drawable.cover1).into((ImageView) findViewById(R.id.bg_toolbar));
            //Glide.with(MainActivity.this).load(R.drawable.cover1).into((AppBarLayout) findViewById(R.id.app_bar_main));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void isEmptyCommerce() {
        if (commerces.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        }
    }

    private int changeImage(String item) {
        int id;
        switch (item) {
            case "Artisanat":
                id = R.drawable.cover1;
                break;
            case "Bien-être":
                id = R.drawable.cover2;
                break;
            case "Décoration":
                id = R.drawable.cover3;
                break;
            case "E-commerce":
                id = R.drawable.cover4;
                break;
            case "Distribution":
                id = R.drawable.cover5;
                break;
            case "Hôtellerie":
                id = R.drawable.cover6;
                break;
            case "Immobilier":
                id = R.drawable.cover7;
                break;
            case "Informatique":
                id = R.drawable.cover8;
                break;
            case "Alimentaire":
                id = R.drawable.cover9;
                break;
            case "Métallurgie":
                id = R.drawable.cover10;
                break;
            case "Médical":
                id = R.drawable.cover11;
                break;
            case "Nautisme":
                id = R.drawable.cover12;
                break;
            case "Paramédical":
                id = R.drawable.cover13;
                break;
            case "Restauration":
                id = R.drawable.cover14;
                break;
            case "Sécurité":
                id = R.drawable.cover15;
                break;
            case "Textile":
                id = R.drawable.cover16;
                break;
            case "Tourisme":
                id = R.drawable.cover17;
                break;
            case "Transport":
                id = R.drawable.cover18;
                break;
            default:
                id = R.drawable.cover19;
                break;
        }
        return id;
    }

    public void initView() {
        recyclerView = findViewById(R.id.main_recycler_view);
        spinner = findViewById(R.id.main_spinner);
        layoutEmpty = findViewById(R.id.main_linear_layout);

        progressBar = findViewById(R.id.main_progress_bar);

        adapter = new CommerceAdapter(this);

        linearLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void initSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.activity_area, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                try {
                    Glide.with(MainActivity.this).load(changeImage(item)).into((ImageView) findViewById(R.id.bg_toolbar));
                    getActivities(item);
                    adapter = new CommerceAdapter(MainActivity.this);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getActivities(String activity_area) {
        commerces = new ArrayList<>();
        // va chercher dans la bd , la table "commerce"
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Commerce");
        final ParseQuery<ParseObject> queryImg = ParseQuery.getQuery("Commerce_Photos");
        // requete qui me cherche toutes les valeurs avec "nomCommerce" et "nombrePartages"
        query.selectKeys(Arrays.asList("nomCommerce", "nombrePartages", "thumbnailPrincipal")).whereEqualTo("typeCommerce", activity_area);

        /*if (isLoading) {
            query.setSkip(limit);
            query.setLimit(6);
        } else {
            query.setLimit(6);
        }*/
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                limit = limit + objects.size();
                if (e == null) {
                    Log.e("COMMERCE", "Nb Commerce " + objects.size() + " scores");

                    for (int i = 0; i < objects.size(); i++) {
                        //ParseObject comm = object.getParseObject("nomCommerce");
                        String img_id = objects.get(i).getParseObject("thumbnailPrincipal").getObjectId();
                        if (img_id != null) {
                            queryImg.whereEqualTo("objectId", img_id);
                        }
                        List<ParseObject> image = null;
                        try {
                            image = queryImg.find();
                        } catch (ParseException err) {
                            err.printStackTrace();
                        }
                        commerces.add(new Commerce(objects.get(i).getString("nomCommerce"), objects.get(i).getInt("nombrePartages"), image.get(0).getParseFile("photo").getUrl()));
                        Log.e("COMMERCE", "Commerce " + commerces.get(i) + " ---- " + img_id);
                    }
                } else {
                    Log.e("COMMERCE", "Error: " + e.getMessage());
                }

                adapter.addAll(commerces);
                isEmptyCommerce();
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final LinearLayout layout = findViewById(R.id.ll_menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
            }
        });

        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.search_src_text);
                editText.setText("");
                searchView.setQuery("", false);
                searchView.onActionViewCollapsed();
                layout.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() != 0) {
                    adapter = new CommerceAdapter(MainActivity.this);
                    adapter.addAll(rechercher(newText));
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter = new CommerceAdapter(MainActivity.this);
                    adapter.addAll(commerces);
                    recyclerView.setAdapter(adapter);
                }
                return true;
            }
        });

        return true;
    }

    public List<Commerce> rechercher(String charString) {
        ArrayList<Commerce> myFilter = new ArrayList<>();
        if (charString.isEmpty()) {
            myFilter = commerces;
        } else {
            for (int i = 0; i < commerces.size(); i++) {
                if (commerces.get(i).getName().toLowerCase().contains(charString)) {
                    myFilter.add(commerces.get(i));
                }
            }
        }
        return myFilter;
    }
























































































    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics()));
    }
}
