package at.ac.tuwien.inso.sepm.ticketline.rest.util;

import java.util.List;

public class PaginationWrapper<T> {

    private List<T> entities;
    private int totalPages;

    public PaginationWrapper(List<T> entities, int totalPages){
        this.entities = entities;
        this.totalPages = totalPages;
    }

    public PaginationWrapper(){

    }

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
