package com.timife.cowryconverter.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.timife.cowryconverter.R
import com.timife.cowryconverter.presentation.common.components.ClickableText
import com.timife.cowryconverter.presentation.ui.theme.Dimens

@Composable
fun GraphSheet(
    modifier: Modifier = Modifier
) {

    DurationTabs(
        tabs = listOf(stringResource(R.string.past_30_days), stringResource(R.string.past_90_days)),
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(
                Dimens.grid_2
            )
        )
    ) {

        Box(
            modifier = Modifier.height(Dimens.grid_29)
        ) {
        }
        ClickableText(
            text = stringResource(R.string.get_rate_alerts),
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        )
    }
}