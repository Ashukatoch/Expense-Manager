package com.example.expensemanager2;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
 * {@link Dashfragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Dashfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashfragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView incomeamount;
    private TextView expenseamount;

    private boolean isopen=false;
    private TextView fab_income_tv,fab_expense_tv;
    private FloatingActionButton fab_income_btn,fab_expense_btn,fab_main_btn;

    private Animation fade_close,fade_open;
    private FirebaseAuth mauth;
    private FirebaseUser muser;
    private DatabaseReference mIncomedatabase;
    private DatabaseReference mExpencedatabase;

    private RecyclerView recyclerViewincome;
    private RecyclerView recyclerViewexpense;


    public static Dashfragment newInstance(String param1, String param2) {
        Dashfragment fragment = new Dashfragment();
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
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_dashfragment, container, false);
        incomeamount=v.findViewById(R.id.incomedatatextview);
        expenseamount=v.findViewById(R.id.expensedatatextview);

        fab_expense_btn=v.findViewById(R.id.expense_ft_btn);
        fab_expense_tv=v.findViewById(R.id.expense_ft_tv);
        fab_income_btn=v.findViewById(R.id.income_ft_btn);
        fab_income_tv=v.findViewById(R.id.income_ft_textview);
        fade_close= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);
        fade_open=AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        fab_main_btn=v.findViewById(R.id.main_ft_btn);

        mauth=FirebaseAuth.getInstance();
        muser=mauth.getCurrentUser();

        String uid=muser.getUid();
        mIncomedatabase= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpencedatabase=FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        recyclerViewincome=v.findViewById(R.id.dashboard_incomerecycler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewincome.setHasFixedSize(true);
        recyclerViewincome.setLayoutManager(linearLayoutManager);
        mIncomedatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                float totalincomeamount=0.00f;
                for(DataSnapshot mydatasnapshot:dataSnapshot.getChildren())
                {
                    data d=mydatasnapshot.getValue(data.class);
                    totalincomeamount+=d.getAmount();
                }
                incomeamount.setText(String.valueOf(totalincomeamount));


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mExpencedatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                float totalexpenseamount=0;
                for(DataSnapshot mydatasnapshot:dataSnapshot.getChildren())
                {
                    data d=mydatasnapshot.getValue(data.class);
                    totalexpenseamount+=d.getAmount();
                }
                expenseamount.setText(String.valueOf(totalexpenseamount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
         fab_main_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v)
             {
                 adddata();

                 if(isopen)
                 {
                     fab_income_btn.startAnimation(fade_close);
                     fab_expense_btn.startAnimation(fade_close);
                     fab_expense_btn.setClickable(false);
                     fab_income_btn.setClickable(false);
                     fab_income_tv.startAnimation(fade_close);
                     fab_expense_btn.startAnimation(fade_close);
                     fab_expense_tv.setClickable(false);
                     fab_income_tv.setClickable(false);
                     isopen=false;
                 }
                 else
                 {
                     fab_expense_btn.setClickable(true);
                     fab_income_btn.setClickable(true);
                     fab_income_btn.startAnimation(fade_open);
                     fab_expense_btn.startAnimation(fade_open);
                     fab_expense_tv.setClickable(true);
                     fab_income_tv.setClickable(true);
                     fab_income_tv.startAnimation(fade_open);
                     fab_expense_btn.startAnimation(fade_open);
                     isopen=true;
                 }

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
    public void adddata()
    {
       fab_income_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               IncomeDataInsert();
           }
       });
       fab_expense_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               ExpenseDataInsert();
           }
       });
    }
    public void IncomeDataInsert()
    {
        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflator=LayoutInflater.from(getActivity());
        View v=inflator.inflate(R.layout.custom_dialog_layout,null);
        mydialog.setView(v);
        final AlertDialog dialog=mydialog.create();
        dialog.setCancelable(false);
        final EditText amount=v.findViewById(R.id.amount_et);
        final EditText note=v.findViewById(R.id.note_et);
        final EditText type=v.findViewById(R.id.type_et);

        Button save=v.findViewById(R.id.save_btn);
        Button cancel=v.findViewById(R.id.cancel_btn);
        dialog.show();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                fadeanimation();
            String mtype=type.getText().toString().trim();
            String mnote=note.getText().toString().trim();
            String mamount=amount.getText().toString().trim();
            if(TextUtils.isEmpty(mnote))
            {
                note.setError("Required Field...");
                return;
            }

            if(TextUtils.isEmpty(mamount))
            {
                amount.setError("Required Field...");
                return;
            }

            if(TextUtils.isEmpty(mtype))
            {
                type.setError("Required Field...");
                return;
            }
                int ouramount=Integer.parseInt(mamount);
            String id=mIncomedatabase.push().getKey();
            String date= DateFormat.getInstance().format(new Date());
            data d=new data(ouramount,id,date,mtype,mnote);
            mIncomedatabase.child(id).setValue(d);
                Toast.makeText(getActivity(),"Data added",Toast.LENGTH_SHORT).show();
              dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                fadeanimation();
                dialog.dismiss();
            }
        });
    }

    public void ExpenseDataInsert()
    {
        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= LayoutInflater.from(getActivity());
        View v=inflater.inflate(R.layout.custom_dialog_layout,null);
        mydialog.setView(v);
        final AlertDialog dialog=mydialog.create();
        dialog.show();
        dialog.setCancelable(false);
        final EditText amount=v.findViewById(R.id.amount_et);
        final EditText note=v.findViewById(R.id.note_et);
        final EditText type=v.findViewById(R.id.type_et);

        Button save=v.findViewById(R.id.save_btn);
        Button cancel=v.findViewById(R.id.cancel_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               fadeanimation();

             String mamount=amount.getText().toString().trim();
             String mnote=note.getText().toString().trim();
             String mtype=type.getText().toString().trim();

                if(TextUtils.isEmpty(mnote))
                {
                    note.setError("Required Field...");
                    return;
                }

                if(TextUtils.isEmpty(mamount))
                {
                    amount.setError("Required Field...");
                    return;
                }

                if(TextUtils.isEmpty(mtype))
                {
                    type.setError("REquired Field...");
                    return;
                }
                int ouramount=Integer.parseInt(mamount);
                String date=DateFormat.getDateInstance().format(new Date());
                String id=mExpencedatabase.push().getKey();
                data d=new data(ouramount,id,date,mtype,mnote);
                mExpencedatabase.child(id).setValue(d);
                Toast.makeText(getActivity(), "Data Added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                fadeanimation();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<data,myViewHolder> adapter=new FirebaseRecyclerAdapter<data, myViewHolder>(
                data.class,
                R.layout.dashboard_recycler_data,
                myViewHolder.class,
                mIncomedatabase
        ) {
            @Override
            protected void populateViewHolder(myViewHolder viewHolder, data model, int position)
            {
                viewHolder.setRamount(model.getAmount());
                viewHolder.setRdate(model.getDate());
                viewHolder.setRtype(model.getType());
            }
        };
        recyclerViewincome.setAdapter(adapter);
    }

    public void fadeanimation()
    {
        fab_income_btn.startAnimation(fade_close);
        fab_expense_btn.startAnimation(fade_close);
        fab_expense_btn.setClickable(false);
        fab_income_btn.setClickable(false);
        fab_income_tv.startAnimation(fade_close);
        fab_expense_btn.startAnimation(fade_close);
        fab_expense_tv.setClickable(false);
        fab_income_tv.setClickable(false);
        isopen=false;
    }
    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        View v;
        public myViewHolder(@NonNull View itemView)
        {
            super(itemView);
            v=itemView;
        }



        private void setRdate(String Date)
        {
            TextView rdate=v.findViewById(R.id.Date_dashboard_tv);
            rdate.setText(Date);
        }
        private void setRamount(int Amount)
        {
            TextView ramount=v.findViewById(R.id.amount_dashboard_tv);
            ramount.setText(String.valueOf(Amount));
        }
        private void setRtype(String Type)
        {
            TextView rtype=v.findViewById(R.id.type_dashboard_tv);

            rtype.setText(Type);
        }


    }
}
