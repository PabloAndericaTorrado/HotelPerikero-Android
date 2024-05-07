package com.pabloat.GameHubConnect.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pabloat.GameHubConnect.R

/**
 * NavTools
 * Biblioteca de funciones de aspectos generales del interface, generalmente de Scaffold.
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar() {
    val logo: Painter = painterResource(id = R.drawable.icono)

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF293036),
            titleContentColor = MaterialTheme.colorScheme.background,
        ),
        title = {
            Row {
                Image(
                    painter = logo,
                    contentDescription = "",
                    Modifier
                        .padding(6.dp, 0.dp)
                        .size(32.dp)
                )
                Text(
                    text = "GameHubConnect",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start,
                )
            }
        }
    )
}
