package apiEngine.model.requests;

import java.util.List;

public class AddBooksRequest {
    private String userId;
    private List<ISBN> collectionOfIsbns;

    public AddBooksRequest() {
        // No-arg constructor needed for Jackson
    }

    public AddBooksRequest(String userId, List<ISBN> collectionOfIsbns) {
        this.userId = userId;
        this.collectionOfIsbns = collectionOfIsbns;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ISBN> getCollectionOfIsbns() {
        return collectionOfIsbns;
    }

    public void setCollectionOfIsbns(List<ISBN> collectionOfIsbns) {
        this.collectionOfIsbns = collectionOfIsbns;
    }
}
