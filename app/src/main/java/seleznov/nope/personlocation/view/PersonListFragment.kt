package seleznov.nope.personlocation.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import seleznov.nope.personlocation.R
import seleznov.nope.personlocation.databinding.FragmentPersonListBinding
import seleznov.nope.personlocation.helper.EventBus
import seleznov.nope.personlocation.model.Person
import seleznov.nope.personlocation.view.adapter.PersonAdapter
import seleznov.nope.personlocation.viewmodel.PersonListViewModel

/**
 * Created by User on 09.07.2018.
 */
class PersonListFragment : Fragment(), PersonAdapter.OnItemClickListener {

    lateinit var binding: FragmentPersonListBinding
    private val personAdapter = PersonAdapter(arrayListOf(), this)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_person_list,container , false)

        val viewModel = ViewModelProviders.of(this).get(PersonListViewModel::class.java)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = personAdapter
        viewModel.list.observe(this,
                Observer<List<Person>> { it?.let{ personAdapter.setList(it as ArrayList<Person>)} })

        return binding.root;
    }

    override fun onItemClick(position: Int) {
        val person = binding.viewModel?.list?.value?.get(position)
        EventBus.instance.publish(person!!)
    }

}