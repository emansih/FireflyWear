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

import android.content.SharedPreferences
import androidx.core.content.edit

class SimpleData(private val sharedPref: SharedPreferences) {

    var networthValue
        get() = sharedPref.getString("networthValue", "")
        set(value) = sharedPref.edit { putString("networthValue", value) }

    var leftToSpend
        get() = sharedPref.getString("leftToSpend", "")
        set(value) = sharedPref.edit { putString("leftToSpend", value) }

    var balance
        get() = sharedPref.getString("balance", "")
        set(value) = sharedPref.edit { putString("balance", value) }

    var earned
        get() = sharedPref.getString("earned", "")
        set(value) = sharedPref.edit { putString("earned", value) }

    var spent
        get() = sharedPref.getString("spent", "")
        set(value) = sharedPref.edit { putString("spent", value) }

    var unPaidBills
        get() = sharedPref.getString("unpaidBills", "")
        set(value) = sharedPref.edit { putString("unpaidBills", value) }

    var paidBills
        get() = sharedPref.getString("paidBills", "")
        set(value) = sharedPref.edit { putString("paidBills", value) }

    var leftToSpendPerDay
        get() = sharedPref.getString("leftToSpendPerDay", "")
        set(value) = sharedPref.edit { putString("leftToSpendPerDay", value) }
}