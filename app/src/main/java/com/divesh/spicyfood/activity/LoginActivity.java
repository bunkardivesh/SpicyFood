package com.divesh.spicyfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.divesh.spicyfood.R;
import com.divesh.spicyfood.Utility.OfflineDatabase;
import com.divesh.spicyfood.Utility.SessionManager;
import com.divesh.spicyfood.databinding.ActivityLoginBinding;

//for login and sign up
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private OfflineDatabase database;
    private SessionManager sessionManager;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = new OfflineDatabase(this);
        sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()){
            Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(homeIntent);
            finish();
        }


    }
        //handle login click
    public void loginViewClick(View view) {
        binding.loginLayoutView.setVisibility(View.VISIBLE);
        binding.signupLayoutView.setVisibility(View.INVISIBLE);
        binding.loginViewBtn.setBackground(getResources().getDrawable(R.drawable.button_bg));
        binding.loginViewBtn.setTextColor(getResources().getColor(R.color.white));

        binding.signupViewBtn.setBackground(getResources().getDrawable(R.drawable.button_bg_white));
        binding.signupViewBtn.setTextColor(getResources().getColor(R.color.red));

    }
        //handle sign up click
    public void signUpViewClick(View view) {
        binding.loginLayoutView.setVisibility(View.INVISIBLE);
        binding.signupLayoutView.setVisibility(View.VISIBLE);
        binding.loginViewBtn.setBackground(getResources().getDrawable(R.drawable.button_bg_white));
        binding.loginViewBtn.setTextColor(getResources().getColor(R.color.red));

        binding.signupViewBtn.setBackground(getResources().getDrawable(R.drawable.button_bg));
        binding.signupViewBtn.setTextColor(getResources().getColor(R.color.white));
    }
        //move to home activity
    public void onLoginClick(View view) {
        String email = binding.emailInput.getEditText().getText().toString();
        String password = binding.passwordInput.getEditText().getText().toString();

        if (email.isEmpty()){
            binding.emailInput.setError("Please enter email!");
            return;
        }else if (password.isEmpty()){
            binding.passwordInput.setError("Enter Password!");
            return;
        }else {
                    if (database.checkUser(email,password)){
                        Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                        finish();
                    }else {
                        Toast.makeText(this,"Check email or password",Toast.LENGTH_SHORT).show();
                    }

        }
    }

    public void onClickSignUp(View view) {
        binding.signupViewBtn.setEnabled(false);
        String email = binding.registerEmail.getEditText().getText().toString();
        String password = binding.registerPassword.getEditText().getText().toString();
        String confPassword = binding.registerConfirmPassword.getEditText().getText().toString();

        if (email.isEmpty()){
            binding.registerEmail.setError("Email can not be empty!");
            binding.signupViewBtn.setEnabled(true);
            return;
        }else if (password.isEmpty()){
            binding.registerPassword.setError("Password can not be empty!");
            binding.signupViewBtn.setEnabled(true);
            return;
        }else if (confPassword.isEmpty()){
            binding.registerConfirmPassword.setError("field can not be empty!");
            binding.signupViewBtn.setEnabled(true);
            return;
        }else if (!email.matches(emailPattern)){
            binding.registerEmail.setError("Enter a valid email!");
            binding.signupViewBtn.setEnabled(true);
            return;
        }else if (password.length() < 6){
                binding.registerPassword.setError("at least 6 character's password is required!.");
            binding.signupViewBtn.setEnabled(true);
            return;
        }else if (!password.equals(confPassword)){
                binding.registerConfirmPassword.setError("Passwords are not matching!");
            binding.signupViewBtn.setEnabled(true);
            return;
        }else {
              database = new OfflineDatabase(this);
              boolean result = database.saveUserToDatabase("",email,"",password);

              if (result){
                  sessionManager = new SessionManager(this);
                  sessionManager.Savedinsharedpref(email);
                  Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                  startActivity(homeIntent);
                  finish();
              }else {
                  Toast.makeText(this,"Try Again!",Toast.LENGTH_SHORT).show();
                  binding.signupViewBtn.setEnabled(true);
              }
        }
    }
}