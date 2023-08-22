package tr.com.cemalaydin.dashboard.enums;

public enum AuthProvider {
    LOCAL(0,"LOCAL"),
    GOOGLE(1,"GOOGLE");
    private final Integer id;
    private final String name;
    AuthProvider(Integer id, String name) {
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
