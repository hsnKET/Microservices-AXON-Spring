package me.ketlas.comptecqrses.commonapi.queries;

public class GetAccountByIdQuery {
    private String id;

    public GetAccountByIdQuery(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
