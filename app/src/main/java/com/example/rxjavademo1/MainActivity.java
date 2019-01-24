package com.example.rxjavademo1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Disposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<String> dogsObservable = getDogsObservable();

        Observer<String> dogsObserver = getDogsObserver();

        // observer subscribing to observable
        
        dogsObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dogsObserver);
    }

    private Observer<String> getDogsObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "Name " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "All the items emitted");
            }
        };
    }

    private Observable<String> getDogsObservable(){
        return Observable.just("Lab Retriever", "Siberian Huskey", "Golden Retriever",
                "German Shepherd", "Tibetan Mastiff", "Labrador");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // don't send events once the activity is destroyed

        disposable.dispose();
    }
}
