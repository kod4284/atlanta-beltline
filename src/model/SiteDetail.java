package model;

public class SiteDetail {
    private String siteName;
    private String openEveryday;
    private String address;

    public SiteDetail(String siteName) {
        this.siteName = siteName;
        this.openEveryday = null;
        this.address = null;
    }

    public SiteDetail(String siteName, String openEveryday,
                      String address) {
        this.siteName = siteName;
        this.openEveryday = openEveryday;
        this.address = address;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getOpenEveryday() {
        return openEveryday;
    }

    public void setOpenEveryday(String openEveryday) {
        this.openEveryday = openEveryday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
