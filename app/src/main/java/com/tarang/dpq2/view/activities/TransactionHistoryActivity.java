package com.tarang.dpq2.view.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.dao.SAFModelDao;
import com.tarang.dpq2.base.room_database.db.dao.TransactionModelDao;
import com.tarang.dpq2.base.room_database.db.tuple.TransactionHistoryTuple;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.view.adapter.TransactionRecyclerAdapter;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class TransactionHistoryActivity extends BaseActivity implements TransactionRecyclerAdapter.DeleteSafItemOnClick {

    RecyclerView recyclerView;
    private SearchView searchView;
    ImageView action_datepicker;
    TransactionRecyclerAdapter transactionRecyclerAdapter;
    LinearLayout sort_param_layout;
    int mYear, mMonth, mDay;
    List<TransactionHistoryTuple> transactionHistoryTuples = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transaction_list);
        if (AppManager.getInstance().getHistoryView().equalsIgnoreCase(ConstantApp.SAF_HISTORY))
            setTitle(getString(R.string.saf_history));
        else
            setTitle(getString(R.string.transaction_view));
        init();
    }

    public void init() {
        recyclerView = (RecyclerView) findViewById(R.id.transactionList);
        sort_param_layout = findViewById(R.id.sort_param_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new TransactionHistoryTask(this, 0).execute();
        filter();

    }

    private void filter() {
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.action_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MIN_VALUE);
        searchView.onActionViewExpanded(); //new Added line
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getString(R.string.search_by_rrn_date_amount));

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                //    transactionRecyclerAdapter.getFilter().filter(query);
                hideSoftKeyboard(TransactionHistoryActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                transactionRecyclerAdapter.getFilter().filter(query);
                return false;
            }
        });

        action_datepicker = findViewById(R.id.action_datepicker);

        action_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                final String[] dateString = new String[1];
                //start changes...

                DatePickerDialog datePickerDialog = new DatePickerDialog(TransactionHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        c.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        dateString[0] = sdf.format(c.getTime());
                        Toast.makeText(TransactionHistoryActivity.this, dateString[0], Toast.LENGTH_SHORT).show();
                        searchView.setQuery(dateString[0], true);
                        searchView.setFocusable(true);
                        transactionRecyclerAdapter.getFilter().filter(dateString[0]);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()+ (1000 * 60 * 60));
                datePickerDialog.show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                //    transactionRecyclerAdapter.getFilter().filter(query);
                hideSoftKeyboard(TransactionHistoryActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                transactionRecyclerAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        if (id == R.id.action_datepick) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            final String[] dateString = new String[1];

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            c.set(year, monthOfYear, dayOfMonth);

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            dateString[0] = sdf.format(c.getTime());
                            Toast.makeText(TransactionHistoryActivity.this, dateString[0], Toast.LENGTH_SHORT).show();

                            searchView.setQuery(dateString[0], false);
                            searchView.setFocusable(true);
                            //     Toast.makeText(context, dayOfMonth + "/" + (monthOfYear + 1) + "/" + year, Toast.LENGTH_SHORT).show();


                            //    searchView.setQuery(data,false);
                            //       txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (searchView != null) {
            if (!searchView.isIconified()) {
                searchView.setIconified(true);
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public void deleteSafItem(int id) {
        Logger.v("safdeletefromoverrride");
        new TransactionHistoryTask(this, id).execute();


    }

/*
    public class GetTransactionHistory extends Worker {
        AppDatabase database;

        public GetTransactionHistory(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
            database = AppDatabase.getInstance(context);
        }

        @NonNull
        @Override
        public Result doWork() {
            TransactionModelDao transactionModelDao = database.getTransactionDao();
            SAFModelDao safModelDao = database.getSAFDao();
            Logger.v("Do work");
            if (AppManager.getInstance().getHistoryView().equalsIgnoreCase(Constant.TRANSACTION_VIEW)) {
                transactionHistoryTuples = transactionModelDao.loadAllTransactions();
            }else if (AppManager.getInstance().getHistoryView().equalsIgnoreCase(Constant.SAF_HISTORY)){
                transactionHistoryTuples = safModelDao.loadAllSAFTransactions();

            }

            // Indicate success or failure with your return value:
            return Result.success();        }
    }
*/

    private class TransactionHistoryTask extends AsyncTask<List<TransactionHistoryTuple>, Void, List<TransactionHistoryTuple>> {

        //Prevent leak
        private WeakReference<Activity> weakActivity;
        AppDatabase database;
        int id;
        int txnHistoryFlag = 0;

        public TransactionHistoryTask(Activity activity, int id) {
            weakActivity = new WeakReference<>(activity);
            database = AppDatabase.getInstance(context);
            this.id = id;
        }

        @Override
        protected List<TransactionHistoryTuple> doInBackground(List<TransactionHistoryTuple>... passing) {
            TransactionModelDao transactionModelDao = database.getTransactionDao();
            SAFModelDao safModelDao = database.getSAFDao();
            AppManager.getInstance().setFinancialAdviceRequired(false);
            if (AppManager.getInstance().getAdminSafHistoryView().equals("DELETE_TXN_HISTORY")) {
                if (database.getTransactionDao().getAll().size() == 0) {
                    //  Toast.makeText(TransactionHistoryActivity.this, getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                    //finish();
                    txnHistoryFlag = 1;
                } else {
                    txnHistoryFlag = 0;
                    database.getTransactionDao().nukeTable();
                }
            } else {

                if (id != 0) {
                    Logger.v("safdeletehappening");
                    database.getSAFDao().deleteSafItem(id);
                }
                Logger.v("Do work");
                if (AppManager.getInstance().getHistoryView().equalsIgnoreCase(ConstantApp.TRANSACTION_VIEW)) {
                    transactionHistoryTuples = transactionModelDao.loadAllTransactions();
                } else if (AppManager.getInstance().getHistoryView().equalsIgnoreCase(ConstantApp.SAF_HISTORY)) {
                    transactionHistoryTuples = safModelDao.loadAllSAFTransactions();
                }
                return transactionHistoryTuples;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<TransactionHistoryTuple> transactionHistoryTuple) {
            Activity activity = weakActivity.get();
            if (activity == null) {
                return;
            }

            if (AppManager.getInstance().getAdminSafHistoryView().equals("DELETE_TXN_HISTORY")) {
                if (txnHistoryFlag == 0) {
                    Utils.alertDialogShow(context, getString(R.string.transaction_history_deleted), false);
//                    Toast.makeText(TransactionHistoryActivity.this, , Toast.LENGTH_SHORT).show();
//                    finish();
                } else {
                    Utils.alertDialogShow(context, getString(R.string.no_data), false);
//                      Toast.makeText(TransactionHistoryActivity.this, getString(R.string.no_data), Toast.LENGTH_SHORT).show();
//                    finish();
                }
            } else {
                if (transactionHistoryTuples.size() > 0) {
                    if (AppManager.getInstance().getHistoryView().equalsIgnoreCase(ConstantApp.TRANSACTION_VIEW)) {
                        transactionRecyclerAdapter = new TransactionRecyclerAdapter(TransactionHistoryActivity.this, transactionHistoryTuples, false, TransactionHistoryActivity.this);
                        transactionRecyclerAdapter.notifyDataSetChanged();

                    } else if (AppManager.getInstance().getHistoryView().equalsIgnoreCase(ConstantApp.SAF_HISTORY)) {
                        if (AppManager.getInstance().getAdminSafHistoryView().equalsIgnoreCase("ADMIN")) {
                            Logger.v("safadmin");
                            transactionRecyclerAdapter = new TransactionRecyclerAdapter(TransactionHistoryActivity.this, transactionHistoryTuples, true, TransactionHistoryActivity.this);
                            transactionRecyclerAdapter.notifyDataSetChanged();
                        } else if (AppManager.getInstance().getAdminSafHistoryView().equalsIgnoreCase("MERCHANT")) {
                            Logger.v("safmerchant");
                            transactionRecyclerAdapter = new TransactionRecyclerAdapter(TransactionHistoryActivity.this, transactionHistoryTuples, false, TransactionHistoryActivity.this);
                            transactionRecyclerAdapter.notifyDataSetChanged();
                        }
                    }

                    recyclerView.setAdapter(transactionRecyclerAdapter);
                    sort_param_layout.setVisibility(View.VISIBLE);
                    //2: If it already exists then prompt user
                    Logger.v("txn loaded");
                } else {
                    Utils.alertDialogShow(context, getString(R.string.history_is_empty), false);
//                    Toast.makeText(activity, getString(R.string.history_is_empty), Toast.LENGTH_LONG).show();
                    sort_param_layout.setVisibility(View.GONE);
                    Logger.v("txn not not loaded");
//                    activity.onBackPressed();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        showTimer = false;
        super.onResume();
    }
}
