package com.example.pokemondex.view.dialog

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.pokemondex.ui.theme.DarkDialogBackground
import com.example.pokemondex.ui.theme.Typography
import com.example.pokemondex.ui.theme.White
import com.example.pokemondex.util.getBlack
import com.example.pokemondex.util.getWhite
import com.example.pokemondex.util.toast
import com.example.pokemondex.view.add.CustomButton
import com.example.pokemondex.view.add.CustomTextFiled

@Composable
fun SelectInfoDialog(
    isShow: State<Boolean>,
    context: Context,
    okClickListener: (String, String) -> Unit
) {

    val startIndex = remember {
        mutableStateOf("")
    }
    val endIndex = remember {
        mutableStateOf("")
    }

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
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "조회할 인덱스를 입력해주세요.",
                        style = Typography.titleLarge,
                        color = getBlack(),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp)
                    )
                    CustomTextFiled(
                        value = startIndex.value,
                        hint = "시작 인덱스",
                        changeListener = { value -> startIndex.value = value },
                        modifier = Modifier.fillMaxWidth()
                    )
                    CustomTextFiled(
                        value = endIndex.value,
                        hint = "종료 인덱스",
                        changeListener = { value -> endIndex.value = value },
                        modifier = Modifier.fillMaxWidth()
                    )
                    CustomButton(value = "조회") {
                        if (startIndex.value.isEmpty() || endIndex.value.isEmpty()) {
                            context.toast("값을 입력해주세요")
                        }
                        okClickListener(startIndex.value, endIndex.value)
                    }
                }
            }
        }
    }

}