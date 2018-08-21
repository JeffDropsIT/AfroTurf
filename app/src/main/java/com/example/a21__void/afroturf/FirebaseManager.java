package com.example.a21__void.afroturf;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FieldValue;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public class FirebaseManager {


    public static final String TAG = "OnFirebase";
    private static FirebaseManager instance;
//    private FirebaseFirestore db;
//    private DocumentReference docRef;

    private FirebaseManager(){

    }

    public void init(String collection, String docPath){
//        this.db = FirebaseFirestore.getInstance();
//        this.docRef = db.collection(collection).document(docPath);
    }

    public void addSite(String collectionName, Map object){

//        db.collection(collectionName).add(object).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Log.d(TAG, "DocumentSnapshot added with ID" + documentReference.getId());
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w(TAG, "Error adding document", e);
//            }
//        });

    }

    public void addUser(String collection, String username, String name, String gender){
//        Map<String, Object> user = new HashMap<>();
//        user.put("username", username);
//        user.put("name", name);
//        user.put("activeSince", FieldValue.serverTimestamp());
//        user    .put("gender", gender);
//        addSite(collection, user);

    }
    public void sendEventType(String collection, String description, String pointId, int eventID, String siteID){
//        Map<String, Object> event = new HashMap<>();
//        event.put("siteId", siteID);
//        event.put("eventId", eventID);
//        event.put("pointId", pointId);
//        event.put("description", description);
//        event.put("timeStamp", FieldValue.serverTimestamp());
//        event.put("location", "N-S");
//        addSite(collection, event);

    }

    public void getSalon(final DataCallback dataCallback){

//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//
//                        Map<String, Object> data = document.getData();
//                        data.put("location", document.getString("location"));
//                        data.put("rating", document.getString("rating"));
//                        data.put("address",  document.getString("address"));
//                        data.put("street",  document.getString("street"));
//
//                        if(dataCallback != null){
//                            dataCallback.onDataReceived(data);
//                        }
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });

    }

    public static FirebaseManager getInstance(){
        if(instance == null)
            instance = new FirebaseManager();

        return instance;
    }

    public void setSettings() {
//        this.db.setFirestoreSettings(settings);
    }

    public interface DataCallback{
        void onDataReceived(Map<String, Object> data);
    }


}
