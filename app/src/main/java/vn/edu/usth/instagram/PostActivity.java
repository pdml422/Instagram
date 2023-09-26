package vn.edu.usth.instagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.socialview.widget.SocialAutoCompleteTextView;
import com.hendraanggrian.appcompat.socialview.widget.SocialTextView;

import java.util.HashMap;
import java.util.List;

import vn.edu.usth.instagram.Fragment.HomeFragment;

public class PostActivity extends AppCompatActivity {


    private Uri imageUri;
    private ImageView close;
    private ImageView imageAdded;
    private TextView post;
    private String imageUrl;
    SocialAutoCompleteTextView description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        close = findViewById(R.id.close);
        imageAdded = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);

        imageAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(PostActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(101);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });
    }

    private void upload() {

        if (imageUri != null) {
            StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            StorageTask uploadtask = filePath.putFile(imageUri);
            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                    String postId = ref.push().getKey();

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("postid", postId);
                    map.put("imageurl", imageUrl);
                    map.put("description", description.getText().toString());
                    map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                    ref.child(postId).setValue(map);

                    DatabaseReference mHashTagRef = FirebaseDatabase.getInstance().getReference().child("HashTags");
                    List<String> hashTags = description.getHashtags();
                    if (!hashTags.isEmpty()) {
                        for (String tag : hashTags) {
                            map.clear();

                            map.put("tag", tag.toLowerCase());
                            map.put("postid", postId);

                            mHashTagRef.child(tag.toLowerCase()).child(postId).setValue(map);
                        }
                    }
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No Image was selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            imageAdded.setImageURI(imageUri);
        } else {
            Toast.makeText(getApplicationContext(), "no image selected", Toast.LENGTH_SHORT).show();
        }
    }
}