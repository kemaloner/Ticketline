package at.ac.tuwien.inso.sepm.ticketline.server.entity;

public enum EventCategory {
    CONCERT, CINEMA, FESTIVAL, THEATRE,
    MUSICAL, OPERA, INVALID;

    public static String fromString(String category){
        return category.toUpperCase();
    }
}
