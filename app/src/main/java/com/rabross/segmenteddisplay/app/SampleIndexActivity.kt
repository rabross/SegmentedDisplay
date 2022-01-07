package com.rabross.segmenteddisplay.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class SampleIndexActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column {
                LaunchButton<SevenSegmentActivity>(text = "7-segment display")
                LaunchButton<SevenSegmentHeartActivity>(text = "7-segment display heart")
                LaunchButton<FourteenSegmentActivity>(text = "14-segment display")
                LaunchButton<TypingActivity>(text = "14-segment display typing")
            }
        }
    }

    @Composable
    inline fun <reified T> LaunchButton(text: String) {
        val context = LocalContext.current
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            onClick = { context.startActivity(Intent(context, T::class.java)) }) {
            Text(text = text)
        }
    }
}