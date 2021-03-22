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
import androidx.core.net.toUri
import com.google.android.gms.wearable.Asset
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xyz.hisname.fireflyiii.common.Constants
import xyz.hisname.fireflyiii.common.data.AccountRepository
import xyz.hisname.fireflyiii.common.data.AppDatabase
import xyz.hisname.fireflyiii.wear.utils.DeviceCheck

class MainActivity : AppCompatActivity() {

    private val URI = "content://xyz.hisname.fireflyiii.provider/accountList".toUri()
    private val accountRepository by lazy { AccountRepository(AppDatabase.getInstance(this@MainActivity).accountDao()) }

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


    override fun onResume() {
        super.onResume()
        if(DeviceCheck.isApproved(this)){
            CoroutineScope(Dispatchers.IO).launch {
                val cursor = this@MainActivity.contentResolver.query(URI,
                        null, null, null, null)
                accountRepository.insert(cursor)
                val dataMap = PutDataMapRequest.create("/accountsList")
                dataMap.dataMap.putAsset("accountsList",
                        Asset.createFromUri(this@MainActivity.getDatabasePath(Constants.DB_NAME).toUri()))
                val request = dataMap.asPutDataRequest()
                request.setUrgent()
                Wearable.getDataClient(this@MainActivity).putDataItem(request)
            }
        }
    }
}