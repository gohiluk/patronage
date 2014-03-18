package pl.patronage.rest.RestClient.httpengine;

import java.util.List;

import pl.patronage.rest.RestClient.model.IModel;

/**
 * Created by gohilukk on 17.03.14.
 */
public interface IHttpEngine<T> {

    public List<T> getList();

    public T get(int id);

    public boolean create(IModel t);

    public void update(IModel t, int id);

    public void remove(int id);
}
