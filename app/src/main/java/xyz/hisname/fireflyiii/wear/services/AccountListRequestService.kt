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
import com.google.android.gms.wearable.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.hisname.fireflyiii.common.Constants
import xyz.hisname.fireflyiii.common.data.AccountRepository
import xyz.hisname.fireflyiii.common.data.AppDatabase
import xyz.hisname.fireflyiii.wear.utils.DeviceCheck

class AccountListRequestService: WearableListenerService() {

    private val URI = "content://xyz.hisname.fireflyiii.provider/accountList".toUri()

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        if(messageEvent.path != null && DeviceCheck.isApproved(applicationContext)){
            CoroutineScope(Dispatchers.IO).launch {
                val cursor = applicationContext.contentResolver.query(URI,
                        null, null, null, null)
                val accountRepository = AccountRepository(AppDatabase.getInstance(applicationContext).accountDao())
                accountRepository.insert(cursor)
                val dataMap = PutDataMapRequest.create("/accountsList")
                dataMap.dataMap.putAsset("accountsList",
                        Asset.createFromUri(applicationContext.getDatabasePath(Constants.DB_NAME).toUri()))
                val request = dataMap.asPutDataRequest()
                request.setUrgent()
                Wearable.getDataClient(applicationContext).putDataItem(request)
            }
        }
    }
}