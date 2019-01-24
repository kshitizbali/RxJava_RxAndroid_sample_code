package com.example.rxjavademo1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.rxjavademo1.model.Task;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CustomDataTypeOperatorsClass extends AppCompatActivity {

    private static final String TAG = CustomDataTypeOperatorsClass.class.getSimpleName();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable.add(getTasksObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Task, Task>() {
                    @Override
                    public Task apply(Task task) throws Exception {
                        task.setTaskName(task.getTaskName().toUpperCase());
                        return task;
                    }
                }).subscribeWith(getTasksObserver()));

    }

    private DisposableObserver<Task> getTasksObserver() {
        return new DisposableObserver<Task>() {
            @Override
            public void onNext(Task task) {
                Log.i(TAG, "Task Name " + task.getTaskName());
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "All the tasks emitted");
            }
        };
    }

    private Observable<Task> getTasksObservable() {
        final List<Task> tasksList = prepareTasks();

        return Observable.create(new ObservableOnSubscribe<Task>() {
            @Override
            public void subscribe(ObservableEmitter<Task> emitter) throws Exception {
                for (Task task : tasksList) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(task);
                    }
                }
                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        });
    }

    private List<Task> prepareTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1, "Search RxJava!"));
        tasks.add(new Task(2, "Find RxJava!"));
        tasks.add(new Task(3, "Read RxJava!"));
        tasks.add(new Task(4, "Understand RxJava!"));
        tasks.add(new Task(5, "Implement RxJava!"));
        return tasks;
    }
}
