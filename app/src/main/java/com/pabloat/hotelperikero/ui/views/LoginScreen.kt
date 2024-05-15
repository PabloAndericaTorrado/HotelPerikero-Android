package com.pabloat.hotelperikero.ui.views

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pabloat.hotelperikero.navigation.Destinations
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

data class LoginResponse(val success: Boolean, val userName: String?, val responseCode: Int)

fun parseLoginResponse(responseBody: String?, responseCode: Int): LoginResponse {
    return try {
        val jsonResponse = JSONObject(responseBody ?: "{}")
        val data = jsonResponse.optJSONObject("data")
        val success = data != null && responseCode == 200
        val userName = data?.optString("name", null.toString())
        LoginResponse(success = success, userName = userName, responseCode = responseCode)
    } catch (e: Exception) {
        Log.e("LoginUser", "Error parsing response: ${e.message}")
        LoginResponse(success = false, userName = null, responseCode = responseCode)
    }
}


@OptIn(DelicateCoroutinesApi::class)
fun loginUser(email: String, password: String, onResult: (LoginResponse) -> Unit) {
    val client = OkHttpClient()

    val json = JSONObject().apply {
        put("email", email)
        put("password", password)
    }

    val requestBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

    val request = Request.Builder()
        .url("http://telamarinera.duckdns.org:16020/api/login")
        .post(requestBody)
        .build()

    Log.d("LoginUser", "Request: ${request.url}, Body: $json")

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            GlobalScope.launch(Dispatchers.Main) {
                Log.e("LoginUser", "Request failed: ${e.message}")
                onResult(LoginResponse(success = false, userName = null, responseCode = 0))
            }
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            val responseCode = response.code
            Log.d("LoginUser", "Response code: $responseCode, Body: $responseBody")

            val loginResponse = parseLoginResponse(responseBody, responseCode)
            Log.d("LoginUser", "Parsed response - success: ${loginResponse.success}, userName: ${loginResponse.userName}")

            GlobalScope.launch(Dispatchers.Main) {
                onResult(loginResponse)
            }
        }
    })
}



@Composable
fun UserForm(
    isCreatedAccount: Boolean = false,
    onDone: (String, String) -> Unit = { _, _ -> }
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisible = rememberSaveable { mutableStateOf(false) }
    val valido = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailInput(emailState = email)
        PasswordInput(passwordState = password, labelId = "Password", passwordVisible = passwordVisible)
        SubmitButton(textId = if (isCreatedAccount) "Crear cuenta " else "Login", inputValido = valido) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    inputValido: Boolean,
    onClic: () -> Unit
) {
    Button(
        onClick = onClic,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        shape = CircleShape,
        enabled = inputValido
    ) {
        Text(
            text = textId,
            modifier = Modifier.padding(5.dp)
        )
    }
}

@Composable
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisible: MutableState<Boolean>
) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = labelId) },
        singleLine = true,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordState.value.isNotBlank()) {
                PasswordVisibleIcon(passwordVisible)
            }
        }
    )
}

@Composable
fun PasswordVisibleIcon(passwordVisible: MutableState<Boolean>) {
    val image = if (passwordVisible.value)
        Icons.Default.VisibilityOff
    else
        Icons.Default.Visibility

    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
        Icon(
            imageVector = image,
            contentDescription = ""
        )
    }
}

@Composable
fun InputField(
    valueState: MutableState<String>,
    labelId: String,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId: String = "Email"
) {
    InputField(
        valueState = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email
    )
}

@Composable
fun LoginScreen(navController: NavController) {
    val showDialog = remember { mutableStateOf(false) }
    val dialogTitle = remember { mutableStateOf("") }
    val dialogMessage = remember { mutableStateOf("") }
    val rememberMeState = remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Inicia sesión")
            UserForm(isCreatedAccount = false) { email, password ->
                loginUser(email, password) { response ->
                    when (response.responseCode) {
                        200 -> {
                            dialogTitle.value = "Bienvenido"
                            dialogMessage.value = "Bienvenido, ${response.userName}"
                            showDialog.value = true
                            // Navegar después de mostrar el diálogo
                            navController.navigate(Destinations.MainScreen.route)
                        }
                        220 -> {
                            dialogTitle.value = "Error"
                            dialogMessage.value = "La contraseña es incorrecta"
                            showDialog.value = true
                        }
                        else -> {
                            dialogTitle.value = "Error"
                            dialogMessage.value = "El correo o la contraseña no son correctos"
                            showDialog.value = true
                        }
                    }
                }
            }
            if (showDialog.value) {
                ShowDialog(showDialog, dialogTitle, dialogMessage)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(
                    checked = rememberMeState.value,
                    onCheckedChange = { isChecked ->
                        rememberMeState.value = isChecked
                        //TODO: GUARDAR EL LOGIN EN PREFERENCIAS
                    },
                )
                Text(text = "Recuérdame")
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text1 = "¿No tienes cuenta?"
                val text2 = "Regístrate"
                Text(text = text1)
                Text(
                    text = text2,
                    modifier = Modifier
                        .clickable { /* TODO: TE MANDA A LA REGISTER SCREEN */ }
                        .padding(start = 5.dp),
                    color = Color.Cyan
                )
            }
        }
    }
}

@Composable
fun ShowDialog(showDialog: MutableState<Boolean>, title: MutableState<String>, message: MutableState<String>) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("OK")
                }
            },
            title = { Text(text = title.value) },
            text = { Text(text = message.value) }
        )
    }
}



