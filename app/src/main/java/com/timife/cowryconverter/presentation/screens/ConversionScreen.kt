package com.timife.cowryconverter.presentation.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.timife.cowryconverter.presentation.common.components.AppBackground
import com.timife.cowryconverter.presentation.common.components.V_MultiStyleText
import com.timife.cowryconverter.presentation.ui.theme.CowryConverterTheme


@Composable
fun ConversionScreen(
    modifier: Modifier = Modifier
){

    LazyColumn {
        item {
            V_MultiStyleText(
                text = "Currency Calculator",
                color1 = MaterialTheme.colorScheme.primary,
                color2 = MaterialTheme.colorScheme.primary,
                modifier = modifier,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }
    }

}


@Composable
@Preview
fun ConversionScreenPreview(){
    CowryConverterTheme {
        AppBackground {
            ConversionScreen()
        }
    }
}