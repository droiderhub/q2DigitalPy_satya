package com.tarang.dpq2.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.room_database.db.tuple.TransactionHistoryTuple;
import com.tarang.dpq2.base.utilities.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.TAG55;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.ViewHolder> implements Filterable {

    Context context;
    List<TransactionHistoryTuple> transactionHistoryTupleList;
    List<TransactionHistoryTuple> transactionHistoryTupleFilterList;
    boolean isAdminSafHistory;
    DeleteSafItemOnClick deleteSafItemOnClick;

    public TransactionRecyclerAdapter(Context context, List<TransactionHistoryTuple> list,boolean isAdminSafHistory, DeleteSafItemOnClick deleteSafItemOnClick) {
        this.context = context;
        this.transactionHistoryTupleList = list;
        this.transactionHistoryTupleFilterList = list;
        this.isAdminSafHistory=isAdminSafHistory;
        this.deleteSafItemOnClick = deleteSafItemOnClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.transaction_item, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        HashMap<String, String> tag55 = Utils.getParsedTag55(Utils.decrypt(transactionHistoryTupleFilterList.get(position).getIccCardSystemRelatedData55()));
        tag55.get(TAG55[17]); // Tag 84
        tag55.get(TAG55[5]); // Tag 95
        tag55.get(TAG55[21]); // Tag 9B
        tag55.get(TAG55[14]); // Tag 9F34
        String[] startTime = transactionHistoryTupleFilterList.get(position).getStartTimeTransaction().split(" ");
        Logger.v("saf_time----"+transactionHistoryTupleFilterList.get(position).getStartTimeTransaction());
        Logger.v("saf_time_split----"+startTime[0]);
        holder.transaction_aid.setText(tag55.get(TAG55[17]));
        holder.transaction_rrn.setText(transactionHistoryTupleFilterList.get(position).getRetriRefNo37());
        holder.transaction_response.setText(transactionHistoryTupleFilterList.get(position).getResponseCode39());
        holder.transaction_date.setText(startTime[0]);
        holder.transaction_amount.setText( Utils.changeAmountFormatWithDecimal(transactionHistoryTupleFilterList.get(position).getAmtTransaction4()));
        holder.delete_saf.setVisibility(View.GONE);
        if (isAdminSafHistory){
            holder.delete_saf.setVisibility(View.VISIBLE);
            holder.delete_saf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.v("safdeletefromadapter");
                    Utils.alertDialogShow(context, context.getString(R.string.delete_history_qstn), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSafItemOnClick.deleteSafItem(transactionHistoryTupleFilterList.get(position).getUid());
                            Utils.dismissDialoge();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.dismissDialoge();

                        }
                    });

                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return transactionHistoryTupleFilterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    transactionHistoryTupleFilterList = transactionHistoryTupleList;
                } else {
                    List<TransactionHistoryTuple> filteredList = new ArrayList<>();
                    for (TransactionHistoryTuple row : transactionHistoryTupleList) {

                        HashMap<String, String> tag55 = Utils.getParsedTag55(row.getIccCardSystemRelatedData55());
                        tag55.get(TAG55[17]); // Tag 84
                        tag55.get(TAG55[5]); // Tag 95
                        tag55.get(TAG55[21]); // Tag 9B
                        tag55.get(TAG55[14]); // Tag 9F34
                        String[] startTime = row.getStartTimeTransaction().split(" ");


                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if ( /*tag55.get(TAG55[17]).toLowerCase().contains(charString.toLowerCase())
                                ||*/ row.getRetriRefNo37().contains(charString.toLowerCase())
                                || startTime[0].contains(charString.toLowerCase())
                                ||  Utils.changeAmountFormatWithDecimal(row.getAmtTransaction4()).contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    transactionHistoryTupleFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = transactionHistoryTupleFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                transactionHistoryTupleFilterList = (ArrayList<TransactionHistoryTuple>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView transaction_aid,transaction_rrn,transaction_response,transaction_status,transaction_date,transaction_amount;
        ImageView delete_saf;

        public ViewHolder(View itemView) {
            super(itemView);
            transaction_aid = (TextView)itemView.findViewById(R.id.transaction_aid);
            transaction_rrn = (TextView)itemView.findViewById(R.id.transaction_rrn);
            transaction_response = (TextView)itemView.findViewById(R.id.transaction_response);
            transaction_status = (TextView)itemView.findViewById(R.id.transaction_status);
            transaction_date = (TextView)itemView.findViewById(R.id.transaction_date);
            transaction_amount = (TextView)itemView.findViewById(R.id.transaction_amount);
            delete_saf = (ImageView) itemView.findViewById(R.id.delete_saf);

        }
    }

    public interface DeleteSafItemOnClick{
        public void deleteSafItem(int id);
    }
}
