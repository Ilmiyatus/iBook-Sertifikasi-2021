package com.example.ibook;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CashFlowAdapter extends RecyclerView.Adapter<CashFlowAdapter.CashflowViewHolder> {
    private List<Cashflow> listCashflow;

    public CashFlowAdapter(List<Cashflow> listCashflow) {
        this.listCashflow = listCashflow;
    }
    @Override
    public CashflowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cashflow, parent, false);
        return new CashflowViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(CashflowViewHolder holder, int position) {
        holder.keterangan.setText(listCashflow.get(position).getKeterangan());
        holder.tanggal.setText(listCashflow.get(position).getTgl());
        if (listCashflow.get(position).getJenis().equals("in")){
            holder.nominal.setText("[+] Rp. "+ listCashflow.get(position).getNominal());
            holder.jenis.setImageResource(R.drawable.left);
        }else{
            holder.nominal.setText("[-] Rp. "+ listCashflow.get(position).getNominal());
            holder.jenis.setImageResource(R.drawable.right);
        }
    }
    @Override
    public int getItemCount() {
        Log.v(CashFlowAdapter.class.getSimpleName(),""+listCashflow.size());
        return listCashflow.size();
    }
    public class CashflowViewHolder extends RecyclerView.ViewHolder {
        public TextView nominal;
        public TextView keterangan;
        public TextView tanggal;
        public ImageView jenis;
        public CashflowViewHolder(View view) {
            super(view);
            nominal = (TextView) view.findViewById(R.id.nominal_cf);
            keterangan = (TextView) view.findViewById(R.id.ket_cf);
            tanggal = (TextView) view.findViewById(R.id.tgl_cf);
            jenis = (ImageView) view.findViewById(R.id.logo_cf);
        }
    }
}
