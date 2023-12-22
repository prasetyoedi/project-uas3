package com.example.project_uas3.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_uas3.R
import com.example.project_uas3.database.model.OrderData
import com.example.project_uas3.adapter.OrderAdapter
import com.google.firebase.database.*

class HistoryOrderFragment : Fragment() {

    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_order)
        orderAdapter = OrderAdapter(ArrayList())

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = orderAdapter

        val historyOrderReference = FirebaseDatabase.getInstance().getReference("HistoryOrder")
        historyOrderReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val historyOrderList = ArrayList<OrderData>()
                for (snapshot in dataSnapshot.children) {
                    val historyOrderData = snapshot.getValue(OrderData::class.java)
                    if (historyOrderData != null) {
                        historyOrderList.add(historyOrderData)
                    }
                }
                orderAdapter.addOrders(historyOrderList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error getting data", databaseError.toException())
            }
        })
    }

    companion object {
        const val TAG = "HistoryOrderFragment"
    }
}
