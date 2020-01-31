package com.example.busticketbooking.ui.booking

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.busticketbooking.R
import kotlinx.android.synthetic.main.fragment_route_choice.*

class RouteChoiceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_route_choice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter<String>(view.context, android.R.layout.simple_dropdown_item_1line)
        val station = resources.getStringArray(R.array.bus_station)
        adapter.addAll(station.toList())

        auto_edit_from.setAdapter(adapter)
        auto_edit_from.setDropDownBackgroundResource(R.color.colorAccent)
        auto_edit_to.setAdapter(adapter)
        auto_edit_to.setDropDownBackgroundResource(R.color.colorAccent)

        ed_date.setOnClickListener {
            DatePickerDialog(view.context,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val date = "$year/$month/$dayOfMonth"
                    ed_date.setText(date)
                }, 0, 0, 0
            ).show()
        }

        val seats = Array(10) { (it + 1).toString() }
        ed_seats.setOnClickListener {
            AlertDialog.Builder(view.context)
                .setItems(seats) { di, i ->
                    ed_seats.setText((i + 1).toString())
                    di.dismiss()
                }
                .show()
        }
    }
}