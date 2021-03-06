package com.example.zhack.share_ie

import android.content.DialogInterface
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import com.example.zhack.share_ie.API.ApiClient
import com.example.zhack.share_ie.berita.control_berita
import com.example.zhack.share_ie.model.status
import com.example.zhack.share_ie.model.status_user_detail
import com.example.zhack.share_ie.model.user_detail
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.berita.*
import org.jetbrains.anko.appcompat.v7.alertDialogLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var session:SessionManagment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        session = SessionManagment(applicationContext)
        session!!.checkLogin()


        var apiclient = ApiClient.create()
        var getUserDetail = apiclient.getUserDetail(session!!.UserId(),session!!.UserToken(), session!!.UserId().toInt())
        getUserDetail.enqueue(object : Callback<status_user_detail> {
            override fun onFailure(call: Call<status_user_detail>, t: Throwable) {

            }

            override fun onResponse(call: Call<status_user_detail>, response: Response<status_user_detail>) {
                if (response.isSuccessful) {
                    var res = response.body()!!.detail
                    res.map {
                        session!!.createUserDetail(it.username,it.name,it.foto)
                        Log.d("cek_error",it.foto)
                    }
                }else{
                    Log.d("cek_error",response.body()!!.message)
                }
            }

        })

        Log.d("cek_error",session!!.UserName()+session!!.UserFoto())

        var retrofit = apiclient.getDetail(session!!.UserId(),session!!.UserToken())
        retrofit.enqueue(object : Callback<status> {
            override fun onFailure(call: Call<status>, t: Throwable) {

            }

            override fun onResponse(call: Call<status>, response: Response<status>) {
                if (response.isSuccessful) {
                    var response = response.body()!!
                    if (response!!.status!!.equals(200)){
                            initFragment(control_berita())
                            berita.setColorFilter(R.color.colorPrimaryDark)
                            berita.setOnClickListener {
                                initFragment(control_berita())
                                berita.setColorFilter(R.color.colorPrimaryDark)
                                seminar.clearColorFilter()
                                jadwal.clearColorFilter()
                                laporan.clearColorFilter()
                            }
                    }
                }else{
                    session!!.logoutUser()
                }
            }

        })


    }

    private fun initFragment(classFragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, classFragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Keluar")
        dialog.setMessage("Yakin Ingin Keluar ?")
        dialog.setPositiveButton("YA"){dialog, which->
            val intent = Intent(applicationContext,Login::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("EXIT",true)
            startActivity(intent)
            finishAffinity()
        }
        dialog.setNegativeButton("TIDAK"){dialog,which->
            dialog.cancel()
        }
        dialog.show()
    }

}
