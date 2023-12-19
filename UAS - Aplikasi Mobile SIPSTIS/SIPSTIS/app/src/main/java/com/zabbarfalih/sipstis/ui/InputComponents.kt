@file:OptIn(ExperimentalMaterial3Api::class)

package com.zabbarfalih.sipstis.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.zabbarfalih.sipstis.R
import com.zabbarfalih.sipstis.ui.theme.SipstisTheme
import com.zabbarfalih.sipstis.ui.theme.poppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        label = label,
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        modifier = modifier,
        trailingIcon = {
            val image = if (passwordVisible) {
                Icons.Filled.VisibilityOff
            } else {
                Icons.Filled.Visibility
            }

            val description = if (passwordVisible) {
                stringResource(id = R.string.hide_password)
            } else {
                stringResource(id = R.string.show_password)
            }

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        }
    )
}

@Composable
fun ConfirmDialog(
    onConfirmRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    @StringRes message: Int,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(18.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(100.dp)
                    )

                    Text(
                        text = stringResource(id = message),
                        style = TextStyle(
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.W700,
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(
                            onClick = onDismissRequest
                        ) {
                            Text(
                                text = stringResource(id = R.string.tidak),
                                style = TextStyle(
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.W700,
                                )
                            )
                        }

                        Button(
                            onClick = onConfirmRequest,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.ya),
                                style = TextStyle(
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.W700,
                                )
                            )
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun ProgressDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(100.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun MessageDialog(
    onDismissRequest: () -> Unit,
    onClose: () -> Unit,
    @StringRes title: Int,
    @StringRes message: Int
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(id = title),
                style = TextStyle(
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W700,
                    fontSize = 20.sp,
                )
            )
        },
        text = {
            Text(
                text = stringResource(id = message),
                style = TextStyle(
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp,
                )
            )
        },
        confirmButton = {
            Button(onClick = onClose) { Text(
                text = stringResource(R.string.tutup),
                style = TextStyle(
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W700,
                    fontSize = 12.sp,
                )
            ) }
        }
    )
}

@Preview
@Composable
fun ConfirmDialogPreview() {
    SipstisTheme {
        ConfirmDialog(
            onConfirmRequest = {},
            onDismissRequest = {},
            message = R.string.konfirmasi_hapus_akun
        )
    }
}