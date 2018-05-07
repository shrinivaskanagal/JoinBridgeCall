package com.jbc.shrinivas.joinbridgecall.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jbc.shrinivas.joinbridgecall.R;
import com.jbc.shrinivas.joinbridgecall.helpers.DBHelper;
import com.jbc.shrinivas.joinbridgecall.utility.Constants;

import models.CallDetail;

/**
 * Created by shrinivas on 4/6/18.
 */

public class AddUpdateCallActivity extends AppCompatActivity implements View.OnClickListener {
    private DBHelper mDBHelper;
    private boolean mIsCallForAdd;
    private CallDetail mCallDetail;
    private int recordId;
    private EditText mEdtName, mEdtPhoneNumber, mEdtBridgeNumber, mEdtPassCode;
    private Button mBtnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_details_update);
        mDBHelper = new DBHelper(this);
        Intent intent = getIntent();
        mIsCallForAdd = intent.getBooleanExtra(Constants.KEY_IS_CALL_FOR_ADD, true);
        if (!mIsCallForAdd) {
            try {
                recordId = Integer.parseInt(intent.getStringExtra(Constants.KEY_CALL_DETAIL_ID));
            } catch (Exception e) {
                recordId = 10000;
            }

            if (recordId == 10000) {
                //record id not present, consider this a new add
                mIsCallForAdd = true;
            } else {
                //fetch record details to edit
                mCallDetail = mDBHelper.getCallDetail(recordId);
            }
        }
        initView();

    }

    private void initView() {
        mEdtName = (EditText) findViewById(R.id.edt_name);
        mEdtPhoneNumber = (EditText) findViewById(R.id.edt_phone_number);
        mEdtBridgeNumber = (EditText) findViewById(R.id.edt_bridge_number);
        mEdtPassCode = (EditText) findViewById(R.id.edt_passcode);
        mBtnSubmit = (Button) findViewById(R.id.btn_add_update);
        mBtnSubmit.setOnClickListener(this);
        if (!mIsCallForAdd) {
            mEdtName.setText(mCallDetail.getName());
            mEdtPhoneNumber.setText(mCallDetail.getPhoneNumber());
            mEdtBridgeNumber.setText(mCallDetail.getBridgeNumber());
            mEdtPassCode.setText(mCallDetail.getPasscode());
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == mBtnSubmit.getId()) {
            //validate and submit the data
            if (isValidated()) {
                ProgressDialog pd = new ProgressDialog(this);
                pd.setMessage("Loading..");
                pd.show();
                mCallDetail = new CallDetail();
                mCallDetail.setName(mEdtName.getText().toString());
                mCallDetail.setPhoneNumber(mEdtPhoneNumber.getText().toString());
                mCallDetail.setBridgeNumber(mEdtBridgeNumber.getText().toString());
                mCallDetail.setPasscode(mEdtPassCode.getText().toString());
                if (mIsCallForAdd) {
                    if (mDBHelper.insertCallDetail(mCallDetail) == -1) {
                        pd.dismiss();
                        Toast.makeText(this, "Failed to Add. Please try again!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Call details added successfully!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        onBackPressed();
                    }
                } else {
                    mCallDetail.setId(recordId);
                    if (mDBHelper.updateCallDetail(mCallDetail) == 0) {
                        pd.dismiss();
                        Toast.makeText(this, "Failed to update. Please try again!", Toast.LENGTH_SHORT).show();
                    } else {
                        pd.dismiss();
                        Toast.makeText(this, "Call details updated successfully!", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }


            }
        }
    }

    private boolean isValidated() {
        boolean isValidated = true;
        if (mEdtName.getText().toString().trim().length() == 0) {
            isValidated = false;
        }
        if (mEdtPhoneNumber.getText().toString().trim().length() == 0) {
            isValidated = false;
        }
        if (mEdtPassCode.getText().toString().trim().length() == 0) {
            isValidated = false;
        }
        if (!isValidated) {
            Toast.makeText(this, "Please input proper data. All fields are mandatory except bridge/conference number field.", Toast.LENGTH_LONG).show();

        }
        return isValidated;
    }
}
