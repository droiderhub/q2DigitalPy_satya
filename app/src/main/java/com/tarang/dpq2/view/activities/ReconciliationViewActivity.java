package com.tarang.dpq2.view.activities;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.model.ReconciliationCardSchemeModel;
import com.tarang.dpq2.view.adapter.ReconciliationViewAdapter;

import java.util.List;


public class ReconciliationViewActivity extends BaseActivity {

    RecyclerView rv;
    List<ReconciliationCardSchemeModel> reconciliationCardSchemeModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconciliation_view);
        setTitle(getCurrentMenu().getMenu_name());
        initViews();
    }

    private void initViews() {
        reconciliationCardSchemeModelList= AppManager.getInstance().getReconciliationCardSchemeModelList();
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        Logger.v("Rcon Size --"+reconciliationCardSchemeModelList.size());
        if (reconciliationCardSchemeModelList.size() != 0){
            rv.setAdapter(new ReconciliationViewAdapter(this,reconciliationCardSchemeModelList));
            rv.setNestedScrollingEnabled(false);
        }else {
            Utils.alertDialogShow(context,getString(R.string.no_data),true);
        }

    }
}
