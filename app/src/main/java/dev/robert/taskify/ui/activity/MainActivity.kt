package dev.robert.taskify.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.taskify.R
import dev.robert.taskify.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private  lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_dark_mode -> {
                Snackbar.make(
                    binding.root,
                    "Dark mode is not implemented yet",
                    Snackbar.LENGTH_LONG
                ).show()
                Log.d(TAG, "onOptionsItemSelected: Dark mode is not implemented yet")
                return true
            }
            R.id.action_about ->{
                Snackbar.make(
                    binding.root,
                    "About is not implemented yet",
                    Snackbar.LENGTH_LONG
                ).show()
                return true
            }

            else ->  super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
}