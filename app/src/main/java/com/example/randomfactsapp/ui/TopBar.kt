package com.example.randomfactsapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.randomfactsapp.R


@Composable
fun TopBar(onToggle: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Random Facts Generator",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h5,
//                color = MaterialTheme.colors.surface
            )

            Spacer(modifier = Modifier.height(8.dp))

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 24.dp, 36.dp, 0.dp),
            horizontalArrangement = Arrangement.End
        ) {
            RandomFactsThemeSwitch(onToggle = { onToggle() })
        }
    }
}

@Composable
fun RandomFactsThemeSwitch(onToggle: () -> Unit) {
    val boo = !isSystemInDarkTheme()
    var isIconDark by remember {
        mutableStateOf(boo)
    }
        Icon(
            painter = if (isIconDark) painterResource(id = R.drawable.ic_light_off)
            else painterResource(id = R.drawable.ic_light_on),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp, 24.dp)
                .clickable(onClick = {
                    onToggle()
                    isIconDark = !isIconDark
                }),
            tint = MaterialTheme.colors.primary
        )
}
