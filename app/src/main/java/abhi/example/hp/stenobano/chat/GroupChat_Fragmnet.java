package abhi.example.hp.stenobano.chat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.giphy.sdk.core.network.response.ListMediaResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import abhi.example.hp.stenobano.Dashboard.User_Home;
import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.chat.Audio.Play_Audio_F;
import abhi.example.hp.stenobano.chat.Audio.SendAudio;
import abhi.example.hp.stenobano.chat.notification_model.DataModel;
import abhi.example.hp.stenobano.chat.notification_model.NotificationModel;
import abhi.example.hp.stenobano.chat.notification_model.RootModel;
import abhi.example.hp.stenobano.config.BaseUrl;
import abhi.example.hp.stenobano.other_class.GetData;
import abhi.example.hp.stenobano.other_class.ProcessingDialog;
import abhi.example.hp.stenobano.retrofit.APIInterface;
import abhi.example.hp.stenobano.session.SesssionManager;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static abhi.example.hp.stenobano.config.BaseUrl.ID;
import static abhi.example.hp.stenobano.session.SesssionManager.IMAGE;
import static abhi.example.hp.stenobano.session.SesssionManager.MOBILE;
import static abhi.example.hp.stenobano.session.SesssionManager.NAME;
import static android.app.Activity.RESULT_OK;

public class GroupChat_Fragmnet extends Fragment implements View.OnClickListener {
    //===============
    DatabaseReference rootref;
    String senderid = "";
    String Receiverid = "";
    String Receiver_name="";
    String Receiver_pic="null",school_id="";
    public static String token="null";

    EditText message;
    private RequestQueue requestQueue;
    private DatabaseReference Adduser_to_inbox;

    private DatabaseReference mchatRef_reteriving;
    private DatabaseReference send_typing_indication;
    private DatabaseReference receive_typing_indication;
    RecyclerView chatrecyclerview;
    TextView user_name;
    private List<Chat_GetSet> mChats=new ArrayList<>();
    ChatAdapter mAdapter;
    ProgressBar p_bar;

    Query query_getchat;
    Query my_block_status_query;
    Query other_block_status_query;
    boolean is_user_already_block=false;

    ImageView profileimage;
    public static String senderid_for_check_notification="";
    public static String uploading_image_id="none";

    private ProcessingDialog dialog;
    Context context;
    View view;
    LinearLayout gif_layout;
    ImageButton upload_gif_btn;
    ImageView sendbtn;
    ImageButton alert_btn;

    public static String uploading_Audio_id="none";

    ImageButton mic_btn;

    File direct;

    SendAudio sendAudio;
    String type="";

    Map<String, String> map;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type=getArguments().getString("type");
        getToken();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_group_chat__fragmnet, container, false);

        context=getContext();
        dialog=new ProcessingDialog(getActivity());

        direct = new File(Environment.getExternalStorageDirectory() +"/Binder/");
        map=new HashMap<>();
        map=new SesssionManager(getActivity()).getUserDetails();
        senderid = map.get(ID);
        rootref = FirebaseDatabase.getInstance().getReference();
        Adduser_to_inbox= FirebaseDatabase.getInstance().getReference();
        message =view.findViewById(R.id.msgedittext);

        user_name=view.findViewById(R.id.username);
        user_name.setOnClickListener(this);
        Bundle bundle = getArguments();
        if (bundle != null) {

            school_id= bundle.getString("school_id");
            Receiverid = bundle.getString("user_id");
            Receiver_name = bundle.getString("name");
            Receiver_pic = bundle.getString("image");
            user_name.setText(Receiver_name);
            senderid_for_check_notification = Receiverid;

            try {
                Picasso.with(getActivity()).load(Receiver_pic)
                        .resize(100, 100)
                        .into(profileimage);
            } catch (IllegalArgumentException e) {

            }
        }
        else
        {

        }


            sendAudio =new SendAudio(context,message,rootref,Adduser_to_inbox,
                    senderid,Receiverid,Receiver_name,Receiver_pic,school_id);

            requestQueue= Volley.newRequestQueue(getContext());







        p_bar=view.findViewById(R.id.progress_bar);
        // this is the black color loader that we see whan we click on save button

        //set layout manager to chat recycler view and get all the privous chat of th user which spacifc user
        chatrecyclerview = view.findViewById(R.id.chatlist);
        final LinearLayoutManager layout = new LinearLayoutManager(context);
        //layout.setStackFromEnd(true);
        chatrecyclerview.setLayoutManager(layout);
        //chatrecyclerview.setHasFixedSize(false);
        // OverScrollDecoratorHelper.setUpOverScroll(chatrecyclerview, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        mAdapter = new ChatAdapter(mChats, senderid, context, new ChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chat_GetSet item, View v) {

                if(item.getType().equals("image"))
                    OpenfullsizeImage(item);

                if(v.getId()==R.id.audio_bubble){
                    RelativeLayout mainlayout=(RelativeLayout) v.getParent();

                    File fullpath = new File(Environment.getExternalStorageDirectory() +"/Binder/"+item.chat_id+".mp3");
                    if(fullpath.exists()) {

                        OpenAudio(fullpath.getAbsolutePath());

                    }else {
                        download_audio((ProgressBar) mainlayout.findViewById(R.id.p_bar),item);
                    }

                }


            }



        } ,new ChatAdapter.OnLongClickListener() {
            @Override
            public void onLongclick(Chat_GetSet item, View view) {
                if (view.getId() == R.id.msgtxt) {
                    if(senderid.equals(item.getSender_id()) && istodaymessage(item.getTimestamp()))
                        delete_Message(item);
                } else if (view.getId() == R.id.chatimage) {
                    if(senderid.equals(item.getSender_id()) && istodaymessage(item.getTimestamp()))
                        delete_Message(item);
                }else if (view.getId() == R.id.audio_bubble) {
                    if(senderid.equals(item.getSender_id()) && istodaymessage(item.getTimestamp()))
                        delete_Message(item);
                }
            }
        });

        chatrecyclerview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        chatrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean userScrolled;
            int scrollOutitems;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollOutitems = layout.findFirstCompletelyVisibleItemPosition();

                if (userScrolled && (scrollOutitems == 0 && mChats.size()>9)) {
                    userScrolled = false;
                    dialog.show("Loading");
                    rootref.child("chat").child(school_id).child(senderid + "-" + Receiverid).orderByChild("chat_id")
                            .endAt(mChats.get(0).getChat_id()).limitToLast(20)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList<Chat_GetSet> arrayList=new ArrayList<>();
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Chat_GetSet item=snapshot.getValue(Chat_GetSet.class);
                                        arrayList.add(item);
                                    }
                                    for (int i=arrayList.size()-2; i>=0; i-- ){
                                        mChats.add(0,arrayList.get(i));
                                        Log.d("mesgsisi",mChats.get(i).getText());
                                    }

                                    mAdapter.notifyDataSetChanged();
                                    dialog.dismiss();

                                    if(arrayList.size()>8){
                                        chatrecyclerview.scrollToPosition(arrayList.size());
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            }
        });


        gif_layout=view.findViewById(R.id.gif_layout);
        sendbtn =view.findViewById(R.id.sendbtn);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(message.getText().toString())){
                    if(gif_layout.getVisibility()== View.VISIBLE){
                        searchGif(message.getText().toString());
                    }else {
                        SendMessage(message.getText().toString());
                        //sendMessage(message.getText().toString());
                        SendSeeeMessage();
                        message.setText("");
                    }

                }
                else
                {
                    Toast.makeText(context, "Message are empty!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.findViewById(R.id.uploadimagebtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        upload_gif_btn= view.findViewById(R.id.upload_gif_btn);
        upload_gif_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gif_layout.getVisibility()== View.VISIBLE){
                    slideDown();
                }else{
                    slideUp();
                    GetGipy();
                }
            }
        });



        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Functions.hideSoftKeyboard(getActivity());
                getActivity().onBackPressed();
            }
        });

        message.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    SendTypingIndicator(false);
                }
            }
        });


        // this is the message field event lister which tells the second user either the user is typing or not
        // most importent to show type indicator to second user
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count==0){
                    SendTypingIndicator(false);
                }
                else {
                    SendTypingIndicator(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // this the mic touch listener
        // when our touch action is Down is will start recording and when our Touch action is Up
        // it will stop the recording
        mic_btn= (ImageButton) view.findViewById(R.id.mic_btn);
        final long[] touchtime = {System.currentTimeMillis()};
        mic_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    if(check_Recordpermission() && check_writeStoragepermission()) {
                        touchtime[0] = System.currentTimeMillis();
                        sendAudio.Runbeep("start");
                        Toast.makeText(context, "Recording...", Toast.LENGTH_SHORT).show();
                    }

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (touchtime[0] + 4000 > System.currentTimeMillis()) {
                        sendAudio.stop_timer();
                        Toast.makeText(context, "Hold Mic Button to Record", Toast.LENGTH_SHORT).show();
                    } else {
                        sendAudio.stopRecording();
                        Toast.makeText(context, "Stop Recording...", Toast.LENGTH_SHORT).show();
                    }

                }
                return false;
            }

        });

        //this method receiver the type indicator of second user to tell that his friend is typing or not
        ReceivetypeIndication();



       // alert_btn=view.findViewById(R.id.alert_btn);


     /*   alert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                block_user_dialog();
            }
        });*/



        getChat_data();

        return view;
    }



    ValueEventListener valueEventListener;

    ChildEventListener eventListener;

    ValueEventListener my_inbox_listener;

    ValueEventListener other_inbox_listener;

    public void getChat_data() {
        mChats.clear();
        mchatRef_reteriving = FirebaseDatabase.getInstance().getReference();
        query_getchat = mchatRef_reteriving.child("chat").child(school_id).child(senderid + "-" + Receiverid);
        Log.d("dwdwdd",senderid + "-" + Receiverid);

        my_block_status_query =mchatRef_reteriving.child("Inbox")
                .child(school_id)
                .child(map.get(ID))
                .child(Receiverid);

        other_block_status_query=mchatRef_reteriving.child("Inbox")
                .child(school_id)
                .child(Receiverid)
                .child(map.get(ID));




        // this will get all the messages between two users
        eventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    Chat_GetSet model = dataSnapshot.getValue(Chat_GetSet.class);
                    mChats.add(model);
                    Log.d("sdsvsvv",model.getText());
                    mAdapter.notifyDataSetChanged();
                    chatrecyclerview.scrollToPosition(mChats.size() - 1);
                }
                catch (Exception ex) {
                    Log.e("caafdfdgdfsd", ex.getMessage());
                }
                ChangeStatus();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    try {
                        Chat_GetSet model = dataSnapshot.getValue(Chat_GetSet.class);

                        for (int i=mChats.size()-1;i>=0;i--){
                            if(mChats.get(i).getTimestamp().equals(dataSnapshot.child("timestamp").getValue())){
                                mChats.remove(i);
                                mChats.add(i,model);
                                break;
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    catch (Exception ex) {
                        Log.e("", ex.getMessage());
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("gfghharre", databaseError.getMessage());
            }
        };


        // this will check the two user are do chat before or not
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(senderid + "-" + Receiverid)){
                    p_bar.setVisibility(View.GONE);
                    query_getchat.removeEventListener(valueEventListener);
                }
                else {
                    p_bar.setVisibility(View.GONE);
                    query_getchat.removeEventListener(valueEventListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        //this will check the block status of user which is open the chat. to know either i am blocked or not
        // if i am block then the bottom Writechat layout will be invisible
        my_inbox_listener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.child("block").getValue()!=null){
                    String block=dataSnapshot.child("block").getValue().toString();
                    if(block.equals("1")){
                        view.findViewById(R.id.writechatlayout).setVisibility(View.INVISIBLE);
                    }else {
                        view.findViewById(R.id.writechatlayout).setVisibility(View.VISIBLE);
                    }
                }else {
                    view.findViewById(R.id.writechatlayout).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        // this will check the block status of other user and according to them the block status dialog's option will be change
        other_inbox_listener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.child("block").getValue()!=null){
                    String block=dataSnapshot.child("block").getValue().toString();
                    if(block.equals("1")){
                        is_user_already_block=true;
                    }else {
                        is_user_already_block=false;
                    }
                }else {
                    is_user_already_block=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        query_getchat.limitToLast(20).addChildEventListener(eventListener);
        mchatRef_reteriving.child("chat").addValueEventListener(valueEventListener);

        my_block_status_query.addValueEventListener(my_inbox_listener);
        other_block_status_query.addValueEventListener(other_inbox_listener);
    }



    // this will add the new message in chat node and update the ChatInbox by new message by present date
    public void SendMessage(final String message) {
        Date c = Calendar.getInstance().getTime();
        final String formattedDate = GetData.df.format(c);
        final String current_user_ref = "chat" +"/"+school_id+ "/" + senderid + "-" + Receiverid;
        final String chat_user_ref = "chat"  +"/"+school_id+ "/" + Receiverid + "-" + senderid;

        DatabaseReference reference = rootref.child("chat").child(school_id).child(senderid + "-" + Receiverid).push();
        final String pushid = reference.getKey();

        final HashMap message_user_map = new HashMap<>();
        message_user_map.put("receiver_id", Receiverid);
        message_user_map.put("sender_id", senderid);
        message_user_map.put("chat_id",pushid);
        message_user_map.put("text", message);
        message_user_map.put("type","text");
        message_user_map.put("pic_url",BaseUrl.profile_image+map.get(IMAGE));
        message_user_map.put("status", "0");
        message_user_map.put("time", formattedDate);
        message_user_map.put("sender_name",map.get(NAME));
        message_user_map.put("timestamp", formattedDate);

        final HashMap user_map = new HashMap<>();
        user_map.put(current_user_ref + "/" + pushid, message_user_map);
        user_map.put(chat_user_ref + "/" + pushid, message_user_map);

        Log.d("message_user_map",message_user_map.toString());
        rootref.updateChildren(user_map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                //if first message then set the visibility of whoops layout gone
                String inbox_sender_ref = "Inbox" +"/"+school_id+ "/" + senderid + "/" + Receiverid;
                String inbox_receiver_ref = "Inbox" +"/"+school_id+ "/" + Receiverid + "/" + senderid;

                HashMap sendermap=new HashMap<>();
                sendermap.put("rid",senderid);
                sendermap.put("name",map.get(NAME));
                sendermap.put("pic",BaseUrl.profile_image+map.get(IMAGE));
                sendermap.put("msg",message);
                sendermap.put("status","0");
                sendermap.put("type",type);
                sendermap.put("timestamp",-1* System.currentTimeMillis());
                sendermap.put("date",formattedDate);
                sendermap.put("token_sender",token);
                sendermap.put("token_reciever",BaseUrl.STENO_TOKEN);
                Log.d("sendermap",sendermap.toString());

                HashMap receivermap=new HashMap<>();
                receivermap.put("rid",Receiverid);
                receivermap.put("name",Receiver_name);
                receivermap.put("pic",Receiver_pic);
                receivermap.put("msg",message);
                receivermap.put("status","1");
                receivermap.put("type",type);
                receivermap.put("token_sender",token);
                receivermap.put("token_reciever",BaseUrl.STENO_TOKEN);
                receivermap.put("timestamp", -1* System.currentTimeMillis());
                receivermap.put("date",formattedDate);
                Log.d("sendermap",receivermap.toString());
                HashMap both_user_map = new HashMap<>();
                both_user_map.put(inbox_sender_ref , receivermap);
                both_user_map.put(inbox_receiver_ref , sendermap);
                Adduser_to_inbox.updateChildren(both_user_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        GroupChat_Fragmnet.SendPushNotification(message,BaseUrl.STENO_TOKEN);

                    }
                });
            }
        });
    }


    public void SendSeeeMessage() {
        Date c = Calendar.getInstance().getTime();
        final String formattedDate = GetData.df.format(c);
        final String current_user_ref = "Seen" +"/"+school_id+ "/"+Receiverid;
        DatabaseReference reference = rootref.child("Seen").child(school_id).child(Receiverid).push();
        final String pushid = reference.getKey();

        final HashMap message_user_map = new HashMap<>();
        message_user_map.put("status", "0");

        final HashMap user_map = new HashMap<>();
        user_map.put(current_user_ref + "/" + pushid, message_user_map);

        rootref.updateChildren(user_map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

            }
        });
    }


    // this method will upload the image in chhat
    public void UploadImage(ByteArrayOutputStream byteArrayOutputStream){
        byte[] data = byteArrayOutputStream.toByteArray();
        Date c = Calendar.getInstance().getTime();
        final String formattedDate = GetData.df.format(c);

        StorageReference reference= FirebaseStorage.getInstance().getReference();
        DatabaseReference dref=rootref.child("chat").child(school_id).child(senderid+"-"+Receiverid).push();
        final String key=dref.getKey();
        uploading_image_id=key;
        final String current_user_ref = "chat" + "/" +school_id+ "/" + senderid + "-" + Receiverid;
        final String chat_user_ref = "chat" + "/" +school_id+ "/" + Receiverid + "-" + senderid;

        HashMap my_dummi_pic_map = new HashMap<>();
        my_dummi_pic_map.put("receiver_id", Receiverid);
        my_dummi_pic_map.put("sender_id", senderid);
        my_dummi_pic_map.put("chat_id",key);
        my_dummi_pic_map.put("text", "");
        my_dummi_pic_map.put("type","image");
        my_dummi_pic_map.put("pic_url","none");
        my_dummi_pic_map.put("status", "0");
        my_dummi_pic_map.put("time", "");
        my_dummi_pic_map.put("sender_name", map.get(NAME));
        my_dummi_pic_map.put("timestamp", formattedDate);

        HashMap dummy_push = new HashMap<>();
        dummy_push.put(current_user_ref + "/" + key, my_dummi_pic_map);
        rootref.updateChildren(dummy_push);

        final StorageReference imagepath= reference.child("images").child(school_id).child(key+".jpg");
        imagepath.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imagepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        uploading_image_id="none";
                        HashMap message_user_map = new HashMap<>();
                        message_user_map.put("receiver_id", Receiverid);
                        message_user_map.put("sender_id", senderid);
                        message_user_map.put("chat_id",key);
                        message_user_map.put("text", "");
                        message_user_map.put("type","image");
                        message_user_map.put("pic_url",uri.toString());
                        message_user_map.put("status", "0");
                        message_user_map.put("time", "");
                        message_user_map.put("sender_name",  map.get(BaseUrl.NAME));
                        message_user_map.put("timestamp", formattedDate);

                        HashMap user_map = new HashMap<>();

                        user_map.put(current_user_ref + "/" + key, message_user_map);
                        user_map.put(chat_user_ref + "/" + key, message_user_map);

                        rootref.updateChildren(user_map, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                String inbox_sender_ref = "Inbox" +  "/" +school_id+ "/" + senderid + "/" + Receiverid;
                                String inbox_receiver_ref = "Inbox" + "/" +school_id +"/" + Receiverid + "/" + senderid;

                                HashMap sendermap=new HashMap<>();
                                sendermap.put("rid",senderid);
                                sendermap.put("name", map.get(BaseUrl.NAME));
                                sendermap.put("pic", BaseUrl.profile_image+map.get(IMAGE));
                                sendermap.put("msg","Send an image...");
                                sendermap.put("status","0");
                                sendermap.put("type",type);
                                sendermap.put("timestamp", -1* System.currentTimeMillis());
                                sendermap.put("date",formattedDate);

                                HashMap receivermap=new HashMap<>();
                                receivermap.put("rid",Receiverid);
                                receivermap.put("name",Receiver_name);
                                receivermap.put("pic",Receiver_pic);
                                receivermap.put("type",type);
                                receivermap.put("msg","Send an image...");
                                receivermap.put("status","1");
                                receivermap.put("timestamp", -1* System.currentTimeMillis());
                                receivermap.put("date",formattedDate);

                                HashMap both_user_map = new HashMap<>();
                                both_user_map.put(inbox_sender_ref , receivermap);
                                both_user_map.put(inbox_receiver_ref , sendermap);

                                Adduser_to_inbox.updateChildren(both_user_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        GroupChat_Fragmnet.SendPushNotification(token,BaseUrl.STENO_TOKEN);

                                    }
                                });


                            }
                        });


                    }
                });

            }
        });
    }


    // this method will upload the image in chhat
    public void SendGif(String url){
        Date c = Calendar.getInstance().getTime();
        final String formattedDate = GetData.df.format(c);


        DatabaseReference dref=rootref.child("chat").child(school_id).child(senderid+"-"+Receiverid).push();
        final String key=dref.getKey();

        String current_user_ref = "chat" + "/" + senderid + "-" + Receiverid;
        String chat_user_ref = "chat" + "/" + Receiverid + "-" + senderid;

        HashMap message_user_map = new HashMap<>();
        message_user_map.put("receiver_id", Receiverid);
        message_user_map.put("sender_id", senderid);
        message_user_map.put("chat_id",key);
        message_user_map.put("text", "");
        message_user_map.put("type","gif");
        message_user_map.put("pic_url",url);
        message_user_map.put("status", "0");
        message_user_map.put("time", "");
        message_user_map.put("sender_name",map.get(BaseUrl.NAME));
        message_user_map.put("timestamp", formattedDate);
        HashMap user_map = new HashMap<>();

        user_map.put(current_user_ref + "/" + key, message_user_map);
        user_map.put(chat_user_ref + "/" + key, message_user_map);

        rootref.updateChildren(user_map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String inbox_sender_ref = "Inbox"+ "/" +school_id+ "/" + senderid + "/" + Receiverid;
                String inbox_receiver_ref = "Inbox" + "/" +school_id+ "/" + Receiverid + "/" + senderid;


                HashMap sendermap=new HashMap<>();
                sendermap.put("rid",senderid);
                sendermap.put("name",map.get(BaseUrl.NAME));
                sendermap.put("pic",BaseUrl.profile_image+map.get(IMAGE));
                sendermap.put("msg","Send an gif image...");
                sendermap.put("status","0");
                sendermap.put("timestamp", -1* System.currentTimeMillis());
                sendermap.put("date",formattedDate);

                HashMap receivermap=new HashMap<>();
                receivermap.put("rid",Receiverid);
                receivermap.put("name",Receiver_name);
                receivermap.put("pic",Receiver_pic);
                receivermap.put("msg","Send an gif image...");
                receivermap.put("status","1");
                receivermap.put("timestamp", -1* System.currentTimeMillis());
                receivermap.put("date",formattedDate);

                HashMap both_user_map = new HashMap<>();
                both_user_map.put(inbox_sender_ref , receivermap);
                both_user_map.put(inbox_receiver_ref , sendermap);

                Adduser_to_inbox.updateChildren(both_user_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        GroupChat_Fragmnet.SendPushNotification(token,BaseUrl.STENO_TOKEN);

                    }
                });

            }
        });
    }





    // this method will change the status to ensure that
    // user is seen all the message or not (in both chat node and Chatinbox node)
    public void ChangeStatus(){
        final Date c = Calendar.getInstance().getTime();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final Query query1 = reference.child("chat").child(school_id).child(Receiverid+"-"+senderid).orderByChild("status").equalTo("0");
        final Query query2 = reference.child("chat").child(school_id).child(senderid+"-"+Receiverid).orderByChild("status").equalTo("0");
        final Query seen = reference.child("Seen").child(school_id).child(senderid).orderByChild("status").equalTo("0");
        final DatabaseReference inbox_change_status_1=reference.child("Inbox").child(school_id).child(senderid+"/"+Receiverid);
        final DatabaseReference inbox_change_status_2=reference.child("Inbox").child(school_id).child(Receiverid+"/"+senderid);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot nodeDataSnapshot : dataSnapshot.getChildren()) {
                    if(!nodeDataSnapshot.child("sender_id").getValue().equals(senderid)){
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        String path = "chat" + "/" +school_id+"/"+ dataSnapshot.getKey() + "/" + key;
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("status", "1");
                        result.put("time",GetData.df2.format(c));
                        reference.child(path).updateChildren(result);

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot nodeDataSnapshot : dataSnapshot.getChildren()) {
                    if(!nodeDataSnapshot.child("sender_id").getValue().equals(senderid)){
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        String path = "chat" +"/"+school_id+ "/" + dataSnapshot.getKey() + "/" + key;
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("status", "1");
                        result.put("time",GetData.df2.format(c));
                        reference.child(path).updateChildren(result);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        seen.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot nodeDataSnapshot : dataSnapshot.getChildren()) {
                    String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                    String path = "Seen" +"/"+school_id+ "/" + dataSnapshot.getKey() + "/" + key;
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("status", "1");
                    reference.child(path).updateChildren(result);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        inbox_change_status_1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    if(dataSnapshot.exists()){
                        if(dataSnapshot.child("rid").getValue().equals(Receiverid)){
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("status", "1");
                            inbox_change_status_1.updateChildren(result);

                        }

                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        inbox_change_status_2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("rid").getValue().equals(Receiverid)){
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("status", "1");
                        inbox_change_status_2.updateChildren(result);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void GetUnseen(){

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final Query query1 = reference.child("chat").child(school_id).child(senderid+"-"+Receiverid).orderByChild("status").equalTo("0");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("unseencount", String.valueOf(dataSnapshot.getChildrenCount()));
                updateSeen(Receiverid, String.valueOf(dataSnapshot.getChildrenCount()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    /*    inbox_change_status_1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    if(dataSnapshot.exists()){
                        if(dataSnapshot.child("rid").getValue().equals(Receiverid)){
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("status", "1");
                            inbox_change_status_1.updateChildren(result);

                        }

                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
    private void updateSeen(String reverid, String count)
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Seen").child(school_id).child(reverid);
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("count",count);
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {


                }
            }
        });

    }


    public void download_audio(final ProgressBar p_bar, Chat_GetSet item){
        p_bar.setVisibility(View.VISIBLE);
        int downloadId = PRDownloader.download(item.getPic_url(), direct.getPath(), item.getChat_id()+".mp3")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        p_bar.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Error error) {

                    }


                });

    }

    //this method will get the big size of image in private chat
    public void OpenAudio(String path){
        Play_Audio_F play_audio_f = new Play_Audio_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("path", path);
        play_audio_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.add(R.id.drawer_layout, play_audio_f).commit();

    }





    // this is the delete message diloge which will show after long press in chat message
    private void delete_Message(final Chat_GetSet chat_getSet) {

        final CharSequence[] options = { "Delete this message","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);

        builder.setTitle(null);

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Delete this message"))

                {
                    update_message(chat_getSet);

                }


                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }


    // we will update the privious message means we will tells the other user that we have seen your message
    public void update_message(Chat_GetSet item){
        final String current_user_ref = "chat" +"/"+school_id+ "/" + senderid + "-" + Receiverid;
        final String chat_user_ref = "chat" +"/"+school_id+ "/" + Receiverid + "-" + senderid;


        final HashMap message_user_map = new HashMap<>();
        message_user_map.put("receiver_id", item.getReceiver_id());
        message_user_map.put("sender_id", item.getSender_id());
        message_user_map.put("chat_id",item.getChat_id());
        message_user_map.put("text", "Delete this message");
        message_user_map.put("type","delete");
        message_user_map.put("pic_url","");
        message_user_map.put("status", "0");
        message_user_map.put("time", "");
        message_user_map.put("sender_name", map.get(BaseUrl.NAME));
        message_user_map.put("timestamp", item.getTimestamp());

        final HashMap user_map = new HashMap<>();
        user_map.put(current_user_ref + "/" + item.getChat_id(), message_user_map);
        user_map.put(chat_user_ref + "/" + item.getChat_id(), message_user_map);

        rootref.updateChildren(user_map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

            }
        });

    }



    // this is the block dialog which will be show when user click on alert buttom of Top right in screen
    private void block_user_dialog() {
        final CharSequence[] options;
        if(is_user_already_block)
            options = new CharSequence[]{"Unblock this User","Clear all","Cancel"};
        else
            options = new CharSequence[]{"Block this User","Clear all","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);

        builder.setTitle(null);

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if(options[item].equals("Block this User")){

                    Block_user();
                }
                else if(options[item].equals("Unblock this User")){

                    UnBlock_user();
                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }
                else if (options[item].equals("Clear all")) {

                    FirebaseDatabase.getInstance().getReference("chat").child(senderid+"-"+Receiverid).removeValue();
                    dialog.dismiss();
                    mChats.clear();
                    mAdapter.notifyDataSetChanged();


                }

            }

        });

        builder.show();

    }


    public void Block_user(){
        rootref.child("Inbox")
                .child(school_id)
                .child(Receiverid)
                .child(map.get(ID)).child("block").setValue("1");
        Toast.makeText(context, "User Blocked", Toast.LENGTH_SHORT).show();

    }

    public void UnBlock_user(){
        rootref.child("Inbox")
                .child(school_id)
                .child(Receiverid)
                .child(map.get(ID)).child("block").setValue("0");
        Toast.makeText(context, "User UnBlocked", Toast.LENGTH_SHORT).show();

    }



    // we will delete only the today message so it is important to check the given message is the today message or not
    // if the given message is the today message then we will delete the message
    public boolean istodaymessage(String date) {
        Calendar cal = Calendar.getInstance();
        int today_day = cal.get(Calendar.DAY_OF_MONTH);
        //current date in millisecond
        long currenttime = System.currentTimeMillis();

        //database date in millisecond
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        long databasedate = 0;
        Date d = null;
        try {
            d = f.parse(date);
            databasedate = d.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference = currenttime - databasedate;
        if (difference < 86400000) {
            int chatday = Integer.parseInt(date.substring(0, 2));
            if (today_day == chatday)
                return true;
            else
                return false;
        }

        return false;
    }


    // this method will show the dialog of selete the either take a picture form camera or pick the image from gallary
    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };



        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {
                    if(check_camrapermission())
                        openCameraIntent();

                }

                else if (options[item].equals("Choose from Gallery"))

                {

                    if(check_ReadStoragepermission()) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }


    // below tis the four types of permission
    //get the permission to record audio
    public boolean check_Recordpermission(){

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    GetData.permission_Recording_audio);
        }
        return false;
    }

    private boolean check_camrapermission() {

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA}, GetData.permission_camera_code);
        }
        return false;
    }

    private boolean check_ReadStoragepermission(){
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            try {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        GetData.permission_Read_data );
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return false;
    }

    private boolean check_writeStoragepermission(){
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            try {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        GetData.permission_write_data );
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == GetData.permission_camera_code) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Tap again", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();

            }
        }

        if (requestCode == GetData.permission_Read_data) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Tap again", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == GetData.permission_write_data) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Tap Again", Toast.LENGTH_SHORT).show();
            }
        }


        if (requestCode == GetData.permission_Recording_audio) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Tap Again", Toast.LENGTH_SHORT).show();
            }
        }


    }



    // below three method is related with taking the picture from camera
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName()+".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, 1);
            }
        }
    }

    String imageFilePath;
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public String getPath(Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }




    //on image select activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                Matrix matrix = new Matrix();
                try {
                    ExifInterface exif = new ExifInterface(imageFilePath);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.postRotate(90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.postRotate(180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.postRotate(270);
                            break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri selectedImage =(Uri.fromFile(new File(imageFilePath)));

                InputStream imageStream = null;
                try {
                    imageStream =getActivity().getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                UploadImage(baos);

            }

            else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream =getActivity().getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

                String path=getPath(selectedImage);
                Matrix matrix = new Matrix();
                ExifInterface exif = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    try {
                        exif = new ExifInterface(path);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                matrix.postRotate(90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                matrix.postRotate(180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                matrix.postRotate(270);
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                UploadImage(baos);

            }

        }

    }



    // send the type indicator if the user is typing message
    public void SendTypingIndicator(boolean indicate){
        // if the type incator is present then we remove it if not then we create the typing indicator
        if(indicate){
            final HashMap message_user_map = new HashMap<>();
            message_user_map.put("receiver_id", Receiverid);
            message_user_map.put("sender_id", senderid);

            send_typing_indication= FirebaseDatabase.getInstance().getReference().child("typing_indicator").child(school_id);
            send_typing_indication.child(senderid+"-"+Receiverid).setValue(message_user_map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    send_typing_indication.child(Receiverid+"-"+senderid).setValue(message_user_map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                }
            });
        }

        else {
            send_typing_indication= FirebaseDatabase.getInstance().getReference().child("typing_indicator").child(school_id);

            send_typing_indication.child(senderid+"-"+Receiverid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    send_typing_indication.child(Receiverid+"-"+senderid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {

                        }
                    });

                }
            });

        }

    }


    // receive the type indication to show that your friend is typing or not
    LinearLayout mainlayout;
    public void ReceivetypeIndication(){
        mainlayout=view.findViewById(R.id.typeindicator);

        receive_typing_indication= FirebaseDatabase.getInstance().getReference().child("typing_indicator").child(school_id);
        receive_typing_indication.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Receiverid+"-"+senderid).exists()){
                    String receiver= String.valueOf(dataSnapshot.child(Receiverid+"-"+senderid).child("sender_id").getValue());
                    if(receiver.equals(Receiverid)){
                        mainlayout.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    mainlayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    // on destory delete the typing indicator
    @Override
    public void onDestroy() {
        super.onDestroy();
        uploading_image_id="none";
        SendTypingIndicator(false);
        query_getchat.removeEventListener(eventListener);
        my_block_status_query.removeEventListener(my_inbox_listener);
        other_block_status_query.removeEventListener(other_inbox_listener);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        uploading_image_id="none";
        SendTypingIndicator(false);
        query_getchat.removeEventListener(eventListener);
        my_block_status_query.removeEventListener(my_inbox_listener);
        other_block_status_query.removeEventListener(other_inbox_listener);
    }


    //this method will get the big size of image in private chat
    public void OpenfullsizeImage(Chat_GetSet item){
        See_Full_Image_F see_image_f = new See_Full_Image_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putSerializable("image_url", item.getPic_url());
        args.putSerializable("chat_id", item.getChat_id());
        see_image_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.drawer_layout, see_image_f).commit();

    }


    Gif_Adapter gif_adapter;
    final ArrayList<String> url_list=new ArrayList<>();
    RecyclerView gips_list;
    GPHApi client = new GPHApiClient(GetData.gif_api_key1);
    public void GetGipy(){
        url_list.clear();
        gips_list=view.findViewById(R.id.gif_recylerview);
        gips_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
        gif_adapter=new Gif_Adapter(context, url_list, new Gif_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(String item) {
                SendGif(item);
                slideDown();
            }
        });
        gips_list.setAdapter(gif_adapter);

        client.trending(MediaType.gif, null, null, null,  new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                if (result == null) {
                    // Do what you want to do with the error
                } else {
                    if (result.getData() != null) {
                        for (Media gif : result.getData()) {

                            url_list.add(gif.getId());
                        }
                        gif_adapter.notifyDataSetChanged();

                    } else {
                        Log.e("giphy error", "No results found");
                    }
                }
            }
        });
    }


    // if we want to search the gif then this mehtod is immportaant
    public void searchGif(String search){
        /// Gif Search
        client.search(search, MediaType.gif, null, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                if (result == null) {
                    // Do what you want to do with the error
                } else {
                    if (result.getData() != null) {
                        url_list.clear();
                        for (Media gif : result.getData()) {
                            url_list.add(gif.getId());
                            gif_adapter.notifyDataSetChanged();
                        }
                        gips_list.smoothScrollToPosition(0);

                    } else {
                        Log.e("giphy error", "No results found");
                    }
                }
            }
        });
    }


    // slide the view from below itself to the current position
    public void slideUp(){
        message.setHint("Search Gifs");
        upload_gif_btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gif_image));
        gif_layout.setVisibility(View.VISIBLE);
        sendbtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_search));
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(700);
        animate.setFillAfter(true);
        gif_layout.startAnimation(animate);
    }


    // slide the view from its current position to below itself
    public void slideDown(){
        message.setHint("Type your message here...");
        message.setText("");
        upload_gif_btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gif_image_gray));
        sendbtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_send));
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(700);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                gif_layout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        gif_layout.startAnimation(animate);
    }



    // this mehtos the will add a node of notification in to database
    // then our firebase cloud function will listen node and send the notification to spacific user
    public static void SendPushNotification(String  messg,String recievertoen){
        RootModel rootModel = new RootModel(recievertoen, new NotificationModel("Chat Message", messg), new DataModel("Name", "30"));

        Log.d("dffefe","Data us: "+new Gson().toJson(rootModel)+"="+recievertoen);
        APIInterface apiService =  NotificationApiClient.getClient().create(APIInterface.class);
        retrofit2.Call<JsonObject> responseBodyCall = apiService.sendNotification(rootModel);

        responseBodyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.d("dffefe","Successfully"+new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                Log.d("dffefe","onFailure"+t.getMessage());
            }
        });





    }







    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.back)
        {
            getActivity().onBackPressed();
        }
    }


    private void sendMessage(String message)
    {


        long time= System.currentTimeMillis();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatRoomMessage").child(school_id);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", map.get(ID));
        hashMap.put("sender_name", "Abhinav shukla");
        hashMap.put("message", message);
        reference.child("101").push().setValue(hashMap);
        Log.d("messageis",hashMap.toString());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getToken()
    {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            // Log.w("sucesssss", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                       token = task.getResult().getToken();

                    }
                });
    }


}
