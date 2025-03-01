package io.github.bindglam.faker.fake;

import java.util.Collection;

public interface FakeServer<T> {
    Collection<T> getAll();

    void add(T var01);

    void remove(T var01);

    void dispose();
}
