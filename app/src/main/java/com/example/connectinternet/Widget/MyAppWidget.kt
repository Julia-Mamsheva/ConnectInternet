package com.example.connectinternet.Widget


import androidx.compose.runtime.Composable
import androidx.glance.Button

import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import com.example.connectinternet.MainActivity

class MyAppWidget : GlanceAppWidget() {

    //Еще Android Manifest
    /*
        <receiver
            android:name=".Widget.SimpleGlanceAppWidgetReceiver"
            android:exported="true">
            <intent-filter>
                <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
            </intent-filter>

            <meta-data
                android:name="android.appwidget.provider"
                android:resource="@xml/simple_glance_widget_info" />
        </receiver>
    */
    //Еще xml/simple_glance_widget_info

    //Еще layout widget_layout
    @Composable
    override fun Content() {

        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Test")
            Row(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    text = "Home",
                    onClick = actionStartActivity<MainActivity>()
                )
                Button(
                    text = "Click",
                    onClick = actionStartActivity<MainActivity>()
                )

            }
        }
    }
}

