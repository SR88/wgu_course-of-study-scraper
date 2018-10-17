package entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created By sethsneddon on Aug, 17 2018.
 */
@Entity
@Table(name = "COURSES")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "COS_TYPE")
    private String cosType;
    @Column(name = "PAMS_VERSION")
    private int pamsVersion;
    @Column(name = "COS_NAME")
    private String cosName;
    @Column(name = "COS_VERSION")
    private int cosVersion;
    @Column(name = "COS_VERSION_ID")
    private String versionId;
    @Column(name = "URL")
    private String url;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED")
    private Date syncDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    public Set<Link> links = new HashSet<>();

    public Course() {
    }

    public Course(String cosType, int pamsVersion, String cosName, int cosVersion, String versionId, String url) {
        Date date = new Date();

        this.cosType = cosType;
        this.pamsVersion = pamsVersion;
        this.cosName = cosName;
        this.cosVersion = cosVersion;
        this.versionId = versionId;
        this.url = url;
        this.syncDate = date;
    }

    public Course(String cosType, int pamsVersion, String cosName, int cosVersion, String versionId, String url, Set<Link> links) {
        Date date = new Date();

        this.cosType = cosType;
        this.pamsVersion = pamsVersion;
        this.cosName = cosName;
        this.cosVersion = cosVersion;
        this.versionId = versionId;
        this.url = url;
        this.syncDate = date;
        this.links = links;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", cosType='" + cosType + '\'' +
                ", pamsVersion=" + pamsVersion +
                ", cosName='" + cosName + '\'' +
                ", cosVersion=" + cosVersion +
                ", versionId='" + versionId + '\'' +
                ", url='" + url + '\'' +
                ", syncDate='" + syncDate + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCosType() {
        return cosType;
    }

    public void setCosType(String cosType) {
        this.cosType = cosType;
    }

    public int getPamsVersion() {
        return pamsVersion;
    }

    public void setPamsVersion(int pamsVersion) {
        this.pamsVersion = pamsVersion;
    }

    public String getCosName() {
        return cosName;
    }

    public void setCosName(String cosName) {
        this.cosName = cosName;
    }

    public int getCosVersion() {
        return cosVersion;
    }

    public void setCosVersion(int cosVersion) {
        this.cosVersion = cosVersion;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getSyncDate() {
        return syncDate;
    }

    public void setSyncDate(Timestamp syncDate) {
        this.syncDate = syncDate;
    }

    public Set<Link> getLinks() {
        return links;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }
}
