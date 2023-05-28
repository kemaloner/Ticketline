package at.ac.tuwien.inso.sepm.ticketline.rest.event;

public class Top10EventDTOImpl implements Top10EventDTO {

    private Long id;
    private String title;
    private Integer count;

    public Top10EventDTOImpl(){}

    public Top10EventDTOImpl(Top10EventDTO top10EventDTO){
        this(top10EventDTO.getId(), top10EventDTO.getTitle(), top10EventDTO.getCount());
    }

    public Top10EventDTOImpl(Long id, String title, Integer count) {
        this.id = id;
        this.title = title;
        this.count = count;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setEventID(Long eventID) {
        this.id = eventID;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Top10EventDTOImpl{" +
            "eventID=" + id +
            ", title='" + title + '\'' +
            ", count=" + count +
            '}';
    }
}
