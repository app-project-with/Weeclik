package com.grace.weeclik;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Switch aSwitch;
    LinearLayout linearLayout;
    boolean isSwitched = false;

    private EditText username;
    private EditText pass;
    private EditText numero_tva;
    private EditText nom_du_commerce;
    private EditText adresse;
    private EditText ville;
    private EditText code_postal;
    private CardView register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();

        onTouch();

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void register(/*final View v*/) {
        if (username.getText().length() == 0 || pass.getText().length() <= 5) {
            return;
        }

        if (!isSwitched) {
            // TODO : connection client normal
            //v.setEnabled(false);

            ParseUser user = new ParseUser();
            user.setUsername(username.getText().toString());
            user.setPassword(pass.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.e("REGISTER", "GOOD");
                        Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("REGISTER", "BAD");
                        //v.setEnabled(true);
                    }
                }
            });
        } else {
            // TODO : connection commerce
            //v.setEnabled(false);

            ParseUser user = new ParseUser();
            /*user.setUsername(username.getText().toString());
            user.setPassword(pass.getText().toString());*/

            user.put("phone", username.getText().toString());
            user.put("password", pass.getText().toString());
            user.put("numero_tva", numero_tva.getText().toString());
            user.put("nom_du_commerce", nom_du_commerce.getText().toString());
            user.put("adresse", adresse.getText().toString());
            user.put("ville", ville.getText().toString());
            user.put("code_postal", code_postal.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.e("REGISTER", "GOOD");
                        Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("REGISTER", "BAD");
                        //v.setEnabled(true);
                    }
                }
            });
        }

    }

    public void initView() {
        linearLayout = findViewById(R.id.ll_register);

        username = findViewById(R.id.et_phone);
        pass = findViewById(R.id.et_password_register);
        aSwitch = findViewById(R.id.switch1);

        numero_tva = findViewById(R.id.et_num_tva);
        nom_du_commerce = findViewById(R.id.et_name_enterprise);
        adresse = findViewById(R.id.et_address);
        ville = findViewById(R.id.et_city);
        code_postal = findViewById(R.id.et_bp);

        register_btn = findViewById(R.id.cv_register);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onTouch() {
        aSwitch.setChecked(false);
        aSwitch.setOnCheckedChangeListener(this);

        username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        numero_tva.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        nom_du_commerce.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        adresse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        ville.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        code_postal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            isSwitched = true;
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            isSwitched = false;
            linearLayout.setVisibility(View.GONE);
        }
    }

}
