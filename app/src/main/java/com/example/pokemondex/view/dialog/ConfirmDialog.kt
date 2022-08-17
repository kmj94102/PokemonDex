package com.example.pokemondex.view.dialog

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.pokemondex.R
import com.example.pokemondex.ui.theme.DarkDialogBackground
import com.example.pokemondex.ui.theme.Typography
import com.example.pokemondex.ui.theme.White
import com.example.pokemondex.util.getWhite

@Composable
fun ConfirmDialog(
    message: String,
    isShow: State<Boolean>,
    okClickListener: () -> Unit
) {
    if (isShow.value) {
        Dialog(onDismissRequest = {}) {
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
                        text = message,
                        style = Typography.titleLarge,
                        color = getWhite(),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) // Text

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            okClickListener()
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.ok),
                            style = Typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = White,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    } // Button
                } // Column
            } // Card
        } // Dialog
    } // if
}