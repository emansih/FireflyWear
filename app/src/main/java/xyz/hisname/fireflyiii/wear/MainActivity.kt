/*
 * Copyright (c) 2021 ASDF Dev Pte. Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package xyz.hisname.fireflyiii.wear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.gms.wearable.Wearable
import xyz.hisname.fireflyiii.wear.utils.DeviceCheck

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!DeviceCheck.isApproved(this)) {
            findViewById<TextView>(R.id.appInstalledText).text =
                "❌ Please install the main app from official stores"
        } else {
            findViewById<TextView>(R.id.appInstalledText).text = "✔️Main app installed!"
            val wearableList = Wearable.getNodeClient(this).connectedNodes
            wearableList.addOnCompleteListener { task ->
                val displayName = task.result?.get(0)?.displayName
                if (displayName != null){
                    findViewById<TextView>(R.id.wearableConnectedText).text = "✔️Connected to $displayName"
                } else {
                    findViewById<TextView>(R.id.appInstalledText).text =
                        "❌ Please ensure you have a connected device running WearOS"
                }
            }
        }
    }
}