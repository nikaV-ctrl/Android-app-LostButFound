package com.example.lostbutfound.Add;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.lostbutfound.AddThings;
import com.example.lostbutfound.MainActivity2;
import com.example.lostbutfound.Model.Animals;
import com.example.lostbutfound.Model.People;
import com.example.lostbutfound.R;
import com.example.lostbutfound.User.LoginActivity;
import com.example.lostbutfound.User.UserPage;
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

public class AddAnimalsStatements extends AppCompatActivity {

    TextInputLayout titleTextInputLayout, statusTextInputLayout;

    private AutoCompleteTextView cityEditText, statusEditText;
    private String[] cityArray = {"Караганда", "Астана", "Алмата"};
    private String[] statusArray = {"Поиски продолжаются", "Животное найдено"};

    private TextInputEditText titleEditText, circumstancesEditText, descriptionEditText,
            breedEditText, lastLocationEditText, rewardEditText, phoneEditText;

    private Button addAnimalBtn;
    private ImageView imageView;
    private DatabaseReference animalsDataBase;
    private Animals newAnimal;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    TextView textView;
    boolean isEditMode = false;
    String id, title, imageUrl, city, circumstances, description,
            breed, lastLocation, status, reward, user, phone;
    String stringDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_animals_statements);

        init();
        adapterForAutoCompleteTextView();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openFileChooser();
                }
            });
            addAnimalBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mUploadTask != null  && mUploadTask.isInProgress()) {
                        Toast.makeText(AddAnimalsStatements.this, "Загрузка в процессе", Toast.LENGTH_SHORT).show();
                    } else {
                        addAnimalStatement();
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
        cityEditText = findViewById(R.id.cityEditText);
        statusEditText = findViewById(R.id.statusEditText);
        titleEditText = findViewById(R.id.titleEditText);
        circumstancesEditText = findViewById(R.id.circumstancesEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        breedEditText = findViewById(R.id.breedEditText);
        lastLocationEditText = findViewById(R.id.lastLocationEditText);
        rewardEditText = findViewById(R.id.rewardEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        imageView = findViewById(R.id.imageView);
        addAnimalBtn = findViewById(R.id.addAnimalBtn);

        animalsDataBase = FirebaseDatabase.getInstance().getReference("Animals");
        mStorageRef = FirebaseStorage.getInstance().getReference("Animals");

        titleTextInputLayout = findViewById(R.id.titleTextInputLayout);
        statusTextInputLayout = findViewById(R.id.statusTextInputLayout);
        textView = findViewById(R.id.textView);

        isEditMode = getIntent().getBooleanExtra("isEditMode", false);
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        imageUrl = getIntent().getStringExtra("imageUrl");
        city = getIntent().getStringExtra("city");
        circumstances = getIntent().getStringExtra("circumstances");
        description = getIntent().getStringExtra("description");
        breed = getIntent().getStringExtra("breed");
        lastLocation = getIntent().getStringExtra("lastLocation");
        status = getIntent().getStringExtra("status");
        reward = getIntent().getStringExtra("reward");
        user = getIntent().getStringExtra("user");
        phone = getIntent().getStringExtra("phone");

        if(id != null && !id.isEmpty()){
            isEditMode = true;
        }

        if(isEditMode){
            statusTextInputLayout.setVisibility(View.VISIBLE);
            statusEditText.setVisibility(View.VISIBLE);

            titleEditText.setText(title);
            cityEditText.setText(city);
            circumstancesEditText.setText(circumstances);
            descriptionEditText.setText(description);
            breedEditText.setText(breed);
            lastLocationEditText.setText(lastLocation);
            statusEditText.setText(status);
            rewardEditText.setText(reward);
            phoneEditText.setText(phone);

            Picasso.get()
                    .load(imageUrl)
                    .fit()
                    .centerInside()
                    .into(imageView);

            textView.setText("Обновить информацию");
            addAnimalBtn.setText("Изменить заявление");
        }
    }

    void adapterForAutoCompleteTextView(){
        ArrayAdapter<String> adapterCity = new ArrayAdapter (this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cityArray);
        ArrayAdapter<String> adapterStatus = new ArrayAdapter (this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, statusArray);
        cityEditText.setAdapter(adapterCity);
        statusEditText.setAdapter(adapterStatus);
    }

    void addAnimalStatement() {
        String title = titleEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String circumstances = circumstancesEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String breed = breedEditText.getText().toString();
        String lastLocation = lastLocationEditText.getText().toString();

        if(isEditMode){
            status = statusEditText.getText().toString();
        }
        if(isEditMode == false){
            status = statusArray[0];
        }

        String reward = rewardEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(isEditMode == false){
            id = animalsDataBase.push().getKey();
        }

        boolean isValidated = validateData(title, city, circumstances, description,
                breed, lastLocation, reward, phone);
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
                                    String fileLink = task.getResult().toString();

                                    Calendar now = Calendar.getInstance();
                                    String stringDate = now.getTime().toString();
                                    Integer year = now.get(Calendar.YEAR);
                                    Integer month = now.get(Calendar.MONTH) + 1;
                                    Integer day = now.get(Calendar.DAY_OF_MONTH);

                                    newAnimal = new Animals(id, title, fileLink, city, circumstances, description,
                                            breed, lastLocation, status, reward, LocalDate.now().toString(),
                                            user, phone, year, month, day);
                                    if(isEditMode){

                                        Query query = animalsDataBase.orderByChild("id").equalTo(id);
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                    if(dataSnapshot.exists()){
                                                        HashMap hashMap = new HashMap();
                                                        hashMap.put("id", id);
                                                        hashMap.put("title", title);
                                                        hashMap.put("city", city);
                                                        hashMap.put("circumstances", circumstances);
                                                        hashMap.put("description", description);
                                                        hashMap.put("breed", breed);
                                                        hashMap.put("lastLocation", lastLocation);
                                                        hashMap.put("status", status);
                                                        hashMap.put("reward", reward);
                                                        hashMap.put("phone", phone);
                                                        hashMap.put("user", user);
                                                        hashMap.put("imageUrl", fileLink);
                                                        hashMap.put("currentTime", LocalDate.now().toString());
                                                        hashMap.put("year", year);
                                                        hashMap.put("month", month);
                                                        hashMap.put("day", day);

                                                        dataSnapshot.getRef().updateChildren(hashMap);
                                                        Utility.showToast(AddAnimalsStatements.this,
                                                                "Заявление было изменено");
                                                        startActivity(new Intent(AddAnimalsStatements.this, MainActivity2.class));
                                                    } else {
                                                        Utility.showToast(AddAnimalsStatements.this,
                                                                "Заявление не найдено");
                                                        startActivity(new Intent(AddAnimalsStatements.this, MainActivity2.class));
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    } else{
                                        animalsDataBase.push().setValue(newAnimal);
                                        Utility.showToast(AddAnimalsStatements.this, "Заявление успешно добавлено");
                                        startActivity(new Intent(AddAnimalsStatements.this, MainActivity2.class));
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Utility.showToast(AddAnimalsStatements.this, e.getMessage());
                        }
                    });
        } else {
            if(isEditMode){

                Calendar now = Calendar.getInstance();
                stringDate = now.getTime().toString();
                Integer year = now.get(Calendar.YEAR);
                Integer month = now.get(Calendar.MONTH) + 1;
                Integer day = now.get(Calendar.DAY_OF_MONTH);

                Query query = animalsDataBase.orderByChild("id").equalTo(id);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.exists()){
                                HashMap hashMap = new HashMap();
                                hashMap.put("id", id);
                                hashMap.put("title", title);
                                hashMap.put("city", city);
                                hashMap.put("circumstances", circumstances);
                                hashMap.put("description", description);
                                hashMap.put("breed", breed);
                                hashMap.put("lastLocation", lastLocation);
                                hashMap.put("status", status);
                                hashMap.put("reward", reward);
                                hashMap.put("phone", phone);
                                hashMap.put("user", user);
                                hashMap.put("imageUrl", imageUrl);
                                hashMap.put("currentTime", LocalDate.now().toString());
                                hashMap.put("year", year);
                                hashMap.put("month", month);
                                hashMap.put("day", day);

                                dataSnapshot.getRef().updateChildren(hashMap);
                                Utility.showToast(AddAnimalsStatements.this,
                                        "Заявление было изменено");
                                startActivity(new Intent(AddAnimalsStatements.this, MainActivity2.class));
                            } else {
                                Utility.showToast(AddAnimalsStatements.this,
                                        "Заявление не найдено");
                                startActivity(new Intent(AddAnimalsStatements.this, MainActivity2.class));
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } else {
                Toast.makeText(this, "Выберите фото", Toast.LENGTH_SHORT).show();
            }        }
    }

    boolean validateData(String title, String city, String circumstances,
                         String description, String breed, String lastLocation,
                         String reward, String phone){

        if(title.length() == 0){
            titleEditText.setError("Введите заголовок объявления");
            return false;
        }
        if(city.length() == 0){
            cityEditText.setError("Выберите город");
            return false;
        }
        if(circumstances.length() == 0){
            circumstancesEditText.setText("Неизвестно");
            return false;
        }
        if(description.length() == 0){
            descriptionEditText.setError("Неизвестно");
            return false;
        }
        if(breed.length() == 0){
            breedEditText.setText("Неизвестно");
            return false;
        }
        if(lastLocation.length() == 0){
            lastLocationEditText.setText("Неизвестно");
            return false;
        }
        if(reward.length() == 0){
            rewardEditText.setText("Неизвестно");
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
