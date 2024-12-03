package com.example.myapplicationsprint11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.myapplicationsprint11.ui.theme.MyApplicationsprint11Theme
import com.example.myapplicationsprint11.view.MedicamentScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationsprint11Theme {
                MedicamentScreen()
            }
        }
    }
}