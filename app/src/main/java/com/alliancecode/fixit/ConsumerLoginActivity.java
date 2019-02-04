package com.alliancecode.fixit;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alliancecode.fixit.Model.UserConsumer;
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

public class ConsumerLoginActivity extends AppCompatActivity {

    private Button nSignin, nRegister;

    private TextView nForgotPassword, nWelcome , tvRegister;
    private EditText etEmail, etPassword;
    private Boolean isDialogDismiss = false;
    private Boolean isEmailSent = false;

    private FirebaseAuth nAuth;
    private FirebaseDatabase nDbase;
    private DatabaseReference nUsers;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    //  private ProgressDialog nDialog;

    private Boolean isDismiss = false;
    private ProgressBar progressBar;

    private TextView nSentEmail;

    private int status = 0;
    private String emailCheck;
    private String strEmail;



    private RelativeLayout nRootLayout;

    //   private CardView nResetLayout;

    //  private TextInputLayout edtEmail, edtPassword, edtName, edtPhone;
    //  private String metEmail, emailPattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_login);



///////////////////////////////////////////////////////FirebaseAuth.AuthStateListener
            nAuth = FirebaseAuth.getInstance();
            firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user != null) {


                        Intent intent = new Intent(ConsumerLoginActivity.this, WelcomeMain.class);
                        //  Intent intent = new Intent(ConsumerLoginActivity.this, ConsumerMapActivity.class);
                        startActivity(intent);
                        finish();
                        return;



                    }
                }
            };





        //Init View
        nSignin = (Button) findViewById(R.id.btnSignIn);
       // nRegister = (Button) findViewById(R.id.btnRegister);
        tvRegister = (TextView) findViewById(R.id.txtRegister);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);


        nRootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        //     nResetLayout = (CardView) findViewById(R.id.resetLayout);
        nForgotPassword = (TextView) findViewById(R.id.forgotPassword);
    //    nWelcome = (TextView) findViewById(R.id.welcomeText);

    //    nDialog = new ProgressDialog(this);
        //fonts
//        Typeface fontNunitoEBI = Typeface.createFromAsset(getAssets(), "fonts/Nunito-ExtraBoldItalic.ttf");
    //    Typeface fontNunitoB = Typeface.createFromAsset(getAssets(), "fonts/Nunito-Black.ttf");


     //   nForgotPassword.setTypeface(fontNunitoEBI);
     //   nWelcome.setTypeface(fontNunitoEBI);
     //   nSignin.setTypeface(fontNunitoB);
   //    nRegister.setTypeface(fontNunitoB);


      progressBar = (ProgressBar) findViewById(R.id.progressbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }


        //Init Firebase
        // nAuth = FirebaseAuth.getInstance();
        nDbase = FirebaseDatabase.getInstance();
        nUsers = nDbase.getReference().child("Users").child("Consumers");
        //Event Listener
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterDialog();
            }
        });


        ///////////////////////////////////////////////////////////////
        nSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                //Check Validation
                if (email.equals("") ) {
                    Toast.makeText(ConsumerLoginActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.equals("")) {
                    Toast.makeText(ConsumerLoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (password.length() < 8) {
                    Toast.makeText(ConsumerLoginActivity.this, "Password too short !!!", Toast.LENGTH_SHORT).show();
                    etPassword.setText("");
                    return;

                }

////////provider should not be a able to sign-in using his email address
                if(email!=null ){
                     String emailString = etEmail.getText().toString();
                     emailCheck = emailString.substring(0,5);
                }
               ////////////////////////
                if (emailCheck.equals("fixit")) {
                    Toast.makeText(ConsumerLoginActivity.this, "Login Failed: Check your email/password !!!", Toast.LENGTH_SHORT).show();
                    etEmail.setText("");
                    etPassword.setText("");
                    return;
                }

                nAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        progressBar.setVisibility(View.GONE);

                        Intent intent = new Intent(ConsumerLoginActivity.this, WelcomeMain.class);
                        startActivity(intent);
                        finish();

                        //       checkEmailVerified();

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ConsumerLoginActivity.this, "Login Failed: Check your email/password !!!", Toast.LENGTH_SHORT).show();
                                etEmail.setText("");
                                etPassword.setText("");
                            }
                        });
            }
        });
/////////////////////////////////////////

        nForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }

      //////////////////////////////////////////////////
 /////////////////////////////////////////////////////////////

 /*   //////////////////////////////////////////////////
    private void showLoginDialog() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please use your email to sign in");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login, null);

      //  final TextInputLayout edtUserName = login_layout.findViewById(R.id.userName);
       // final TextInputLayout edtEmail = login_layout.findViewById(R.id.email);
        final TextInputLayout edtUsername = login_layout.findViewById(R.id.textInput_username);
        final TextInputLayout edtEmail = login_layout.findViewById(R.id.textInput_email);

        final TextInputLayout edtPassword = login_layout.findViewById(R.id.textInput_password);

        edtUsername.setVisibility(View.GONE);

        dialog.setView(login_layout);
        dialog.setCancelable(false);
        progressBar.setVisibility(View.VISIBLE);



        //Set Button
        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                progressBar.setVisibility(View.GONE);
                final String emailString = edtEmail.getEditText().getText().toString();
                final String emailCheck = emailString.substring(0,5);////////////////////////

                //Check Validation
                if (TextUtils.isEmpty(edtEmail.getEditText().getText().toString())) {
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

                if (edtPassword.getEditText().getText().toString().length() < 8) {
                    Snackbar.make(nRootLayout, "Password too short !!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                ////////provider should not be a able to sign-in using his email address
                if (emailCheck.equals("fixit")) {
                    Snackbar.make(nRootLayout, "Login Failed: Check your email/password !!!", Snackbar.LENGTH_SHORT).show();
                   return;
                }

                final String email = edtEmail.getEditText().getText().toString();
                final String password = edtPassword.getEditText().getText().toString();


                nAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        progressBar.setVisibility(View.GONE);

                        Intent intent = new Intent(ConsumerLoginActivity.this, WelcomeMain.class);
                        startActivity(intent);
                        finish();

                        //       checkEmailVerified();

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

                progressBar.setVisibility(View.GONE);
                dialogInterface.dismiss();
            }
        });
        dialog.show();

    }
/////////////////////////////////////////////////////////////   */


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
                   // Toast.makeText(ConsumerLoginActivity.this, "Please enter registered email !!!", Toast.LENGTH_SHORT).show();
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
                                dialog.dismiss();

                             //   isDialogDismiss = true;
                                return;


                            } else {
                              //  Snackbar.make(nRootLayout, "Error sending password reset email !!!" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                              //  Snackbar.make(nRootLayout, "Error sending password reset email !!! ", Snackbar.LENGTH_SHORT).show();
                             //   Toast.makeText(ConsumerLoginActivity.this, "Error sending password reset email !!!", Toast.LENGTH_SHORT).show();
                                edtResetPassword.setErrorEnabled(false); edtResetPassword.setError(null);
                                edtResetPassword.setErrorEnabled(true); edtResetPassword.setError("Error sending password reset email.");
                                return;
                            }

                        }
                    });
                }

          //      if(isDialogDismiss){
          //          isDialogDismiss = false;
           //         dialog.dismiss();
          //      }

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
//////////////////////////////////////////forgot password

    ///////////////////////////////////////////////

    public void signOut() {

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ConsumerLoginActivity.this, WelcomeMain.class));
        finish();
        Snackbar.make(nRootLayout, "No account found, please register !!!", Snackbar.LENGTH_SHORT).show();
        return;
    }

    ////////////////////////////////////////////showRegisterDialog
    private void showRegisterDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("REGISTER");
        builder.setMessage("Please use your email to register");
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register, null);

        final TextInputLayout edtEmail = register_layout.findViewById(R.id.textInput_email);
        final TextInputLayout edtPassword = register_layout.findViewById(R.id.textInput_password);
        final TextInputLayout edtPasswordConfirm = register_layout.findViewById(R.id.textInput_passwordConfirm);

        final TextInputLayout edtName = register_layout.findViewById(R.id.textInput_name);
        final TextInputLayout edtSurName = register_layout.findViewById(R.id.textInput_surname);

        final TextInputLayout edtPhone = register_layout.findViewById(R.id.textInput_phone);

        builder.setView(register_layout);

        builder.setPositiveButton("REGISTER",
                                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                       }
                });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });


        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);

//Overriding the button handler
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
              //  isDialogDismiss = false;
                //Do stuff, possibly set wantToCloseDialog to true then...
                final String strName = edtName.getEditText().getText().toString().trim();
                final String strSurname = edtSurName.getEditText().getText().toString().trim();
                final String pwd1 = edtPassword.getEditText().getText().toString();
                final String pwd2 = edtPasswordConfirm.getEditText().getText().toString();
                final String strPhone = edtPhone.getEditText().getText().toString();
                 strEmail = edtEmail.getEditText().getText().toString();

                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.

                //  checkValidation();
                if (TextUtils.isEmpty(edtEmail.getEditText().getText().toString())) {
                   // Snackbar.make(nRootLayout, "Email cannot be empty", Snackbar.LENGTH_SHORT).show();
                    edtEmail.setErrorEnabled(true);
                    edtEmail.setError("Email is empty");
                    return;
                }else
                    edtEmail.setErrorEnabled(false); edtEmail.setError(null);

                if (edtEmail.getEditText().getText().toString().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
                    // Snackbar.make(nRootLayout, "Email cannot be empty", Snackbar.LENGTH_SHORT).show();
                    edtEmail.setErrorEnabled(true); edtEmail.setError("");
                    edtEmail.setErrorEnabled(false); edtEmail.setError(null);
                }else{
                    edtEmail.setErrorEnabled(true);
                    edtEmail.setError("Email is invalid");
                    return;
                }

////////////////////////////////
                if (TextUtils.isEmpty(edtPassword.getEditText().getText().toString())) {
                   // Snackbar.make(nRootLayout, "Password cannot be empty", Snackbar.LENGTH_SHORT).show();
                    edtPassword.setErrorEnabled(true);
                    edtPassword.setError("Password is empty");
                    return;
                }else
                    edtPassword.setErrorEnabled(false); edtPassword.setError(null);

                if (edtPassword.getEditText().getText().toString().length() < 8) {
                    // Snackbar.make(nRootLayout, "Password too short: Must be atleast 8 characters !!!", Snackbar.LENGTH_SHORT).show();
                    edtPassword.setErrorEnabled(true);
                    edtPassword.setError("Password must be atleast 8 characters.");
                    return;
                }else
                    edtPassword.setErrorEnabled(false); edtPassword.setError(null);

                if (TextUtils.isEmpty(edtPasswordConfirm.getEditText().getText().toString())) {
                    // Snackbar.make(nRootLayout, "Password cannot be empty", Snackbar.LENGTH_SHORT).show();
                    edtPasswordConfirm.setErrorEnabled(true);
                    edtPasswordConfirm.setError("Confirm password is empty");
                    return;
                }else
                    edtPasswordConfirm.setErrorEnabled(false); edtPasswordConfirm.setError(null);

                if (!pwd1.equals(pwd2)) {
                    // Snackbar.make(nRootLayout, "Password does not matched !!!", Snackbar.LENGTH_SHORT).show();
                    edtPasswordConfirm.setErrorEnabled(true);
                    edtPasswordConfirm.setError("Password does not matched");
                    return;
                }else
                    edtPasswordConfirm.setErrorEnabled(false); edtPasswordConfirm.setError(null);
////////////////////////////////
                if (TextUtils.isEmpty(edtName.getEditText().getText().toString())) {
                  //  Snackbar.make(nRootLayout, "Name cannot be empty", Snackbar.LENGTH_SHORT).show();
                    edtName.setErrorEnabled(true);
                    edtName.setError("Name is empty");
                    return;
                }else
                    edtName.setErrorEnabled(false); edtName.setError(null);

                if (edtName.getEditText().getText().toString().length() <3) {
                    // Snackbar.make(nRootLayout, "Name too short: Must be atleast 6 characters !!!", Snackbar.LENGTH_SHORT).show();
                    edtName.setErrorEnabled(true);
                    edtName.setError("Name too short ");
                    return;
                }else
                    edtName.setErrorEnabled(false); edtName.setError(null);

                if (strName.matches(" *") || strName.matches("[a-zA-Z]{1} *") || strName.matches("[a-zA-Z]{2} *") ) {
                    edtName.setErrorEnabled(true);
                    edtName.setError("Invalid name input ");
                    return;
                }else
                    edtName.setErrorEnabled(false); edtName.setError(null);
////////////////////////////////
                if (TextUtils.isEmpty(edtSurName.getEditText().getText().toString())) {
                  //  Snackbar.make(nRootLayout, "Last Name cannot be empty", Snackbar.LENGTH_SHORT).show();
                    edtSurName.setErrorEnabled(true);
                    edtSurName.setError("Surname is empty");
                    return;
                }else
                    edtSurName.setErrorEnabled(false); edtSurName.setError(null);

                if (edtSurName.getEditText().getText().toString().length() < 2) {
                    //   Snackbar.make(nRootLayout, "Name too short: Must be atleast 6 characters !!!", Snackbar.LENGTH_SHORT).show();
                    edtSurName.setErrorEnabled(true);
                    edtSurName.setError("Surname too short ");
                    return;
                }else
                    edtSurName.setErrorEnabled(false); edtSurName.setError(null);

                if (strSurname.matches(" *") || strSurname.matches("[a-zA-Z]{1} *") ) {
                    edtSurName.setErrorEnabled(true);
                    edtSurName.setError("Invalid name input ");
                    return;
                }else
                    edtSurName.setErrorEnabled(false); edtSurName.setError(null);
////////////////////////////////
                if (TextUtils.isEmpty(edtPhone.getEditText().getText().toString())) {
                   // Snackbar.make(nRootLayout, "Phone cannot be empty", Snackbar.LENGTH_SHORT).show();
                    edtPhone.setErrorEnabled(true);
                    edtPhone.setError("Phone is empty");
                    return;
                }else
                    edtPhone.setErrorEnabled(false); edtPhone.setError(null);

                if (strPhone.length() != 11) {
                    // Snackbar.make(nRootLayout, "Please enter valid phone number", Snackbar.LENGTH_SHORT).show();
                    edtPhone.setErrorEnabled(true);
                    edtPhone.setError("Invalid phone number ");
                    return;
                }else
                    edtPhone.setErrorEnabled(false); edtPhone.setError(null);

                if (strPhone.matches("0[8-9]{1}[0-9]*") ) {
                    // Snackbar.make(nRootLayout, "Please enter valid phone number", Snackbar.LENGTH_SHORT).show();
                    edtPhone.setErrorEnabled(true); edtPhone.setError("");
                    edtPhone.setErrorEnabled(false); edtPhone.setError(null);
                }else{
                    edtPhone.setErrorEnabled(true);
                    edtPhone.setError("Invalid phone number ");
                    return;
                }

///////////////////////////////

                progressBar.setVisibility(View.VISIBLE);
                nAuth.createUserWithEmailAndPassword(edtEmail.getEditText().getText().toString(), edtPassword.getEditText().getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {


                        //Save UserConsumer database
                        UserConsumer user = new UserConsumer();
                        user.setCancelCount(0);
                        user.setEmail(edtEmail.getEditText().getText().toString());
                        user.setPassword(edtPassword.getEditText().getText().toString());
                     //   user.setName(edtName.getEditText().getText().toString());
                        user.setName(strName + " " + strSurname);
                        user.setPhone(edtPhone.getEditText().getText().toString());
                        // user.setProfileImageUrl("");
                        user.setProfileImageUrl("https://firebasestorage.googleapis.com/v0/b/fixit-dbae9.appspot.com/o/profile_images%2Fic_profileimage.png?alt=media&token=6e08b911-8a2f-4ba6-a16f-6f5e92964645");
                        //Use UID to key
                      //  "https://firebasestorage.googleapis.com/v0/b/fixit-dbae9.appspot.com/o/profile_images%2FznsUoIuMY9PAGcNU2mYVNx1UEi73?alt=media&token=2803ce5c-6623-4460-9455-bd16ee92214e"
                        // nUsers.child(nAuth.getUid()).setValue(user);
                        //  nUsers.child(nAuth.getUid());
                        nUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user)                                              //saving data
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        isDialogDismiss = true;
                                        progressBar.setVisibility(View.GONE);
                                        sendVerificationEmail();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        isDialogDismiss = true;
                                        Snackbar.make(nRootLayout, "Registration Failed  !!!" + e.getMessage(), Snackbar.LENGTH_SHORT).show();

                                    }
                                });


                    }
                })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                isDismiss = true;
                                Snackbar.make(nRootLayout, "Registration Failed !!! " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });
                if(isDialogDismiss){
                    isDialogDismiss = false;
                    dialog.dismiss();
                }
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

    }
    ///////////////


       //   sendEmailVerification
    //////////////////////////////////////////////////////////////sendVerificationEmail
    private void sendVerificationEmail() {

        try{


            final FirebaseUser user = nAuth.getCurrentUser();
            user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (task.isSuccessful()) {

   /*
                              if(isEmailSent){
                                  isEmailSent = false;
                                  Intent intent = new Intent(ConsumerLoginActivity.this, WelcomeMain.class);
                                  startActivity(intent);
                                  finish();
                              }


*/


                            } else {
                                //   Log.e(TAG, "sendEmailVerification", task.getException());
                                Toast.makeText(ConsumerLoginActivity.this,
                                        "Failed to send verification email.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



        }catch (Exception e){
            e.printStackTrace();
        }



    }
/////////////////////////////
    private void emailSent() {
        isEmailSent = false;
        final FirebaseUser user = nAuth.getCurrentUser();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View sent_layout = inflater.inflate(R.layout.layout_message_emailsent, null);
        nSentEmail = sent_layout.findViewById(R.id.emailRegistered);
        nSentEmail.setText(strEmail);
        dialog.setView(sent_layout);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            isEmailSent = true;
              //  Intent intent = new Intent(ConsumerLoginActivity.this, WelcomeMain.class);
               // startActivity(intent);
             //   finish();
            }
        });
        dialog.show();
    }
/////////////////////////






    //////////////////////////////////////////////////////////////////////////checkEmailVerified
    private void checkEmailVerified() {
        progressBar.setVisibility(View.GONE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        boolean emailVerified = user.isEmailVerified();
        if (!emailVerified) {

            verifyEmail();

       //     Intent intent = new Intent(ConsumerLoginActivity.this, ConsumerLoginActivity.class);
        //    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       //     startActivity(intent);

       //     Toast.makeText(this, "EMAIL NOT VERIFIED: Please verify your email !!!", Toast.LENGTH_LONG).show();
       //     return;

        }else{
            Intent intent = new Intent(ConsumerLoginActivity.this, WelcomeMain.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }


    }

//////////////
    private void verifyEmail() {
        final FirebaseUser user = nAuth.getCurrentUser();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View sent_layout = inflater.inflate(R.layout.layout_message_emailverification, null);

        dialog.setView(sent_layout);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                nAuth.signOut();
                return;
              //  Intent intent = getIntent();
               // startActivity(intent);

            }
        });
        dialog.show();

    }


    ///////////////////////////////

    @Override
    protected void onStart() {
        super.onStart();

        nAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onBackPressed() {
     //  finish();
        finishAffinity();
       // super.onBackPressed();
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
