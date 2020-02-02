package com.urgentx.randomusers.view

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.urgentx.randomusers.R
import com.urgentx.randomusers.model.User
import kotlinx.android.synthetic.main.dialog_detail.view.*

class DetailDialogFragment : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_detail, null)

        val user = arguments?.getParcelable<User>(PARAM_USER)!!

        Glide.with(activity!!).load(user.picture.highestRes() ?: "").into(view.detail_image)
        view.detail_title.text = user.name.title
        view.detail_first_name.text = user.name.first
        view.detail_last_name.text = user.name.last
        view.detail_email.text = user.email

        builder.setView(view)
        builder.setPositiveButton("Done") { _, _ ->
            dismiss()
        }

        return builder.create()
    }

    companion object {
        private const val PARAM_USER = "asfaf9ajaf9asf91j912f12f"

        @JvmStatic
        fun newInstance(user: User): DetailDialogFragment {
            val fragment = DetailDialogFragment()
            val bundle = Bundle()
            bundle.putParcelable(PARAM_USER, user)
            fragment.arguments = bundle
            return fragment
        }
    }
}