package io.hikarilan.tishinenghacker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.hikarilan.tishinenghacker.ui.theme.TiShiNengHackerTheme

class MainActivity : ComponentActivity() {

    private val distance = mutableStateOf(70000.0f)
    private val step = mutableStateOf(100000)

    private val running = mutableStateOf(false)
    private val firstRun = mutableStateOf(true)
    private val runningThread = Thread {
        while (true) {
            if (System.currentTimeMillis() % 5 != 0L) continue
            if (!running.value) continue
            sendBroadcast(Intent().apply {
                this.action = "ADD_START_POINT"
                this.putExtra(
                    "startTime",
                    System.currentTimeMillis() - 6000000L
                )
            })
            sendBroadcast(Intent().apply {
                this.action = "ADD_LINE"
                this.putExtra("distance", distance.value)
            })
            sendBroadcast(Intent().apply {
                this.action = "UPDATE_STEP"
                this.putExtra("stepCount", step.value)
            })
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiShiNengHackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(100.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp), verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "距离")
                            TextField(value = distance.value.toString(), onValueChange = {
                                if (it.toFloatOrNull() == null) return@TextField
                                else distance.value = it.toFloat()
                            })
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp), verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "步数")
                            TextField(value = step.value.toString(), onValueChange = {
                                if (it.toIntOrNull() == null) return@TextField
                                else step.value = it.toInt()
                            })
                        }
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "点击以设置步数为 ${distance.value}，距离为 ${step.value}",
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    if (firstRun.value) {
                                        runningThread.start()
                                        firstRun.value = false
                                    }
                                    running.value = !running.value
                                }
                            ) {
                                Text(text = "运行状态：${running.value}")
                            }
                        }
                    }
                }
            }
        }
    }
}