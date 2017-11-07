package es.source.code.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.source.code.model.User;

/**
 * A login screen that offers login via email/password.
 */
public class LoginOrRegister extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;

    private Boolean oldUser = true;


    @BindView(R.id.login_progress)
    ProgressBar mProgressView;
    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.email_sign_in_button)
    Button mEmailSignInButton;
    @BindView(R.id.email_sign_up_button)
    Button mEmailSignUpButton;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });*/

        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
        if(pref.getString("username", "") == ""){
            mEmailSignInButton.setVisibility(View.GONE);
        } else {
            mEmailSignUpButton.setVisibility(View.GONE);
            mEmailView.setText(pref.getString("username", ""));
        }


        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                oldUser = false;
                attemptLogin();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_password_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mEmailView.setError(getString(R.string.error_username_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isValid(username)) {
            mEmailView.setError(getString(R.string.error_invalid));
            focusView = mEmailView;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isValid(String word) {
        Pattern p = Pattern.compile("^[A-Za-z0-9]+", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(word);
        return m.matches();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
            if(pref.getString("username", "") != ""){
                SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
                editor.putInt("loginState", 0);
                editor.apply();
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            mProgressView.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try{
                URL url = new URL("http://10.0.2.2:8080/SCOSServer/LoginValidator");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                connection.setRequestProperty("Charset", "UTF-8");
                connection.connect();

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());

                String content = "username="+ URLEncoder.encode(mUsername, "UTF-8");
                content += "&password=" + URLEncoder.encode(mPassword, "UTF-8");

                out.writeBytes(content);
                out.flush();
                out.close();

                InputStream in = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null){
                    response.append(line);
                }

                return parseJSON(response.toString());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(connection != null){
                    connection.disconnect();
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            mProgressView.setVisibility(View.GONE);

            if (success) {
                User loginUser = new User();
                loginUser.setUserName(mUsername);
                loginUser.setPassword(mPassword);
                if(oldUser){
                    loginUser.setOldUser(oldUser);
                } else {
                    loginUser.setOldUser(oldUser);
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("user", loginUser);
                setResult(RESULT_OK, returnIntent);

                SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
                editor.putString("username", mUsername);
                editor.putInt("loginState", 1);
                editor.apply();

                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            mProgressView.setVisibility(View.GONE);
        }
    }

    private Boolean parseJSON(String data){
        int resultcode = 0;

        try {
            JSONObject jsonObject = new JSONObject(data);
            resultcode = jsonObject.getInt("RESULTCODE");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(resultcode == 1){
            return true;
        }

        return false;
    }
}

