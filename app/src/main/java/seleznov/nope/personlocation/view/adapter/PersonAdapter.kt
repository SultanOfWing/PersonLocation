package seleznov.nope.personlocation.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import seleznov.nope.personlocation.databinding.ItemPersonBinding
import seleznov.nope.personlocation.model.Person

/**
 * Created by User on 10.07.2018.
 */
class PersonAdapter(private var list: ArrayList<Person>,  private var listener: OnItemClickListener)
    : RecyclerView.Adapter<PersonAdapter.PersonHolder>() {

    override fun onBindViewHolder(holder: PersonHolder, position: Int)
        = holder.bind(list[position], listener)

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPersonBinding.inflate(layoutInflater, parent, false)
        return PersonHolder(binding)
    }

    fun setList(arrayList: ArrayList<Person>) {
        list = arrayList
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class PersonHolder(private var binding: ItemPersonBinding) :
            RecyclerView.ViewHolder(binding.root){

        fun bind(person: Person, listener: OnItemClickListener?) {
            binding.person = person
            if (listener != null) {
                binding.root.setOnClickListener({ _ -> listener.onItemClick(layoutPosition) })
            }

            binding.executePendingBindings()
        }

    }
}