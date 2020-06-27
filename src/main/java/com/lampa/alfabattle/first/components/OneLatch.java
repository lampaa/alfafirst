package com.lampa.alfabattle.first.components;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class OneLatch<T> {
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private T object;

    public void await(int seconds) throws InterruptedException {
        countDownLatch.await(seconds, TimeUnit.SECONDS);
    }

    public void compete(T result) {
        this.object = result;
        countDownLatch.countDown();
    }

    public T getResult() {
        return this.object;
    }
}
