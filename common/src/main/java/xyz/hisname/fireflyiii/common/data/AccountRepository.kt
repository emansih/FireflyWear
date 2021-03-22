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

package xyz.hisname.fireflyiii.common.data

import android.database.Cursor
import xyz.hisname.fireflyiii.common.model.AccountData

class AccountRepository(private val accountDao: AccountDao) {

    suspend fun insert(cursor: Cursor?){
        if (cursor != null && cursor.moveToFirst()) {
            accountDao.deleteAll()
            do {
                accountDao.insert(
                        AccountData(
                                cursor.getLong(cursor.getColumnIndex("accountId")),
                                cursor.getString(cursor.getColumnIndex("name")),
                                cursor.getString(cursor.getColumnIndex("current_balance")),
                                cursor.getString(cursor.getColumnIndex("currency_symbol"))
                        )
                )
            } while (cursor.moveToNext())
        }
        cursor?.close()
    }

    fun getData() = accountDao.getAll()
}