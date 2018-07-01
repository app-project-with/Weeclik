package com.grace.weeclik;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.grace.weeclik.adapter.CommerceAdapter;
import com.grace.weeclik.model.Commerce;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewName;

    private RecyclerView recyclerView;
    private ArrayList<Commerce> commerces;
    private CommerceAdapter adapter;

    LinearLayout layoutEmpty;
    LinearLayoutManager linearLayoutManager;

    ParseUser parseUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();

        getSharingCommerce();
    }

    public void initView() {
        textViewName = findViewById(R.id.profile_text_view_username);
        recyclerView = findViewById(R.id.profile_recycler_view);

        commerces = new ArrayList<>();

        adapter = new CommerceAdapter(this);

        linearLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new ProfileActivity.GridSpacingItemDecoration(2, dpToPx(), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void getSharingCommerce() {
        if (parseUser != null) {
            getCommercesSharing();
        }
//        adapter.notifyDataSetChanged();
//        adapter.notifyDataSetChanged();
    }

    public void getCommercesSharing() {
        if (parseUser != null) {
        ArrayList<String> mesPartages = (ArrayList<String>) parseUser.get("mes_partages");
        Log.e("T------- ", ""+ parseUser.get("mes_partages"));
        // va chercher dans la bd , la table "commerce"
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Commerce");
        ParseQuery<ParseObject> queryImg = ParseQuery.getQuery("Commerce_Photos");

        String imgId;

        if (mesPartages != null) {
            for (int i = 0; i < mesPartages.size(); i++) {
                query.whereEqualTo("objectId", mesPartages.get(i));
                List<ParseObject> resultsCommerce = null;
                try {
                    resultsCommerce = query.find();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                imgId = resultsCommerce != null ? resultsCommerce.get(i).getParseObject("thumbnailPrincipal").getObjectId() : null;
                queryImg.whereEqualTo("objectId", imgId);
                List<ParseObject> resultsCommerceImg = null;

                try {
                    resultsCommerceImg = queryImg.find();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (resultsCommerceImg != null) {
                    assert resultsCommerce != null;
                    commerces.add(new Commerce(resultsCommerce.get(i).getString("nomCommerce"), resultsCommerce.get(i).getInt("nombrePartages"), resultsCommerceImg.get(i).getParseFile("photo").getUrl()));
                }
            }
        }

        adapter.addAll(commerces);
        isEmptyCommerce();
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();}
    }

    public void isEmptyCommerce() {
        if (commerces.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            //layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            //layoutEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ParseUser parseUser = ParseUser.getCurrentUser();
        showProfile(parseUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log_out:
                logOut();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showProfile(ParseUser user) {
        if (user != null) {
            String username = user.getString("username");
            if (username != null) {
                textViewName.setText(user.getUsername());
            }
        }
    }

    private void logOut() {
        ParseUser.logOut();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
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
