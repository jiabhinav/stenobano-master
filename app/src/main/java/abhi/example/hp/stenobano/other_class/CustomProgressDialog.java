package abhi.example.hp.stenobano.other_class;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import abhi.example.hp.stenobano.R;


public class CustomProgressDialog
{
    ProgressDialog progressDialog;
    Context context;
    public CustomProgressDialog(Context context) {
        this.context=context;
    }

    public void show()
    {
        progressDialog = ProgressDialog.show(context, null, null);
        progressDialog.setContentView(R.layout.progress_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
    }

    public void dissmis()
    {
        progressDialog.dismiss();
    }


}
