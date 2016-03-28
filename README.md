#调用系统应用选择联系人并返回联系人信息
##实现步骤:

 1. 用startActivityForResult的方式启动Intent
 2. onActivityResult获取返回的Uri信息
 3. 根据Uri查询联系人信息

##简单演示(因为是真机演示,所以截走了联系人的界面)
![这里写图片描述](http://img.blog.csdn.net/20160328104947063)


##1.启动联系人选择:
```
 Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
 //REQUEST_CODE为自定义的请求码
startActivityForResult(intent, REQUEST_CODE);
```
###2.获取返回的Uri
```
 @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Cursor cursor = managedQuery(uri, null, null, null, null);
                //查询联系人姓名的方法
                String contactName = getContactName(cursor);
                //查询联系人电话的方法
                String contactNumber = getContactNumbler(cursor);
        }
    }
```

###3.根据uri查询联系人信息
####获取联系人姓名:
```
private String getContactName(Cursor cursor) {
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        return name;
    }
```
###获取联系人电话:
```
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
```

