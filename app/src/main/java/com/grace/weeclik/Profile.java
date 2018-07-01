package com.grace.weeclik;

import com.parse.ui.ParseLoginDispatchActivity;

/**
 * com.grace.weeclik
 * Created by grace on 02/06/2018.
 */
public class Profile extends ParseLoginDispatchActivity {
    @Override
    protected Class<?> getTargetClass() {
        return ProfileActivity.class;
    }
}
