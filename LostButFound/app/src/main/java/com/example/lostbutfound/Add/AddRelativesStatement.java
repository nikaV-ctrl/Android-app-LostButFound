package com.example.lostbutfound.Add;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lostbutfound.Model.People;
import com.example.lostbutfound.Model.Relatives;
import com.example.lostbutfound.R;
import com.example.lostbutfound.User.LoginActivity;
import com.example.lostbutfound.User.UserPage;
import com.example.lostbutfound.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class AddRelativesStatement extends AppCompatActivity {

    private AutoCompleteTextView sexEditText, cityEditText, statusEditText;
    private String[] sex = {"Женщина", "Мужчина"};
    private String[] city = {"Караганда", "Астана", "Алмата"};
    private String[] status = {"Личность еще не установлена",
            "Личность установлена. Родственники найдены"};

    private TextInputEditText titleEditText, ageEditText,
            circumstancesEditText, signsEditText, clothesEditText,
            lastLocationEditText, phoneEditText;

    private Button addRelativeBtn;
    private ImageView imageView;
    private DatabaseReference relativesDataBase;
    private Relatives newRelative;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_relatives_statement);

        init();
        adapterForAutoCompleteTextView();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openFileChooser();
                }
            });
            addRelativeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mUploadTask != null  && mUploadTask.isInProgress()) {
                        Utility.showToast(AddRelativesStatement.this, "Загрузка в процессе");
                    } else {
                        addRelativeStatement();
                    }
                }
            });

        } else {
            Utility.showToast(this, "Вы не авторизованы");
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    void init(){
        sexEditText = findViewById(R.id.sexEditText);
        cityEditText = findViewById(R.id.cityEditText);
        statusEditText = findViewById(R.id.statusEditText);

        titleEditText = findViewById(R.id.titleEditText);
        ageEditText = findViewById(R.id.ageEditText);
        circumstancesEditText = findViewById(R.id.circumstancesEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        imageView = findViewById(R.id.imageView);
        addRelativeBtn = findViewById(R.id.addRelativeBtn);

        relativesDataBase = FirebaseDatabase.getInstance().getReference("Relatives");
        mStorageRef = FirebaseStorage.getInstance().getReference("Relatives");
    }

    void adapterForAutoCompleteTextView(){
        ArrayAdapter<String> adapterSex = new ArrayAdapter (this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sex);
        ArrayAdapter<String> adapterCity = new ArrayAdapter (this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, city);
        ArrayAdapter<String> adapterStatus = new ArrayAdapter (this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, status);
        sexEditText.setAdapter(adapterSex);
        cityEditText.setAdapter(adapterCity);
        statusEditText.setAdapter(adapterStatus);
    }

    void addRelativeStatement() {
        String title = titleEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String sex = sexEditText.getText().toString();
        String circumstances = circumstancesEditText.getText().toString();
        String status = statusEditText.getText().toString();
        Integer age = Integer.parseInt(ageEditText.getText().toString());
        Integer phone = Integer.parseInt(phoneEditText.getText().toString());
        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String id = relativesDataBase.push().getKey();

        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 500);

                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String fileLink = task.getResult().toString();

                                    Calendar now = Calendar.getInstance();
                                    String stringDate = now.getTime().toString();
                                    Integer year = now.get(Calendar.YEAR);
                                    Integer month = now.get(Calendar.MONTH) + 1;
                                    Integer day = now.get(Calendar.DAY_OF_MONTH);

                                    newRelative = new Relatives(id, title, fileLink, city,
                                            sex, circumstances, status,
                                            user, stringDate, age,
                                            phone, year, month, day);
                                    relativesDataBase.push().setValue(newRelative);
                                    Utility.showToast(AddRelativesStatement.this,
                                            "Заявление успешно добавлено");
//                                    startActivity(new Intent(AddRelativesStatement.this, UserPage.class));
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Utility.showToast(AddRelativesStatement.this, e.getMessage());
                        }
                    });
        } else {
            Utility.showToast(this, "Выберите фото");
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(imageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}









