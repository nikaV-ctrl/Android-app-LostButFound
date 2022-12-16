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
import android.util.Patterns;
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
import com.example.lostbutfound.Model.People;
import com.example.lostbutfound.R;
import com.example.lostbutfound.User.LoginActivity;
import com.example.lostbutfound.User.UserPage;
import com.example.lostbutfound.Utility;
import com.example.lostbutfound.itemPageView.HumanPageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.Map;

public class AddPeopleStatements extends AppCompatActivity {

    TextInputLayout titleTextInputLayout, statusTextInputLayout;

    private AutoCompleteTextView sexEditText, locationEditText, statusEditText;
    private String[] sexArray = {"Женщина", "Мужчина"};
    private String[] cityArray = {"Караганда", "Астана", "Алмата"};
    private String[] statusArray = {"Поиски продолжаются", "Человек найден. Жив",
            "Человек найден. Погиб"};
    private TextInputEditText titleEditText, fioEditText, ageEditText,
            circumstancesEditText, signsEditText, clothesEditText,
            lastLocationEditText, phoneEditText;
    private Button addHumanBtn;
    private ImageView imageView;
    private DatabaseReference humanDataBase = FirebaseDatabase.getInstance().getReference("People");
    private People newPeople;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    String stringDate;


    TextView textView;
    boolean isEditMode = false;
    String id, title, fio, imageUrl, location, status, circumstances,
            signs, clothes, lastLocation, sex, user, phone;
    Integer age, year, month, day;

//    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
//    String em = currentUser;

    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people_statements);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            init();
            adapterForAutoCompleteTextView();

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openFileChooser();
                }
            });
            addHumanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mUploadTask != null  && mUploadTask.isInProgress()) {
                        Toast.makeText(AddPeopleStatements.this,
                                "Загрузка в процессе", Toast.LENGTH_SHORT).show();
                    } else {
                        addHumanStatement();
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
        sexEditText = findViewById(R.id.sexEditText);
        locationEditText = findViewById(R.id.locationEditText);
        statusEditText = findViewById(R.id.statusEditText);
        titleEditText = findViewById(R.id.titleEditText);
        fioEditText = findViewById(R.id.fioEditText);
        ageEditText = findViewById(R.id.ageEditText);
        circumstancesEditText = findViewById(R.id.circumstancesEditText);
        signsEditText = findViewById(R.id.signsEditText);
        clothesEditText = findViewById(R.id.clothesEditText);
        lastLocationEditText = findViewById(R.id.lastLocationEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        imageView = findViewById(R.id.imageView);
        addHumanBtn = findViewById(R.id.addHumanBtn);

        mStorageRef = FirebaseStorage.getInstance().getReference("People");

        titleTextInputLayout = findViewById(R.id.titleTextInputLayout);
        statusTextInputLayout = findViewById(R.id.statusTextInputLayout);
        textView = findViewById(R.id.textView);

        isEditMode = getIntent().getBooleanExtra("isEditMode", false);
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        fio = getIntent().getStringExtra("fio");
        sex = getIntent().getStringExtra("sex");
        circumstances = getIntent().getStringExtra("circumstances");
        signs = getIntent().getStringExtra("signs");
        clothes = getIntent().getStringExtra("clothes");
        lastLocation = getIntent().getStringExtra("lastLocation");
        location = getIntent().getStringExtra("location");
        status = getIntent().getStringExtra("status");
        age = getIntent().getIntExtra("age", 0);
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
            fioEditText.setText(fio);
            sexEditText.setText(sex);
            circumstancesEditText.setText(circumstances);
            signsEditText.setText(signs);
            clothesEditText.setText(clothes);
            lastLocationEditText.setText(lastLocation);
            locationEditText.setText(location);
            statusEditText.setText(status);
            ageEditText.setText(age.toString());
            phoneEditText.setText(phone);

            Picasso.get()
                    .load(imageUrl)
                    .fit()
                    .centerInside()
                    .into(imageView);

            textView.setText("Обновить информацию");
            addHumanBtn.setText("Изменить заявление");
        }

    }

    void adapterForAutoCompleteTextView(){
        ArrayAdapter<String> adapterSex = new ArrayAdapter (this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sexArray);
        ArrayAdapter<String> adapterCity = new ArrayAdapter (this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cityArray);
        ArrayAdapter<String> adapterStatus = new ArrayAdapter (this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, statusArray);
        sexEditText.setAdapter(adapterSex);
        locationEditText.setAdapter(adapterCity);
        statusEditText.setAdapter(adapterStatus);
    }

    void addHumanStatement() {
        String title = titleEditText.getText().toString();
        String fio = fioEditText.getText().toString();
        String location = locationEditText.getText().toString();
        String sex = sexEditText.getText().toString();
        String circumstances = circumstancesEditText.getText().toString();
        String signs = signsEditText.getText().toString();
        String clothes = clothesEditText.getText().toString();
        String lastLocation = lastLocationEditText.getText().toString();

        if(isEditMode){
            status = statusEditText.getText().toString();
        }
        if(isEditMode == false){
            status = statusArray[0];
        }

        Integer age = Integer.parseInt(ageEditText.getText().toString());
        String phone = phoneEditText.getText().toString();
        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(isEditMode == false){
            id = humanDataBase.push().getKey();
        }

        boolean isValidated = validateData(title, fio, location, sex, circumstances,
                signs, clothes, lastLocation, age, phone);
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

                                    newPeople = new People(id, title, fio, location, sex, circumstances,
                                            signs, clothes, lastLocation, status, age, phone, user,
                                            fileLink, LocalDate.now().toString(), year, month, day);
                                    if(isEditMode){

                                        Query query = humanDataBase.orderByChild("id").equalTo(id);
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                    if(dataSnapshot.exists()){
                                                        HashMap hashMap = new HashMap();
                                                        hashMap.put("id", id);
                                                        hashMap.put("title", title);
                                                        hashMap.put("fio", fio);
                                                        hashMap.put("location", location);
                                                        hashMap.put("sex", sex);
                                                        hashMap.put("circumstances", circumstances);
                                                        hashMap.put("signs", signs);
                                                        hashMap.put("clothes", clothes);
                                                        hashMap.put("lastLocation", lastLocation);
                                                        hashMap.put("status", status);
                                                        hashMap.put("age", age);
                                                        hashMap.put("phone", phone);
                                                        hashMap.put("user", user);
                                                        hashMap.put("imageUrl", fileLink);
                                                        hashMap.put("currentTime", LocalDate.now().toString());
                                                        hashMap.put("year", year);
                                                        hashMap.put("month", month);
                                                        hashMap.put("day", day);

                                                        dataSnapshot.getRef().updateChildren(hashMap);
                                                        Utility.showToast(AddPeopleStatements.this,
                                                                "Заявление было изменено");
                                                        startActivity(new Intent(AddPeopleStatements.this, MainActivity2.class));
                                                    } else {
                                                        Utility.showToast(AddPeopleStatements.this,
                                                                "Заявление не найдено");
                                                        startActivity(new Intent(AddPeopleStatements.this, MainActivity2.class));
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    } else{
                                        humanDataBase.push().setValue(newPeople);
                                        Utility.showToast(AddPeopleStatements.this, "Заявление успешно добавлено");
                                        startActivity(new Intent(AddPeopleStatements.this, MainActivity2.class));
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddPeopleStatements.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            if(isEditMode){

                Calendar now = Calendar.getInstance();
                stringDate = now.getTime().toString();
                Integer year = now.get(Calendar.YEAR);
                Integer month = now.get(Calendar.MONTH) + 1;
                Integer day = now.get(Calendar.DAY_OF_MONTH);

                Query query = humanDataBase.orderByChild("id").equalTo(id);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.exists()){
                                HashMap hashMap = new HashMap();
                                hashMap.put("id", id);
                                hashMap.put("title", title);
                                hashMap.put("fio", fio);
                                hashMap.put("location", location);
                                hashMap.put("sex", sex);
                                hashMap.put("circumstances", circumstances);
                                hashMap.put("signs", signs);
                                hashMap.put("clothes", clothes);
                                hashMap.put("lastLocation", lastLocation);
                                hashMap.put("status", status);
                                hashMap.put("age", age);
                                hashMap.put("phone", phone);
                                hashMap.put("user", user);
                                hashMap.put("imageUrl", imageUrl);
                                hashMap.put("currentTime", LocalDate.now().toString());
                                hashMap.put("year", year);
                                hashMap.put("month", month);
                                hashMap.put("day", day);

                                dataSnapshot.getRef().updateChildren(hashMap);
                                Utility.showToast(AddPeopleStatements.this,
                                        "Заявление было изменено");
                                startActivity(new Intent(AddPeopleStatements.this, MainActivity2.class));
                            } else {
                                Utility.showToast(AddPeopleStatements.this,
                                        "Заявление не найдено");
                                startActivity(new Intent(AddPeopleStatements.this, MainActivity2.class));
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

    boolean validateData(String title, String fio, String location,
                         String sex, String circumstances, String signs,
                         String clothes, String lastLocation, Integer age,
                         String phone){
        if(title.length() == 0){
            titleEditText.setError("Введите заголовок объявления");
            return false;
        }
        if(fio.length() == 0){
            fioEditText.setError("Введите ФИО");
            return false;
        }
        if(location.length() == 0){
            locationEditText.setError("Выберите город");
            return false;
        }
        if(sex.length() == 0){
            sexEditText.setError("Выберите пол");
            return false;
        }
        if(circumstances.length() == 0){
            circumstancesEditText.setText("Неизвестно");
            return false;
        }
        if(signs.length() == 0){
            signsEditText.setText("Неизвестно");
            return false;
        }
        if(clothes.length() == 0){
            clothesEditText.setText("Неизвестно");
            return false;
        }
        if(lastLocation.length() == 0){
            lastLocationEditText.setText("Неизвестно");
            return false;
        }
        if(age == 0){
            ageEditText.setError("Укажите хотя бы приблизительный возраст");
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









