package my.project.internetprovider.db.entity;

import java.util.List;

/**
 * Page entity.
 *
 */
public class Page<T> {
    private List<T> elements;
    private int totalElements;
    private int totalPages;
    private int pageNumber;

    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return pageNumber;
    }

    public void setCurrentPage(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
