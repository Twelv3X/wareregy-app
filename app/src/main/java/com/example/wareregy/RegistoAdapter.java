package com.example.wareregy;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RegistoAdapter extends RecyclerView.Adapter<RegistoAdapter.RegistoViewHolder>{
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Registo> registoList;

    //getting the context and product list with constructor
    public RegistoAdapter(Context mCtx, List<Registo> registoList) {
        this.mCtx = mCtx;
        this.registoList = registoList;
    }

    @Override
    public RegistoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.lista_registos, null);
        return new RegistoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RegistoViewHolder holder, int position) {
        //getting the product of the specified position
        Registo registo = registoList.get(position);

        //binding the data with the viewholder views
        holder.textViewProdutoId.setText(String.valueOf(registo.getProdutoId()));
        holder.textViewNomeProduto.setText(registo.getProdutoNome());
        holder.textViewProdutoPeso.setText(String.valueOf(registo.getProdutoPeso()));
        holder.textViewRegistoHora.setText(String.valueOf(segundosParaHm(registo.getRegistoHora())));
        if (position%2 == 0) {
            holder.textViewProdutoId.setBackgroundColor(Color.WHITE);
            holder.textViewNomeProduto.setBackgroundColor(Color.WHITE);
            holder.textViewProdutoPeso.setBackgroundColor(Color.WHITE);
            holder.textViewRegistoHora.setBackgroundColor(Color.WHITE);
        }
else
            {
                holder.textViewProdutoId.setBackgroundColor(Color.argb(25,91,89,93));
                holder.textViewNomeProduto.setBackgroundColor(Color.argb(25,91,89,93));
                holder.textViewProdutoPeso.setBackgroundColor(Color.argb(25,91,89,93));
                holder.textViewRegistoHora.setBackgroundColor(Color.argb(25,91,89,93));
            }
        }

    private String segundosParaHm (int segundos){
        int h = segundos / 3600;
        int m = (segundos % 3600) / 60;

        String hm = String.format("%02d:%02d", h, m);
        return hm;
    }

    @Override
    public int getItemCount() {
        return registoList.size();
    }


    class RegistoViewHolder extends RecyclerView.ViewHolder {

        TextView textViewProdutoId, textViewNomeProduto, textViewProdutoPeso, textViewRegistoHora;

        public RegistoViewHolder(View itemView) {
            super(itemView);

            textViewProdutoId = itemView.findViewById(R.id.textViewProdutoId);
            textViewNomeProduto = itemView.findViewById(R.id.textViewNomeProduto);
            textViewProdutoPeso = itemView.findViewById(R.id.textViewProdutoPeso);
            textViewRegistoHora = itemView.findViewById(R.id.textViewRegistoHora);

        }
    }
}
