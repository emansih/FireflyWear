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

package xyz.hisname.fireflyiii.wear.utils

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.os.Build
import xyz.hisname.fireflyiii.wear.BuildConfig

// This is a simple checker and it does not deter anyone from using the app if they are determined.
object DeviceCheck {

    private const val MAIN_APP_ID = "xyz.hisname.fireflyiii"

    fun isApproved(context: Context): Boolean{
        if(BuildConfig.DEBUG){
            return true
        } else {
            if(checkAccount(context)){
                if(isFromGooglePlay(context)){
                    return true
                } else {
                    return isFromHuaWeiStore(context)
                }
            } else {
                return false
            }
        }
    }


    private fun isFromGooglePlay(context: Context): Boolean{
        val googlePlayInstaller = "com.android.vending"
        return if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
            context.packageManager
                .getInstallSourceInfo(MAIN_APP_ID).installingPackageName?.contentEquals(googlePlayInstaller) ?: false
        } else {
            context.packageManager
                .getInstallerPackageName(MAIN_APP_ID)?.contentEquals(googlePlayInstaller) ?: false
        }
    }

    private fun isFromHuaWeiStore(context: Context): Boolean{
        val huaweiMarket = "com.huawei.appmarket"
        return if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
            context.packageManager
                .getInstallSourceInfo(MAIN_APP_ID).installingPackageName?.contentEquals(huaweiMarket) ?: false
        } else {
            context.packageManager
                .getInstallerPackageName(MAIN_APP_ID)
            context.packageManager
                .getInstallerPackageName(MAIN_APP_ID)?.contentEquals(huaweiMarket) ?: false
        }
    }

    private fun checkAccount(context: Context): Boolean{
        try {
            val account = Account("Firefly III Mobile", "OAUTH")
            val accountManager = AccountManager.get(context)
            return accountManager.getUserData(account, "USER_EMAIL").isNotBlank()
        } catch (exception: Exception){
            return false
        }
    }
}