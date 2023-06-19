package com.example.githubuserapp.followitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.adapter.ListUserAdapter
import com.example.githubuserapp.databinding.FragmentFollowItemBinding
import com.example.githubuserapp.main.ItemsItem

class FollowItemFragment : Fragment() {

    private val followItemViewModel by viewModels<FollowItemViewModel>()
    var listUsers = ArrayList<ItemsItem>()
    private lateinit var binding: FragmentFollowItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowItemBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followItemViewModel.userList.observe(viewLifecycleOwner) { follows ->
            setFollowData(follows)
        }

        followItemViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        followItemViewModel.isConnectionFailed.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { isConnectionFailed ->
                showConnectionFailedToast(isConnectionFailed)
            }
        }

        if (arguments != null) {
            val position = arguments?.getString(ARG_POSITION)
            val username = arguments?.getString(ARG_USERNAME)
            if (username != null && position != null) {
                followItemViewModel.findFollowItem(username, position)
            }
        }
    }

    private fun setFollowData(users: List<ItemsItem>) {
        this.listUsers = users as ArrayList<ItemsItem>
        if (this.listUsers.size == 0) {
            binding.tvStatus.text = getString(R.string.none)
        }
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
        val listUserAdapter = ListUserAdapter(listUsers)
        binding.rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showSelectedUser(data.login)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSelectedUser(username: String?) {
        Toast.makeText(requireContext(), "Kamu memilih $username", Toast.LENGTH_SHORT).show()
    }

    private fun showConnectionFailedToast(isConnectionFailed: Boolean) {
        if (isConnectionFailed) {
            Toast.makeText(
                requireContext(),
                "Pengambilan data gagal. Harap periksa koneksi pada perangkatmu!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        const val ARG_POSITION = "extra_position"
        const val ARG_USERNAME = "extra_username"
    }
}