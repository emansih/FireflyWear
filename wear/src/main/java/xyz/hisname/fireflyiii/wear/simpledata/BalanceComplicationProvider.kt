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

package xyz.hisname.fireflyiii.wear.simpledata

import android.support.wearable.complications.ComplicationData
import android.support.wearable.complications.ComplicationManager
import android.support.wearable.complications.ComplicationProviderService
import android.support.wearable.complications.ComplicationText
import androidx.preference.PreferenceManager
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BalanceComplicationProvider: ComplicationProviderService() {

    override fun onComplicationActivated(complicationId: Int, type: Int, complicationManager: ComplicationManager) {
        super.onComplicationActivated(complicationId, type, complicationManager)
        getSimpleData()
        val balance = PreferenceManager.getDefaultSharedPreferences(this).getString("balance", "")
        val complicationData = ComplicationData.Builder(ComplicationData.TYPE_LONG_TEXT)
                .setLongText(ComplicationText.plainText(balance))
                .build()
        complicationManager.updateComplicationData(complicationId, complicationData)
    }

    override fun onComplicationUpdate(complicationId: Int, dataType: Int,
                                      complicationManager: ComplicationManager) {
        getSimpleData()
        val balance = PreferenceManager.getDefaultSharedPreferences(this).getString("balance", "")
        val complicationData = ComplicationData.Builder(ComplicationData.TYPE_LONG_TEXT)
                .setLongText(ComplicationText.plainText(balance))
                .build()
        complicationManager.updateComplicationData(complicationId, complicationData)
    }

    private fun getSimpleData(){
        CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }).launch {
            val nodeListTask = Wearable.getNodeClient(this@BalanceComplicationProvider).connectedNodes
            val nodes = Tasks.await(nodeListTask)
            val messageClient = Wearable.getMessageClient(this@BalanceComplicationProvider)
            val sendMessage = messageClient.sendMessage(nodes[0].id, "simpleData", "Balance".toByteArray())
            Tasks.await(sendMessage)
            sendMessage.addOnSuccessListener {
                println("balance success!")
            }
            sendMessage.addOnFailureListener {
                it.printStackTrace()
            }
        }
    }
}