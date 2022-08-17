package abhi.example.hp.stenobano.chat;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.config.BaseUrl;
import abhi.example.hp.stenobano.databinding.FragmentInboxBinding;
import abhi.example.hp.stenobano.retrofit.APIClient;
import abhi.example.hp.stenobano.session.SesssionManager;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static abhi.example.hp.stenobano.config.BaseUrl.ID;
import static abhi.example.hp.stenobano.config.BaseUrl.NAME;
import static abhi.example.hp.stenobano.session.SesssionManager.IMAGE;


public class Inbox_F extends Fragment implements View.OnClickListener {



    Context context;

    public static List<ModelClass> classList,sectionList;
    Map<String, String> map;
    ArrayList<Inbox_Get_Set> inbox_arraylist;
    DatabaseReference root_ref;
    String class_id=null,classname=null,section=null,section_id=null;
    Inbox_Adapter inbox_adapter;

    private Spinner selectclass,selectsection;
    boolean isview_created=false;
    BottomSheetDialog mBottomSheetDialog=null;
    private FragmentInboxBinding binding;

    String senderid = "";
    String Receiverid = "";
    String Receiver_name="";
    String Receiver_pic="null",school_id="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classList=new ArrayList<>();
        sectionList=new ArrayList<>();

      /*  Receiverid = model_assignment.getClassId()+"-"+model_assignment.getSectionId();
        Receiver_name=model_assignment.getClass_name()+"-"+model_assignment.getSection_name();
        Receiver_pic="empty";*/
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_inbox, container, false);
        binding.toolbar.setVisibility(View.VISIBLE);
        context=getContext();
        map=new HashMap<>();
        map=new SesssionManager(getActivity()).getUserDetails();
        school_id=BaseUrl.SCHOOL_ID;
        root_ref= FirebaseDatabase.getInstance().getReference();
        inbox_arraylist=new ArrayList<>();

        LinearLayoutManager layout = new LinearLayoutManager(context);
        binding.inboxlist.setLayoutManager(layout);
        binding.inboxlist.setHasFixedSize(false);
        binding.back.setOnClickListener(this);
        binding.chatclick.setOnClickListener(this);
        isview_created=true;



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                binding.refresh.setRefreshing(false);
            }
        });
        getData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }



    ValueEventListener eventListener2;

    Query inbox_query;
    public void getData() {
        Log.d("useridddd",new SesssionManager(getActivity()).userID()+"="+school_id);
        inbox_query=root_ref.child("Inbox").child(school_id).child(new SesssionManager(getActivity()).userID());
        inbox_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inbox_arraylist.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        Inbox_Get_Set model = new Inbox_Get_Set();
                        model.setId(ds.getKey());
                        //model.setId(ds.child("rid").getValue().toString());
                        model.setName(ds.child("name").getValue().toString());
                        model.setMessage(ds.child("msg").getValue().toString());
                        model.setDate(ds.child("date").getValue().toString());
                        model.setTimestamp(ds.child("timestamp").getValue().toString());
                        model.setStatus(ds.child("status").getValue().toString());
                        model.setPicture(ds.child("pic").getValue().toString());
                        model.setType(ds.child("type").getValue().toString());
                        model.setToken_reciever(ds.child("type").getValue().toString());
                        inbox_arraylist.add(model);
                    }
                    catch (NullPointerException e)
                    {

                    }
                }


                if (inbox_arraylist.isEmpty()) {
                    //binding.noDataLayout.setVisibility(View.VISIBLE);
                }
                else {
                   // binding.noDataLayout.setVisibility(View.GONE);
                    // Collections.reverse(inbox_arraylist);
                    Collections.sort(inbox_arraylist, new Comparator<Inbox_Get_Set>()
                    {
                        @Override
                        public int compare(Inbox_Get_Set lhs, Inbox_Get_Set rhs) {

                            return Long.valueOf(lhs.getTimestamp()).compareTo(Long.valueOf(rhs.getTimestamp()));

                        }
                    });

                    inbox_adapter=new Inbox_Adapter(context, inbox_arraylist, new Inbox_Adapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Inbox_Get_Set item) {

                            // if user allow the stroage permission then we open the chat view
                            if(check_ReadStoragepermission())
                                chatFragment(item.getId(),item.getName(),item.getPicture(),item.getType());
                            Log.d("nameeee",item.getName());

                        }
                    }, new Inbox_Adapter.OnLongItemClickListener() {
                        @Override
                        public void onLongItemClick(Inbox_Get_Set item) {

                        }
                    });

                    binding.inboxlist.setAdapter(inbox_adapter);
                    inbox_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



    // on stop we will remove the listener
    @Override
    public void onStop() {
        super.onStop();
        if(inbox_query!=null)
            try {
                inbox_query.removeEventListener(eventListener2);
            }
            catch (NullPointerException e)
            {

            }

    }



    //open the chat fragment and on item click and pass your id and the other person id in which
    //you want to chat with them and this parameter is that is we move from match list or inbox list
    public void chatFragment(String receiverid, String name, String picture, String type){
        GroupChat_Fragmnet chat_activity = new GroupChat_Fragmnet();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("user_id", receiverid);
        args.putString("name",name);
        args.putString("image",picture);
        args.putString("type",type);
        args.putString("school_id",school_id);
        chat_activity.setArguments(args);
        transaction.addToBackStack(null);
        // transaction.replace(R.id.MainMenuFragment, chat_activity).commit();
        transaction.add(R.id.drawer_layout, chat_activity).commit();
    }



    //this method will check there is a storage permission given or not
    private boolean check_ReadStoragepermission(){
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED ){
            return true;
        }
        else {
            try {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        789);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return false;
    }




    @Override
    public void onClick(View view) {
     if (view.getId()==R.id.back)
        {
            getActivity().onBackPressed();
        }
     else if (view.getId()==R.id.chatclick)
     {
         chatclick();
     }

    }

    void chatclick()
    {
        GroupChat_Fragmnet frag = new GroupChat_Fragmnet();
        Bundle args=new Bundle();
        args.putString("type","0");
        args.putString("user_id", BaseUrl.STENO_MOBILE);
        args.putString("name", BaseUrl.STENO_NAME);
        args.putString("image", BaseUrl.STENO_IMAGE);
        args.putString("school_id", BaseUrl.SCHOOL_ID);
        Log.d("datsijhdj", "chatclick: "+args.toString());
        frag.setArguments(args);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_layout,frag,"Chat");
        transaction.addToBackStack(null);
        transaction.commit();
    }





}
