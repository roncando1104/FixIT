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












public class ConsumerSettingsActivity extends AppCompatActivity {

    private EditText nNameField, nPhoneField;            //#15 Saving Consumer Information

    private Button nConfirm, nBack;              //#15 Saving Consumer Information

    private FirebaseAuth nAuth;                       //#15 Saving Consumer Information
    private DatabaseReference nConsumerDatabase;      //#15 Saving Consumer Information

    private String userId;  //#15 Saving Consumer Information     will contain user ID
    private String nName;
    private String nPhone;
    private String nProfileImageUrl;

    private ImageView nProfileImage;

   // private Uri resultUri;
    private Uri resultUri;

    private Bitmap testbitmap;

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_settings);

        nNameField = (EditText) findViewById(R.id.name);
        nPhoneField = (EditText) findViewById(R.id.phoneNumber);

        nProfileImage = (ImageView) findViewById(R.id.profileImage);


    //    nConfirm = (Button) findViewById(R.id.confirm);
    //    nBack = (Button) findViewById(R.id.back);

        nAuth = FirebaseAuth.getInstance();                                                                                                 //#15 Saving Consumer Information
        userId = nAuth.getCurrentUser().getUid();                                                                                              //#15 Saving Consumer Information
        nConsumerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId);                    //#15 Saving Consumer Information

        getConsumerInformation();     // ***Calling the function*****

        nProfileImage.setOnClickListener(new View.OnClickListener() {      //#16 Saving profile image
            @Override
            public void onClick(View view) {                            //#16 Saving profile image
               Intent intent = new Intent(Intent.ACTION_PICK);        //Calling the Gallery
               intent.setType("image/*");                            // setting what type of files to be picked
              //  intent.setAction(Intent.ACTION_GET_CONTENT);
              //  intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_CODE );        //starting and Calling the preDefined activityResult method   ....requestCode is 1


            }
        });

      /*  ////////////////////////////
        nConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                         //#15 Saving Consumer Information
                saveConsumerInformation();                           //#15 Saving Consumer Information
            }
        });

        nBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                   //#15 Saving Consumer Information
                finish();
                return;
            }
        });

        ///////////////////////////  */

    }

    private void getConsumerInformation(){                                      //#15 Saving Consumer Information     getting the consumer information*****************
        nConsumerDatabase.addValueEventListener(new ValueEventListener() {      // *******using addValueEventListener   to look for every changes in the field**********
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){     //#15 Saving Consumer Information     making sure that the database reference have data inside of it*****************
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();      // ********putting dataSnapshot into a map, easy way to get the data **************** using Map<K, V>

                    if (map.get("name")!=null){              //#15 Saving Consumer Information //*******Automatic creation of child*****asking if it has a value
                        nName = map.get("name").toString();
                        nNameField.setText(nName);
                    }

                    if (map.get("phone")!=null){              //#15 Saving Consumer Information //*******Automatic creation of child*****asking if it  has a value
                        nPhone = map.get("phone").toString();
                        nPhoneField.setText(nPhone);

                    }

                    if (map.get("profileImageUrl")!=null){                         //#16 Saving profile image //*******asking if child("profileImageUrl ") has a value
                        nProfileImageUrl = map.get("profileImageUrl").toString();  //#16 Saving profile image //******
                        Glide.with(getApplication()).load(nProfileImageUrl).into(nProfileImage);        //#16 Saving profile image //****** using Glide

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////saveConsumerInformation
    private void saveConsumerInformation() {                     //#15 Saving Consumer Information       ****************saving the Information **************
        nName = nNameField.getText().toString();
        nPhone = nPhoneField.getText().toString();

        Map consumerInfo = new HashMap();          // *************using HashMap to bundle altogether the items or data, and saving them
        consumerInfo.put("name", nName);
        consumerInfo.put("phone", nPhone);
        nConsumerDatabase.updateChildren(consumerInfo);

        if (resultUri != null){                                                                                                 //#16 Saving profile image
            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userId);     //#16 Saving profile image     ..creating, writing and saving to storage firebase.

            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);         //#16 Saving profile image  // passing resultUri image into bitmap.....
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();            //#16 Saving profile image...  compressing the bitmap image
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);       //#16 Saving profile image...  compressing the bitmap image
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);                  // trying to upload the image in the Storage Firebase...

            uploadTask.addOnFailureListener(new OnFailureListener() {     //#16 Saving profile image... creating listener
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                    return;
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {       //#16 Saving profile image... creating listener
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {                         //#16 Saving profile image...
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();                    // getting the downloaded image.

                    Map newImage = new HashMap();                                       //saving image
                    newImage.put("profileImageUrl", downloadUrl.toString());
                    nConsumerDatabase.updateChildren(newImage);

                    finish();
                    return;


                }
            });
        } else {
            finish();
            return;
        }


    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////
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
//////////////////////////////////////////////////////////



}


