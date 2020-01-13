package com.example.expensemanager2;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Dashfragment.OnFragmentInteractionListener , Incomefragment.OnFragmentInteractionListener,Expensefragment.OnFragmentInteractionListener {
private Toolbar toolbar;
private DrawerLayout drawerLayout;
private NavigationView navigationView;
private ActionBarDrawerToggle toggle;
private BottomNavigationView bottomNavigationView;
private FrameLayout frameLayout;
private Dashfragment dashfragment;
private Expensefragment expensefragment;
private Incomefragment incomefragment;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar=findViewById(R.id.mytoolbar);
        toolbar.setTitle("Expense Manager");
        setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.drawerlayout);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        dashfragment=new Dashfragment();
        expensefragment=new Expensefragment();
        incomefragment=new Incomefragment();
        setFragment(dashfragment);

        navigationView=findViewById(R.id.navview);
        navigationView.setNavigationItemSelectedListener(this);

        frameLayout=findViewById(R.id.main_frame);
        bottomNavigationView=findViewById(R.id.bottomnavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

          switch(menuItem.getItemId())
          {
              case R.id.dashboard:
                  setFragment(dashfragment);
                  bottomNavigationView.setItemBackgroundResource(R.color.dashcolor);
                  return true;
              case R.id.income:
                  setFragment(incomefragment);
                  bottomNavigationView.setItemBackgroundResource(R.color.inccolor);
                  return true;
              case R.id.expense:
                  setFragment(expensefragment);
                  bottomNavigationView.setItemBackgroundResource(R.color.expcolor);
                  return true;
              default:
                  return false;
          }


            }
        });
    }

     public void setFragment(Fragment fragment)
     {
       FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
       fragmentTransaction.replace(R.id.main_frame,fragment);
       fragmentTransaction.commit();
     }


    @Override
    public void onBackPressed()
    {
        if(drawerLayout.isDrawerOpen(GravityCompat.END))
        {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else
        super.onBackPressed();
    }
    public void displayselectedlistener(int itemid)
    {
        Fragment fragment=null;
        switch(itemid)
        {
            case R.id.dashboard:
            {
                setFragment(dashfragment);
                break;
            }
            case R.id.income:
            {
                setFragment(incomefragment);
                break;
            }
            case R.id.expense:
            {
                setFragment(expensefragment);
                break;
            }
        }
        if(fragment!=null)
        {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame,fragment);
            ft.commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        displayselectedlistener(menuItem.getItemId());
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
