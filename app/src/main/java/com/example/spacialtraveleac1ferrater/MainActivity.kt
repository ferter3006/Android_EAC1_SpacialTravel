package com.example.spacialtraveleac1ferrater

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spacialtraveleac1ferrater.ui.theme.SpacialTravelEAc1FerraterTheme

enum class Pantalla {
    INICIAL, STEP1_NOM, STEP2_EDAT, STEP3_DESTI, RESUM_FINAL
}

enum class Destins(val descripcioResource: Int) {
    SELECCIONA(R.string.seleccio), MART(R.string.mart), LLUNA(R.string.lluna), JUPITER(R.string.jupiter), SATURN(
        R.string.saturn
    )
}

data class InfoUsuari(
    val nom: String, val edat: String, val desti: Destins
)

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate Called")
        enableEdgeToEdge()
        setContent {
            SpacialTravelEAc1FerraterTheme {
                SpacialTravelApp()
            }
        }
    }

}

@Composable
fun CohetIcon(modifier: Modifier = Modifier, tamany: Dp = 100.dp) {
    Image(
        painter = painterResource(id = R.drawable.project),
        contentDescription = "NaveEspacial",
        modifier = Modifier.size(tamany)
    )
}

@Composable
fun BotoDinamic(
    isActive: Boolean, onNextButtonClick: () -> Unit, pantallaActual: Pantalla

) {
    var textDelBoton = when (pantallaActual) {
        Pantalla.INICIAL -> stringResource(id = R.string.btn_iniciar)
        Pantalla.RESUM_FINAL -> stringResource(id = R.string.btn_reiniciar)
        else -> stringResource(id = R.string.btn_seguent)
    }
    Button(
        onClick = { onNextButtonClick() }, enabled = isActive
    ) {
        Text(text = textDelBoton)

    }
}

@Composable
fun TextAndButton(
    pantallaActual: Pantalla, onNextButtonClick: () -> Unit, modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.Benvinguda),
        fontSize = 25.sp,
        textAlign = TextAlign.Center
    )
    BotoDinamic(
        isActive = true,
        onNextButtonClick = { onNextButtonClick() },
        pantallaActual = pantallaActual
    )

}

@Composable
fun NameInput(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.nom), fontSize = 25.sp, textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(16.dp))
    TextField(value = value, onValueChange = onValueChange)
}

@Composable
fun AgeInput(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.edat), fontSize = 25.sp, textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(16.dp))

    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun DestiInput(
    value: Destins, onValueChange: (Destins) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Text(
        stringResource(id = R.string.desti), fontSize = 25.sp, textAlign = TextAlign.Center
    )
    Box(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(id = value.descripcioResource),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(16.dp),
            textAlign = TextAlign.Center)

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
        ) {
            Destins.values().forEach { desti ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = desti.descripcioResource),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        onValueChange(desti)
                        expanded = false
                    },
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpacialTravelApp() {
    var infoActual by remember {
        mutableStateOf(InfoUsuari("", "", Destins.SELECCIONA))
    }

    var pantallaActual by remember { mutableStateOf(Pantalla.INICIAL) }

    fun pasarDePantalla() {
        pantallaActual = when (pantallaActual) {
            Pantalla.INICIAL -> Pantalla.STEP1_NOM
            Pantalla.STEP1_NOM -> Pantalla.STEP2_EDAT
            Pantalla.STEP2_EDAT -> Pantalla.STEP3_DESTI
            Pantalla.STEP3_DESTI -> Pantalla.RESUM_FINAL
            Pantalla.RESUM_FINAL -> Pantalla.INICIAL
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CohetIcon()
        Text(text = stringResource(id = R.string.autor))
        Spacer(modifier = Modifier.height(16.dp))

        if (pantallaActual == Pantalla.INICIAL) {
            // Petita pantalla inicial
            TextAndButton(
                pantallaActual = pantallaActual,
                onNextButtonClick = { pasarDePantalla() },
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        } else if (pantallaActual == Pantalla.STEP1_NOM) {
            NameInput(
                value = infoActual.nom,
                onValueChange = { infoActual = infoActual.copy(nom = it) },
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
            BotoDinamic(
                isActive = infoActual.nom != "",
                onNextButtonClick = { pasarDePantalla() },
                pantallaActual = pantallaActual
            )

        } else if (pantallaActual == Pantalla.STEP2_EDAT) {
            AgeInput(
                value = infoActual.edat,
                onValueChange = { infoActual = infoActual.copy(edat = it) },
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
            BotoDinamic(
                isActive = infoActual.edat != "",
                onNextButtonClick = { pasarDePantalla() },
                pantallaActual = pantallaActual
            )
        } else if (pantallaActual == Pantalla.STEP3_DESTI) {
            DestiInput(value = infoActual.desti,
                onValueChange = { infoActual = infoActual.copy(desti = it) })
            BotoDinamic(
                isActive = infoActual.desti != Destins.SELECCIONA,
                onNextButtonClick = { pasarDePantalla() },
                pantallaActual = pantallaActual
            )
        } else if (pantallaActual == Pantalla.RESUM_FINAL) {
            Text(stringResource(id = R.string.Final))
            Text(
                stringResource(
                    id = R.string.text_final,
                    infoActual.nom,
                    infoActual.edat,
                    infoActual.desti.toString()
                )
            )
            BotoDinamic(
                isActive = true, onNextButtonClick = {
                    infoActual = InfoUsuari("", "", Destins.SELECCIONA)
                    pasarDePantalla()
                }, pantallaActual = pantallaActual
            )
        }
    }
}
