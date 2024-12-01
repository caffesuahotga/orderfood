package com.example.orderfood.services;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.orderfood.R;
import com.example.orderfood.models.Account;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "login"; // Thêm TAG cho log
    private CallbackManager mCallbackManager;
    private LoginButton loginButton; // Đổi tên biến để tuân thủ quy tắc đặt tên
    private FirebaseAuth mAuth;
    private EditText edtusername, edtpassword;
    private Button btnLogin, btnSignUp;


    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    boolean isLoggedIn = accessToken != null && !accessToken.isExpired();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // fb
        FacebookSdk.sdkInitialize(getApplicationContext()); // Sử dụng getApplicationContext()

        mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button); // Sử dụng ID chính xác trong layout
        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });


        //gg
        FirebaseApp.initializeApp(this);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, options);

        auth = FirebaseAuth.getInstance();

        SignInButton signInButton = findViewById(R.id.login_gg);

        // set cho button login google call giao diện đăng nhập.
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("GoogleSignIn", "hello");
                Intent intent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });

        //cơ bản
        edtusername = findViewById(R.id.username);
        edtpassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);


        //login Bình thường
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        final CollectionReference table_user = database.collection("account");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);
                mDialog.setMessage("Vui lòng đợi...");
                mDialog.show();

                final String inputUsername = edtusername.getText().toString();
                final String inputPassword = edtpassword.getText().toString();

                if (!inputUsername.isEmpty() && !inputPassword.isEmpty()) {
                    // Kiểm tra sự tồn tại của username trong Firestore
                    table_user.whereEqualTo("username", inputUsername).get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (!querySnapshot.isEmpty()) {
                                        // Lấy thông tin người dùng từ Firestore
                                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                        Account user = document.toObject(Account.class);
                                        mDialog.dismiss();
                                        // Kiểm tra mật khẩu
                                        if (user != null && user.getPassword().equals(edtpassword.getText().toString())) {
                                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu đã nhập sai, vui lòng sửa lại !", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        mDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Tên đăng nhập không tồn tại !", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Lỗi truy xuất dữ liệu!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    mDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //signup
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleFacebookAccessToken(com.facebook.AccessToken token) { // Sử dụng đúng kiểu AccessToken
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng nhập thành công, cập nhật UI với thông tin người dùng đã đăng nhập
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // Nếu đăng nhập thất bại, hiển thị thông báo cho người dùng
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        // Cập nhật giao diện người dùng sau khi đăng nhập
        if (user != null) {
            // Hiển thị thông tin người dùng hoặc chuyển hướng đến màn hình chính
            // Ví dụ: startActivity(new Intent(this, MainActivity.class));
        } else {
            // Xử lý khi người dùng không đăng nhập
        }
    }


    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    Shape shape;

    // hàm nhận token từ google => firebase nhận token => xác thực => có được thông tin
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                auth = FirebaseAuth.getInstance();
                                Log.d("login thanh cong", "Tên người dùng: "
                                        + auth.getCurrentUser().getDisplayName() +
                                        ", Email: " + auth.getCurrentUser().getEmail());
                            } else {
                                Exception exception = task.getException();  // Lấy lỗi chi tiết
                                Log.d("login thất bại", "Lỗi: " + (exception != null ? exception.getMessage() : "Không rõ nguyên nhân"));

                            }
                        }
                    });
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }

    });
}
