package pl.patronage.rest.RestClient.httpengine;

import java.util.List;

/**
 * Created by gohilukk on 17.03.14.
 */
public interface IHttpEngine<T> {

    public List<T> getList();

    public T get(int id);

    public boolean create(T t);

    public void update(T t);

    public void remove(int id);
}
