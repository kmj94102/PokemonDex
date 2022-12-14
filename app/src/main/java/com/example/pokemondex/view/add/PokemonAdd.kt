package com.example.pokemondex.view.add

import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.pokemondex.R
import com.example.pokemondex.ui.theme.*
import com.example.pokemondex.util.*
import com.example.pokemondex.view.dialog.RegisterResultDialog
import com.example.pokemondex.view.dialog.SearchIndexDialog
import com.example.pokemondex.view.navigation.RouteAction

@Composable
fun PokemonAddContainer(
    routeAction: RouteAction,
    viewModel: PokemonAddViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    val loadingDialogState = remember { mutableStateOf(false) }
    val searchDialogState = remember { mutableStateOf(false) }
    val resultDialogState = remember { mutableStateOf(false) }
    val resultState = remember { mutableStateOf("") }

    LazyColumn(modifier = Modifier.fillMaxSize()) {

        val state = viewModel.pokemonState

        /** ?????? ????????? **/
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 17.dp, horizontal = 17.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_prev),
                    contentDescription = "prev",
                    colorFilter = ColorFilter.tint(getBlack()),
                    modifier = Modifier
                        .nonRippleClickable {
                            routeAction.popupBackStack()
                        }
                )
                Text(
                    text = "????????? ??????",
                    style = Typography.titleLarge,
                    textAlign = TextAlign.Center,
                    color = MainColor,
                    modifier = Modifier.align(Alignment.Center)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_setting),
                    contentDescription = "setting",
                    colorFilter = ColorFilter.tint(getBlack()),
                    modifier = Modifier
                        .nonRippleClickable {
                            searchDialogState.value = true
                        }
                        .align(Alignment.TopEnd)
                )
            } // Box
        } // ?????? ?????????

        /** ????????? ?????? & ?????? **/
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                /** ????????? ?????? **/
                CustomTextFiled(
                    value = state.value.number ?: "",
                    hint = "????????? ??????",
                    changeListener = {
                        viewModel.textFieldChange(PokemonAddViewModel.Number, it)
                    },
                    inputType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                /** ????????? ?????? **/
                CustomTextFiled(
                    value = state.value.name ?: "",
                    hint = "????????? ??????",
                    changeListener = {
                        viewModel.textFieldChange(PokemonAddViewModel.Name, it)
                    },
                    modifier = Modifier.weight(2f)
                )
            } // Row
        } // ????????? ?????? & ??????

        item { Spacer(modifier = Modifier.height(10.dp)) }

        /** ????????? ?????? **/
        item {
            CustomTextFiled(
                value = state.value.description ?: "",
                hint = "????????? ??????",
                changeListener = {
                    viewModel.textFieldChange(PokemonAddViewModel.Description, it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        } // ????????? ??????

        item { Spacer(modifier = Modifier.height(10.dp)) }

        /** ????????? ?????? **/
        item {
            CustomTextFiled(
                value = state.value.classification ?: "",
                hint = "????????? ??????",
                changeListener = {
                    viewModel.textFieldChange(PokemonAddViewModel.Classification, it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        } // ????????? ??????

        item { Spacer(modifier = Modifier.height(10.dp)) }

        /** ????????? ?????? **/
        item {
            viewModel.characteristicList.forEachIndexed { index, it ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    /** ????????? ?????? **/
                    CustomTextFiled(
                        value = it.name ?: "",
                        hint = "????????? ??????",
                        changeListener = { value ->
                            viewModel.event(
                                AddEvent.InputCharacteristic(
                                    index,
                                    PokemonAddViewModel.Characteristic,
                                    value
                                )
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    /** ????????? ?????? ?????? **/
                    CustomTextFiled(
                        value = it.description ?: "",
                        hint = "??????",
                        changeListener = { value ->
                            viewModel.event(
                                AddEvent.InputCharacteristic(
                                    index,
                                    PokemonAddViewModel.Description,
                                    value
                                )
                            )
                        },
                        modifier = Modifier.weight(2f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                } // Row
            } // forEach

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                /** ?????? ?????? **/
                CustomButton(
                    value = "?????? ??????",
                    clickListener = {
                        viewModel.event(AddEvent.AddCharacteristic)
                    },
                    modifier = Modifier
                        .weight(1f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                /** ?????? ?????? **/
                CustomButton(
                    value = "?????? ??????",
                    clickListener = {
                        viewModel.event(AddEvent.RemoveCharacteristic)
                    },
                    modifier = Modifier
                        .weight(1f)
                )
            } // Row
        } // ????????? ??????

        /** ????????? ?????? **/
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                CustomTextFiled(
                    value = viewModel.typeList[0],
                    hint = "????????? ??????",
                    changeListener = { value ->
                        viewModel.event(
                            AddEvent.InputType(0, value)
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                CustomTextFiled(
                    value = viewModel.typeList[1],
                    hint = "????????? ??????",
                    changeListener = { value ->
                        viewModel.event(
                            AddEvent.InputType(1, value)
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            } // Row
        } // ????????? ??????

        item { Spacer(modifier = Modifier.height(10.dp)) }

        /** ????????? ?????? **/
        item {
            CustomTextFiled(
                value = "${state.value.generation ?: 0}",
                hint = "????????? ??????",
                changeListener = { value ->
                    viewModel.textFieldChange(PokemonAddViewModel.Generation, value)
                },
                inputType = KeyboardType.Number,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        } // ????????? ??????

        item { Spacer(modifier = Modifier.height(10.dp)) }

        /** ????????? ?????? **/
        gridItems(
            data = viewModel.statusList,
            columnCount = 3,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 24.dp)
        ) { data, _ ->
            CustomTextFiled(
                value = "${data.value}",
                hint = data.name,
                changeListener = { value ->
                    viewModel.textFieldChange(data.name, value)
                },
                inputType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth()
            )
        } // ????????? ??????

        item { Spacer(modifier = Modifier.height(10.dp)) }

        item {
            CustomButton(
                value = "????????? ????????? ??????",
                clickListener = {
                    viewModel.event(AddEvent.DefaultImageSetting)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        /** ????????? ????????? **/
        item {
            CustomTextFiled(
                value = state.value.image ?: "",
                hint = "????????? ?????????",
                changeListener = { value ->
                    viewModel.textFieldChange(PokemonAddViewModel.Image, value)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        } // ????????? ?????????

        item { Spacer(modifier = Modifier.height(10.dp)) }

        /** ????????? ????????? ????????? **/
        item {
            CustomTextFiled(
                value = state.value.shinyImage ?: "",
                hint = "????????? ????????? ?????????",
                changeListener = { value ->
                    viewModel.textFieldChange(PokemonAddViewModel.ShinyImage, value)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        } // ????????? ????????? ?????????

        item { Spacer(modifier = Modifier.height(10.dp)) }

        /** ????????? ?????? ????????? **/
        item {
            CustomTextFiled(
                value = state.value.dotImage ?: "",
                hint = "????????? ?????? ?????????",
                changeListener = { value ->
                    viewModel.textFieldChange(PokemonAddViewModel.DotImage, value)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        } // ????????? ?????? ?????????

        item { Spacer(modifier = Modifier.height(10.dp)) }

        /** ????????? ?????? ????????? ????????? **/
        item {
            CustomTextFiled(
                value = state.value.dotShinyImage ?: "",
                hint = "????????? ?????? ????????? ?????????",
                changeListener = { value ->
                    viewModel.textFieldChange(PokemonAddViewModel.DotShinyImage, value)
                },
                inputType = KeyboardType.Number,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        } // ????????? ?????? ????????? ?????????

        item { Spacer(modifier = Modifier.height(10.dp)) }

        /** ????????? ?????? : ??????, ????????? **/
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = state.value.image,
                    contentDescription = null,
                    error = painterResource(id = R.drawable.img_monsterbal),
                    placeholder = painterResource(id = R.drawable.img_monsterbal),
                    imageLoader = imageLoader,
                    modifier = Modifier.size(100.dp)
                )

                AsyncImage(
                    model = state.value.shinyImage,
                    contentDescription = null,
                    error = painterResource(id = R.drawable.img_monsterbal),
                    placeholder = painterResource(id = R.drawable.img_monsterbal),
                    imageLoader = imageLoader,
                    modifier = Modifier.size(100.dp)
                )
            }
        } // ????????? ?????? : ??????, ?????????

        item { Spacer(modifier = Modifier.height(10.dp)) }

        /** ????????? ?????? : ?????? ??????, ????????? ?????? **/
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = state.value.dotImage,
                    contentDescription = null,
                    error = painterResource(id = R.drawable.img_monsterbal),
                    placeholder = painterResource(id = R.drawable.img_monsterbal),
                    imageLoader = imageLoader,
                    modifier = Modifier.size(100.dp)
                )

                AsyncImage(
                    model = state.value.dotShinyImage,
                    contentDescription = null,
                    error = painterResource(id = R.drawable.img_monsterbal),
                    placeholder = painterResource(id = R.drawable.img_monsterbal),
                    imageLoader = imageLoader,
                    modifier = Modifier.size(100.dp)
                )
            }
        } // ????????? ?????? : ?????? ??????, ????????? ??????

        item { Spacer(modifier = Modifier.height(10.dp)) }

        /** ???????????? **/
        item {
            Button(
                onClick = {
                    viewModel.event(
                        AddEvent.Register(
                            successListener = {
                                resultState.value = it
                                resultDialogState.value = true
                                Log.e("+++++", "success : \n$it")
                            },
                            failureListener = {
                                resultState.value = "?????? ??????"
                                resultDialogState.value = true
                                Log.e("+++++", "failure")
                            }
                        )
                    )
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "????????????",
                    textAlign = TextAlign.Center,
                    color = White,
                    style = Typography.bodyLarge,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

    } // LazyColumn

    SearchIndexDialog(
        isShow = searchDialogState,
        okClickListener = {
            searchDialogState.value = false
            viewModel.event(AddEvent.SearchPokemonInfo(
                index = it,
                failureListener = { msg ->
                    context.toast(msg)
                }
            ))
        }
    )

    RegisterResultDialog(
        result = resultState.value,
        isShow = resultDialogState
    ) {
        resultDialogState.value = false
    }

}

private fun PokemonAddViewModel.textFieldChange(type: String, value: String) {
    event(AddEvent.InputTextChange(type, value))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextFiled(
    value: String,
    hint: String,
    isEnabled: Boolean = true,
    inputType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions(),
    changeListener: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            changeListener(it)
        },
        label = {
            Text(
                text = hint,
                style = Typography.bodyMedium,
            )
        },
        enabled = isEnabled,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = getBlack(),
            containerColor = Color.Transparent,
            cursorColor = MainColor,
            unfocusedBorderColor = getBlack(),
            unfocusedLabelColor = getGray(),
            focusedBorderColor = MainColor,
            focusedLabelColor = MainColor
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = inputType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        modifier = modifier
    )
}

@Composable
fun CustomButton(
    value: String,
    modifier: Modifier = Modifier,
    color: Color = Yellow,
    textColor: Color = Black,
    clickListener: () -> Unit
) {
    Button(
        onClick = { clickListener() },
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, MainColor),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        modifier = modifier
    ) {
        Text(
            text = value,
            color = textColor,
            style = Typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}