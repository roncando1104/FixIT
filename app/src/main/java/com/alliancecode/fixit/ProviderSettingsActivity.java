package com.alliancecode.fixit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Map;


//
import java.io.FileNotFoundException;
import java.io.InputStream;
import android.graphics.BitmapFactory;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class ProviderSettingsActivity extends AppCompatActivity {

    private EditText nNameField, nPhoneField, nServiceTypeField;

    private Button nConfirm, nBack;

    private FirebaseAuth nAuth;
    private DatabaseReference nProviderDatabase;

    private String userId;
    private String nName;
    private String nPhone;


    private String nProfileImageUrl;


    private ImageView nProfileImage;

    // private Uri resultUri;
    private Uri resultUri;

    private Bitmap testbitmap;

    private static final int REQUEST_CODE = 1;

    private String nServiceType;///////////////////////////////
    private RadioGroup nRadioGroup; ///////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_settings);

      //  nNameField = (EditText) findViewById(R.id.name);
    //    nPhoneField = (EditText) findViewById(R.id.phoneNumber);
    //    nServiceTypeField = (EditText) findViewById(R.id.serviceType);
     //   nRadioGroup = (RadioGroup) findViewById(R.id.radioGroupServiceType);///////////////////

        nProfileImage = (ImageView) findViewById(R.id.profileImage);


    //    nConfirm = (Button) findViewById(R.id.confirm);
     //   nBack = (Button) findViewById(R.id.back);

        nAuth = FirebaseAuth.getInstance();
        userId = nAuth.getCurrentUser().getUid();
        nProviderDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId);

        getProviderInformation();     // ***Calling the function*****

        nProfileImage.setOnClickListener(new View.OnClickListener() {      //#16 Saving profile image
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //  intent.setAction(Intent.ACTION_GET_CONTENT);
                //  intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_CODE );


            }
        });

     /* //////////////////////////////////////  ///////////
        nConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProviderInformation();
            }
        });

        nBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });
        //////////////////////////////////////////////////   */

    }

    private void getProviderInformation(){
        nProviderDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name")!=null){
                        nName = map.get("name").toString();
                        nNameField.setText(nName);
                    }

                    if (map.get("phone")!=null){
                        nPhone = map.get("phone").toString();
                        nPhoneField.setText(nPhone);
                    }

                    if (map.get("profileImageUrl")!=null){
                        nProfileImageUrl = map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(nProfileImageUrl).into(nProfileImage);

                    }

/*
                    if (map.get("serviceType")!=null){                   /////////////////////////////////////////////////////
                        nServiceType = map.get("serviceType").toString();

                        switch(nServiceType){
                            case "Plumber":
                                nRadioGroup.check(R.id.plumber);
                                break;

                            case "Laundry":
                                nRadioGroup.check(R.id.laundry);
                                break;

                            case "Electrician":
                                nRadioGroup.check(R.id.electrician);
                                break;
                        }
                      //  nServiceTypeField.setText(nServiceType);

                    }//////////////////////////////
*/

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void saveProviderInformation() {
        nName = nNameField.getText().toString();
        nPhone = nPhoneField.getText().toString();
     //   nServiceType = nServiceTypeField.getText().toString();

        int selectedId = nRadioGroup.getCheckedRadioButtonId();////////////////////////
        final RadioButton nRadioButton = (RadioButton) findViewById(selectedId);///////////////////////

        if (nRadioButton == null){
            return;
        }

        nServiceType = nRadioButton.getText().toString();//////////////////////


        Map providerInfo = new HashMap();
        providerInfo.put("name", nName);
        providerInfo.put("phone", nPhone);
     //   providerInfo.put("serviceType", nServiceType);
        providerInfo.put("serviceType", nServiceType);///////////////////////

        nProviderDatabase.updateChildren(providerInfo);

        if (resultUri != null){
            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userId);

            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {     //#16 Saving profile image... creating listener
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                    return;
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {       //#16 Saving profile image... creating listener
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Map newImage = new HashMap();
                    newImage.put("profileImageUrl", downloadUrl.toString());
                    nProviderDatabase.updateChildren(newImage);

                   // finish();
                    finish();
                    return;


                }
            });
        } else {
           finish();

           // return;
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {            //#16 Saving profile image   Creating the preDefined method onActivityResult
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try{
                final Uri imageUri = data.getData();                                               //#16 Saving profile image   // getting picture from gallery   ...final
                resultUri = imageUri;
                final InputStream imageStream = getContentResolver().openInputStream(resultUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                nProfileImage.setImageBitmap(selectedImage);                                  //#16 Saving profile image   //setting the ImageView... changing the picture

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}


