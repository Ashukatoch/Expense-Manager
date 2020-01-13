package com.example.expensemanager2;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.expensemanager2.Data.data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Incomefragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Incomefragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Incomefragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mauth;
    private DatabaseReference mIncomedatabase;
    private FirebaseUser muser;
    private String uid;

    private RecyclerView recyclerView;
    private TextView amt;
    private EditText eamount,etype,enote,edate;

    private Button update,delete;

    private String gnote,gtype,gpush_key,gdate;
    private int Amt;

    private OnFragmentInteractionListener mListener;
    public static Incomefragment newInstance(String param1, String param2) {
        Incomefragment fragment = new Incomefragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_incomefragment, container, false);
        mauth=FirebaseAuth.getInstance();
        muser=mauth.getCurrentUser();
        uid=muser.getUid();
        mIncomedatabase=FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        recyclerView=v.findViewById(R.id.income_rv);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        amt=v.findViewById(R.id.income_amount);
        mIncomedatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                float totalamount=0;
                for(DataSnapshot mydatasnapshot:dataSnapshot.getChildren())
                {
                    data d=mydatasnapshot.getValue(data.class);
                    totalamount+=d.getAmount();
                }
                amt.setText(String.valueOf(totalamount));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseRecyclerAdapter<data,MyViewHolder> adapter=new FirebaseRecyclerAdapter<data, MyViewHolder>(
                data.class,
                R.layout.income_recycler_data
                ,MyViewHolder.class
                ,mIncomedatabase
        ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final data model, final int position)
            {
                viewHolder.setamount(model.getAmount());
                viewHolder.settype(model.getType());
                viewHolder.setDate(model.getDate());
                viewHolder.setNote(model.getNote());
                viewHolder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        gpush_key=getRef(position).getKey();
                        gnote=model.getNote();
                        Amt=model.getAmount();
                        gtype=model.getType();

                        updatedialog();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        View v;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            v=itemView;
        }




        private void setDate(String Date)
        {
            TextView date=v.findViewById(R.id.date_income_rv);

            date.setText("Date :"+" "+Date);
        }
        private void setNote(String Note)
        {
            TextView note=v.findViewById(R.id.note_income_rv);
            note.setText("Note: "+" "+Note);
        }
        private void setamount(int Amount)
        {
            TextView amount=v.findViewById(R.id.amount_income_rv);
            amount.setText("Amount :"+" "+String.valueOf(Amount));
        }
        private void settype(String Type)
        {
            TextView type=v.findViewById(R.id.type_income_rv);
            type.setText("Type :"+" "+Type);
        }


    }
    private void updatedialog()
    {
        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View v=inflater.inflate(R.layout.update_custom_layout,null);
        mydialog.setView(v);
        final AlertDialog dialog=mydialog.create();
        dialog.show();
        eamount=v.findViewById(R.id.update_amount_et);
        enote=v.findViewById(R.id.update_note_et);
        etype=v.findViewById(R.id.update_type_et);
        update=v.findViewById(R.id.update_btn);
        delete=v.findViewById(R.id.delete_btn);

        eamount.setText(String.valueOf(Amt));
        eamount.setSelection(String.valueOf(Amt).length());

        enote.setText(gnote);
        enote.setSelection(gnote.length());

        etype.setText(gtype);
        etype.setSelection(gtype.length());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                 gtype=etype.getText().toString().trim();
                 gnote=enote.getText().toString().trim();
                 int myamount=Integer.parseInt(eamount.getText().toString().trim());
                 String gdate= DateFormat.getInstance().format(new Date());
                 data d=new data(myamount,gpush_key,gdate,gtype,gnote);
                 mIncomedatabase.child(gpush_key).setValue(d);
                 dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mIncomedatabase.child(gpush_key).removeValue();
                dialog.dismiss();
            }
        });

    }
}

