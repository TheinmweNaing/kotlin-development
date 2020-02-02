package com.example.busticketbooking.ui.booking

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.busticketbooking.R
import kotlinx.android.synthetic.main.fragment_route_choice.*
import org.joda.time.LocalDate

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

        val station = resources.getStringArray(R.array.bus_station)
        val adapter =
            ArrayAdapter<String>(view.context, android.R.layout.simple_list_item_1, station)

        auto_edit_from.setAdapter(adapter)
        auto_edit_to.setAdapter(adapter)

        val today = LocalDate.now()
        ed_date.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                DatePickerDialog(
                    view.context,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val date = "$year/${month + 1}/$dayOfMonth"
                        ed_date.setText(date)
                    }, today.year, today.monthOfYear - 1, today.dayOfMonth).show()
                v.requestFocus()
            }
            return@setOnTouchListener true
        }

        val seats = Array(10) { (it + 1).toString() }
        ed_seats.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                AlertDialog.Builder(view.context)
                    .setItems(seats) { di, i ->
                        ed_seats.setText((i + 1).toString())
                        di.dismiss() }.show()
                v.requestFocus()
            }
            return@setOnTouchListener true
        }
    }
}