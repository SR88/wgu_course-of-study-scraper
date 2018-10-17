package entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created By sethsneddon on Sep, 24 2018.
 */
@Entity
@Table(name = "LRPS_ENDPOINTS")
public class EndPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "PAGE_TITLE")
    private String pageTitle;
    @Column(name = "PROVIDER_URL")
    private String providerUrl;
    @Column(name = "FULL_URL")
    private String directUrl;
    @Column(name = "LRPS")
    private String lrps;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED")
    private Date updated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "endPoint")
    public Set<Link> links = new HashSet<>();

    public EndPoint() {
    }

    public EndPoint(String providerUrl, String pageTitle, String directUrl, Set<Link> links, String lrps) {
        Date date = new Date();

        this.pageTitle = pageTitle;
        this.providerUrl = providerUrl;
        this.directUrl = directUrl;
        this.updated = date;
        this.links = links;
        this.updated = date;
        this.lrps = lrps;
    }

    @Override
    public String toString() {
        return "EndPoint{" +
                "id=" + id +
                ", pageTitle='" + pageTitle + '\'' +
                ", providerUrl='" + providerUrl + '\'' +
                ", directUrl='" + directUrl + '\'' +
                ", lrps='" + lrps + '\'' +
                ", updated=" + updated +
                ", links=" + links +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndPoint endPoint = (EndPoint) o;
        return Objects.equals(id, endPoint.id) &&
                Objects.equals(pageTitle, endPoint.pageTitle) &&
                Objects.equals(providerUrl, endPoint.providerUrl) &&
                Objects.equals(directUrl, endPoint.directUrl) &&
                Objects.equals(lrps, endPoint.lrps) &&
                Objects.equals(updated, endPoint.updated) &&
                Objects.equals(links, endPoint.links);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, pageTitle, providerUrl, directUrl, lrps, updated, links);
    }

    public String getLrps() {
        return lrps;
    }

    public void setLrps(String lrps) {
        this.lrps = lrps;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getProviderUrl() {
        return providerUrl;
    }

    public void setProviderUrl(String providerUrl) {
        this.providerUrl = providerUrl;
    }

    public String getDirectUrl() {
        return directUrl;
    }

    public void setDirectUrl(String directUrl) {
        this.directUrl = directUrl;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Set<Link> getLinks() {
        return links;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }
}