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


public class Expensefragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mauth;
    private DatabaseReference mExpensedatabse;
    private FirebaseUser muser;

    private RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    private EditText eamount,etype,enote;
    private Button update,delete;

    private String gtype,gnote,gdate,gpush_key;
    private int Amt;
    public static Expensefragment newInstance(String param1, String param2)
    {
        Expensefragment fragment = new Expensefragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_expensefragment, container, false);
        mauth=FirebaseAuth.getInstance();
        muser=mauth.getCurrentUser();
        String uid=muser.getUid();
        mExpensedatabse= FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);
        LinearLayoutManager linearlayoutmanager=new LinearLayoutManager(getActivity());
        linearlayoutmanager.setStackFromEnd(true);
        linearlayoutmanager.setReverseLayout(true);
        recyclerView=v.findViewById(R.id.expense_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearlayoutmanager);
        final TextView amount=v.findViewById(R.id.expense_amount);

        mExpensedatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                float totalamount = 0;
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    data d=dataSnapshot1.getValue(data.class);
                     totalamount+=d.getAmount();
                }
                amount.setText(String.valueOf(totalamount));

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

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<data,MyViewHolder> adapter=new FirebaseRecyclerAdapter<data, MyViewHolder>(
                data.class,
                R.layout.expense_recycler_data,
                MyViewHolder.class,
                mExpensedatabse

        ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, data model, int position)
            {
                viewHolder.setNote(model.getNote());
                viewHolder.setamount(model.getAmount());
                viewHolder.setDate(model.getDate());
                viewHolder.setType(model.getType());

                gtype=model.getType();
                gnote=model.getNote();
                Amt=model.getAmount();
                gpush_key=getRef(position).getKey();
                viewHolder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                     updatedialog();

                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
           View v;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
             v=itemView;
        }
        private void setDate(String Date)
        {
            TextView date=v.findViewById(R.id.expense_rv_date);
            date.setText("Date :"+" "+Date);
        }

        private void setamount(int Amount)
        {
            TextView date=v.findViewById(R.id.expense_rv_date);
            date.setText("Amount :"+" "+String.valueOf(Amount));
        }

        private void setType(String Type)
        {
            TextView type=v.findViewById(R.id.expense_rv_type);
            type.setText("Type :"+" "+Type);
        }
        private void setNote(String Note)
        {
            TextView date=v.findViewById(R.id.expense_rv_note);
            date.setText("Note :"+" "+Note);
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
                mExpensedatabse.child(gpush_key).setValue(d);
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mExpensedatabse.child(gpush_key).removeValue();
                dialog.dismiss();
            }
        });

    }
}
