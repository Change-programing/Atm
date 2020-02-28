package com.example.atm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.FunctionViewHolder>{

    private final String[] functions;
    Context context;
    public FunctionAdapter(Context context){
        this.context=context;
        functions=context.getResources().getStringArray(R.array.functions);
    }
    @NonNull
    @Override
    public FunctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(
                android.R.layout.simple_list_item_1,parent,false);
        return new FunctionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FunctionViewHolder holder, int position) {
        holder.nameText.setText(functions[position]);
    }

    @Override
    public int getItemCount() {
        return functions.length;
    }

    public class FunctionViewHolder extends RecyclerView.ViewHolder{

        TextView nameText;
        public FunctionViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText=itemView.findViewById(android.R.id.text1);
        }
    }
}
