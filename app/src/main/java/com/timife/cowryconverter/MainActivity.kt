package com.timife.cowryconverter

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.timife.cowryconverter.presentation.screens.ConversionScreen
import com.timife.cowryconverter.presentation.ui.theme.CowryConverterTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CowryConverterTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(
                        colors = TopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            navigationIconContentColor = MaterialTheme.colorScheme.secondary,
                            actionIconContentColor = MaterialTheme.colorScheme.secondary,
                            titleContentColor = MaterialTheme.colorScheme.secondary,
                            scrolledContainerColor = MaterialTheme.colorScheme.secondary
                        ),
                        title = {},
                        navigationIcon = {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_menu),
                                    modifier = Modifier,
                                    contentDescription = ""
                                )
                            }
                        },
                        actions = {
                            TextButton(
                                modifier = Modifier,
                                onClick = {},
                            ) {
                                Text(
                                    stringResource(R.string.sign_up),
                                    modifier = Modifier,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    )
                }) { innerPadding ->
                    ConversionScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CowryConverterTheme {
        Greeting("Android")
    }
}