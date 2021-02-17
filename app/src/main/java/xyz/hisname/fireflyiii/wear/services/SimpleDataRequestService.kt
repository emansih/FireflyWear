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

package xyz.hisname.fireflyiii.wear.services

import androidx.core.net.toUri
import androidx.preference.PreferenceManager
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.hisname.fireflyiii.wear.SimpleData
import xyz.hisname.fireflyiii.wear.utils.DeviceCheck

class SimpleDataRequestService: WearableListenerService() {

    private val URI = "content://xyz.hisname.fireflyiii.provider.simpleData/simpleData".toUri()

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        if(messageEvent.path != null && DeviceCheck.isApproved(applicationContext)){
            val cursor = this.contentResolver.query(URI, null,
                    null, null, null)
            val dataStore = SimpleData(PreferenceManager.getDefaultSharedPreferences(this))
            if(cursor != null){
                cursor.moveToFirst()
                cursor.columnNames.forEachIndexed { index, name ->
                    when {
                        name.contentEquals("Balance") -> {
                            dataStore.balance = cursor.getString(index)
                        }
                        name.contentEquals("Bills To Pay") -> {
                            dataStore.unPaidBills = cursor.getString(index)
                        }
                        name.contentEquals("Paid") -> {
                            dataStore.paidBills = cursor.getString(index)
                        }
                        name.contentEquals("Left To Spend") -> {
                            dataStore.leftToSpend = cursor.getString(index)
                        }
                        name.contentEquals("Per Day") -> {
                            dataStore.leftToSpendPerDay = cursor.getString(index)
                        }
                        name.contentEquals("Net Worth") -> {
                            dataStore.networthValue = cursor.getString(index)
                        }
                    }
                }
            }
            cursor?.close()
            CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { coroutineContext, throwable ->
                throwable.printStackTrace()
            }).launch {
                var dataToSend: String? = ""
                var dataPath = ""
                when {
                    String(messageEvent.data).contentEquals("Balance") -> {
                        dataToSend = dataStore.balance
                        dataPath = "balance"
                    }
                    String(messageEvent.data).contentEquals("Bills To Pay") -> {
                        dataToSend = dataStore.unPaidBills
                        dataPath = "billsToPay"
                    }
                    String(messageEvent.data).contentEquals("Paid") -> {
                        dataToSend = dataStore.paidBills
                        dataPath = "paid"
                    }
                    String(messageEvent.data).contentEquals("Left To Spend") -> {
                        dataToSend = dataStore.leftToSpend
                        dataPath = "leftToSpend"
                    }
                    String(messageEvent.data).contentEquals("Per Day") -> {
                        dataToSend = dataStore.leftToSpendPerDay
                        dataPath = "perDay"
                    }
                    String(messageEvent.data).contentEquals("Net Worth") -> {
                        dataToSend = dataStore.networthValue
                        dataPath = "netWorth"
                    }
                }
                val nodeListTask = Wearable.getNodeClient(this@SimpleDataRequestService).connectedNodes
                val nodes = Tasks.await(nodeListTask)
                val sendMessage = Wearable.getMessageClient(this@SimpleDataRequestService)
                        .sendMessage(nodes[0].id, dataPath, dataToSend?.toByteArray())
                Tasks.await(sendMessage)
            }
        }
    }
}