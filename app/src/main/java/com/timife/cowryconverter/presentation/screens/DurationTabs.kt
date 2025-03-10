package com.timife.cowryconverter.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.timife.cowryconverter.presentation.ui.theme.Dimens

@Composable
fun DurationTabs(
    tabs: List<String>,
    modifier: Modifier = Modifier,
    isDotSelector: Boolean = true,
    onTabIndexChanged: @Composable (index: Int) -> Unit
) {
    var tabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.grid_2)
    ) {
        TabRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = Dimens.grid_1_5)
                .background(
                    color = Color.Transparent
                ),
            indicator = {},
            selectedTabIndex = tabIndex,
            containerColor = Color.Transparent,
            divider = {},
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = tabIndex == index
                Tab(
                    text = {
                        Text(
                            text = tab,
                            style = switchIndex(tabIndex == index),
                        )

                        if (isDotSelector) Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(
                                Dimens.grid_0_5
                            )
                        ) {
                            Text(
                                text = tab,
                                style = switchIndex(tabIndex == index),
                            )
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(Dimens.grid_1)
                                        .background(
                                            color = MaterialTheme.colorScheme.secondary,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                )
            }
        }
        onTabIndexChanged(tabIndex)

    }
}

@Composable
private fun switchIndex(isSelected: Boolean): TextStyle {
    return if (isSelected) {
        MaterialTheme.typography
            .titleMedium.copy(
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight =FontWeight.SemiBold,
            )
    } else {
        MaterialTheme.typography
            .titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color.LightGray,
            )
    }
}