package com.tarang.dpq2.view.adapter;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.model.MenuModel;
import com.tarang.dpq2.view.dialoge.PopupDialoge;

import java.util.List;

public class SubMenuRecyclerAdapter extends RecyclerView.Adapter<SubMenuRecyclerAdapter.ViewHolder> {

    private final LifecycleOwner owner;
//    private K21Pininput pinkey;
    Context context;
    List<MenuModel.MenuItem> list;
    Printer print;

    public SubMenuRecyclerAdapter(Context context, List<MenuModel.MenuItem> list, Printer print, LifecycleOwner owner) {
        this.context = context;
        this.list = list;
        this.print = print;
        this.owner = owner;
//        this.pinkey = SDKDevice.getInstance(context).getK21Pininput();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_submenu, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txt_title.setText(list.get(position).getMenu_name());
        if (list.get(position).getMenuDrawable() != 0) {
            holder.img_drawable.setVisibility(View.VISIBLE);
            holder.img_drawable.setImageDrawable(context.getResources().getDrawable(list.get(position).getMenuDrawable()));
        } else
            holder.img_drawable.setVisibility(View.GONE);
        holder.ll_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.v("isDisplayData --" + list.get(position).isDisplayData());
                Logger.v("isDisplayData --" + list.get(position).isPrint());
                Logger.v("isDisplayData --" + list.get(position).isRegisteValidation());
                if(list.get(position).isRegisteValidation()){
                    if(((list.get(position).getMenu_tag().equalsIgnoreCase(ConstantApp.FULL_DOWNLOAD)) || (list.get(position).getMenu_tag().equalsIgnoreCase(ConstantApp.PARTIAL_DOWNLOAD)))){
                        if (!AppManager.getInstance().getOnlyInitializationStatus(context)) {
                            return;
                        }
                    }else {
                        if (!AppManager.getInstance().getInitializationStatus(context)) {
                            return;
                        }
                    }
                }
                AppManager.getInstance().setMenuItem(list.get(position));
                if(list.get(position).isEditAllowed()){
                    new PopupDialoge(context).createEditText(list.get(position), new DisplayMenuDataRecyclerAdapter.Clicked() {
                        @Override
                        public void onClick(boolean loadKeys) {
                            Logger.v("notifyDataSetChanged");
                            if(loadKeys){
//                                new LoadKeys(context, pinkey).loadDukptKey();
                            }
                        }
                    });
                }else if (list.get(position).isPrint()) {
                    Logger.v("Print Flow");
                    if(!Utils.checkPrinterPaper(context)) {
//                        if (list.get(position).getMenu_tag().equalsIgnoreCase(ConstantApp.LAST_EMV))
////                            print.doPrint(8);
////                        else if (list.get(position).getMenu_tag().equalsIgnoreCase(ConstantApp.PRINT_SYS_INFO))
////                            print.doPrint(9);
////                        else
////                            print.doPrint(position);
                        print.doPrint(list.get(position).getMenu_tag());
                    }
                } else if (list.get(position).isDisplayData()) {
                    Logger.v("Display Flow");
                    MapperFlow.getInstance().moveToDisplaySubMenu(context, list.get(position));
                } else if (list.get(position).getSubmenu() != null && list.get(position).getSubmenu().size() != 0) {
                    Logger.v("SubMenu Flow");
                    MapperFlow.getInstance().moveToSubMenu(context, list.get(position));
                } else{
                    print.normalFlow(list.get(position));
                    Logger.v("Out Of Flow");
//                    outOfServiceDialoe();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title;
        ImageView img_drawable;
        LinearLayout ll_holder;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            ll_holder = itemView.findViewById(R.id.ll_holder);
            img_drawable = itemView.findViewById(R.id.img_drawable);
        }
    }

    public interface Printer {
        public void doPrint(String i);
        public void normalFlow(MenuModel.MenuItem i);
    }
}
