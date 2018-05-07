package com.jbc.shrinivas.joinbridgecall.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.jbc.shrinivas.joinbridgecall.R;
import com.jbc.shrinivas.joinbridgecall.adapters.CallListAdapter;
import com.jbc.shrinivas.joinbridgecall.helpers.DBHelper;
import com.jbc.shrinivas.joinbridgecall.utility.Constants;

import models.CallDetail;

import static com.jbc.shrinivas.joinbridgecall.utility.Constants.PERMISSION_CALL;

public class MainActivity extends Activity implements OnRequestPermissionsResultCallback {
    private RecyclerView mRecyclerView;
    private List<CallDetail> mCallList = new ArrayList<>();
    private CallListAdapter mAdapter;
    DBHelper mDBHelper;
    TextView mTxtNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(MainActivity.this, AddUpdateCallActivity.class);
                intent.putExtra(Constants.KEY_IS_CALL_FOR_ADD, true);
                startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CALL);
        }
        handleData();


    }


    @Override
    protected void onResume() {
        handleData();
        super.onResume();
    }

    private void initUI() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rcl_callList);
        mTxtNoData = (TextView) findViewById(R.id.txt_nodata);
        mDBHelper = new DBHelper(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void handleData() {
        mCallList = mDBHelper.getAllCallDetails();
        mAdapter = new CallListAdapter(mCallList,this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        if (mCallList.size() == 0) {
            mTxtNoData.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mTxtNoData.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.PERMISSION_CALL) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted.

            } else {
                Toast.makeText(MainActivity.this, "Please provide call permission. To allow please go to settings->apps->permissions.", Toast.LENGTH_LONG).show();

            }
            return;
        }
    }


}
