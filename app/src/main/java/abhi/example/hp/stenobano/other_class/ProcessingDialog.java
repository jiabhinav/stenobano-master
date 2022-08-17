package abhi.example.hp.stenobano.other_class;

import android.app.AlertDialog;
import android.content.Context;


import abhi.example.hp.stenobano.R;
import dmax.dialog.SpotsDialog;

public class ProcessingDialog
{
    Context context;
    String mesg;
    AlertDialog progressDialog;
    public ProcessingDialog(Context context)
    {
        this.context=context;
        this.mesg=mesg;


    }


    public void show(String mesg)
    {
         progressDialog=new SpotsDialog(context, R.style.CustomDialog);
        progressDialog.show();
    }

    public void dismiss()
    {
        progressDialog.dismiss();
    }


}
