package com.tarang.dpq2.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.utilities.Utils;
import com.wizarpos.wizarviewagentassistant.aidl.IAPNManagerService;

public class ApnSettingActivity extends BaseActivity {

    private IAPNManagerService apnManagerService = null;
    ServiceConnection serviceConnectionAPN1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apn_setting);
        setTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindApn();
        setListView();
    }

    private void setListView() {
        ListView lv = (ListView) findViewById(R.id.my_listView);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, AppInit.apnName);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alertDialogGPRSShow(ApnSettingActivity.this,i);
            }
        });
    }


    public void alertDialogGPRSShow(final Context context, final int position) {
        if (((BaseActivity) context).isFinishing())
            return;

//        final ApnModule module = new ApnModule(context);
//        final List<ApnEntity> nameList = module.getAllAPIList();
//        Logger.v("module --" + nameList.size());
//        if (nameList.size() == 0) {
//            return;
//        }
        final Dialog alertDialog1 = new Dialog(context);
        alertDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (alertDialog1.getWindow() != null)
            alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog1.setContentView(R.layout.list_select);

        final LinearLayout ll_holder = alertDialog1.findViewById(R.id.ll_holder);
        final LinearLayout save_btnnn = alertDialog1.findViewById(R.id.save_btnnn);
        Button btn_save = alertDialog1.findViewById(R.id.btn_save);
        final EditText edt_value = alertDialog1.findViewById(R.id.edt_value);
        final EditText edt_value1 = alertDialog1.findViewById(R.id.edt_value1);
        final EditText edt_value_mnc = alertDialog1.findViewById(R.id.edt_value_mnc);
        final EditText edt_value_mcc = alertDialog1.findViewById(R.id.edt_value_mcc);

        TextView txt_selected = alertDialog1.findViewById(R.id.txt_selected);
        txt_selected.setText("Access Point Names \n نقاط الوصول");
        ll_holder.setVisibility(View.GONE);
        save_btnnn.setVisibility(View.VISIBLE);
        edt_value.setText(AppInit.apnName[position]);
        edt_value1.setText(AppInit.apnList[position]);
        edt_value_mnc.setText(AppInit.mnc[position]);
        edt_value_mcc.setText(AppInit.mcc[position]);
//        final String[] nameList = AppInit.apnName;
//        for (int i = 0; i < nameList.length; i++) {
//            Button button = new Button(context);
//            button.setText(nameList[i]);
//            final int ii = i;
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Logger.v("setOnClickListener-" + ii);
//                    Logger.v("setOnClickListener-" + nameList[ii]);
//                    ll_holder.setVisibility(View.GONE);
//                    save_btnnn.setVisibility(View.VISIBLE);
//                    type[0] = ii;
//                    Logger.v("type -" + type[0]);
//                    edt_value.setText(AppInit.apnName[type[0]]);
//                    edt_value1.setText(AppInit.apnList[type[0]]);
//                    edt_value_mnc.setText(AppInit.mnc[type[0]]);
//                    edt_value_mcc.setText(AppInit.mcc[type[0]]);
////                    showEditApn(ii, click, context);
//                }
//            });
//            ll_holder.addView(button);
//        }
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.v("click save");
                if (edt_value.getText().toString().trim().length() != 0 && edt_value1.getText().toString().trim().length() != 0
                        && edt_value_mnc.getText().toString().trim().length() != 0 && edt_value_mcc.getText().toString().trim().length() != 0) {

                    Logger.v("click save APN");
                    try {
                        String apnStatus = apnManagerService.addByMCCAndMNC(edt_value.getText().toString().trim(), edt_value1.getText().toString().trim()
                                , edt_value_mcc.getText().toString().trim(), edt_value_mnc.getText().toString().trim());
                     //   String apnStatus = apnManagerService.add(edt_value.getText().toString().trim(), edt_value1.getText().toString().trim());
                        Logger.v("Created " + apnStatus);
                        if (apnStatus.equalsIgnoreCase("succeed")) {
                            alertDialog1.dismiss();
                            apnManagerService.setSelected(AppInit.apnName[position]);
                            AppManager.getInstance().setGPRSAPN(AppInit.apnName[position]);
                            AppManager.getInstance().setConnectonMode(true);
                            Utils.saveConnection(context);
                        } else
                            Toast.makeText(context, context.getResources().getString(R.string.encountered_error), Toast.LENGTH_SHORT).show();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.please_enter), Toast.LENGTH_SHORT).show();
                    Logger.v("Created else");
                }
            }
        });
        alertDialog1.setCanceledOnTouchOutside(false);

        Window window = alertDialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.BOTTOM;
        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        wlp.y = 140;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog1.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MapperFlow.getInstance().moveToLandingActivity(context);
    }

    public void bindApn() {
        serviceConnectionAPN1 = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    apnManagerService = IAPNManagerService.Stub.asInterface(service);
                    Log.d("IAPNManagerService", "IAPNManagerService  bind success.");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    unbindService(this);
                }

            }
        };
        startConnectService(this, new ComponentName("com.wizarpos.wizarviewagentassistant", "com.wizarpos.wizarviewagentassistant.APNManagerService"), serviceConnectionAPN1);
    }


    private void startConnectService(Context context, ComponentName comp, ServiceConnection connection) {
        try {
            Intent intent = new Intent();
            intent.setPackage(comp.getPackageName());
            intent.setComponent(comp);
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        context.unbindService(serviceConnectionAPN1);
//    }
}