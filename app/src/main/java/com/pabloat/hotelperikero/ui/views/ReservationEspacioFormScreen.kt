package com.pabloat.hotelperikero.ui.views

import android.os.Build
import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pabloat.hotelperikero.data.local.entities.ReservaEventos
import com.pabloat.hotelperikero.viewmodel.HotelViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationEspaciosFormScreen(
    espacioId: Int,
    userId: Int,
    navHostController: NavHostController,
    viewModel: HotelViewModel
) {
    val espacio = viewModel.getEspacioById(espacioId) ?: return
    var checkInDate by remember { mutableStateOf(TextFieldValue("")) }
    var checkOutDate by remember { mutableStateOf(TextFieldValue("")) }
    val numeroPersonas = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }

    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun isValidDateTime(dateTime: String): Boolean {
        return try {
            LocalDateTime.parse(dateTime, dateTimeFormatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    fun formatInputAsDateTime(input: TextFieldValue): TextFieldValue {
        val cleaned = input.text.replace(Regex("[^\\d]"), "")
        val newText = buildString {
            for (i in cleaned.indices) {
                when (i) {
                    4, 6 -> append('-')
                    8 -> append(' ')
                    10, 12 -> append(':')
                }
                append(cleaned[i])
            }
        }
        val newCursorPosition =
            newText.length.coerceAtMost(input.selection.start + (newText.length - input.text.length))
        return TextFieldValue(
            text = newText,
            selection = TextRange(newCursorPosition)
        )
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
            Log.d("ReservationEspaciosFormScreen", "Reservas existentes: $existingReservas")
            val newCheckIn = LocalDateTime.parse(checkIn, dateTimeFormatter)
            val newCheckOut = LocalDateTime.parse(checkOut, dateTimeFormatter)

            existingReservas.none {
                val existingCheckIn = LocalDateTime.parse(it.check_in, dateTimeFormatter)
                val existingCheckOut = LocalDateTime.parse(it.check_out, dateTimeFormatter)
                Log.d(
                    "ReservationEspaciosFormScreen",
                    "Comparando con reserva existente: $existingCheckIn - $existingCheckOut"
                )
                newCheckIn.isBefore(existingCheckOut) && newCheckOut.isAfter(existingCheckIn)
            }
        } catch (e: DateTimeParseException) {
            errorMessage.value = "Las fechas deben estar en formato yyyy-MM-dd HH:mm:ss"
            false
        } catch (e: Exception) {
            errorMessage.value = "Error al comprobar la disponibilidad del espacio"
            Log.e("ReservationEspaciosFormScreen", "Error al comprobar disponibilidad", e)
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
    ) { it ->
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

            OutlinedTextField(
                value = checkInDate,
                onValueChange = {
                    checkInDate = formatInputAsDateTime(it)
                    errorMessage.value = ""
                },
                label = { Text("Fecha de Inicio (yyyy-MM-dd HH:mm:ss)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = checkOutDate,
                onValueChange = {
                    checkOutDate = formatInputAsDateTime(it)
                    errorMessage.value = ""
                },
                label = { Text("Fecha de Fin (yyyy-MM-dd HH:mm:ss)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = numeroPersonas.value,
                onValueChange = {
                    if ((it.toIntOrNull() ?: 0) <= espacio.capacidad_maxima) {
                        numeroPersonas.value = it
                    }
                },
                label = { Text("Número de Personas (Máx ${espacio.capacidad_maxima})") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(vertical = 8.dp)
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
                    if (!isValidDateTime(checkInDate.text) || !isValidDateTime(checkOutDate.text)) {
                        errorMessage.value = "Las fechas deben estar en formato yyyy-MM-dd HH:mm:ss"
                    } else {
                        val checkIn = checkInDate.text
                        val checkOut = checkOutDate.text
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
                                        viewModel.addReservaEvento(reserva)
                                        navHostController.popBackStack()
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = checkInDate.text.isNotEmpty() && checkOutDate.text.isNotEmpty() && numeroPersonas.value.isNotEmpty(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Reservar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
