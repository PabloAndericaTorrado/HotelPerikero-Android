package com.pabloat.GameHubConnect.ui.views

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
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pabloat.GameHubConnect.navigation.Destinations
import com.pabloat.GameHubConnect.viewmodel.FireBaseViewModel
import com.pabloat.GameHubConnect.viewmodel.PreferenceUtils


/**
 * Composable para mostrar el formulario de inicio de sesión o registro de usuario.
 *
 * @param isCreatedAccount Indica si se está creando una nueva cuenta de usuario.
 * @param onDone La acción a realizar cuando se completa el formulario.
 */
@Composable
fun UserForm(
    isCreatedAccount: Boolean = false,
    onDone: (String, String) -> Unit = { email, pwd -> }
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
        // enviar el formulario y ocultar el teclado
        SubmitButton(textId = if (isCreatedAccount) "Crear cuenta " else "Login", inputValido = valido) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

/**
 * Composable para mostrar el botón de envío del formulario.
 *
 * @param textId El texto que se mostrará en el botón.
 * @param inputValido Indica si la entrada del formulario es válida o no.
 * @param onClic La acción a realizar cuando se hace clic en el botón.
 */
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

/**
 * Composable para mostrar el campo de entrada de contraseña.
 *
 * @param passwordState El estado de la contraseña.
 * @param labelId El ID del campo de texto.
 * @param passwordVisible Indica si la contraseña es visible o no.
 */
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
        // Se le indica que el teclado a utilizar es el de contraseña.
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordState.value.isNotBlank()) {
                PasswordVisibleIcon(passwordVisible)
            }
        }
    )
}

/**
 * Composable para mostrar el icono de visualización de contraseña.
 *
 * @param passwordVisible El estado de visibilidad de la contraseña.
 */
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

/**
 * Composable para mostrar un campo de entrada de texto genérico.
 *
 * @param valueState El estado del valor del campo de texto.
 * @param labelId El ID del campo de texto.
 * @param isSingleLine Indica si el campo de texto es de una sola línea.
 * @param keyboardType El tipo de teclado a utilizar.
 */
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

/**
 * Composable para mostrar el campo de entrada de correo electrónico.
 *
 * @param emailState El estado del correo electrónico.
 * @param labelId El ID del campo de texto.
 */
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

/**
 * Composable para mostrar la pantalla de inicio de sesión.
 *
 * @param navController Controlador de navegación para la navegación entre pantallas.
 * @param fireBaseViewModel ViewModel para interactuar con Firebase Authentication.
 */
@Composable
fun LoginScreen(navController: NavController, fireBaseViewModel: FireBaseViewModel = viewModel()) {
    val showSnackbar = remember { mutableStateOf(false) }
    val showLoginForm = rememberSaveable { mutableStateOf(true) }
    val context = LocalContext.current
    val preferencesUtils = PreferenceUtils()
    val rememberMeState = remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showLoginForm.value) {
                Text(text = "Inicia sesión")
                UserForm(isCreatedAccount = false) { email, password ->
                    Log.d("MV", "Logueado con $email y $password")
                    fireBaseViewModel.storeEmail(email)
                    Log.d("MV", "Entra en el try")
                    fireBaseViewModel.SingInWithEmailAndPassword(
                        context, email, password,
                        home = {
                            if (fireBaseViewModel.getStoredEmail() == "admin@admin.com") {
                                Log.d("MV", "Es ADMIN")
                                navController.navigate(Destinations.ManageScreen.route)
                            } else {
                                Log.d("MV", "No es ADMIN")
                                navController.navigate(Destinations.InitScreen.route)
                            }
                        },
                        fail = {
                            showSnackbar.value = true
                        }
                    )

                    Log.d("MV", "Aqui Chat")
                }
                if (showSnackbar.value) {
                    ShowSnackbar(showSnackbar)
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
                            preferencesUtils.saveRememberMeState(isChecked, context)
                        },
                    )
                    Text(text = "Recuérdame")
                }
            } else {
                Text("Crea una cuenta")
                UserForm(isCreatedAccount = true) { email, password ->
                    Log.d("MV", "Creando cuenta con $email,$password")
                    fireBaseViewModel.createUserWithEmailAndPassword(email, password) {
                        navController.navigate(Destinations.InitScreen.route)
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text1 =
                    if (showLoginForm.value) "¿No tienes cuenta?"
                    else "¿Ya tienes cuenta?"
                val text2 =
                    if (showLoginForm.value) "Regístrate"
                    else "Inicia sesión"
                Text(text = text1)
                Text(
                    text = text2,
                    modifier = Modifier
                        .clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 5.dp),
                    color = Color.Cyan
                )
            }
        }
    }
}

/**
 * Composable para mostrar una Snackbar.
 *
 * @param showSnackbar El estado que indica si se debe mostrar la Snackbar.
 */
@Composable
private fun ShowSnackbar(showSnackbar: MutableState<Boolean>) {
    if (showSnackbar.value) {
        Snackbar(
            action = {
                TextButton(onClick = { showSnackbar.value = false }) {
                    Text("Cerrar")
                }
            }
        ) {
            Text("El correo o la contraseña no son correctos")
        }
    }
}