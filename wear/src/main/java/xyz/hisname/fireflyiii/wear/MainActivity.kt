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

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import xyz.hisname.fireflyiii.common.Constants
import xyz.hisname.fireflyiii.common.data.AccountRepository
import xyz.hisname.fireflyiii.common.data.AppDatabase
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class MainActivity: Activity(), DataClient.OnDataChangedListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getAccountData()
    }

    private fun getAccountData(){
        CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Toast.makeText(this@MainActivity,
                    throwable.localizedMessage, Toast.LENGTH_LONG).show()
        }).launch {
            val nodeListTask = Wearable.getNodeClient(this@MainActivity).connectedNodes
            val nodes = Tasks.await(nodeListTask)
            val sendMessage = Wearable.getMessageClient(this@MainActivity).sendMessage(nodes[0].id,
                    "accountsList", "accountsList".toByteArray())
            Tasks.await(sendMessage)
            sendMessage.addOnSuccessListener {
                displayData()
            }
            sendMessage.addOnFailureListener {
                displayData()
            }
        }
    }

    private fun displayData(){
        val recyclerView = findViewById<WearableRecyclerView>(R.id.recycler_launcher_view)
        recyclerView.apply {
            isEdgeItemsCenteringEnabled = true
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
            layoutManager = WearableLinearLayoutManager(this@MainActivity,  CustomScrollingLayoutCallback())
            isCircularScrollingGestureEnabled = true
            bezelFraction = 0.5f
            scrollDegreesPerScreen = 90f
        }
        CoroutineScope(Dispatchers.Main).launch {
            val accountRepository = AccountRepository(AppDatabase.getInstance(this@MainActivity).accountDao())
            accountRepository.getData().collectLatest {  accountData ->
                recyclerView.adapter = AccountListRecyclerView(accountData)
            }
        }
    }

    override fun onResume() {
        Wearable.getDataClient(this).addListener(this)
        super.onResume()
    }
    
    override fun onPause() {
        Wearable.getDataClient(this).removeListener(this)
        super.onPause()
    }

    override fun onDataChanged(dataEventBuffer: DataEventBuffer) {
        dataEventBuffer.filter {
            it.type == DataEvent.TYPE_CHANGED && it.dataItem.uri.path == "/accountsList"
        }.forEach { event ->
            DataMapItem.fromDataItem(event.dataItem)
                    .dataMap.getAsset("accountsList").let { asset ->
                        CoroutineScope(Dispatchers.IO).launch{
                            val assetInputStream =
                                    Tasks.await(Wearable.getDataClient(this@MainActivity).getFdForAsset(asset)).inputStream
                            var out: OutputStream? = null
                            try {
                                out = FileOutputStream(this@MainActivity.getDatabasePath(Constants.DB_NAME))
                                val buf = ByteArray(1024)
                                var len: Int
                                while (assetInputStream.read(buf).also { len = it } > 0) {
                                    out.write(buf, 0, len)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            } finally {
                                try {
                                    out?.close()
                                    assetInputStream.close()
                                } catch (e: IOException) { }
                            }
                        }
                    }
        }
    }
}