package com.project.havewemet.model;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

class FirebaseModel {

    private final FirebaseAuth auth;
    private FirebaseUser fbUser;

    private final FirebaseFirestore dbFirestore;  //for storing user and status data
    private final FirebaseStorage storageFirestore;  //storage for storing profile images

    FirebaseModel(){
        storageFirestore = FirebaseStorage.getInstance();
        dbFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        dbFirestore.setFirestoreSettings(settings);

        auth = FirebaseAuth.getInstance();
        fbUser = auth.getCurrentUser();
    }

    void getAllAppUsersSince(Long since, Model.Listener<List<AppUser>> listener){
        dbFirestore.collection(AppUser.COLLECTION)
                .whereGreaterThanOrEqualTo(AppUser.LAST_UPDATED, new Timestamp(since,0))
                //todo: add since, or remove  comment above
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<AppUser> userList = new LinkedList<>();
                        if (task.isSuccessful()){
                            QuerySnapshot jsonsList = task.getResult();
                            for (DocumentSnapshot json: jsonsList){
                                AppUser appUser = AppUser.fromJson(json.getData());
                                appUser.setId(json.getId());
                                userList.add(appUser);
                            }
                        }
                        listener.onComplete(userList);
                    }
                });
    }

    void getAllStatusesSince(Long since, Model.Listener<List<Status>> listener){
        dbFirestore.collection(Status.COLLECTION)
                .whereGreaterThanOrEqualTo(Status.LAST_UPDATED, new Timestamp(since,0))
                //todo: add since, or remove  comment above
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Status> statusList = new LinkedList<>();
                        if (task.isSuccessful()){
                            QuerySnapshot jsonsList = task.getResult();
                            for (DocumentSnapshot json: jsonsList){
                                Status status = Status.fromJson(json.getData());
                                statusList.add(status);
                            }
                        }
                        listener.onComplete(statusList);
                    }
                });
    }

    void addStatus(Status status, Model.Listener<Void> listener){
        dbFirestore.collection(Status.COLLECTION)
                .add(status.toJson())
                .addOnCompleteListener(task -> {listener.onComplete(null);});
    }

    void addAppUser(AppUser appUser, Model.Listener<Boolean> listener){
        dbFirestore.collection(AppUser.COLLECTION)
                .document(fbUser.getUid())
                .set(appUser.toJson())
                .addOnCompleteListener(saveUserInfoTask ->{
                    if (saveUserInfoTask.isSuccessful()){
                        listener.onComplete(true);
                    }else{
                        //if app fails to save extra info related to this user
                        //then delete the user and let user allow to create a new one
                        fbUser.delete();
                        listener.onComplete(false);
                    }
                });
    }

    void signIn(String email, String pass, Model.Listener<Boolean> listener){
        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        fbUser = task.getResult().getUser(); // get the firebase user instance
                        listener.onComplete(true);
                    }
                    else
                        listener.onComplete(false);
                });
    }

    // as we are storing extra info of appuser, so pass the appUser as well
    void signUp(String email, String pass, AppUser appUser, Model.Listener<Boolean> listener){
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        fbUser = task.getResult().getUser();
                        Log.e("task", "user created!");
                        //if signup successful, then the firebase user becomes the appuser
                        addAppUser(appUser, listener);
                    }
                    else{
                        listener.onComplete(false);
                        Log.e("task", "user not created!");
                    }
                });
    }

    FirebaseUser getFirebaseUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void signout() {
        auth.signOut();
    }

    public void getUserById(String userId, Model.Listener<AppUser> listener){
        dbFirestore.collection(AppUser.COLLECTION)
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        listener.onComplete(AppUser.fromJson(task.getResult().getData()));
                    }
                });
    }

    public void editProfile(AppUser appUser, String email, Model.Listener<Boolean> listener) {
        dbFirestore.collection(AppUser.COLLECTION)
                .document(fbUser.getUid())
                .set(appUser.toJson())
                .addOnCompleteListener(taskUserUpdate -> {
                    if (taskUserUpdate.isSuccessful()){
                        if (!email.equals(fbUser.getEmail())) { //check if there is need to update email
                            fbUser.updateEmail(email)
                                    .addOnCompleteListener(taskUpdateEmail->listener.onComplete(taskUpdateEmail.isSuccessful()));
                        }else{
                            listener.onComplete(true);
                        }

                    }else{
                        listener.onComplete(false);
                    }
                });
    }

    void uploadImage(String name, Bitmap bitmap, Model.Listener<String> listener){
        StorageReference storageRef = storageFirestore.getReference();
        StorageReference imagesRef = storageRef.child("images/" + name + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);

        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                imagesRef.getDownloadUrl().addOnSuccessListener(uri -> listener.onComplete(uri.toString()));
            }else{
                listener.onComplete(null);
            }
        });
    }
}
