package com.tarang.dpq2.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.model.ReconciliationCardSchemeModel;

import java.util.List;

public class ReconciliationViewAdapter extends RecyclerView.Adapter<ReconciliationViewAdapter.ViewHolder> {
    Context context;
    List<ReconciliationCardSchemeModel> reconciliationCardSchemeModelList;

    public ReconciliationViewAdapter(Context context, List<ReconciliationCardSchemeModel> reconciliationCardSchemeModelList) {
        this.context = context;
        this.reconciliationCardSchemeModelList = reconciliationCardSchemeModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.custom_reconciliation_layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);

//        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_reconciliation_layout,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Logger.v("Position --"+position);
        Logger.v("Position --"+reconciliationCardSchemeModelList.get(position).getCardSchemeEnglish());
        if (reconciliationCardSchemeModelList.size()!=0) {
            holder.txn_type_ar_tv.setText(reconciliationCardSchemeModelList.get(position).getCardSchemeArabic());
            holder.txn_type_tv.setText(reconciliationCardSchemeModelList.get(position).getCardSchemeEnglish());

            Logger.v("Host --"+reconciliationCardSchemeModelList.get(position).getMadaHostTotalsCount());
            Logger.v("Host --"+reconciliationCardSchemeModelList.get(position).getMadaHostTotalsAmountInSar());
            if (reconciliationCardSchemeModelList.get(position).getMadaHostTotalsCount() == null && reconciliationCardSchemeModelList.get(position).getMadaHostTotalsAmountInSar() == null) {
                holder.no_txn_layout.setVisibility(View.VISIBLE);
                holder.txn_layout.setVisibility(View.GONE);
            } else{
                holder.no_txn_layout.setVisibility(View.GONE);
                holder.txn_layout.setVisibility(View.VISIBLE);
                //madahost
                holder.total_db_count.setText(reconciliationCardSchemeModelList.get(position).getMadaHostTotalDbCount());
            holder.total_db_amount_insar.setText(reconciliationCardSchemeModelList.get(position).getMadaHostTotalDbAmountInSar());
            holder.total_cr_count.setText(reconciliationCardSchemeModelList.get(position).getMadaHostTotalCrCount());
            holder.total_cr_amount_insar.setText(reconciliationCardSchemeModelList.get(position).getMadaHostTotalCrAmountInSar());
            holder.naqd_count.setText(reconciliationCardSchemeModelList.get(position).getMadaHostNAQDCount());
            holder.naqd_insar.setText(reconciliationCardSchemeModelList.get(position).getMadaHostNAQDAmountInSar());
            holder.cadv_count.setText(reconciliationCardSchemeModelList.get(position).getMadaHostCashAdvanceCount());
            holder.cadv_insar.setText(reconciliationCardSchemeModelList.get(position).getMadaHostCashAdvanceAmountInSar());
            holder.auth_count.setText(reconciliationCardSchemeModelList.get(position).getMadaHostAuthCount());
            holder.auth_insar.setText(reconciliationCardSchemeModelList.get(position).getMadaHostAuthAmountInSar());
            holder.totals_count.setText(reconciliationCardSchemeModelList.get(position).getMadaHostTotalsCount());
            holder.totals_insar.setText(reconciliationCardSchemeModelList.get(position).getMadaHostTotalsAmountInSar());

            //pos terminal
            holder.pt_total_db_count.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalTotalDbCount());
            holder.pt_total_db_amount_insar.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalTotalDbAmountInSar());
            holder.pt_total_cr_count.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalTotalCrCount());
            holder.pt_total_cr_amount_insar.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalTotalCrAmountInSar());
            holder.pt_naqd_count.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalNAQDCount());
            holder.pt_naqd_insar.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalNAQDAmountInSar());
            holder.pt_cadv_count.setText(reconciliationCardSchemeModelList.get(position).getPasTerminalCashAdvancedCount());
            holder.pt_cadv_insar.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalCashAdvanceAmountInSar());
            holder.pt_auth_count.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalAuthCount());
            holder.pt_auth_insar.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalAuthAmountInSar());
            holder.pt_totals_count.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalTotalsCount());
            holder.pt_totals_insar.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalTotalsAmountInSar());

            //pos terminal details
            holder.poff_count.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalDetailsPOFFCount());
            holder.poff_amount_insar.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalDetailsPOFFAmountInSar());
            holder.pon_count.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalDetailsPONCount());
            holder.pon_amount_insar.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalDetailsPONAmountInSar());
            holder.pur_naqd_count.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalDetailsPurNaqdCount());
            holder.pur_naqd_insar.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalDetailsPurNaqdAmountInSar());
            holder.reversal_count.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalDetailsReversalCount());
            holder.reversal_insar.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalDetailsReversalAmountInSar());
            holder.refund_count.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalDetailsRefundCount());
            holder.refund_insar.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalDetailsRefundAmountInSar());
            holder.comp_count.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalDetailsCompCount());
            holder.comp_insar.setText(reconciliationCardSchemeModelList.get(position).getPosTerminalDetailsCompAmountInSar());

        }
        }

    }

    @Override
    public int getItemCount() {
       try {
           return reconciliationCardSchemeModelList.size();

       }catch (Exception e){
           return 0;
       }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txn_type_ar_tv,txn_type_tv,total_db_count,total_db_amount_insar,total_cr_count,total_cr_amount_insar,
        naqd_count,naqd_insar,cadv_count,cadv_insar,auth_count,auth_insar,totals_count,totals_insar,pt_total_db_count,
        pt_total_db_amount_insar,pt_total_cr_count,pt_total_cr_amount_insar,pt_naqd_count,pt_naqd_insar,pt_cadv_count,pt_cadv_insar,
        pt_auth_count,pt_auth_insar ,pt_totals_count,pt_totals_insar,poff_count,poff_amount_insar,pon_count,pon_amount_insar,pur_naqd_count
                ,pur_naqd_insar,reversal_count,reversal_insar,refund_count,refund_insar,comp_count,comp_insar;
        LinearLayout no_txn_layout,txn_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            no_txn_layout =  itemView.findViewById(R.id.no_txn_layout);
            txn_layout =  itemView.findViewById(R.id.txn_layout);

            txn_type_ar_tv =  itemView.findViewById(R.id.txn_type_ar_tv);
            txn_type_tv =  itemView.findViewById(R.id.txn_type_tv);
            total_db_count =  itemView.findViewById(R.id.total_db_count);
            total_db_amount_insar =  itemView.findViewById(R.id.total_db_amount_insar);
            total_cr_count =  itemView.findViewById(R.id.total_cr_count);
            total_cr_amount_insar =  itemView.findViewById(R.id.total_cr_amount_insar);
            naqd_count =  itemView.findViewById(R.id.naqd_count);
            naqd_insar =  itemView.findViewById(R.id.naqd_insar);
            cadv_count =  itemView.findViewById(R.id.cadv_count);
            cadv_insar =  itemView.findViewById(R.id.cadv_insar);
            auth_count =  itemView.findViewById(R.id.auth_count);
            auth_insar =  itemView.findViewById(R.id.auth_insar);
            totals_count =  itemView.findViewById(R.id.totals_count);
            totals_insar =  itemView.findViewById(R.id.totals_insar);
            pt_total_db_count =  itemView.findViewById(R.id.pt_total_db_count);
            pt_total_db_amount_insar =  itemView.findViewById(R.id.pt_total_db_amount_insar);
            pt_total_cr_count =  itemView.findViewById(R.id.pt_total_cr_count);
            pt_total_cr_amount_insar =  itemView.findViewById(R.id.pt_total_cr_amount_insar);
            pt_naqd_count =  itemView.findViewById(R.id.pt_naqd_count);
            pt_naqd_insar =  itemView.findViewById(R.id.pt_naqd_insar);
            pt_cadv_count =  itemView.findViewById(R.id.pt_cadv_count);
            pt_cadv_insar =  itemView.findViewById(R.id.pt_cadv_insar);
            pt_auth_count =  itemView.findViewById(R.id.pt_auth_count);
            pt_auth_insar =  itemView.findViewById(R.id.pt_auth_insar);
            pt_totals_count =  itemView.findViewById(R.id.pt_totals_count);
            pt_totals_insar =  itemView.findViewById(R.id.pt_totals_insar);
            poff_count =  itemView.findViewById(R.id.poff_count);
            poff_amount_insar =  itemView.findViewById(R.id.poff_amount_insar);
            pon_count =  itemView.findViewById(R.id.pon_count);
            pon_amount_insar =  itemView.findViewById(R.id.pon_amount_insar);
            pur_naqd_count =  itemView.findViewById(R.id.pur_naqd_count);
            pur_naqd_insar =  itemView.findViewById(R.id.pur_naqd_insar);
            reversal_count =  itemView.findViewById(R.id.reversal_count);
            reversal_insar =  itemView.findViewById(R.id.reversal_insar);
            refund_count =  itemView.findViewById(R.id.refund_count);
            refund_insar =  itemView.findViewById(R.id.refund_insar);
            comp_count =  itemView.findViewById(R.id.comp_count);
            comp_insar =  itemView.findViewById(R.id.comp_insar);
        }
    }
}
