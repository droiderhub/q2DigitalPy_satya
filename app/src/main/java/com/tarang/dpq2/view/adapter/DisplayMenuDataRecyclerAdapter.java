package com.tarang.dpq2.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.model.MenuModel;
import com.tarang.dpq2.view.dialoge.PopupDialoge;

import java.util.List;

public class DisplayMenuDataRecyclerAdapter extends RecyclerView.Adapter<DisplayMenuDataRecyclerAdapter.ViewHolder> {

    private List<MenuModel.DisplayValue> display;
    Context context;
    List<MenuModel.MenuItem> list;
    boolean isEdit;
    boolean isDisplayModel;
    int size;
//    K21Pininput pinkey;

    public DisplayMenuDataRecyclerAdapter(Context context, List<MenuModel.MenuItem> list, boolean isEdit) {
        this.context = context;
        this.list = list;
        this.isEdit = isEdit;
        size = list.size();
//        pinkey = SDKDevice.getInstance(context).getK21Pininput();
    }

    public DisplayMenuDataRecyclerAdapter(Context context, List<MenuModel.DisplayValue> display) {
        this.context = context;
        isDisplayModel = true;
        size = display.size();
        this.display = display;
//        pinkey = SDKDevice.getInstance(context).getK21Pininput();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_display_submenu, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isDisplayModel) {
            setUpDisplayData(holder, position);
        } else {
            setUpSubMenu(holder, position);
        }
    }

    private void setUpDisplayData(ViewHolder holder, int position) {
        holder.txt_title.setText(display.get(position).getTitle());
        holder.txt_value.setText(display.get(position).getValue());
        holder.img_edit.setVisibility(View.GONE);
    }

    private void setUpSubMenu(ViewHolder holder, final int position) {
        holder.txt_title.setText(list.get(position).getMenu_name());
        holder.txt_value.setText(AppManager.getInstance().getString(list.get(position).getMenu_tag()));
        if (isEdit) {
            holder.img_edit.setVisibility(View.VISIBLE);
            holder.img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new PopupDialoge(context).createEditText(list.get(position), new Clicked() {
                        @Override
                        public void onClick(boolean loadKeys) {
                            Logger.v("notifyDataSetChanged");
                            if(loadKeys){
//                                new LoadKeys(context, pinkey).loadDukptKey();
                                notifyDataSetChanged();
                            }else {
                                notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
        } else {
            holder.img_edit.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title;
        TextView txt_value;
        ImageView img_edit;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_value = itemView.findViewById(R.id.txt_value);
            img_edit = itemView.findViewById(R.id.img_edit);
        }
    }

    public interface Clicked {
        public void onClick(boolean loadKeys);
    }
}
