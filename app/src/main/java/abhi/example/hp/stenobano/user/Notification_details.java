package abhi.example.hp.stenobano.user;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import abhi.example.hp.stenobano.R;

public class Notification_details extends AppCompatActivity {

    TextView job_type,details,link,date;
String serch_link;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        job_type=findViewById(R.id.job_type);
        details=findViewById(R.id.details);
        link=findViewById(R.id.job_link);
        date=findViewById(R.id.date);
        Intent i=getIntent();
        job_type.setText(i.getStringExtra("type"));
        details.setText(i.getStringExtra("details"));
        link.setText("http://"+i.getStringExtra("link"));
        serch_link=i.getStringExtra("link");
        date.setText(i.getStringExtra("date"));
        link.setPaintFlags(link.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        link.setTextColor(Color.rgb(0,150,0));
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="http://"+serch_link;
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
            }
        });
    }
}
