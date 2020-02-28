package com.example.atm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FinanceActivity extends AppCompatActivity {

    private ExpenseAdapter adapter;
    private RecyclerView recyclerView;
    private ExpenseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinanceActivity.this,AddActivity.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        helper = new ExpenseHelper(this);
        Cursor cursor = helper.getReadableDatabase()
                .query("expense",
                        null,null,null,
                        null,null,null);
        adapter = new ExpenseAdapter(cursor);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Cursor cursor = helper.getReadableDatabase()
                .query("expense",
                        null,null,null,
                        null,null,null);
        adapter = new ExpenseAdapter(cursor);
        recyclerView.setAdapter(adapter);
    }

    public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseHolder> {
        Cursor cursor;
        public ExpenseAdapter(Cursor cursor) {
            this.cursor = cursor;
        }

        @NonNull
        @Override
        public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.expense_item,parent,false);
            return new ExpenseHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExpenseHolder holder, int position) {
            cursor.moveToPosition(position);
            String date = cursor.getString(cursor.getColumnIndex("cdate"));
            String info = cursor.getString(cursor.getColumnIndex("info"));
            int amount = cursor.getInt(cursor.getColumnIndex("amount"));
            holder.dateText.setText(date);
            holder.infoText.setText(info);
            holder.amountText.setText(String.valueOf(amount));
        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        public class ExpenseHolder extends RecyclerView.ViewHolder{
            TextView dateText;
            TextView infoText;
            TextView amountText;
            public ExpenseHolder(@NonNull View itemView) {
                super(itemView);
                dateText = itemView.findViewById(R.id.item_date);
                infoText = itemView.findViewById(R.id.item_info);
                amountText = itemView.findViewById(R.id.item_amount);
            }
        }
    }

}
