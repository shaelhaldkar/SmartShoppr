package com.sukritapp.smartshoppr.fragments;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.adapter.ContactAdapter;
import com.sukritapp.smartshoppr.database.LocalDataBase;
import com.sukritapp.smartshoppr.model.ContactModel;
import com.sukritapp.smartshoppr.sosservice.LockService;
import com.sukritapp.smartshoppr.sosservice.SmsDeliveredReceiver;
import com.sukritapp.smartshoppr.sosservice.SmsSentReceiver;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.RecyclerItemClickListener;
import com.sukritapp.smartshoppr.util.SmartShopprApp;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by abc on 8/15/2017.
 */

public class SosFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private final String TAG = AppLog.getClassName();
    private ImageView mImgVwBack = null;
    private SwitchCompat mSwCoBtnSos = null;
//    private LinearLayout mLLSOSSMSGEO = null;


    private Button mBtnSaveConatct = null;
    private Button mBtnSendSOSMSG = null;
    private LocationManager mLocationManager;
    private Location mLocation;
    int storagePermission = PackageManager.PERMISSION_DENIED;
    int permissionLocation = PackageManager.PERMISSION_DENIED;
    public static Intent serviceIntent = null;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    private final int REQUREST_PERMISSION_CODE = 101;
    private boolean isNetworkEnabled = false;
    private int REQUEST_CODE_CONTACT = 0;
    private RecyclerView mRcVwContact = null;
    private Button mBtnAddItem = null;
    private ContactAdapter mAdapterContact = null;
    private ArrayList<ContactModel> mlistConact;
    private EditText edTvMobile;
    private EditText edTvName;

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppLog.enter(TAG, AppLog.getMethodName());
        View rootView = inflater.inflate(R.layout.lay_frag_sos, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        initView(rootView);
        AppLog.exit(TAG, AppLog.getMethodName());
        return rootView;
    }

    private void initView(View rootView) {
        mSwCoBtnSos = (SwitchCompat) rootView.findViewById(R.id.swBtn_sos);
        //    mLLSOSSMSGEO = (LinearLayout) rootView.findViewById(R.id.ll_sos_sms_geo);
        mSwCoBtnSos.setOnCheckedChangeListener(this);
       /* mLLSOSSMSGEO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMSGToAll();
            }
        });*/
        mRcVwContact = (RecyclerView) rootView.findViewById(R.id.recVw_conatcts);
        mSwCoBtnSos.setChecked(LocalDataBase.getInstance().isSOSServiceEnable());
        mBtnSaveConatct = (Button) rootView.findViewById(R.id.btn_save_sos_contact);
        mBtnSendSOSMSG = (Button) rootView.findViewById(R.id.btn_send_sos_msg);
        mBtnAddItem = (Button) rootView.findViewById(R.id.btn_add_item);
        mBtnSaveConatct.setOnClickListener(this);
        mBtnSendSOSMSG.setOnClickListener(this);
        setDefaultConatct();
        mlistConact = LocalDataBase.getInstance().getContactList();
        mSwCoBtnSos.setChecked(LocalDataBase.getInstance().isSOSServiceEnable());
        mBtnAddItem.setOnClickListener(this);
        mRcVwContact.addItemDecoration(
                new DividerItemDecoration(getActivity(), 1));
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRcVwContact.setLayoutManager(mLayoutManager1);

        mAdapterContact = new ContactAdapter(getActivity(), mlistConact);
        mRcVwContact.setAdapter(mAdapterContact);

        mRcVwContact.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {
                if (childView instanceof ImageView) {
                    if (childView.getId() == R.id.imgVw_sos_cancel) {
                        mAdapterContact.getList().remove(position);
                        saveContactInfoData();
                        mAdapterContact.notifyDataSetChanged();
                    }
                    if (childView.getId() == R.id.imgVw_edit_contact) {
                        showAddContactDialog(getActivity(), mAdapterContact.getList().get(position));
                        mAdapterContact.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onItemLongPress(View childView, int position) {

            }
        }));

        // mRcVwContact.smoothScrollToPosition(mAdapterContact.getItemCount() - 1);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        LocalDataBase.getInstance().setSOSServiceEnable(isChecked);
        if (isChecked) {
//            checkReadContactPermission();
            checkAndRequestPermissions();

        } else {
            if (serviceIntent != null) {
                getActivity().stopService(serviceIntent);
            }
        }
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            storagePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS);

            permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
            }
            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(getActivity(),
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUREST_PERMISSION_CODE);
            } else {
                startMSGService();
            }

        } else {
            startMSGService();
        }
    }

    private void startMSGService() {
        // create class object
        locationManager = (LocationManager) getActivity()
                .getSystemService(LOCATION_SERVICE);

        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

//        if (!isGPSEnabled && !isNetworkEnabled) {
//            SmartShopprUtils.getInstance().showSettingsAlert(getActivity());
//        } else {
        serviceIntent = new Intent(SmartShopprApp.getApplication(), LockService.class);
        getActivity().startService(serviceIntent);
//        }


    }

    private void saveContactInfoData() {
//        AppLog.enter(TAG, AppLog.getMethodName());
//
//        //makeContactViewNonEditable();
//        ArrayList<ContactModel> contactList = new ArrayList<>();
//        ContactModel contact1 = new ContactModel(strContact1Name, strContact1Mobile);
//        contactList.add(contact1);
        LocalDataBase.getInstance().saveContactData(mAdapterContact.getList());
//
////        } else {
////            SmartShopprUtils.getInstance().showAlertDialog(getActivity(), "Please fill profile information !!", "");
////        }

        AppLog.exit(TAG, AppLog.getMethodName());
    }

    private void setDefaultConatct() {
       mlistConact = LocalDataBase.getInstance().getContactList();
        if (mlistConact != null) {

        }
    }

    public void sendMSGToAll() {
        Location loc = fetchLoc();
        //   sendSMS("8802216669", "hello saviour https://maps.google.com/maps/dir//" + loc.getLatitude() + "," + loc.getLongitude());
        if (LocalDataBase.getInstance().getContactList() != null && !LocalDataBase.getInstance().getContactList().isEmpty()) {
            for (int i = 0; i < LocalDataBase.getInstance().getContactList().size(); i++) {
//                sendSMS("9818555384", "hello saviour https://maps.google.com/maps/dir//" + loc.getLatitude() + "," + loc.getLongitude());
                String contact = LocalDataBase.getInstance().getContactList().get(i).getContact();
                if (!contact.isEmpty()&& mSwCoBtnSos.isChecked()) {
                    if (loc != null) {
                        sendSMS(contact, "Emergency Alert!!  https://maps.google.com/maps/dir//" + loc.getLatitude() + "," + loc.getLongitude());
                    } else {
                        sendSMS(contact, "Emergency Alert!! Could not fetch last location");
                    }
                }
            }
        }

    }

    private Location fetchLoc() {
        if (Build.VERSION.SDK_INT <= 23 || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            List<String> providers = mLocationManager.getProviders(true);
            for (String provider : providers) {
                mLocation = mLocationManager.getLastKnownLocation(provider);
                AppLog.info("last known location, provider: %s, location: %s", provider);

                if (mLocation == null) {
                    continue;
                }
            }
        }
        return mLocation;
    }

    private void sendSMS(String phoneNumber, String message) {

        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
        PendingIntent sentPI = PendingIntent.getBroadcast(getActivity(), 0,
                new Intent(getActivity(), SmsSentReceiver.class), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(getActivity(), 0,
                new Intent(getActivity(), SmsDeliveredReceiver.class), 0);
        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> mSMSMessage = sms.divideMessage(message);
            for (int i = 0; i < mSMSMessage.size(); i++) {
                sentPendingIntents.add(i, sentPI);
                deliveredPendingIntents.add(i, deliveredPI);
            }
            sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
                    sentPendingIntents, deliveredPendingIntents);

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("LOB", "exception while sending message : " + e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_sos_contact:
                saveContactInfoData();
                break;
            case R.id.btn_send_sos_msg:
                sendMSGToAll();
                break;
            case R.id.btn_add_item:
                showAddContactDialog(getActivity(), null);
                break;
//            case R.id.imgVw_sos_add_contact1:
//                REQUEST_CODE_CONTACT = REQUEST_CODE_CONTACT_1;
//                checkReadContactPermission(REQUEST_CODE_CONTACT_1);
//
//                break;
        }
    }

    public void checkReadContactPermission(int request_code_contact) {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, REQUREST_PERMISSION_CODE);
                return;
            } else {
                pickConatct(request_code_contact);
            }
        } else {
            pickConatct(request_code_contact);
        }
    }

    private void pickConatct(int request_code_contact) {

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

//        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        startActivityForResult(intent, request_code_contact);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri contactData = data.getData();
            Cursor cursor = getActivity().managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();
            Uri contact = data.getData();
            ContentResolver cr = getActivity().getContentResolver();

            Cursor c = getActivity().managedQuery(contact, null, null, null, null);
            //      c.moveToFirst();
            String name = "";
            String phone = "";

            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));

                name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    while (pCur.moveToNext()) {
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    }
                }

            }
            edTvMobile.setText(phone);
            edTvName.setText(name);
            edTvName.setSelection(name.length());
            //mAdapterContact.updateTheValue(requestCode,new ContactModel(name,phone));
        }
    }

    public void showAddContactDialog(Context context, final ContactModel contactModel) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dailog_sos_conatct, null);
        dialogBuilder.setView(view);

        edTvName = (EditText) view.findViewById(R.id.edtv_eme_contact_name);
        edTvMobile = (EditText) view.findViewById(R.id.edtv_eme_contact_mobile);

        final ImageView imgVwAdd = (ImageView) view.findViewById(R.id.imgVw_sos_add_contact);
        imgVwAdd.setVisibility(View.VISIBLE);
        imgVwAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkReadContactPermission(REQUEST_CODE_CONTACT);
            }
        });


        dialogBuilder.setTitle("Emergency Contact");
        dialogBuilder.setMessage("Enter conatct details below ");
        dialogBuilder.setPositiveButton("Done",null);

if(contactModel !=null){
    edTvName.setText(contactModel.getName());
    edTvMobile.setText(contactModel.getContact());
}

//        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //do something with edt.getText().toString();
//
//            }
//        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.setOnShowListener(new DialogInterface.OnShowListener() {

                                @Override
                                public void onShow(final DialogInterface dialog) {
                                    Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                                    button.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View view) {
                                            if (edTvName.getText().toString().isEmpty()) {
                                                edTvName.setError("Please enter the name");
                                                return;
                                            } else if (edTvMobile.getText().toString().isEmpty()) {
                                                edTvMobile.setError("Please enter the mobile");
                                                return;
                                            } else {
                                                if(contactModel !=null){
                                                    contactModel.setName(edTvName.getText().toString());
                                                    contactModel.setContact(edTvMobile.getText().toString());
                                                    mAdapterContact.notifyDataSetChanged();
                                                }else {
                                                    mAdapterContact.add(new ContactModel(edTvName.getText().toString(), edTvMobile.getText().toString()));
                                                }
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            });
        b.show();
    }
}
