package seleznov.nope.personlocation.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import seleznov.nope.personlocation.R
import seleznov.nope.personlocation.databinding.FragmentPersonListBinding

/**
 * Created by User on 09.07.2018.
 */
class PersonListFragment : Fragment() {
    lateinit var binding: FragmentPersonListBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_person_list,container , false)

        return binding.root;
    }
}