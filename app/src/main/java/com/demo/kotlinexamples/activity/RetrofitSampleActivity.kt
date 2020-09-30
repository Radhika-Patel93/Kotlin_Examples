package com.demo.kotlinexamples.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.kotlinexamples.R
import com.demo.kotlinexamples.model.TimeModel
import com.demo.kotlinexamples.server.ApiBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_retrofit_sample.*

class RetrofitSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit_sample)

        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            ApiBuilder.buildService().getTime()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({response -> onResponse(response)}, {t -> onFailure(t) }))

    }

    private fun onResponse(timeModel: TimeModel?) {
        txtTime.text = timeModel?.datetime
    }

    private fun onFailure(error: Throwable) {
        txtTime.text = error.toString()
    }
}