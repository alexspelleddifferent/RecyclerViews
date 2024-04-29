package com.example.recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

interface OnListItemClickedListener {
    fun onMapRequestButtonClicked(place: Place)
    fun onStarredStatusChanged(place: Place, isStarred: Boolean)
}

class PlaceRecyclerAdapter(var places: List<Place>,
                           private val onListItemClickedListener: OnListItemClickedListener):
    RecyclerView.Adapter<PlaceRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        fun bind(place: Place) {
            val placeNameText: TextView = view.findViewById(R.id.place_name)
            placeNameText.text = place.name
            val placeReasonText: TextView = view.findViewById(R.id.place_reason)
            placeReasonText.text = place.reason
            val mapIcon: ImageView = view.findViewById(R.id.map_icon)
            mapIcon.setOnClickListener {
                onListItemClickedListener.onMapRequestButtonClicked(place)
            }

            view.findViewById<CheckBox>(R.id.star_check).apply {
                setOnCheckedChangeListener(null)
                isChecked = place.starred
                setOnCheckedChangeListener { checkbox, isChecked ->
                    onListItemClickedListener.onStarredStatusChanged(place, isChecked)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place)
    }

    override fun getItemCount(): Int {
        return places.size
    }
}
