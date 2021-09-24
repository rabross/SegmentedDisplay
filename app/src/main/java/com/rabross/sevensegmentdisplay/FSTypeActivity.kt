package com.rabross.sevensegmentdisplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.time.ExperimentalTime

@ExperimentalTime
class FSTypeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var text by remember { mutableStateOf("Hello World") }

            Column {
                Surface(modifier = Modifier
                    .weight(1f), color = Color.Black) {
                    Row(modifier = Modifier.padding(24.dp, 24.dp, 24.dp, 0.dp)) {
                        if(text.isNullOrBlank()){
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            for (c in text) {
                                FourteenSegmentDisplay(
                                    modifier = Modifier.weight(1f),
                                    decoder = BinaryFourteenSegmentDecoder(
                                        BinaryFourteenSegmentDecoder.mapToDigit(c.uppercaseChar())
                                    )
                                )
                            }
                        }
                    }
                }
                Row(
                    Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .padding(24.dp, 0.dp)
                ) {
                    TextField(modifier = Modifier.fillMaxWidth(),
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("input") }
                    )
                }

            }
        }
    }

}