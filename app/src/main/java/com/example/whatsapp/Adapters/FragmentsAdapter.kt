package com.example.whatsapp.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.whatsapp.Fragments.CallsFragment
import com.example.whatsapp.Fragments.ChatsFragment
import com.example.whatsapp.Fragments.StatusFragment

public class FragmentsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {

        /*if(position==0){
            return ChatsFragment()
        }
        else if(position==1){
            return StatusFragment()
        }
        else{
            return CallsFragment()
        }*/

        when(position){
            0->return ChatsFragment()
            1->return StatusFragment()
            2->return CallsFragment()
            else-> return ChatsFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? =null

        if(position==0){
            title="CHATS"
        }
        else if(position==1){
            title="STATUS"
        }
        else if(position==2){
            title="CALLS"
        }
        return title
    }
}