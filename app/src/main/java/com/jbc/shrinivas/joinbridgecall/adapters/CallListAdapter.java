package com.jbc.shrinivas.joinbridgecall.adapters;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jbc.shrinivas.joinbridgecall.R;
import com.jbc.shrinivas.joinbridgecall.helpers.DBHelper;
import com.jbc.shrinivas.joinbridgecall.utility.Constants;
import com.jbc.shrinivas.joinbridgecall.view.AddUpdateCallActivity;
import com.jbc.shrinivas.joinbridgecall.view.MainActivity;

import java.util.List;

import models.CallDetail;

/**
 * Created by shrinivas on 4/6/18.
 */

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.CallViewHolder> {

    private List<CallDetail> mCallList;
    private Context mContext;
    private int mPosition;
    MainActivity mActivity;

    public class CallViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public RelativeLayout relRow;

        public CallViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_call_name);
            relRow = (RelativeLayout) view.findViewById(R.id.rel_row);
        }
    }


    public CallListAdapter(List<CallDetail> callList, Context context) {

        this.mCallList = callList;
        this.mContext = context;
        mActivity = (MainActivity) context;
    }

    @Override
    public CallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bridge_call_row, parent, false);

        return new CallViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CallViewHolder holder, final int position) {
        final CallDetail callDetail = mCallList.get(position);
        holder.txtName.setText(callDetail.getName());
        holder.relRow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    Toast.makeText(mContext, "Please provide call permission. To allow please go to settings->apps->permissions.", Toast.LENGTH_LONG).show();
                } else {
                    //initiate call
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    if (mCallList.get(position).getBridgeNumber() == null || (mCallList.get(position).getBridgeNumber() != null && mCallList.get(position).getBridgeNumber().trim().length() == 0)) {
                        intent.setData(Uri.parse("tel:" + mCallList.get(position).getPhoneNumber() + ",,," + mCallList.get(position).getPasscode()));
                    } else {
                        intent.setData(Uri.parse("tel:" + mCallList.get(position).getPhoneNumber() + ",,," + mCallList.get(position).getBridgeNumber() + ",,," + mCallList.get(position).getPasscode()));
                    }

                    mContext.startActivity(intent);
                }

            }
        });
        holder.relRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPosition = position;
                showOptionsDialog();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCallList.size();
    }

    private void showOptionsDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Update");
        arrayAdapter.add("Delete");


        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);

                if (strName.equalsIgnoreCase("update")) {
                    Intent intent = new Intent(mContext, AddUpdateCallActivity.class);
                    intent.putExtra(Constants.KEY_IS_CALL_FOR_ADD, false);
                    intent.putExtra(Constants.KEY_CALL_DETAIL_ID, "" + mCallList.get(mPosition).getId());
                    mContext.startActivity(intent);
                } else {
                    DBHelper dBhelper = new DBHelper(mContext);
                    dBhelper.deleteCallDetail(mCallList.get(mPosition));
                    Toast.makeText(mContext, "Call detail successfully deleted!", Toast.LENGTH_SHORT).show();
                    mActivity.handleData();

                }
            }
        });
        builderSingle.show();
    }
}
