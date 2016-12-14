package blue.aodev.memory;

/**
 * From https://github.com/googlesamples/android-architecture/blob/todo-mvp
 */

public interface BaseView<T> {

    void setPresenter(T presenter);

}
