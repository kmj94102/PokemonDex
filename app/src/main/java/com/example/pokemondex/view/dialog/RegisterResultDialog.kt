package com.example.pokemondex.view.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.pokemondex.ui.theme.Typography
import com.example.pokemondex.ui.theme.White
import com.example.pokemondex.util.getBlack

@Composable
fun RegisterResultDialog(
    result: String,
    isShow: State<Boolean>,
    okClickListener: () -> Unit
) {
    if (isShow.value) {
        Dialog(onDismissRequest = {}) {

            Card(
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "등록 결과",
                        style = Typography.titleLarge,
                        color = getBlack(),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = result,
                        style = Typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            okClickListener()
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
                }
            } // Card
        } // Dialog
    } // if
}