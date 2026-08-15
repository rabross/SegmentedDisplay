package com.rabross.segmenteddisplay.app

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.rabross.segmenteddisplay.seven.BinaryDecoder
import com.rabross.segmenteddisplay.seven.DigitalClock
import com.rabross.segmenteddisplay.seven.SegmentStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.*
import kotlin.time.Duration.Companion.milliseconds

class DigitalClockActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setContent {
            var time by remember { mutableStateOf(Calendar.getInstance()) }

            LaunchedEffect(Unit) {
                while (isActive) {
                    time = Calendar.getInstance()
                    delay(1000.milliseconds)
                }
            }

            Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(32.dp)) {
                    val hour = time.get(Calendar.HOUR_OF_DAY)
                    val minute = time.get(Calendar.MINUTE)
                    val second = time.get(Calendar.SECOND)

                    DigitalClock(
                        modifier = Modifier.fillMaxWidth(),
                        hourFirst = BinaryDecoder.mapToDisplay(hour / 10),
                        hourSecond = BinaryDecoder.mapToDisplay(hour % 10),
                        minuteFirst = BinaryDecoder.mapToDisplay(minute / 10),
                        minuteSecond = BinaryDecoder.mapToDisplay(minute % 10),
                        secondFirst = BinaryDecoder.mapToDisplay(second / 10),
                        secondSecond = BinaryDecoder.mapToDisplay(second % 10),
                        delimiterSignal = 3,
                        segmentStyle = SegmentStyle.DIFFUSER
                    )
                }
            }
        }
    }
}
