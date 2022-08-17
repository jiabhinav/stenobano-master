package abhi.example.hp.stenobano.notification;

import android.app.ProgressDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.adapter.Notification_Adapter_Fcm;
import abhi.example.hp.stenobano.model.Chat;

public class ResultNotification extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference reference;
    private List<Chat> mChat;
    Notification_Adapter_Fcm adapter_fcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_notification);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Notification");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        recyclerView=findViewById(R.id.recyclerView);
        readMessage();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void readMessage()
    {
        mChat=new ArrayList<>();
        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        reference= FirebaseDatabase.getInstance().getReference("Notification").child("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    //Toast.makeText(ResultNotification.this, ""+snapshot.getKey(), Toast.LENGTH_SHORT).show();
                    Chat chat=snapshot.getValue(Chat.class);

                    mChat.add(chat);
                }
                dialog.dismiss();
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,true);
                linearLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter_fcm=new Notification_Adapter_Fcm(mChat,getApplicationContext());
                recyclerView.setAdapter(adapter_fcm);
                adapter_fcm.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });

    }




}
