package com.example.lostbutfound;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lostbutfound.Add.AddPeopleStatements;
import com.example.lostbutfound.Model.People;
import com.example.lostbutfound.Model.Things;
import com.example.lostbutfound.R;
import com.example.lostbutfound.User.LoginActivity;
import com.example.lostbutfound.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;

public class AddThings extends AppCompatActivity {

    TextInputLayout titleTextInputLayout, statusTextInputLayout;
    private AutoCompleteTextView locationEditText, statusEditText;
    private String[] cityArray = {"Караганда", "Астана", "Алмата"};
    private String[] statusArray = {"Поиски продолжаются", "Вещь найдена"};
    private TextInputEditText titleEditText, descriptionEditText,
            lastLocationEditText, phoneEditText;
    private Button addThingBtn;
    private ImageView imageView;
    private DatabaseReference thingDataBase;
    private Things newThing;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    TextView textView;
    boolean isEditMode = false;
    String id, title, imageUrl, location, status, description,
            lastLocation, user, phone;
    Integer year, month, day;
    String fileLink;
    String stringDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_things);

        init();
        adapterForAutoCompleteTextView();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openFileChooser();
                }
            });
            addThingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mUploadTask != null  && mUploadTask.isInProgress()) {
                        Toast.makeText(AddThings.this,
                                "Загрузка в процессе", Toast.LENGTH_SHORT).show();
                    } else {
                        addThingStatement();
//                        Toast.makeText(AddPeopleStatements.this, "Фото не добавлено", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Utility.showToast(this, "Вы не авторизованы");
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    void init(){
        locationEditText = findViewById(R.id.locationEditText);
        statusEditText = findViewById(R.id.statusEditText);
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        lastLocationEditText = findViewById(R.id.lastLocationEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        imageView = findViewById(R.id.imageView);
        addThingBtn = findViewById(R.id.addThingBtn);

        thingDataBase = FirebaseDatabase.getInstance().getReference("Things");
        mStorageRef = FirebaseStorage.getInstance().getReference("Things");

        titleTextInputLayout = findViewById(R.id.titleTextInputLayout);
        statusTextInputLayout = findViewById(R.id.statusTextInputLayout);
        textView = findViewById(R.id.textView);

        isEditMode = getIntent().getBooleanExtra("isEditMode", false);
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        lastLocation = getIntent().getStringExtra("lastLocation");
        location = getIntent().getStringExtra("location");
        status = getIntent().getStringExtra("status");
        imageUrl = getIntent().getStringExtra("imageUrl");
        user = getIntent().getStringExtra("user");
        phone = getIntent().getStringExtra("phone");

        if(id != null && !id.isEmpty()){
            isEditMode = true;
        }

        if(isEditMode){
            statusTextInputLayout.setVisibility(View.VISIBLE);
            statusEditText.setVisibility(View.VISIBLE);

            titleEditText.setText(title);
            descriptionEditText.setText(description);
            lastLocationEditText.setText(lastLocation);
            locationEditText.setText(location);
            statusEditText.setText(status);
            phoneEditText.setText(phone);

            Picasso.get()
                    .load(imageUrl)
                    .fit()
                    .centerInside()
                    .into(imageView);

            textView.setText("Обновить информацию");
            addThingBtn.setText("Изменить заявление");
        }

    }

    void adapterForAutoCompleteTextView(){
        ArrayAdapter<String> adapterCity = new ArrayAdapter (this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cityArray);
        ArrayAdapter<String> adapterStatus = new ArrayAdapter (this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, statusArray);
        locationEditText.setAdapter(adapterCity);
        statusEditText.setAdapter(adapterStatus);
    }

    void addThingStatement() {
        String title = titleEditText.getText().toString();
        String location = locationEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String lastLocation = lastLocationEditText.getText().toString();

        if(isEditMode){
            status = statusEditText.getText().toString();
        }
        if(isEditMode == false){
            status = statusArray[0];
        }

        String phone = phoneEditText.getText().toString();
        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(isEditMode == false){
            id = thingDataBase.push().getKey();
        }

        boolean isValidated = validateData(title, location, description,
                lastLocation, phone);
        if(!isValidated){
            return ;
        }

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
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    fileLink = task.getResult().toString();

                                    Calendar now = Calendar.getInstance();
                                    stringDate = now.getTime().toString();
                                    Integer year = now.get(Calendar.YEAR);
                                    Integer month = now.get(Calendar.MONTH) + 1;
                                    Integer day = now.get(Calendar.DAY_OF_MONTH);

                                    newThing = new Things(id, title, location, description, lastLocation,
                                            status, user, fileLink, LocalDate.now().toString(), phone, year, month, day);
                                    if(isEditMode){

                                        Query query = thingDataBase.orderByChild("id").equalTo(id);

                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                    if(dataSnapshot.exists()){
                                                        HashMap hashMap = new HashMap();
                                                        hashMap.put("id", id);
                                                        hashMap.put("title", title);
                                                        hashMap.put("location", location);
                                                        hashMap.put("description", description);
                                                        hashMap.put("lastLocation", lastLocation);
                                                        hashMap.put("status", status);
                                                        hashMap.put("phone", phone);
                                                        hashMap.put("user", user);
                                                        hashMap.put("imageUrl", fileLink);
                                                        hashMap.put("currentTime", LocalDate.now().toString());
                                                        hashMap.put("year", year);
                                                        hashMap.put("month", month);
                                                        hashMap.put("day", day);

                                                        dataSnapshot.getRef().updateChildren(hashMap);
                                                        Utility.showToast(AddThings.this,
                                                                "Заявление было изменено");
                                                        startActivity(new Intent(AddThings.this, MainActivity2.class));
                                                    } else {
                                                        Utility.showToast(AddThings.this,
                                                                "Заявление не найдено");
                                                        startActivity(new Intent(AddThings.this, MainActivity2.class));

                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    } else{
                                        thingDataBase.push().setValue(newThing);
                                        Utility.showToast(AddThings.this, "Заявление успешно добавлено");
                                        startActivity(new Intent(AddThings.this, MainActivity2.class));
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddThings.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            if(isEditMode){

                Calendar now = Calendar.getInstance();
                stringDate = now.getTime().toString();
                Integer year = now.get(Calendar.YEAR);
                Integer month = now.get(Calendar.MONTH) + 1;
                Integer day = now.get(Calendar.DAY_OF_MONTH);

                Query query = thingDataBase.orderByChild("id").equalTo(id);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.exists()){
                                HashMap hashMap = new HashMap();
                                hashMap.put("id", id);
                                hashMap.put("title", title);
                                hashMap.put("location", location);
                                hashMap.put("description", description);
                                hashMap.put("lastLocation", lastLocation);
                                hashMap.put("status", status);
                                hashMap.put("phone", phone);
                                hashMap.put("user", user);
                                hashMap.put("imageUrl", imageUrl);
                                hashMap.put("currentTime", LocalDate.now().toString());
                                hashMap.put("year", year);
                                hashMap.put("month", month);
                                hashMap.put("day", day);

                                dataSnapshot.getRef().updateChildren(hashMap);
                                Utility.showToast(AddThings.this,
                                        "Заявление было изменено");
                                startActivity(new Intent(AddThings.this, MainActivity2.class));
                            } else {
                                Utility.showToast(AddThings.this,
                                        "Заявление не найдено");
                                startActivity(new Intent(AddThings.this, MainActivity2.class));

                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


            } else {
                Toast.makeText(this, "Выберите фото", Toast.LENGTH_SHORT).show();
            }
        }
    }

    boolean validateData(String title, String location,
                         String description, String lastLocation,
                         String phone){
        if(title.length() == 0){
            titleEditText.setError("Введите заголовок объявления");
            return false;
        }
        if(location.length() == 0){
            locationEditText.setError("Выберите город");
            return false;
        }
        if(description.length() == 0){
            descriptionEditText.setText("Неизвестно");
            return false;
        }
        if(lastLocation.length() == 0){
            lastLocationEditText.setText("Неизвестно");
            return false;
        }
        if(phone.length() == 0){
            phoneEditText.setError("Укажите номер для связи");
            return false;
        }

        return true;
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








