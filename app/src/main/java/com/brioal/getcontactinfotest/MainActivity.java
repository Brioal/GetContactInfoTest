package com.brioal.getcontactinfotest;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 11;
    private TextView tv_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        tv_msg = (TextView) findViewById(R.id.main_msg);
    }

    public void chooseContact(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = managedQuery(uri, null, null, null, null);
            String contactName = getContactName(cursor);
            String contactNumbers = getContactNumbler(cursor);
            tv_msg.setText(contactName + "\n" + contactNumbers);
        }

    }

    private String getContactNumbler(Cursor cursor) {
        StringBuilder builder = new StringBuilder();
        //获取所选联系人的电话的个数
        int count = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        if (count > 0) { // 存在电话
            //获取联系人的id
            int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //根据id查询电话
            Cursor phoneCursor = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);
            if (phoneCursor.moveToFirst()) {
                String numbler = "";
                do {
                    numbler = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    builder.append(numbler+"\n");
                } while (phoneCursor.moveToNext());
            }


        }

        return builder.toString();
    }

    private String getContactName(Cursor cursor) {
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


        return name;
    }
}
