package com.alliancecode.fixit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alliancecode.fixit.Model.UserConsumer;
import com.alliancecode.fixit.Model.UserProvider;
import com.firebase.geofire.GeoFire;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

import java.io.IOException;

public class ProviderLoginActivity extends AppCompatActivity {

    private Button nSignin, nRegister;

    private TextView nForgotPassword,nWelcome, tvRegister;

    private EditText etUsername, etPassword;

    private FirebaseAuth nAuth;
    private FirebaseDatabase nDbase;
    private DatabaseReference nUsers;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private RelativeLayout nRootLayout;

    private Boolean isDialogDismiss =false;
    private String userId, emailCheck;

    private Boolean isLoggingOut = false;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_login);

        nAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(ProviderLoginActivity.this, WelcomeMainProvider.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        //Init View
        nSignin = (Button) findViewById(R.id.btnSignIn);
    //    tvRegister = (TextView) findViewById(R.id.txtRegister);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        nRootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        nForgotPassword = (TextView) findViewById(R.id.forgotPassword);
      //  nWelcome = (TextView) findViewById(R.id.welcomeText);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }

        nDbase = FirebaseDatabase.getInstance();
        nUsers = nDbase.getReference().child("Users").child("Providers");

        //Event Listener
 /*       tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterDialog();
            }
        });
*/
        nSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                //Check Validation
                if (username.equals("") ) {
                    Toast.makeText(ProviderLoginActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.equals("")) {
                    Toast.makeText(ProviderLoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8) {
                    Toast.makeText(ProviderLoginActivity.this, "Password too short !!!", Toast.LENGTH_SHORT).show();
                    etPassword.setText("");
                    return;
                }


                /////////////////
                final String user = etUsername.getText().toString();
                final String prefix = "fixit.";
                final String suffix = "@gmail.com";
                final String userName = prefix + user + suffix;


                //////////////////
                nAuth.signInWithEmailAndPassword(userName, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {


                        progressBar.setVisibility(View.GONE);

                        Intent intent = new Intent(ProviderLoginActivity.this, WelcomeMainProvider.class);
                        startActivity(intent);
                        finish();

                        //   checkEmailVerified();

                /*        ////////////////////////old filter for consumer
                        if (true){
                            progressBar.setVisibility(View.GONE);


                            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            nUsers = nDbase.getReference().child("Users").child("Providers").child(userId).child("serviceType");

                            nUsers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
//
                                    // String email2 = dataSnapshot.child("email").getValue().toString();
                                    if (dataSnapshot.exists()){
                                        startActivity(new Intent(ProviderLoginActivity.this, WelcomeMainProvider.class));
                                        finish();

                                    }else{
                                        signOut();

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user == null) {
                                            Intent intent = new Intent(ProviderLoginActivity.this, ProviderLoginActivity.class);
                                            startActivity(intent);
                                            Toast.makeText(getApplicationContext(), "Login Failed: Check your Username/Password !!!", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                        ///////////////////  */

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ProviderLoginActivity.this, "Login Failed: Check your email/password !!!", Toast.LENGTH_SHORT).show();
                                etUsername.setText("");
                                etPassword.setText("");
                            }
                        });

               // showLoginDialog();
            }
        });

        nForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });


    }


    ///////////////////////////////
    public void resetPassword() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("RESET PASSWORD");
        builder.setMessage("Go to your email to check the password reset verification");

        LayoutInflater inflater = LayoutInflater.from(this);
        View reset_layout = inflater.inflate(R.layout.layout_reset_password, null);

        final TextInputLayout edtResetPassword = reset_layout.findViewById(R.id.textInput_resetPassword);

        builder.setView(reset_layout);

        builder.setPositiveButton("RESET",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);


//Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String userEmail = edtResetPassword.getEditText().getText().toString().trim();

                if (userEmail.equals("")) {
                    //    Snackbar.make(nRootLayout, "Please enter registered email  !!!", Snackbar.LENGTH_SHORT).show();
                  //  Toast.makeText(ProviderLoginActivity.this, "Please enter registered email !!!", Toast.LENGTH_SHORT).show();
                    edtResetPassword.setErrorEnabled(false); edtResetPassword.setError(null);
                    edtResetPassword.setErrorEnabled(true); edtResetPassword.setError("Please enter registered email.");
                    return;
                } else {
                    nAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Snackbar.make(nRootLayout, "Password reset email sent !!!", Snackbar.LENGTH_SHORT).show();
                                //  startActivity(new Intent(ConsumerLoginActivity.this, ConsumerLoginActivity.class));

                                isDialogDismiss = true;
                                return;


                            } else {
                                //  Snackbar.make(nRootLayout, "Error sending password reset email !!!" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                //  Snackbar.make(nRootLayout, "Error sending password reset email !!! ", Snackbar.LENGTH_SHORT).show();
                               // Toast.makeText(ProviderLoginActivity.this, "Error sending password reset email !!!", Toast.LENGTH_SHORT).show();
                                edtResetPassword.setErrorEnabled(false); edtResetPassword.setError(null);
                                edtResetPassword.setErrorEnabled(true); edtResetPassword.setError("Error sending password reset email.");
                                return;
                            }

                        }
                    });
                }

                if(isDialogDismiss){
                    isDialogDismiss = false;
                    dialog.dismiss();
                }

            }        });


        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });


    }

/*    //////////////////////////////////////////////
    private void showLoginDialog() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please use your username to sign in");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login, null);

      //  final TextInputLayout edtUserName = login_layout.findViewById(R.id.userName);
     //   final TextInputLayout edtEmail = login_layout.findViewById(R.id.email);
        final TextInputLayout edtEmail = login_layout.findViewById(R.id.textInput_email);
        final TextInputLayout edtUsername = login_layout.findViewById(R.id.textInput_username);
        final TextInputLayout edtPassword = login_layout.findViewById(R.id.textInput_password);

        edtEmail.setVisibility(View.GONE);
        dialog.setView(login_layout);
        dialog.setCancelable(false);

        //Set Button
        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
             //   progressBar.setVisibility(View.GONE);

                //Check Validation
                if (TextUtils.isEmpty(edtUsername.getEditText().getText().toString())) {
                    Snackbar.make(nRootLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(edtPassword.getEditText().getText().toString())) {
                    Snackbar.make(nRootLayout, "Please enter password", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (edtPassword.getEditText().getText().toString().length() < 8) {
                    Snackbar.make(nRootLayout, "Password too short !!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

             //   final String email = "fixit_" + edtEmail.getEditText().getText().toString() +"@gmail.com" ;

                final String user = edtUsername.getEditText().getText().toString();
                final String password = edtPassword.getEditText().getText().toString();
                final String prefix = "fixit_";
                final String suffix = "@gmail.com";
                final String username = prefix + user + suffix;


                progressBar.setVisibility(View.VISIBLE);


             //////////////////
                nAuth.signInWithEmailAndPassword(username, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                     //   checkEmailVerified();

                        if (true){
                            progressBar.setVisibility(View.GONE);


                            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            nUsers = nDbase.getReference().child("Users").child("Providers").child(userId).child("serviceType");

                            nUsers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
//
                                   // String email2 = dataSnapshot.child("email").getValue().toString();
                                   if (dataSnapshot.exists()){
                                       startActivity(new Intent(ProviderLoginActivity.this, WelcomeMainProvider.class));
                                        finish();

                                   }else{
                                      signOut();

                                       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                       if (user == null) {
                                          Intent intent = new Intent(ProviderLoginActivity.this, ProviderLoginActivity.class);
                                          startActivity(intent);

                                               Toast.makeText(getApplicationContext(), "Login Failed: Check your Username/Password !!!", Toast.LENGTH_LONG).show();

                                       }
                                   }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                progressBar.setVisibility(View.GONE);
                                Snackbar.make(nRootLayout, "Login Failed: Check your email/password !!!", Snackbar.LENGTH_SHORT).show();
                            }
                        });


            }

        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                progressBar.setVisibility(View.GONE);
            }
        });
        dialog.show();

    }
/////////////////////////////////////////// */

//////////////////////////////////////////////////////////////////////////////////////checkEmailVerified
private void checkEmailVerified() {
    progressBar.setVisibility(View.GONE);
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    boolean emailVerified = user.isEmailVerified();
    if (!emailVerified) {
        nAuth.signOut();
        Intent intent = new Intent(ProviderLoginActivity.this, ProviderLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        Toast.makeText(this, "EMAIL NOT VERIFIED: Please verify your email !!!", Toast.LENGTH_LONG).show();
        return;

    }
}




    ///////////////////////////////////////////////////////////////signOut
    public void signOut() {

        isLoggingOut = true;
        FirebaseAuth.getInstance().signOut();
        finish();

       try {
         Intent intent = new Intent(ProviderLoginActivity.this, ProviderLoginActivity.class);
          startActivity(intent);
           Snackbar.make(nRootLayout, "Login Failed: Check your email/password !!!", Snackbar.LENGTH_SHORT).show();
    } catch (Exception e) {
            // This will catch any exception, because they are all descended from Exception
        System.out.println("Error " + e.getMessage());

     }
    }

//////////////////////////////////////////////


//////////////////////////////////////////////

    ////////////////////////////////////////////
    private void showRegisterDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Please use your email to register");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register_provider, null);

        final TextInputLayout edtEmail = register_layout.findViewById(R.id.textInput_email);
        final TextInputLayout edtPassword = register_layout.findViewById(R.id.textInput_password);
        final TextInputLayout edtPasswordConfirm = register_layout.findViewById(R.id.textInput_passwordConfirm);
        final TextInputLayout edtName = register_layout.findViewById(R.id.textInput_name);
        final TextInputLayout edtAddress = register_layout.findViewById(R.id.textInput_address);
        final TextInputLayout edtAge = register_layout.findViewById(R.id.textInput_age);
        final TextInputLayout edtPhone = register_layout.findViewById(R.id.textInput_phone);
        final TextInputLayout edtServiceType = register_layout.findViewById(R.id.textInput_serviceType);


        dialog.setView(register_layout);
        dialog.setCancelable(false);

        //Set Button
        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //  checkValidation();
                if (TextUtils.isEmpty(edtEmail.getEditText().getText().toString())) {
                    Snackbar.make(nRootLayout, "Email cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edtPassword.getEditText().getText().toString())) {
                    Snackbar.make(nRootLayout, "Password cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edtName.getEditText().getText().toString())) {
                    Snackbar.make(nRootLayout, "Name cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edtAddress.getEditText().getText().toString())) {
                    Snackbar.make(nRootLayout, "Address cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edtAge.getEditText().getText().toString())) {
                    Snackbar.make(nRootLayout, "Age cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edtPhone.getEditText().getText().toString())) {
                    Snackbar.make(nRootLayout, "Phone cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edtServiceType.getEditText().getText().toString())) {
                    Snackbar.make(nRootLayout, "Service Type cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }



                ///////////


                if (edtPassword.getEditText().getText().toString().length() < 8) {
                    Snackbar.make(nRootLayout, "Password too short: Must be atleast 8 characters !!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (edtName.getEditText().getText().toString().length() < 6) {
                    Snackbar.make(nRootLayout, "Name too short: Must be atleast 6 characters !!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (edtAddress.getEditText().getText().toString().length() < 10) {
                    Snackbar.make(nRootLayout, "Address too short: Must be atleast 10 characters !!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                int age=Integer.parseInt(edtAge.getEditText().getText().toString());
                if (age < 18) {
                    Snackbar.make(nRootLayout, "Age must be 18 above !!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                String pwd1 = edtPassword.getEditText().getText().toString();
                String pwd2 = edtPasswordConfirm.getEditText().getText().toString();

                if (!pwd1.equals(pwd2)) {
                    Snackbar.make(nRootLayout, "Password does not matched !!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (edtName.getEditText().getText().toString().matches("")) {
                    edtName.setError("Invalid Name Input ");
                    return;

                }


                if (edtPhone.getEditText().getText().toString().length() != 11) {
                    Snackbar.make(nRootLayout, "Please enter valid phone number", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);

                nAuth.createUserWithEmailAndPassword(edtEmail.getEditText().getText().toString(), edtPassword.getEditText().getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        //Save UserConsumer database
                        UserProvider user = new UserProvider();
                        user.setEmail(edtEmail.getEditText().getText().toString());
                        user.setPassword(edtPassword.getEditText().getText().toString());
                        user.setName(edtName.getEditText().getText().toString());
                        user.setAddress(edtAddress.getEditText().getText().toString());
                        user.setAge(edtAge.getEditText().getText().toString());
                        user.setPhone(edtPhone.getEditText().getText().toString());
                        user.setServiceType(edtServiceType.getEditText().getText().toString());


                        //Use UID to key
                        nUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user)                                                          //saving data
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressBar.setVisibility(View.GONE);
                                        sendVerificationEmail();

                               //         Snackbar.make(nRootLayout, "Register successfully !!!", Snackbar.LENGTH_SHORT).show();
                                //        startActivity(new Intent(ProviderLoginActivity.this, ProviderMapActivity.class));
                               //         finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(nRootLayout, "Register Failed !!!" + e.getMessage(), Snackbar.LENGTH_SHORT).show();

                                    }
                                });


                    }
                })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(nRootLayout, "Failed register !!!" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });

            }
        });


        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        dialog.show();

    }


    ////////////////////////////////////////////////////////////sendVerificationEmail
    private void sendVerificationEmail() {

        final FirebaseUser user = nAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(ProviderLoginActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //   Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(ProviderLoginActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }







    @Override
    protected void onStart() {
        super.onStart();
        nAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        nAuth.removeAuthStateListener(firebaseAuthListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
          }
}
