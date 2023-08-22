package tr.com.cemalaydin.dashboard.enums;


public enum Status  {
    DELETED(-1,"DELETED"),
    PASSIVE(0,"PASSIVE"),
    ACTIVE(1,"ACTIVE");
    private final Integer id;
    private final String name;
    Status(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
