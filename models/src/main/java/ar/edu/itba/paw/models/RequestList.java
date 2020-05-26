package ar.edu.itba.paw.models;

import java.util.AbstractList;
import java.util.List;

public class RequestList extends AbstractList<Request> {
    private final Request[] requestList;
    private final String maxPage;

    public RequestList(List<Request> requestArray, String maxPage) {
        super();
        this.maxPage = maxPage;
        requestList = requestArray.toArray(new Request[0]);
    }

    public Request get(int index) {
        return requestList[index];
    }

    public Request set(int index, Request request) {
        Request oldValue = requestList[index];
        requestList[index] = request;
        return oldValue;
    }

    public int size() {
        return requestList.length;
    }

    public String getMaxPage() {
        return maxPage;
    }
}
