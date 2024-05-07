package com.pabloat.GameHubConnect.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.pabloat.GameHubConnect.data.local.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar las interacciones con Firebase Authentication y Firestore.
 */
class FireBaseViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    private val _storedError: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * Función para iniciar sesión con correo electrónico y contraseña.
     *
     * @param context El contexto de la aplicación.
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @param home La acción a realizar cuando el inicio de sesión es exitoso.
     * @param fail La acción a realizar cuando el inicio de sesión falla.
     */
    fun SingInWithEmailAndPassword(
        context: Context,
        email: String,
        password: String,
        home: () -> Unit,
        fail: () -> Unit
    ) = viewModelScope.launch {
        val preferencesUtils = PreferenceUtils()
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("MV", "Logueado")
                    if (preferencesUtils.getRememberMeState(context)) {
                        preferencesUtils.saveUserCredentials(email, password, context)
                    }
                    home()
                } else {
                    Log.d("MV", "Inicio de sesión fallido")
                    fail()
                }
            }
        } catch (ex: Exception) {
            _storedError.value = ex.message.toString()
            Log.e("MV", "Excepción en SingInWithEmailAndPassword: ${ex.message}", ex)
        }
    }

    /**
     * Función para crear una cuenta de usuario con correo electrónico y contraseña.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @param home La acción a realizar cuando se crea la cuenta de usuario exitosamente.
     */
    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName = task.result.user?.email?.split("@")?.get(0)
                        createUser(displayName)
                        home()
                    } else {
                        Log.d("MV", task.result.toString())
                    }
                    _loading.value = false
                }
        }
    }

    /**
     * Función privada para crear un usuario en Firestore.
     *
     * @param displayName El nombre del usuario.
     */
    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid

        val user = User(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "",
            rol = "user",
            id = null
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("MV", "Creado ${it.id}")
            }.addOnFailureListener {
                Log.d("MV", "Mal ${it}")
            }
    }

    private val _storedString: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * Función para almacenar el correo electrónico del usuario.
     *
     * @param value El correo electrónico del usuario.
     */
    fun storeEmail(value: String) {
        _storedString.value = value
    }

    /**
     * Función para obtener el correo electrónico almacenado del usuario.
     *
     * @return El correo electrónico del usuario.
     */
    fun getStoredEmail(): String {
        return _storedString.value
    }

    /**
     * Función para obtener el rol actual del usuario desde Firestore.
     *
     * @param userId El ID del usuario.
     * @param onSuccess La acción a realizar cuando se obtiene el rol exitosamente.
     * @param onFailure La acción a realizar cuando falla la obtención del rol.
     */
    fun getCurrentUserRoleFromFirestore(
        userId: String,
        onSuccess: (String?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(userId)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val rol = document.getString("rol")
                    onSuccess(rol)
                } else {
                    onFailure(Exception("El documento del usuario no existe"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    /**
     * Función para obtener el nombre actual del usuario.
     *
     * @return El nombre del usuario.
     */
    fun getCurrentUserName(): String? {
        val currentUser = auth.currentUser
        return currentUser?.displayName
    }

    /**
     * Función para cerrar sesión.
     */
    fun signOut() {
        auth.signOut()
    }
}