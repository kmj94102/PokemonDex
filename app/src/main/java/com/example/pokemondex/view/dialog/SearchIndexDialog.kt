package com.example.pokemondex.view.dialog

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.pokemondex.ui.theme.*
import com.example.pokemondex.util.getBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchIndexDialog(
    isShow: MutableState<Boolean>,
    okClickListener: (String) -> Unit
) {
    var valueState by remember { mutableStateOf("") }

    if (isShow.value) {
        Dialog(onDismissRequest = { }) {
            Card(
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSystemInDarkTheme()) DarkDialogBackground else White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "포켓몬 정보 조회",
                        style = Typography.titleLarge,
                        color = getBlack(),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = valueState,
                        onValueChange = { valueState = it },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = if (isSystemInDarkTheme()) DarkDialogBackground else White,
                            focusedLabelColor = MainColor,
                            unfocusedLabelColor = Gray,
                            cursorColor = MainColor,
                            textColor = getBlack(),
                            unfocusedBorderColor = MainColor,
                            focusedBorderColor = MainColor
                        ),
                        placeholder = {
                            Text(text = "포켓몬 번호", style = Typography.bodyMedium)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) // OutlinedTextField

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            okClickListener(valueState)
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "확인",
                            style = Typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = White,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                } // Column
            } // Card
        } // Dialog
    } // If
}