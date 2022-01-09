package com.example.musicbook.ui.user;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicbook.OptionActivity;
import com.example.musicbook.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import javax.xml.transform.Result;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class UserFragment extends Fragment {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 400;
    //
    String cameraPermissions[];
    String storagePermissions[];
    //for checking profile or cover photo
    String profileOrCoverPhoto;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //Storage
    StorageReference storageReference;
    //path where images of user profile and cover will stored
    String storagePath = "User_Profile_Cover_Imgs/";

    //view from xml
    CircleImageView mAvatarimv;
    ImageView mbackgroundimv, mMenuimv;
    TextView mNameTV, mSchoolTV, mLocationTV, mJobTV;
    ImageButton mEditProfileBtn;
    ProgressDialog pd;
    Uri uri_image;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference= FirebaseStorage.getInstance().getReference(); //firebase storage reference

        //init arrays of permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //take id
        mAvatarimv = view.findViewById(R.id.imgv_avatar);
        mbackgroundimv = view.findViewById(R.id.img_user_background);
        mNameTV = view.findViewById(R.id.tv_UserName);
        mJobTV = view.findViewById(R.id.tv_job);
        mLocationTV = view.findViewById(R.id.tv_location);
        mSchoolTV = view.findViewById(R.id.tv_school);
        mMenuimv=view.findViewById(R.id.btn_menu);

        mEditProfileBtn = view.findViewById(R.id.img_btn_more);

        //init progress dialog
        pd = new ProgressDialog(getActivity());


        //Query
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //check until required data get
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //get data
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue();
                    String background = "" + ds.child("cover").getValue();
                    String location = "" + ds.child("location").getValue();
                    String school = "" + ds.child("school").getValue();
                    String job = "" + ds.child("job").getValue();

                    //set data
                    mNameTV.setText(name);
                    if (location.isEmpty()) {
                        mLocationTV.setVisibility(View.GONE);
                    } else {
                        mLocationTV.setText("Quê quán "+ location);
                    }

                    if (job.isEmpty()) {
                        mJobTV.setVisibility(View.GONE);
                    } else {
                        mJobTV.setText("Làm việc tại "+job);
                    }

                    if (school.isEmpty()) {
                        mSchoolTV.setVisibility(View.GONE);
                    } else {
                        mSchoolTV.setText("Học tại "+school);
                    }
                    try {
                        Picasso.get().load(image).into(mAvatarimv);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.blankavatar).into(mAvatarimv);

                    }
                    try {
                        Picasso.get().load(background).fit().into(mbackgroundimv);
                    }catch (Exception e){
                        Picasso.get().load(R.color.blank_space).into(mbackgroundimv);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        mEditProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });
        mMenuimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OptionActivity.class));

            }
        });
        // Inflate the layout for this fragment
        return view;

    }

    private void pickFromCamera() {
        //Intent of picking image from device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        //put img uri
        uri_image = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //intent to start camera

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri_image);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_REQUEST_CODE);
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_REQUEST_CODE);
    }


    private boolean checkStoragePermission() {
        //check if storage permission is enable or not, true if enable
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        requestPermissions( storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        //check if storage permission is enable or not, true if enable
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
       requestPermissions( cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void showEditProfileDialog() {
        /*Show dialog containing options
        1. Edit Profile picture
        2. Edit Cover photo
        3. Edit name
        4. EEdit name
         */


        String[] options = {"Chỉnh sửa ảnh đại diện",
                "Chỉnh sửa ảnh nền",
                "Đổi tên",
                "Sửa thông tin cá nhân"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Chọn hành động");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item clicks
                if (which == 0) {
                    //Edit Profile picture
                    pd.setMessage("Cập nhật ảnh đại diện");
                    profileOrCoverPhoto = "image";
                    showImagePicDialog();

                } else if (which == 1) {
                    //Edit Cover photo
                    pd.setMessage("Thay đổi ảnh nền");
                    profileOrCoverPhoto = "cover";
                    showImagePicDialog();
                } else if (which == 2) {
                    //Edit name
                    pd.setMessage("Thay đổi tên");
                    showInfoUpdateDialog("name");
                } else if (which == 3) {
                    //Edit info
                    pd.setMessage("Cập nhật thông tin");
                    showOtherInfo();
                }
            }
        });
        builder.create().show();


    }

    private void showImagePicDialog() {
        //show dialog containong options camera and gallery to pick
        String[] options = {"Camera",
                "Bộ sưu tập"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Chọn ảnh");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item clicks
                if (which == 0) {
                    //Camera

                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }

                } else if (which == 1) {
                    //Gallery
                    if (!checkStoragePermission()) {
                        requestStoragePermission();

                    } else {
                        pickFromGallery();
                    }

                }

            }
        });
        builder.create().show();
    }

    private void showOtherInfo() {
        //show dialog containong options camera and gallery to pick

        String[] options = {"Số điện thoại",
                "Địa chỉ","Công việc","Trường học"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Chọn thông tin");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item clicks
                if (which == 0) {
                    //SDT
                    showInfoUpdateDialog("phone");


                } else if (which == 1) {
                    //Địa chỉ
                    showInfoUpdateDialog("location");


                }
                else if (which == 2) {
                    //Công việc
                    showInfoUpdateDialog("job");


                }
                else if (which == 3) {
                    //trường học
                    showInfoUpdateDialog("school");


                }


            }
        });
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        //this method will call when you click allow or deny from permission request dialog

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean camereAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camereAccepted && writeStorageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(getActivity(), "Vui lòng cho phép truy cập camera và bộ nhớ", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            }

            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    boolean writeStoragerAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStoragerAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(getActivity(), "Vui lòng cho phép truy cập vào bộ nhớ", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            }

        }
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode==RESULT_OK&&requestCode == IMAGE_PICK_CAMERA_REQUEST_CODE&& data.getData()!=null) {
                uri_image = data.getData();
                Picasso.get().load(uri_image).into(mAvatarimv);
                Log.d("onAR",String.valueOf(uri_image));
                uploadProfileCoverPhoto(uri_image);
            }
//            else{
//                Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
//            }

            if (resultCode==RESULT_OK&&requestCode == IMAGE_PICK_GALLERY_REQUEST_CODE&& data.getData()!=null) {
                uri_image = data.getData();
                Log.d("onAR",String.valueOf(uri_image));
                uploadProfileCoverPhoto(uri_image);
            }



    }

    private void uploadProfileCoverPhoto(Uri uri) {
        //path and name of image to be stored in firebase storage
        String filePathAndName = storagePath + "" + profileOrCoverPhoto + "_" + user.getUid();

        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //image is upload to storage, now get its url and store in user's database
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri downloaduri = uriTask.getResult();
                        //check if image is uploaded or not and uri is received
                        if (uriTask.isSuccessful()) {
                            //image uploađe
                            HashMap<String, Object> results = new HashMap<>();
                            results.put(profileOrCoverPhoto, downloaduri.toString());
                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            //url in database of user is added successfully
                                            //dismiss progess bar
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Image Updated", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            //error adding url in database of user
                                            //đismiss progress bar
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Error Updated Image", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        } else {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                        pd.setMessage("Uploading");
                        pd.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void showInfoUpdateDialog(String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cập nhật " + key);

        //set layout of dialog
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //add edit text
        EditText editText = new EditText(getActivity());
        editText.setHint("Enter " + key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);
        builder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                String value = editText.getText().toString().trim();
                //validate if user has entered something or not
                if (!TextUtils.isEmpty(value)) {
                    pd.show();
                    HashMap<String,Object> result=new HashMap<>();
                    result.put(key,value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Có lỗi gì đó đã xảy ra", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(getActivity(), "Hãy nhập " + key, Toast.LENGTH_SHORT).show();
                }



        }
    }).

    setNegativeButton("Hủy",new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog,int which){

        }
    })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                });

        builder.create().show();

}
}