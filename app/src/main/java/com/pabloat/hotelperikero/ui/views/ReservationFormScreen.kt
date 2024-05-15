package com.pabloat.hotelperikero.ui.views

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.pabloat.hotelperikero.data.local.entities.Reserva
import com.pabloat.hotelperikero.viewmodel.HotelViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationFormScreen(
    habitacionId: Int,
    userId: Int,
    navHostController: NavHostController,
    viewModel: HotelViewModel
) {
    val habitacion = viewModel.getHabitacionById(habitacionId) ?: return
    var checkInDate by remember { mutableStateOf(TextFieldValue("")) }
    var checkOutDate by remember { mutableStateOf(TextFieldValue("")) }
    val numeroPersonas = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun isValidDate(date: String): Boolean {
        return try {
            LocalDate.parse(date, dateFormatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    fun formatInputAsDate(input: TextFieldValue): TextFieldValue {
        val cleaned = input.text.replace("-", "")
        val newText = buildString {
            for (i in cleaned.indices) {
                if (i == 4 || i == 6) append('-')
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Formulario de Reserva")

        OutlinedTextField(
            value = checkInDate,
            onValueChange = {
                checkInDate = formatInputAsDate(it)
                errorMessage.value = ""
            },
            label = { Text("Fecha de Check-In (yyyy-MM-dd)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = checkOutDate,
            onValueChange = {
                checkOutDate = formatInputAsDate(it)
                errorMessage.value = ""
            },
            label = { Text("Fecha de Check-Out (yyyy-MM-dd)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = numeroPersonas.value,
            onValueChange = {
                if ((it.toIntOrNull() ?: 0) <= habitacion.capacidad) {
                    numeroPersonas.value = it
                }
            },
            label = { Text("Número de Personas (Máx ${habitacion.capacidad})") },
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
                if (!isValidDate(checkInDate.text) || !isValidDate(checkOutDate.text)) {
                    errorMessage.value = "Las fechas deben estar en formato yyyy-MM-dd"
                } else {
                    val checkIn = checkInDate.text
                    val checkOut = checkOutDate.text
                    viewModel.viewModelScope.launch {
                        if (!isRoomAvailable(habitacionId, checkIn, checkOut)) {
                            errorMessage.value =
                                "La habitación ya está reservada en las fechas seleccionadas"
                        } else {
                            val totalPrice =
                                calculateTotalPrice(checkIn, checkOut, habitacion.precio.toDouble())
                            val now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
                            val reserva = Reserva(
                                id = null,
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
                            viewModel.addReserva(reserva)
                            navHostController.popBackStack()
                        }
                    }
                }
            },
            modifier = Modifier.padding(vertical = 16.dp),
            enabled = checkInDate.text.isNotEmpty() && checkOutDate.text.isNotEmpty() && numeroPersonas.value.isNotEmpty()
        ) {
            Text("Reservar")
        }
    }
}
