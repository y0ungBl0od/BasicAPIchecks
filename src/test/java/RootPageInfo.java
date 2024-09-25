public class RootPageInfo {
    private Integer page;
    private Integer per_page;
    private Integer total;
    private Integer total_pages;

    public RootPageInfo(Integer page, Integer per_page, Integer total, Integer total_pages) {
        this.page = page;
        this.per_page = per_page;
        this.total = total;
        this.total_pages = total_pages;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPer_page() {
        return per_page;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }
}
