package com.grace.weeclik;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.grace.weeclik.adapter.ViewPagerAdapter;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class DetailsActivity extends AppCompatActivity {

    ParseUser commerce_user = ParseUser.getCurrentUser();    // commerce est un utilisateur
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Commerce");


    String nameCommerce;
    int nbShareCommerce;
    String typeCommerce;
    String phoneCommerce;
    String addressCommerce;
    String webSiteCommerce;
    String descriptionCommerce;
    String promo;
    String objectID;

    private TextView name;
    private TextView nbShare;
    private TextView type;
    private TextView address;
    private TextView webSite;
    private TextView description;
    private TextView promotion;

    private ViewPager viewPager;
    private ImageButton imageButton_back;
    private CardView cardViewPromo;
    private CardView cardViewGallery;
    ImageButton image_button_call;
    ImageButton image_button_location;
    ImageButton image_button_website;
    Button image_button_share;


    int NB_IMG_IN_VIEW_PAGER = 4;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;

    private boolean peutPartager = true;


    ParseUser parseUser = ParseUser.getCurrentUser();
    ParseObject nb_share = new ParseObject("Commerce");
    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Commerce");

    private String[] images = {"mXgbiGT40r"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getDataFromMain();

        queryDetail(nameCommerce);

        initView();

        autoSwipe();












        if (aPartage()) {
            // TODO Action quand je partage
        } else {
            // TODO Action quand j'ai déjà partagé
        }
    }

    private boolean aPartage() {
        boolean result = false;
        if (parseUser != null) {

            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("_User");
            parseQuery.whereEqualTo("objectId", parseUser.getObjectId());
            parseQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (object == null) {
                        Log.d("score", "The getFirst request failed.");
                        Toast.makeText(getApplicationContext(), "BOFF ", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "__________________________ " +object.get("mes_partages"), Toast.LENGTH_LONG).show();
                    }
                }
            });

            ArrayList<String> mes_partages = (ArrayList<String>) parseUser.get("mes_partages");
            //Toast.makeText(this, "__________________________ " +ParseUser.getCurrentUser().getString("username"), Toast.LENGTH_LONG).show();
            ArrayList<String> mes_partages_par_date = (ArrayList<String>) parseUser.get("mes_partages_dates");
            int trouve = 0;
            if (mes_partages != null) {
                for (int i = 0; i < mes_partages.size(); i++) {
                    if (mes_partages.get(i).equals(objectID)) {
                        // TODO partage par date
                        trouve += 1;
                    }
                }
                if (trouve != 0) {
                    result = true;
                }
            }
        }
        return result;
    }

    public void initView() {

        cardViewPromo = findViewById(R.id.details_card_view_promo);
        if (promo.isEmpty()) {
            cardViewPromo.setVisibility(View.GONE);
        } else {
            cardViewPromo.setVisibility(View.VISIBLE);
        }

        cardViewGallery = findViewById(R.id.details_card_view_gallery);
        cardViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(DetailsActivity.this, GalleryActivity.class);
                gallery.putExtra("objectID", objectID);
                startActivity(gallery);
            }
        });

        name = findViewById(R.id.tv_name_detail);
        name.setText(nameCommerce);

        description = findViewById(R.id.details_text_view_description);
        description.setText(descriptionCommerce);

        promotion = findViewById(R.id.details_text_view_promotion);
        promotion.setText(promo);

        Resources resources = getResources();
        String nb = resources.getQuantityString(R.plurals.numberOfShare, nbShareCommerce, nbShareCommerce);

        nbShare = findViewById(R.id.tv_nb_share_detail);
        nbShare.setText(nb/*String.valueOf(nbShareCommerce)*/);

        image_button_call = findViewById(R.id.ib_call);
        image_button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionCall();
            }
        });

        image_button_location = findViewById(R.id.ib_location);
        image_button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionLocation();
            }
        });

        image_button_website = findViewById(R.id.ib_website);
        image_button_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionWebsite();
            }
        });

        image_button_share = findViewById(R.id.ib_share);
        image_button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionShare();
            }
        });

        imageButton_back = findViewById(R.id.ib_back_home);
        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager = findViewById(R.id.vp_details);
//TODO        Integer[] images = {R.drawable.cover1, R.drawable.cover2, R.drawable.cover13};
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, getImgForViewPager());
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tl_details);
        tabLayout.setupWithViewPager(viewPager, true);
    }

    public void autoSwipe() {
        // DONE : utiliser le timer apres avoir demarrer le DetailsActivity
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == NB_IMG_IN_VIEW_PAGER - 1) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    public void getDataFromMain() {
        nameCommerce = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_TRADE");
        nbShareCommerce = Objects.requireNonNull(getIntent().getExtras()).getInt("NBSHARE_TRADE");
        typeCommerce = Objects.requireNonNull(getIntent().getExtras()).getString("TYPE_TRADE");
    }

    /*@Override
    public void onBackPressed() {
        //moveTaskToBack(true);
        Intent backMain = new Intent(Intent.ACTION_MAIN);
        backMain.addCategory(Intent.CATEGORY_HOME);
        backMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(backMain);
    }*/

    private void actionCall() {
        Intent intent_call = new Intent(Intent.ACTION_DIAL);
        intent_call.setData(Uri.parse("tel:" + phoneCommerce));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (ActivityCompat.shouldShowRequestPermissionRationale(DetailsActivity.this, Manifest.permission.CALL_PHONE)) {

            } else {
                ActivityCompat.requestPermissions(DetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }

            return;
        }
        startActivity(intent_call);
    }

    private void actionLocation() {
        Animation animation = AnimationUtils.loadAnimation(DetailsActivity.this, R.anim.bounce);
        image_button_location.startAnimation(animation);

        Uri uri = Uri.parse("geo:0,0?q=" + addressCommerce);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    private void actionWebsite() {
        Animation animation = AnimationUtils.loadAnimation(DetailsActivity.this, R.anim.bounce);
        image_button_website.startAnimation(animation);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(webSiteCommerce));
        startActivity(intent);
    }

    private void actionShare() {
        Animation animation = AnimationUtils.loadAnimation(DetailsActivity.this, R.anim.bounce);
        image_button_share.startAnimation(animation);

        if (parseUser != null) {
            if (peutPartager) {
                ArrayList<String> mes_partages;
                if (parseUser.get("mes_partages_dates") != null) {
                    mes_partages = (ArrayList<String>) parseUser.get("mes_partages");
                } else {
                    mes_partages = new ArrayList<>();
                }
                parseUser.put("mes_partages", mes_partages);

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                String message = getString(R.string.message_share, nameCommerce, addressCommerce, getLatLonFromAddr(addressCommerce));
                sendIntent.putExtra("sms_body", message);
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);
            }
        }
    }

    private String getLatLonFromAddr(String address) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        String point = "";
        String f;
        try {
            addresses = geocoder.getFromLocationName(address, 5);
            if (addresses != null) {
                Address location = addresses.get(0);
                f = location.getLatitude() + "," + location.getLongitude();
                point += "http://maps.google.com/maps?f=q&q=(";
                point += f;
                return point + ")";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void queryDetail(String name) {
        //ParseQuery<ParseObject> query = ParseQuery.getQuery("Commerce");
        query2.selectKeys(Arrays.asList("nomCommerce", "siteWeb", "adresse", "promotions", "mail", "tel", "description"))
                .whereContains("nomCommerce", name);
        List<ParseObject> res = null;
        try {
            res = query2.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        phoneCommerce = res.get(0).getString("tel");
        addressCommerce = res.get(0).getString("adresse");
        webSiteCommerce = res.get(0).getString("siteWeb");
        descriptionCommerce = res.get(0).getString("description");
        promo = res.get(0).getString("promotions");
        objectID = res.get(0).getObjectId();
    }

    private String[] getImgForViewPager() {
        // TODO obtenir les liens d'image via objetID
        String[] res_link = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Commerce");
        ParseQuery<ParseObject> queryImg = ParseQuery.getQuery("Commerce_Photos");
        String idimg;


       /// String[] les_img ="";// queryImg.whereEqualTo("commerce", commerceId);



        queryImg.selectKeys(Arrays.asList("photo", "namePhoto"))
                .whereEqualTo("commerce", "ID_COMMERCE");
        List<ParseObject> res = null;
        try {
            res = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        phoneCommerce = res.get(0).getString("tel");
        addressCommerce = res.get(0).getString("adresse");
        webSiteCommerce = res.get(0).getString("siteWeb");
        descriptionCommerce = res.get(0).getString("description");
        promo = res.get(0).getString("promotions");
        return res_link;
    }



    @SuppressLint("ValidFragment")
    class ShareBottomSheet extends BottomSheetDialogFragment {

        private static final String TAG = "ShareBottomSheet";

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

            final View view = View.inflate(getContext(), R.layout.share_sheet_view, null);

            dialog.setContentView(view);
            BottomSheetBehavior behavior = BottomSheetBehavior.from((View) view.getParent());

            return dialog;
        }

        public void show(final FragmentActivity fragmentActivity) {
            show(fragmentActivity.getSupportFragmentManager(), TAG);
        }
    }
}
