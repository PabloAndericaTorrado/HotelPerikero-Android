package com.pabloat.hotelperikero.ui.views

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pabloat.hotelperikero.data.local.dao.LocalReservaEventosDao
import com.pabloat.hotelperikero.data.local.entities.ReservaEventos
import com.pabloat.hotelperikero.viewmodel.HotelViewModel
import com.pabloat.hotelperikero.viewmodel.ReservaEventosViewModel
import com.pabloat.hotelperikero.viewmodel.ReservaEventosViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Calendar

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationEspaciosFormScreen(
    espacioId: Int,
    userId: Int,
    navHostController: NavHostController,
    viewModel: HotelViewModel,
    reservaEventosDao: LocalReservaEventosDao
) {
    val reservaEventosViewModel: ReservaEventosViewModel =
        viewModel(factory = ReservaEventosViewModelFactory(reservaEventosDao))
    val espacio = viewModel.getEspacioById(espacioId) ?: return
    var checkInDate by remember { mutableStateOf("") }
    var checkOutDate by remember { mutableStateOf("") }
    val numeroPersonas = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }

    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val context = LocalContext.current

    fun openDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                val time = "00:00:00"  // Default time
                onDateSelected("$date $time")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    fun isValidDateTime(dateTime: String): Boolean {
        return try {
            LocalDateTime.parse(dateTime, dateTimeFormatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    fun calculateTotalPrice(checkIn: String, checkOut: String, pricePerHour: Double): Double {
        val checkInDateTime = LocalDateTime.parse(checkIn, dateTimeFormatter)
        val checkOutDateTime = LocalDateTime.parse(checkOut, dateTimeFormatter)
        val hoursBetween = ChronoUnit.HOURS.between(checkInDateTime, checkOutDateTime).toInt()
        return hoursBetween * pricePerHour
    }

    suspend fun isEspacioAvailable(espacioId: Int, checkIn: String, checkOut: String): Boolean {
        return try {
            val existingReservas = viewModel.getReservasByEspacio(espacioId)
            val newCheckIn = LocalDateTime.parse(checkIn, dateTimeFormatter)
            val newCheckOut = LocalDateTime.parse(checkOut, dateTimeFormatter)

            existingReservas.none {
                val existingCheckIn = LocalDateTime.parse(it.check_in, dateTimeFormatter)
                val existingCheckOut = LocalDateTime.parse(it.check_out, dateTimeFormatter)
                newCheckIn.isBefore(existingCheckOut) && newCheckOut.isAfter(existingCheckIn)
            }
        } catch (e: DateTimeParseException) {
            errorMessage.value = "Las fechas deben estar en formato yyyy-MM-dd HH:mm:ss"
            false
        } catch (e: Exception) {
            errorMessage.value = "Error al comprobar la disponibilidad del espacio"
            false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formulario de Reserva") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = getEspacioImageUrl(espacio.id)),
                contentDescription = "Imagen del espacio",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Espacio ${espacio.nombre}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = espacio.descripcion,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Precio por hora: ${espacio.precio}€",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        openDatePickerDialog { date ->
                            checkInDate = date
                            errorMessage.value = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = "Select Check-In Date",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = if (checkInDate.isEmpty()) "Seleccionar fecha de inicio" else "Inicio: $checkInDate")
                }

                Button(
                    onClick = {
                        openDatePickerDialog { date ->
                            checkOutDate = date
                            errorMessage.value = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = "Select Check-Out Date",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = if (checkOutDate.isEmpty()) "Seleccionar fecha de fin" else "Fin: $checkOutDate")
                }

                OutlinedTextField(
                    value = numeroPersonas.value,
                    onValueChange = {
                        if ((it.toIntOrNull() ?: 0) <= espacio.capacidad_maxima) {
                            numeroPersonas.value = it
                            errorMessage.value = ""
                        }
                    },
                    label = { Text("Número de Personas (Máx ${espacio.capacidad_maxima})") },
                    leadingIcon = {
                        Icon(Icons.Default.People, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                if (errorMessage.value.isNotEmpty()) {
                    Text(
                        text = errorMessage.value,
                        color = Color.Red,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        if (!isValidDateTime(checkInDate) || !isValidDateTime(checkOutDate)) {
                            errorMessage.value = "Las fechas deben estar en formato yyyy-MM-dd HH:mm:ss"
                        } else {
                            val checkIn = checkInDate
                            val checkOut = checkOutDate
                            val checkInParsed = LocalDateTime.parse(checkIn, dateTimeFormatter)
                            val checkOutParsed = LocalDateTime.parse(checkOut, dateTimeFormatter)
                            val today = LocalDateTime.now()

                            when {
                                checkInParsed.isAfter(checkOutParsed) -> {
                                    errorMessage.value =
                                        "La fecha de inicio no puede ser posterior a la de fin"
                                }

                                checkInParsed.isBefore(today) || checkOutParsed.isBefore(today) -> {
                                    errorMessage.value =
                                        "Las fechas deben ser posteriores al día de hoy ${
                                            today.format(dateTimeFormatter)
                                        }"
                                }

                                else -> {
                                    viewModel.viewModelScope.launch {
                                        if (!isEspacioAvailable(espacioId, checkIn, checkOut)) {
                                            errorMessage.value =
                                                "El espacio ya está reservado en las fechas seleccionadas"
                                        } else {
                                            val totalPrice = calculateTotalPrice(
                                                checkIn,
                                                checkOut,
                                                espacio.precio.toDouble()
                                            )
                                            val now = LocalDateTime.now()
                                                .format(DateTimeFormatter.ISO_DATE_TIME)
                                            val reserva = ReservaEventos(
                                                id = null,
                                                users_id = userId,
                                                espacio_id = espacioId,
                                                check_in = checkIn,
                                                check_out = checkOut,
                                                precio_total = totalPrice.toString(),
                                                pagado = 0,
                                                cantidad_personas = numeroPersonas.value.toInt(),
                                                created_at = now,
                                                updated_at = now
                                            )
                                            //viewModel.addReservaEvento(reserva)
                                            reservaEventosViewModel.crearReservaEventos(reserva)
                                            showDialog.value = true
                                        }
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(50.dp),
                    enabled = checkInDate.isNotEmpty() && checkOutDate.isNotEmpty() && numeroPersonas.value.isNotEmpty(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        "Realizar reserva",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                LaunchedEffect(key1 = reservaEventosViewModel.reservaEventosCreada.collectAsState().value) {
                    reservaEventosViewModel.reservaEventosCreada.value?.let {
                        showDialog.value = true
                        reservaEventosViewModel.resetReservaEventosCreada()
                    }
                }

                if (showDialog.value) {
                    androidx.compose.material3.AlertDialog(
                        onDismissRequest = { showDialog.value = false },
                        title = { Text("Reserva efectuada con éxito") },
                        text = {
                            Text("¡Tu reserva ha sido creada con éxito para el día ${checkInDate}!")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDialog.value = false
                                    navHostController.popBackStack()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                            ) {
                                Text("Cerrar")
                            }
                        }
                    )
                }
            }
        }
    }
}
