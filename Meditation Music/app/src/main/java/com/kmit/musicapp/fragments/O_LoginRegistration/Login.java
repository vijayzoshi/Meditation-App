package com.kmit.musicapp.fragments.O_LoginRegistration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.kmit.musicapp.Model.User;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.Methods;
import com.kmit.musicapp.activities.HomeActivity;
import com.kmit.musicapp.activities.LoginRegisrationActivity;
import com.kmit.musicapp.activities.SplashActivity;
import com.kmit.musicapp.asyncTask.Asy_Login;
import com.kmit.musicapp.fragmentanimations.MoveAnimation;

import cn.refactor.library.SmoothCheckBox;

public class Login extends Fragment {

    private EditText editText_email, editText_password;
    private String email, password;


    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private InputMethodManager imm;

    LoginRegisrationActivity loginRegistration;
    private Methods methods;

    public Login(LoginRegisrationActivity loginRegistration) {
        this.loginRegistration = loginRegistration;
    }

    public static Login newInstance(LoginRegisrationActivity loginRegistration) {
        Login fragment = new Login(loginRegistration);
        return fragment;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return MoveAnimation.create(MoveAnimation.RIGHT, enter, 800);
        } else {
            return MoveAnimation.create(MoveAnimation.LEFT, enter, 800);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        methods = new Methods(getActivity());
        pref = getActivity().getSharedPreferences(Constant.mypreference, 0); // 0 - for private mode
        editor = pref.edit();

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        progressDialog = new ProgressDialog(getContext());

        editText_email = view.findViewById(R.id.editText_email_login_activity);
        editText_password = view.findViewById(R.id.editText_password_login_activity);
        TextView button_login = view.findViewById(R.id.button_login_activity);
        TextView textView_signup_login = view.findViewById(R.id.textView_signup_login);
        final SmoothCheckBox checkBox = view.findViewById(R.id.checkbox_login_activity);
        checkBox.setChecked(false);


        if (pref.getBoolean(Constant.pref_check, false)) {
            editText_email.setText(pref.getString(Constant.pref_email, null));
            editText_password.setText(pref.getString(Constant.pref_password, null));
            checkBox.setChecked(true);
        } else {
            editText_email.setText("");
            editText_password.setText("");
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                Log.d("SmoothCheckBox", String.valueOf(isChecked));
                if (isChecked) {
                    editor.putString(Constant.pref_email, editText_email.getText().toString());
                    editor.putString(Constant.pref_password, editText_password.getText().toString());
                    editor.putBoolean(Constant.pref_check, true);
                    editor.commit();
                } else {
                    editor.putBoolean(Constant.pref_check, false);
                    editor.commit();
                }
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = editText_email.getText().toString();
                password = editText_password.getText().toString();

                editText_email.clearFocus();
                editText_password.clearFocus();
                imm.hideSoftInputFromWindow(editText_email.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(editText_password.getWindowToken(), 0);

                login(checkBox);
            }
        });

        textView_signup_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registration f2 = Registration.newInstance(loginRegistration);
                loginRegistration.loadFrag(f2, getResources().getString(R.string.regis));
            }
        });

        return view;
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void login(SmoothCheckBox checkBox) {

        editText_email.setError(null);
        editText_password.setError(null);

        if (!isValidMail(email) || email.isEmpty()) {
            editText_email.requestFocus();
            editText_email.setError(getResources().getString(R.string.please_enter_email));
        } else if (password.isEmpty()) {
            editText_password.requestFocus();
            editText_password.setError(getResources().getString(R.string.please_enter_password));
        } else {
            login(email, password, checkBox);

        }
    }


    public void login(final String sendEmail, final String sendPassword, final SmoothCheckBox checkBox) {

        new Asy_Login(methods.getAPIRequest(Constant.METHOD_LOGIN,sendEmail , sendPassword, "", "", ""), Constant.Signin,
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
                            if (checkBox.isChecked()) {
                                editor.putString(Constant.pref_email, editText_email.getText().toString());
                                editor.putString(Constant.pref_password, editText_password.getText().toString());
                                editor.putBoolean(Constant.pref_check, true);
                                editor.commit();
                            }
                            Constant.edite_profile(getContext(),user_id);
                            Intent i = new Intent(loginRegistration, HomeActivity.class);
                            startActivity(i);
                            loginRegistration.finish();

                        }

                    }
                }).execute();
    }
}
