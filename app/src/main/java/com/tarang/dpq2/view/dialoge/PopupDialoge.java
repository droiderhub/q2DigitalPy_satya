package com.tarang.dpq2.view.dialoge;

import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.model.MenuModel;
import com.tarang.dpq2.view.adapter.DisplayMenuDataRecyclerAdapter;

public class PopupDialoge {
    Context context;

    public PopupDialoge(Context context) {
        this.context = context;
    }

    public void createEditText(final MenuModel.MenuItem type, final DisplayMenuDataRecyclerAdapter.Clicked clicked) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_edittext);
        Button btn_save = dialog.findViewById(R.id.btn_save);
        final EditText edt_value = dialog.findViewById(R.id.edt_value);
        TextView txt_title = dialog.findViewById(R.id.txt_title);
        txt_title.setText(type.getMenu_name());
        txt_title.setVisibility(View.GONE);
        dialog.setTitle(type.getMenu_name());
        Logger.v("type.getMenu_tag()--" + type.getMenu_tag());
        String value = AppManager.getInstance().getString(type.getMenu_tag());

        if (type.getMenu_tag().equalsIgnoreCase(ConstantApp.SPRM_TRSM_ID) && AppManager.getInstance().getInitializationStatus(context)) {
//            edt_value.setEnabled(false);
            edt_value.setFocusable(false);
            edt_value.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            edt_value.setClickable(false);
            btn_save.setEnabled(false);
            btn_save.getBackground().setAlpha(100);
        } else {
 //           edt_value.setEnabled(true);
            edt_value.setFocusable(true);
            edt_value.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            edt_value.setClickable(true);
            btn_save.setEnabled(true);
            btn_save.getBackground().setAlpha(255);
        }

        if (value.trim().length() != 0)
            edt_value.setText(value);
        else
            edt_value.setHint(type.getMenu_name());
        if (type.getMenu_tag().equalsIgnoreCase(ConstantApp.SPRM_NII_ID)){
            Logger.v("reaching_nii-----"+ ConstantApp.SPRM_NII_ID);
            edt_value.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3) });
        }else
            Logger.v("else-----"+ ConstantApp.SPRM_NII_ID);

        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_value.getText().toString().trim().length() != 0) {
                    Logger.v("type.getMenu_tag()--" + type.getMenu_tag());
                    if(!AppManager.getInstance().getString(type.getMenu_tag()).equalsIgnoreCase(edt_value.getText().toString())) {
                        if (checkLoadKeysNeeeded(type.getMenu_tag())) {
                            Logger.v("Loding keys");
                            AppManager.getInstance().setString(type.getMenu_tag(), edt_value.getText().toString());
                            clicked.onClick(true);

                        }else {
                            AppManager.getInstance().setString(type.getMenu_tag(), edt_value.getText().toString());
                            clicked.onClick(false);
                        }
                    }
                    dialog.dismiss();
                } else
                    Toast.makeText(context, context.getString(R.string.please_enter) + type.getMenu_name(), Toast.LENGTH_SHORT).show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean checkLoadKeysNeeeded(String menu_tag) {
        return menu_tag.equalsIgnoreCase(ConstantApp.SPRM_VENDER_ID) || menu_tag.equalsIgnoreCase(ConstantApp.SPRM_TRSM_ID) || menu_tag.equalsIgnoreCase(ConstantApp.SPRM_KEY_ISSUE_);
    }

    public void createEditText(final MenuModel.MenuItem type) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_edittext);
        Button btn_save = dialog.findViewById(R.id.btn_save);
        final EditText edt_value = dialog.findViewById(R.id.edt_value);
        TextView txt_title = dialog.findViewById(R.id.txt_title);
        txt_title.setText(type.getMenu_name());
        txt_title.setVisibility(View.GONE);
        dialog.setTitle(type.getMenu_name());

        Logger.v("type.getMenu_tag()--" + type.getMenu_tag());
        String value = AppManager.getInstance().getString(type.getMenu_tag());
        if (value.trim().length() != 0)
            edt_value.setText(value);
        else
            edt_value.setHint(type.getMenu_name());

        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_value.getText().toString().trim().length() != 0) {
                    Logger.v("type.getMenu_tag()--" + type.getMenu_tag());
                    AppManager.getInstance().setString(type.getMenu_tag(), edt_value.getText().toString());
                    dialog.dismiss();
                } else
                    Toast.makeText(context, context.getString(R.string.please_enter) + type.getMenu_name(), Toast.LENGTH_SHORT).show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void createwith2Edittext(final MenuModel.MenuItem type) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_edittext);
        Button btn_save = dialog.findViewById(R.id.btn_save);
        final EditText edt_value = dialog.findViewById(R.id.edt_value);
        final EditText edt_value1 = dialog.findViewById(R.id.edt_value1);
        TextView txt_title = dialog.findViewById(R.id.txt_title);
        txt_title.setText(type.getMenu_name());
        txt_title.setVisibility(View.GONE);
        dialog.setTitle(type.getMenu_name());
        Logger.v("type.getMenu_tag()--" + type.getMenu_tag());
        String value = AppManager.getInstance().getString(type.getMenu_tag());
        edt_value.setHint("Current Password");
        edt_value1.setHint("New Password");
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_value.getText().toString().trim().length() != 0) {
                    Logger.v("type.getMenu_tag()--" + type.getMenu_tag());
                    if (type.getMenu_tag().equalsIgnoreCase(ConstantApp.CHANGE_PASSWORD_ADMIN)) {
                        if (!(edt_value.getText().toString().equalsIgnoreCase(AppManager.getInstance().getAdminPassword()))) {
                            Toast.makeText(context, "Invalid Current password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        AppManager.getInstance().setAdminPassword(edt_value1.getText().toString());
                    } else {
                        if (!(edt_value.getText().toString().equalsIgnoreCase(AppManager.getInstance().getMerchantPassword()))) {
                            Toast.makeText(context, "Invalid Current password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        AppManager.getInstance().setMerchantPassword(edt_value1.getText().toString());
                    }
                    dialog.dismiss();
                } else
                    Toast.makeText(context, "Please enter " + type.getMenu_name(), Toast.LENGTH_SHORT).show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void createWithResetButtons(final MenuModel.MenuItem type) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_buttons);
        Button btn_1 = dialog.findViewById(R.id.btn_1);
        Button btn_2 = dialog.findViewById(R.id.btn_2);
        dialog.setTitle(type.getMenu_name());
        Logger.v("type.getMenu_tag()--" + type.getMenu_tag());
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword(context, true);
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword(context, false);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private static void resetPassword(final Context context, final boolean admin) {
        Utils.alertYesDialogShow(context, context.getString(R.string.are_you_sure_reset_password), new View.OnClickListener() {
            @Override
            public void onClick(View dialog) {
                if (admin)
                    AppManager.getInstance().resetAdminPassword();
                else
                    AppManager.getInstance().resetMerchantPassword();
                Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show();
                Utils.dismissDialoge();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View dialog) {
//                dialog.dismiss();
                Utils.dismissDialoge();
            }
        });

    }
}
