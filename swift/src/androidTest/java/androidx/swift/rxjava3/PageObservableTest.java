package androidx.swift.rxjava3;

import android.accounts.Account;
import android.util.Log;

import androidx.swift.rxjava3.paging.PageObservable;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * @author RAE
 * @date 2022/03/15
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class PageObservableTest {

    @Test
    public void testPage() {
        Log.d("Rae", "运行");
        PageObservable.create(page -> {
            List<Account> data = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                data.add(new Account("dd" + i, "aa"));
            }
            Log.i("Rae", "分页方法");
            return data;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(data -> {
            for (Account item : data) {
                Log.d("rae", "结果：" + item.name);
            }
        });
    }
}
