package com.project.havewemet.model;

import java.util.List;

public class ExtApiSearchResult {
    List<External> Search;

    public void setSearch(List<External> search) {
        Search = search;
    }

    public List<External> getSearch() {
        return Search;
    }
}
