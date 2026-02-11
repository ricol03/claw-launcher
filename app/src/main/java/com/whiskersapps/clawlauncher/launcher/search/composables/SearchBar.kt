package com.whiskersapps.clawlauncher.launcher.search.composables

import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.whiskersapps.clawlauncher.R
import com.whiskersapps.clawlauncher.shared.model.App

@Composable
fun SearchBar(
    text: String,
    onChange: (String) -> Unit,
    onDone: () -> Unit = {},
    enabled: Boolean = true,
    placeholder: String = stringResource(id = R.string.Search),
    borderRadius: Int = 100,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    focus: Boolean = false,
    onFocused: () -> Unit = {},
    quickButton: Boolean,
    secondButton: Boolean,
    packageNameOne: String?,
    packageNameTwo: String?,
    apps: List<App>?
) {
    val context = LocalContext.current
    val localDensity = LocalDensity.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var fieldHeight by remember { mutableStateOf(0.dp) }

    var trueAppOne: App? = null
    var trueAppTwo: App? = null

    if (quickButton) {
        if (apps != null) {
            for (app in apps) {
                if (app.packageName == packageNameOne)
                    trueAppOne = app

                if (secondButton)
                    if (app.packageName == packageNameTwo)
                        trueAppTwo = app
            }
        }
    }

    LaunchedEffect(focus) {
        if (focus) {
            focusManager.clearFocus()
            focusRequester.requestFocus()
            onFocused()
        }
    }

    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(fieldHeight)
                .clip(RoundedCornerShape(borderRadius))
                .background(backgroundColor)
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onGloballyPositioned {
                    fieldHeight = with(localDensity) { it.size.height.toDp() }
                },
            value = text,
            onValueChange = { onChange(it) },
            shape = RoundedCornerShape(borderRadius),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "search icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    onDone()
                }
            ),
            maxLines = 1,
            singleLine = true,
            enabled = enabled
        )

        if (quickButton) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Row (
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    IconButton(
                        modifier = Modifier.size(32.dp),
                        content = {
                            val bitmap = trueAppOne?.icons?.stock?.default?.asImageBitmap()

                            bitmap?.let {
                                Image(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .aspectRatio(1f),
                                    bitmap = it,
                                    contentDescription = "${trueAppOne?.packageName} icon",
                                    contentScale = ContentScale.FillBounds,
                                )
                            }
                        },
                        onClick = {
                            val intent = context.packageManager
                                .getLaunchIntentForPackage(packageNameOne!!)

                            intent?.let { context.startActivity(it) }
                        },

                    )

                    if (secondButton) {
                        IconButton(
                            modifier = Modifier.size(32.dp),
                            content = {
                                val bitmap = trueAppTwo?.icons?.stock?.default?.asImageBitmap()

                                bitmap?.let {
                                    Image(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .aspectRatio(1f),
                                        bitmap = it,
                                        contentDescription = "${trueAppTwo?.packageName} icon",
                                        contentScale = ContentScale.FillBounds,
                                    )
                                }
                            },
                            onClick = {
                                val intent = context.packageManager
                                    .getLaunchIntentForPackage(packageNameTwo!!)

                                intent?.let { context.startActivity(it) }
                            },
                        )
                    }
                }
            }
        }


    }
}