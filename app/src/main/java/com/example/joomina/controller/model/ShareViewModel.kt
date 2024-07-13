package com.example.joomina.controller.model

import android.graphics.Bitmap
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import com.example.joomina.data.Account
import com.example.joomina.data.Product
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class ShareViewModel() : ViewModel() {

    fun addAccount(account: Account) {
        CoroutineScope(Dispatchers.IO).launch {
            val fireStoreRef = Firebase.firestore
                .collection("Account")
                .document(account.userName)

            try {
                fireStoreRef.set(account).await()
            } catch (e: Exception) {
                println("Lỗi khi thêm tài khoản: ${e.message}")
            }
        }
    }

    fun getAllAccount(listAccount: (List<Account>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val fireStoreRef = Firebase.firestore.collection("Account")

            try {
                val querySnapshot = fireStoreRef.get().await()
                val listOfAccount = mutableListOf<Account>()

                for (document in querySnapshot) {
                    val account = document.toObject(Account::class.java)
                    listOfAccount.add(account)
                }

                listAccount(listOfAccount)
            } catch (e: Exception) {
                Log.e("getAllAccount", "Error getting documents: ", e)
                listAccount(emptyList())
            }
        }
    }

    fun retrieveData(
        userName: String,
        data: (Account) -> Unit,
        onError: (Exception) -> Unit = {}
    ) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("Account")
            .document(userName)
        try {
            fireStoreRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onError(e)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val account = snapshot.toObject(Account::class.java)
                    if (account != null) {
                        CoroutineScope(Dispatchers.Main).launch {
                            data(account)
                        }
                    }
                }
            }
        }catch (e: Exception) {
            onError(e)
        }
    }

    fun uploadImage(
        userName: String,
        bitmap: Bitmap,
        callBack:(Boolean, String) -> Unit
    ) {
        val storage = Firebase.storage.reference
        val imageRef = storage.child("Account/$userName")

        val bao = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao) //  luu bitmap vao baos
        val imageData = bao.toByteArray()

        imageRef.putBytes(imageData).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {uri ->
                val imageUrl = uri.toString()
                callBack(true, imageUrl)
            }.addOnFailureListener{
                callBack(false, null.toString())
            }
        }.addOnFailureListener{
            callBack(false, null.toString())
        }
    }

    fun deleteData(
        userName: String,
    ) = CoroutineScope(Dispatchers.IO).launch{
        val fireStoreRef = Firebase.firestore
            .collection("Account")
            .document(userName)

        try {
            fireStoreRef.delete().await()
        }catch (e: Exception) {
            println("error")
        }
    }
}