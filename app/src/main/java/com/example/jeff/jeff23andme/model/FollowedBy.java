package com.example.jeff.jeff23andme.model;

import java.util.List;

public class FollowedBy {

    private Pagination pagination;
    private List<User> data;

    public Pagination getPagination() {
        return pagination;
    }

    public List<User> getUsers() {
        return data;
    }

}
