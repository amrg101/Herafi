package com.amrg.herafi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileButton(
    modifier: Modifier = Modifier,
    prefix: @Composable () -> Unit,
    text: String,
    height: Dp = 50.dp,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(7.dp))
            .background(MaterialTheme.colors.surface)
            .border(
                width = 0.1.dp,
                color = MaterialTheme.colors.secondaryVariant,
                shape = RoundedCornerShape(7.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            prefix()
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                modifier = Modifier.weight(1F),
                text = text,
                style = MaterialTheme.typography.h5.copy(
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 13.5.sp
                )
            )
        }
    }
}