package abhi.example.hp.stenobano.gatway;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import abhi.example.hp.stenobano.config.BaseUrl;
import abhi.example.hp.stenobano.config.Base_Url;
import abhi.example.hp.stenobano.auth.Login;
import abhi.example.hp.stenobano.session.SesssionManager;

public class PaytmGatway extends AppCompatActivity implements PaytmPaymentTransactionCallback {
    String custid="", orderId="",amount="";
   //String mid="NbdSbW21611155320608";
    String mid="TeJeFa30872083811435";
    String CHECKSUMHASH;
    //String mid="vpVzpN79131668330806";
    RequestQueue requestQueue;
    private String ORDERID="",TXNAMOUNT="",day="",email="";

    //String url="http://getlowestairfares.com/Android_MLM/paytm/generateChecksum.php";
    String url= Base_Url.url+"paytm/generateChecksum.php";
    //String CallBackURL ="http://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
    String CallBackURL ="https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="+orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_paytm_gatway);
        requestQueue= Volley.newRequestQueue(this);
        Intent intent = getIntent();
        orderId = intent.getExtras().getString("orderid");
        custid = intent.getExtras().getString("custid");
        amount=intent.getExtras().getString("amount");
        day=intent.getExtras().getString("day");
        email=intent.getExtras().getString("email");

        getCheckSum();

    }


    private  void getCheckSum()
    {
        float f = Float.valueOf(amount);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        float twoDigitsF = Float.valueOf(decimalFormat.format(f));
        amount=String.valueOf(twoDigitsF);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CHECKSUMHASH:",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.has("CHECKSUMHASH"))
                    {
                        CHECKSUMHASH=jsonObject.getString("CHECKSUMHASH");
                        //  Toast.makeText(PaytmGatway.this, "CHECKSUMHASH"+CHECKSUMHASH, Toast.LENGTH_SHORT).show();
                        // PaytmPGService Service = PaytmPGService.getStagingService();
                        // when app is ready to publish use production service
                        PaytmPGService Service = PaytmPGService.getProductionService();
                        // now call paytm service here
                        //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
                        HashMap<String, String> paramMap = new HashMap<String, String>();
                        //these are mandatory parameters
                        paramMap.put("MID", mid); //MID provided by paytm
                        paramMap.put("ORDER_ID", orderId);
                        paramMap.put("CUST_ID", custid);
                        paramMap.put("CHANNEL_ID", "WAP");
                        paramMap.put("TXN_AMOUNT", amount);
                        paramMap.put("WEBSITE", "DEFAULT");
                        paramMap.put("CALLBACK_URL" ,CallBackURL);
                        paramMap.put( "EMAIL" , email);  //no need
                        paramMap.put( "MOBILE_NO" ,custid); //no need
                        paramMap.put("CHECKSUMHASH" ,CHECKSUMHASH);
                        //paramMap.put("PAYMENT_TYPE_ID" ,"CC");  //no need
                        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                        PaytmOrder Order = new PaytmOrder(paramMap);
                        Service.initialize(Order,null);
                        // start payment service call here
                        Service.startPaymentTransaction(PaytmGatway.this, true, true, PaytmGatway.this );
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PaytmGatway.this, ""+error, Toast.LENGTH_SHORT).show();
                Log.d("errorrrrr",error.toString());

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramMap = new HashMap<String,String>();
                paramMap.put( "MID" , mid);
                paramMap.put( "ORDER_ID" , orderId);
                paramMap.put( "CUST_ID" , custid);
                paramMap.put( "MOBILE_NO" , custid);
                paramMap.put( "EMAIL" , email);
                paramMap.put( "CHANNEL_ID" , "WAP");
                paramMap.put( "TXN_AMOUNT" , amount);
                paramMap.put( "WEBSITE" , "DEFAULT");
                paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
                paramMap.put( "CALLBACK_URL" , CallBackURL);
                return paramMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


    @Override
    public void onTransactionResponse(Bundle inResponse) {
        Log.d("succesRespons",inResponse.toString());
        String success= inResponse.getString("TXN_SUCCESS");
         ORDERID= inResponse.getString("ORDERID");
        Toast.makeText(getApplicationContext(), "success " + success, Toast.LENGTH_LONG).show();
        if (inResponse.getString("STATUS").equals("TXN_SUCCESS"))
        {
            update_status();
            Toast.makeText(this, "Tracsaction Succcess", Toast.LENGTH_SHORT).show();

        }
        else
        {
            String mesg = inResponse.getString("RESPMSG");
           Toast t= Toast.makeText(this, ""+mesg, Toast.LENGTH_LONG);
           t.setGravity(Gravity.CENTER,0,0);
           t.show();
            Intent i=new Intent(PaytmGatway.this,Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(0,0);



        }
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {
        Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void someUIErrorOccurred(String inErrorMessage) {
        Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();

    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
        Toast.makeText(getApplicationContext(), "Unable to load webpage " + inFailingUrl.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
        Toast.makeText(getApplicationContext(), "onTransactionCancel" , Toast.LENGTH_LONG).show();
    }


    private void update_status() {
        String  invoice = "STB"+String.valueOf((Math.random() * 99999000));
        // Toast.makeText(this, "" + invoice + " " + invoice_amt + " " + email_id + " " + mobile_no, Toast.LENGTH_SHORT).show();
        String url = BaseUrl.update_statuss;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PaytmGatway.this, "" + response, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    if (jsonObject.getString("status").equals("Success"));
                    {
                        Toast.makeText(PaytmGatway.this, "Success", Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray1=jsonObject.getJSONArray("result");
                        JSONObject jsonObject1=jsonArray1.getJSONObject(0);
                        new SesssionManager(PaytmGatway.this).updateValid(jsonObject1.getString("valid"));
                        Intent i=new Intent(PaytmGatway.this,Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        overridePendingTransition(0,0);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("invoice", ORDERID);
                data.put("id", new SesssionManager(PaytmGatway.this).userID());
                data.put("amount", amount);
                data.put("email", email);
                data.put("mobile", custid);
                data.put("days", day);
                return data;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }
}
