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

import android.content.ComponentName
import android.support.wearable.complications.ProviderUpdateRequester
import androidx.preference.PreferenceManager
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

class SimpleDataListenerService: WearableListenerService() {

    private var complication: ComponentName? = null

    override fun onMessageReceived(messageEvent: MessageEvent) {
        val dataReceived = String(messageEvent.data)
        val dataStore = PreferenceManager.getDefaultSharedPreferences(this)
        println("message path: " + messageEvent.path + "  data: " + dataReceived)
        when(messageEvent.path){
            "balance" -> {
                dataStore.edit().putString("balance", dataReceived).apply()
                complication = ComponentName(this, BalanceComplicationProvider::class.java)
            }
            "billsToPay" -> {
                dataStore.edit().putString("billsToPay", dataReceived).apply()
                complication = ComponentName(this, BillsToPayComplicationProvider::class.java)
            }
            "paid" -> {
                dataStore.edit().putString("paid", dataReceived).apply()
                complication = ComponentName(this, PaidComplicationProvider::class.java)
            }
            "leftToSpend" -> {
                dataStore.edit().putString("leftToSpend", dataReceived).apply()
                complication = ComponentName(this, LeftToSpendComplicationProvider::class.java)
            }
            "perDay" -> {
                dataStore.edit().putString("perDay", dataReceived).apply()
                complication = ComponentName(this, BalanceComplicationProvider::class.java)
            }
            "netWorth" -> {
                dataStore.edit().putString("netWorth", dataReceived).apply()
                complication = ComponentName(this, BalanceComplicationProvider::class.java)
            }
        }
        if(complication !== null){
            val providerUpdateRequester = ProviderUpdateRequester(this, complication)
            providerUpdateRequester.requestUpdateAll()
        }
    }

}