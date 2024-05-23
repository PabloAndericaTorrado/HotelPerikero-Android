package com.pabloat.hotelperikero.ui.views

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.pabloat.hotelperikero.data.local.dao.LocalReservaDao
import com.pabloat.hotelperikero.data.local.dao.LocalReservaServicioDao
import com.pabloat.hotelperikero.data.local.entities.Reserva
import com.pabloat.hotelperikero.data.local.entities.ReservaServicio
import com.pabloat.hotelperikero.data.local.entities.Servicio
import com.pabloat.hotelperikero.viewmodel.HotelViewModel
import com.pabloat.hotelperikero.viewmodel.ReservaServicioViewModel
import com.pabloat.hotelperikero.viewmodel.ReservaServiciosViewModelFactory
import com.pabloat.hotelperikero.viewmodel.ReservaViewModel
import com.pabloat.hotelperikero.viewmodel.ReservaViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Calendar

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationFormScreen(
    habitacionId: Int,
    userId: Int,
    navHostController: NavHostController,
    viewModel: HotelViewModel,
    reservaDao: LocalReservaDao,
    reservaServicioDao: LocalReservaServicioDao

) {
    val reservaViewModel: ReservaViewModel =
        viewModel(factory = ReservaViewModelFactory(reservaDao))
    val reservaServicioViewModel: ReservaServicioViewModel =
        viewModel(factory = ReservaServiciosViewModelFactory(reservaServicioDao))
    val habitacion = viewModel.getHabitacionById(habitacionId) ?: return
    var checkInDate by remember { mutableStateOf("") }
    var checkOutDate by remember { mutableStateOf("") }
    val numeroPersonas = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val successMessage = remember { mutableStateOf("") }
    val servicios by viewModel.servicios.collectAsState()
    val selectedServicios = remember { mutableStateOf(mutableMapOf<Servicio, Int>()) }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val context = LocalContext.current

    fun openDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val date = LocalDate.of(year, month + 1, dayOfMonth).format(dateFormatter)
                onDateSelected(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    fun isValidDate(date: String): Boolean {
        return try {
            LocalDate.parse(date, dateFormatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    fun calculateTotalPrice(checkIn: String, checkOut: String, pricePerNight: Double): Double {
        val checkInDate = LocalDate.parse(checkIn, dateFormatter)
        val checkOutDate = LocalDate.parse(checkOut, dateFormatter)
        val daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate).toInt()
        return daysBetween * pricePerNight
    }

    suspend fun isRoomAvailable(habitacionId: Int, checkIn: String, checkOut: String): Boolean {
        return try {
            val existingReservas = viewModel.getReservasByHabitacion(habitacionId)
            Log.d("ReservationFormScreen", "Reservas existentes: $existingReservas")
            val newCheckIn = LocalDate.parse(checkIn, dateFormatter)
            val newCheckOut = LocalDate.parse(checkOut, dateFormatter)

            existingReservas.none {
                val existingCheckIn = LocalDate.parse(it.check_in, dateFormatter)
                val existingCheckOut = LocalDate.parse(it.check_out, dateFormatter)
                Log.d(
                    "ReservationFormScreen",
                    "Comparando con reserva existente: $existingCheckIn - $existingCheckOut"
                )
                newCheckIn.isBefore(existingCheckOut) && newCheckOut.isAfter(existingCheckIn)
            }
        } catch (e: DateTimeParseException) {
            errorMessage.value = "Las fechas deben estar en formato yyyy-MM-dd"
            false
        } catch (e: Exception) {
            errorMessage.value = "Error al comprobar la disponibilidad de la habitación"
            Log.e("ReservationFormScreen", "Error al comprobar disponibilidad", e)
            false
        }
    }

    var expanded by remember { mutableStateOf(false) }

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
                painter = rememberAsyncImagePainter(model = getHabitacionImageUrl(habitacion.id)),
                contentDescription = "Imagen de la habitación",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Habitación ${habitacion.numero_habitacion}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = habitacion.tipo,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Precio por noche: ${habitacion.precio}€",
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
                            successMessage.value = ""
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
                    Text(text = if (checkInDate.isEmpty()) "Seleccionar fecha de Check-In" else "Check-In: $checkInDate")
                }

                Button(
                    onClick = {
                        openDatePickerDialog { date ->
                            checkOutDate = date
                            errorMessage.value = ""
                            successMessage.value = ""
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
                    Text(text = if (checkOutDate.isEmpty()) "Seleccionar fecha de Check-Out" else "Check-Out: $checkOutDate")
                }

                OutlinedTextField(
                    value = numeroPersonas.value,
                    onValueChange = {
                        if ((it.toIntOrNull() ?: 0) <= habitacion.capacidad) {
                            numeroPersonas.value = it
                            errorMessage.value = ""
                            successMessage.value = ""
                        }
                    },
                    label = { Text("Número de Personas (Máx ${habitacion.capacidad})") },
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

                if (successMessage.value.isNotEmpty()) {
                    Text(
                        text = successMessage.value,
                        color = Color.Green,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                OutlinedButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Seleccionar servicios adicionales")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    servicios.forEach { servicio ->
                        var checked by remember {
                            mutableStateOf(
                                selectedServicios.value.contains(
                                    servicio
                                )
                            )
                        }
                        DropdownMenuItem(onClick = {
                            checked = !checked
                            if (checked) {
                                selectedServicios.value[servicio] = 1  // default quantity
                            } else {
                                selectedServicios.value.remove(servicio)
                            }
                        }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = { isChecked ->
                                        checked = isChecked
                                        if (checked) {
                                            selectedServicios.value[servicio] =
                                                1  // default quantity
                                        } else {
                                            selectedServicios.value.remove(servicio)
                                        }
                                    },
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(servicio.nombre)
                                if (checked) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    OutlinedTextField(
                                        value = selectedServicios.value[servicio]?.toString()
                                            ?: "1",
                                        onValueChange = { value ->
                                            selectedServicios.value[servicio] =
                                                value.toIntOrNull() ?: 1
                                        },
                                        label = { Text("Cantidad") },
                                        modifier = Modifier.width(80.dp),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        if (!isValidDate(checkInDate) || !isValidDate(checkOutDate)) {
                            errorMessage.value = "Las fechas deben estar en formato yyyy-MM-dd"
                        } else {
                            val checkIn = checkInDate
                            val checkOut = checkOutDate
                            val checkInParsed = LocalDate.parse(checkIn, dateFormatter)
                            val checkOutParsed = LocalDate.parse(checkOut, dateFormatter)
                            val today = LocalDate.now()

                            when {
                                checkInParsed.isAfter(checkOutParsed) -> {
                                    errorMessage.value =
                                        "La fecha de Check-In no puede ser posterior a la de Check-Out"
                                }

                                checkInParsed.isBefore(today) || checkOutParsed.isBefore(today) -> {
                                    errorMessage.value =
                                        "Las fechas deben ser posteriores al día de hoy ${
                                            today.format(dateFormatter)
                                        }"
                                }

                                else -> {
                                    viewModel.viewModelScope.launch {
                                        if (!isRoomAvailable(habitacionId, checkIn, checkOut)) {
                                            errorMessage.value =
                                                "La habitación ya está reservada en las fechas seleccionadas"
                                        } else {
                                            val totalPrice = calculateTotalPrice(
                                                checkIn,
                                                checkOut,
                                                habitacion.precio.toDouble()
                                            )
                                            val now = LocalDateTime.now()
                                                .format(DateTimeFormatter.ISO_DATE_TIME)
                                            val reserva = Reserva(
                                                id = habitacion.id,
                                                users_id = userId,
                                                habitacion_id = habitacionId,
                                                check_in = checkIn,
                                                check_out = checkOut,
                                                precio_total = totalPrice.toString(),
                                                pagado = 0,
                                                confirmado = 0,
                                                dni = null,
                                                numero_personas = numeroPersonas.value.toInt(),
                                                created_at = now,
                                                updated_at = now
                                            )
                                            //viewModel.addReserva(reserva)
                                            reservaViewModel.crearReserva(reserva)

                                            val response = viewModel.getLastReservationId()
                                            val reservaId = response.last_id

                                            selectedServicios.value.forEach { (servicio, cantidad) ->
                                                val reservaServicio = reservaId?.let { it1 ->
                                                    ReservaServicio(
                                                        id = null,
                                                        reserva_id = it1,
                                                        servicio_id = servicio.id!!,
                                                        cantidad = cantidad,
                                                        created_at = now,
                                                        updated_at = now
                                                    )
                                                }
                                                Log.d("Vakero", reservaServicio.toString())
                                                if (reservaServicio != null) {
                                                    reservaServicioViewModel.crearReserva(
                                                        reservaServicio
                                                    )
                                                }
                                            }

                                            successMessage.value = "¡Reserva creada con éxito!"
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
                LaunchedEffect(key1 = reservaViewModel.reservaCreada.collectAsState().value) {
                    reservaViewModel.reservaCreada.value?.let {
                        showDialog.value = true
                        reservaViewModel.resetReservaCreada()
                    }
                }

                if (showDialog.value) {
                    AlertDialog(
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
