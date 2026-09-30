package com.paliy.composenotes.presentation.screens.creation

import android.R
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.paliy.composenotes.domain.ContentItem
import com.paliy.composenotes.presentation.ui.theme.CustomIcons
import com.paliy.composenotes.presentation.utils.DateFormatter
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteScreen(
    modifier: Modifier = Modifier,
    /*context: Context = LocalContext.current.applicationContext,
    viewModel: CreateNoteViewModel = viewModel{
        CreateNoteViewModel(context)
    },*/
    viewModel: CreateNoteViewModel = hiltViewModel(),
    onFinished:() -> Unit
){
    val state = viewModel.state.collectAsState()
    val currentState = state.value

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri ->
            uri?.let {
                viewModel.processCommand(CreateNoteCommand.AddImage(it))
            }
        }
    )

    when(currentState) {
        is CreateNoteState.Creation -> {
            //LaunchedEffect(key1 = currentState.isSaved) { }
            Scaffold(
                modifier = modifier,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Create Note",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent,
                            navigationIconContentColor = MaterialTheme.colorScheme.onSurface),
                        navigationIcon = {
                            Icon(
                                modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                                    .clickable {
                                        viewModel.processCommand(CreateNoteCommand.Back)
                                    },
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        },
                        actions = {
                            Icon(
                                modifier = Modifier.clickable{
                                    imagePicker.launch("image/*")
                                }
                                    .padding(end = 24.dp),
                                imageVector = CustomIcons.AddPhoto,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
            ) {innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        value = currentState.title,
                        onValueChange = {
                            viewModel.processCommand(CreateNoteCommand.InputTitle(it))
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        textStyle = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        placeholder = {
                            Text(
                                text = "Title",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                            )
                        }
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = DateFormatter.formatCurrentDate(),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Content(
                        modifier = Modifier.weight(1f),
                        content = currentState.content,
                        onDeleteImageClick = {
                            viewModel.processCommand(CreateNoteCommand.DeleteImage(it))
                        },
                        onTextChanged = {index, text ->
                            viewModel.processCommand(
                                CreateNoteCommand.InputContent(content = text, index = index)
                            )
                        }
                    )
                    Button(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxWidth(),
                        onClick = {
                            viewModel.processCommand(CreateNoteCommand.Save)
                        },
                        shape = RoundedCornerShape(10.dp),
                        enabled = currentState.isSaveEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = "Save Note"

                        )
                    }
                }
            }
        }
        CreateNoteState.Finished -> {
            LaunchedEffect(key1 = Unit) {
                onFinished()
            }
        }
    }
}


@Composable
private fun Content(
    modifier: Modifier = Modifier,
    content : List<ContentItem>,
    onDeleteImageClick: (Int) -> Unit,
    onTextChanged: (Int, String) -> Unit
){
    LazyColumn(
        modifier = modifier
    ) {
        content.forEachIndexed { index, contentItem ->
            item(key = index) {
                when(contentItem) {
                    is ContentItem.Image -> {
                        val isAlreadyDisplayed =
                            index > 0 && content[index - 1] is ContentItem.Image
                        content.takeIf { !isAlreadyDisplayed }
                            ?.drop(index)
                            ?.takeWhile { it is ContentItem.Image }
                            ?.map { (it as ContentItem.Image).uri }
                            ?.let { urls ->
                                ImageGroup(
                                    modifier = Modifier.padding(horizontal = 24.dp),
                                    imageUrls = urls,
                                    onDeleteImageClick ={imageIndex ->
                                        onDeleteImageClick(index + imageIndex)
                                    }
                                )
                            }

                    }
                    is ContentItem.Text -> {
                        TextContent(
                            text = contentItem.content,
                            onTextChanged = {
                                onTextChanged(index,it)
                            }
                        )
                    }
                }
            }
        }
    }
}



@Composable
private fun ImageGroup(
    modifier: Modifier = Modifier,
    imageUrls: List<String>,
    onDeleteImageClick: (Int) -> Unit
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        imageUrls.forEachIndexed { index, url ->
            ImageContent(
                modifier = Modifier.weight(1f),
                imageUrl = url,
                onDeleteImageClick = {
                    onDeleteImageClick(index)
                }
            )
        }
    }
}


@Composable
private fun ImageContent(
    modifier: Modifier = Modifier,
    imageUrl: String,
    onDeleteImageClick: () -> Unit
){
    Box(
        modifier = modifier
    ){
        AsyncImage(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Icon(
            modifier = Modifier.align(Alignment.TopEnd)
                .padding(8.dp)
                .size(24.dp)
                .clickable{
                    onDeleteImageClick()
                },
            imageVector = Icons.Default.Close,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Composable
private fun TextContent(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit

){
    TextField(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 8.dp),
        value = text,
        onValueChange = onTextChanged,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        placeholder = {
            Text(
                text = "Note",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
        }
    )
}