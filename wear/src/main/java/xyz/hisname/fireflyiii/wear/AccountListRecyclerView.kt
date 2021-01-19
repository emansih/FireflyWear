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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import xyz.hisname.fireflyiii.common.model.AccountData

class AccountListRecyclerView(private val items: List<AccountData>): RecyclerView.Adapter<AccountListRecyclerView.AccountHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.account_layout,parent,false)
        return AccountHolder(view)
    }

    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        val accountData = items[position]
        holder.accountName.text = accountData.accountName
        holder.accountAmount.text = accountData.accountSymbol + accountData.accountAmount
    }

    override fun getItemCount() = items.size


    inner class AccountHolder(view: View): RecyclerView.ViewHolder(view){
        val accountName: TextView = view.findViewById(R.id.accountName)
        val accountAmount: TextView = view.findViewById(R.id.accountAmount)
    }

}