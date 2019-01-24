package com.example.rxjavademo1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Disposable disposable;

    //CompositeDisposable: Can maintain list of subscriptions in a pool and can dispose them all at once.
    //Usually we call compositeDisposable.clear() in onDestroy() method, but you can call anywhere in the code.
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<String> dogsObservable = getDogsObservable();

        Observer<String> dogsObserver = getDogsObserver();

        DisposableObserver<String> disposableDogsObserver = getAllCapsDogsObserver();

        // observer subscribing to observable
        // filter result
        dogsObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toLowerCase().startsWith("l");
                    }
                })
                .subscribe(dogsObserver);


        //CompositeDisposable: Can maintain list of subscriptions in a pool and can dispose them all at once.
        //Usually we call compositeDisposable.clear() in onDestroy() method, but you can call anywhere in the code.
        compositeDisposable.add(dogsObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toLowerCase().startsWith("l");
                    }
                }).map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s.toUpperCase();
                    }
                })
                .subscribeWith(disposableDogsObserver));


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

    private DisposableObserver<String> getAllCapsDogsObserver() {
        return new DisposableObserver<String>() {
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

    private Observable<String> getDogsObservable() {
        return Observable.fromArray("Lab Retriever", "Siberian Huskey", "Golden Retriever",
                "German Shepherd", "Tibetan Mastiff", "Labrador", "Boxer", "Alaskan Malamute", "Longdog"
                , "Lakeland Terrier");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // don't send events once the activity is destroyed
        if (!disposable.isDisposed())
            disposable.dispose();

        //CompositeDisposable: Can maintain list of subscriptions in a pool and can dispose them all at once.
        //Usually we call compositeDisposable.clear() in onDestroy() method, but you can call anywhere in the code.
        compositeDisposable.clear();
    }
}
