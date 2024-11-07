package com.example.app_datn_haven_inn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB: ViewBinding>: Fragment() {

    protected lateinit var viewBinding: VB

    abstract fun inflateViewBinding(): VB


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = inflateViewBinding()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        bindView()
    }

    open fun initView(){
    }

    open fun bindView(){}

//    override fun onAttach(mcontext: Context) {
//        super.onAttach(mcontext)
//        this.context = mcontext
//        this.activity = requireActivity()
//    }


}