package com.tarang.dpq2.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.utilities.multilangutils.LocalizationActivityDelegate;

import java.util.Locale;

public class ChangeLanguageActivity extends BaseActivity {

    RadioButton english_rb,arabic_rb;
     LocalizationActivityDelegate localizationDelegate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        setTitle(getString(R.string.select_language));
        changeLanguage();
    }

    private void changeLanguage() {
         english_rb = findViewById(R.id.english_rb);
         arabic_rb = findViewById(R.id.arabic_rb);
        localizationDelegate = new LocalizationActivityDelegate(this);
        Logger.v("localizationDelegate----" + localizationDelegate.getLanguage(context));
        Logger.v("localizationDelegateus----" + new Locale("en", "US"));

        if (localizationDelegate.getLanguage(context).equals(new Locale("en", "US"))) {
            english_rb.setChecked(true);
            arabic_rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    localizationDelegate.setLanguage(context, "ar", "EH");
                    Intent intent = new Intent(context, LandingPageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((BaseActivity) context).finish();
                }
            });

        } else if (localizationDelegate.getLanguage(context).equals(new Locale("ar", "EH"))) {
            arabic_rb.setChecked(true);
            english_rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    localizationDelegate.setLanguage(context, "en", "US");
                    Intent intent = new Intent(context, LandingPageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((BaseActivity) context).finish();
                }
            });
        }
    }


}
