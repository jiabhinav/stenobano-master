package abhi.example.hp.stenobano.user;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import abhi.example.hp.stenobano.R;

public class Help extends AppCompatActivity implements View.OnClickListener {
private Button email,call,call2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Help");
        email=findViewById(R.id.email);
        email.setOnClickListener(this);

        call=findViewById(R.id.call);
        call.setOnClickListener(this);
        call2=findViewById(R.id.call2);
        call2.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.email)
        {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" +  "nirankarirajender@gmail.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Complaint/ any Query!!!");
            emailIntent.putExtra(Intent.EXTRA_TEXT,  "Enter complaint Or query message here....");
            startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
        }
        else if (view.getId()==R.id.call)
        {
            String phone = "7290000723";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
        }
        else if (view.getId()==R.id.call2)
        {
            String phone = "8368429064";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
        }
    }
}
