package model;

public class DailyDetail {
    private String date;
    private String managerName;

    public DailyDetail (String date, String managerName) {
        this.date = date;
        this.managerName = managerName;
    }

    public String getDate() {
        return date;
    }
    public String getManagerName () {
        return managerName;
    }
}
