package com.kmit.musicapp.fragments.O_LoginRegistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.kmit.musicapp.Model.User;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.Methods;
import com.kmit.musicapp.Util.util;
import com.kmit.musicapp.activities.HomeActivity;
import com.kmit.musicapp.activities.LoginRegisrationActivity;
import com.kmit.musicapp.asyncTask.Asy_Login;
import com.kmit.musicapp.fragmentanimations.MoveAnimation;

public class Registration extends Fragment {
    private EditText editText_name, editText_email, editText_password, editText_phoneNo;
    private String name, email, password, phoneNo;
    private ProgressDialog progressDialog;
    private TextView textView1;
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    LoginRegisrationActivity loginRegistration;
    private Methods methods;

    public Registration(LoginRegisrationActivity loginRegistration) {
        this.loginRegistration = loginRegistration;
        // Required empty public constructor
    }

    public static Registration newInstance(LoginRegisrationActivity loginRegistration) {
        Registration fragment = new Registration(loginRegistration);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        methods = new Methods(getActivity());
        pref = getActivity().getSharedPreferences(Constant.mypreference, 0); // 0 - for private mode
        editor = pref.edit();

        progressDialog = new ProgressDialog(getContext());

        editText_name = view.findViewById(R.id.editText_name_register);
        editText_email = view.findViewById(R.id.editText_email_register);
        editText_password = view.findViewById(R.id.editText_password_register);
        editText_phoneNo = view.findViewById(R.id.editText_phoneNo_register);
        TextView button_submit = view.findViewById(R.id.button_submit);
        TextView textView_login = view.findViewById(R.id.textView_login_register);


        textView_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login f1 = Login.newInstance(loginRegistration);
                loginRegistration.loadFrag(f1, getResources().getString(R.string.login));
            }
        });


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = editText_name.getText().toString();
                email = editText_email.getText().toString();
                password = editText_password.getText().toString();
                phoneNo = editText_phoneNo.getText().toString();

                form();

            }
        });


        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return MoveAnimation.create(MoveAnimation.LEFT, enter, 800);
        } else {
            return MoveAnimation.create(MoveAnimation.RIGHT, enter, 800);
        }
    }


    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void form() {

        editText_name.setError(null);
        editText_email.setError(null);
        editText_password.setError(null);
        editText_phoneNo.setError(null);

        if (name.equals("") || name.isEmpty()) {
            editText_name.requestFocus();
            editText_name.setError(getResources().getString(R.string.please_enter_name));
        } else if (!isValidMail(email) || email.isEmpty()) {
            editText_email.requestFocus();
            editText_email.setError(getResources().getString(R.string.please_enter_email));
        } else if (password.equals("") || password.isEmpty()) {
            editText_password.requestFocus();
            editText_password.setError(getResources().getString(R.string.please_enter_password));
        } else if (phoneNo.equals("") || phoneNo.isEmpty()) {
            editText_phoneNo.requestFocus();
            editText_phoneNo.setError(getResources().getString(R.string.please_enter_phone));
        } else {

            editText_name.clearFocus();
            editText_email.clearFocus();
            editText_password.clearFocus();
            editText_phoneNo.clearFocus();

            if (util.isNetworkAvailable(getContext())) {
                register(name, email, password, phoneNo);
            } else {
            }

        }
    }

    public void register(String sendName, String sendEmail, String sendPassword, String sendPhone) {


        new Asy_Login(methods.getAPIRequest(Constant.METHOD_REGISTRATION, sendName, sendEmail, sendPassword, "", ""), Constant.Signup,
                new Asy_Login.Listener_Artist() {
                    @Override
                    public void onStart() {
                        progressDialog.show();
                        progressDialog.setMessage(getResources().getString(R.string.loading));
                        progressDialog.setCancelable(false);

                    }

                    @Override
                    public void onEnd(String success, User user_id) {


                        progressDialog.dismiss();
                        if(user_id.getUser_id().equals("0")){
                            Toast.makeText(getContext(), "Something Went Wrong Please Try Again", Toast.LENGTH_SHORT).show();
                        }else{

                            Constant.user_id=user_id.getUser_id();
                            Constant.user_name=user_id.getUser_name();
                            Constant.user_email=user_id.getUser_email();
                            Constant.user_profile_pic=user_id.getUser_profile_pic();
                            editor.putString(Constant.pref_email, editText_email.getText().toString());
                            editor.putString(Constant.pref_password, editText_password.getText().toString());
                            editor.putBoolean(Constant.pref_check, true);
                            editor.commit();

                            Constant.edite_profile(getContext(),user_id);
                            Intent i = new Intent(loginRegistration, HomeActivity.class);
                            startActivity(i);
                            loginRegistration.finish();

                        }
                    }
                }).execute();



    }


}
