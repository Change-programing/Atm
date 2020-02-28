package com.example.atm;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//ctrl+alt+F 將區域變數提升成為類別成員
//ctrl+alt+M 將程式碼抽取成為一個方法
//alt+enter Extract字串資源
//ctrl+d 複製游標的那一行
//shift+alt+↓ 該行往下移動
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOGIN = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
    boolean logon=false;
    private List<Function> functions;
    //String[] functions=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!logon){
            Intent intent =new Intent(this,LoginActivity.class);
            startActivityForResult(intent,REQUEST_LOGIN);//REQUEST_LOGIN按一下Alt+Enter 選擇Create constant filed  constant就是常數的意思 屬性
            //startActivity(intent);//使用startActivity無法得知它的結果
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        //Recycler
        setupFunctions();


        RecyclerView recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        //Adapter
        //FunctionAdapter adapter=new FunctionAdapter(this);
        IconAdapter adapter = new IconAdapter();
        recyclerView.setAdapter(adapter);
    }
    //ctrl+alt+M 移出變方法
    private void setupFunctions() {
        //ctrl+alt+F 成為屬性
        functions = new ArrayList<>();
        String[] funcs = getResources().getStringArray(R.array.functions);
        //shift+F6 可修改圖檔名稱
        functions.add(new Function(funcs[0],R.drawable.func_transaction));
        functions.add(new Function(funcs[1],R.drawable.func_balance));
        functions.add(new Function(funcs[2],R.drawable.func_finance));
        functions.add(new Function(funcs[3],R.drawable.func_contacts));
        functions.add(new Function(funcs[4],R.drawable.func_exit));
    }

    public class IconAdapter extends  RecyclerView.Adapter<IconAdapter.IconHolder> {
        //當Recycler元件還沒顯示任何資料的時候
        //getItemCount這個方法會被自動呼叫
        //是要知道它身上總共有多少個資料要顯示
        //取得數量後，在顯示一筆資料之前 會先呼叫onCreateViewHolder
        //這時它應該要回傳幾個 內部是沒有任何資料的一個ViewHolder物件
        //最後當畫面上它打算顯示第0筆資料的時候 會呼叫onBindViewHolder
        //onBindViewHolder就是告訴他我想顯是第幾筆資料 請它準備好內容
        @NonNull
        @Override
        public IconHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.activity_item_icon,parent,false);
            return new IconHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull IconHolder holder, int position) {
            final Function function = functions.get(position);
            holder.nameText.setText(function.getName());
            holder.iconImage.setImageResource(function.getIcon());//傳入function裡面的icon ID值
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked(function);
                }
            });
        }

        @Override
        public int getItemCount() {
            return functions.size();
        }

        public class IconHolder extends RecyclerView.ViewHolder{
            ImageView iconImage;
            TextView nameText;

            public IconHolder(@NonNull View itemView) {
                super(itemView);
                iconImage=itemView.findViewById(R.id.item_icon);
                nameText=itemView.findViewById(R.id.item_name);

            }
        }
    }

    private void itemClicked(Function function) {
        Log.d(TAG, "itemClicked:"+function.getName());
        switch (function.getIcon()){
            case R.drawable.func_transaction:
                startActivity(new Intent(this,TransActivity.class));
                break;
            case R.drawable.func_balance:
                break;
            case R.drawable.func_finance:
                Intent finance = new Intent(this,FinanceActivity.class);
                startActivity(finance);
                break;
            case R.drawable.func_contacts:
                Intent contacts=new Intent(this,ContactActivity.class);
                startActivity(contacts);
                break;
            case R.drawable.func_exit:
                finish();
                break;
        }
    }

    //複寫父類別方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_LOGIN){
            if (resultCode != RESULT_OK){
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
