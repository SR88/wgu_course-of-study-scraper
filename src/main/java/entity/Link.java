package entity;

import javax.persistence.*;

/**
 * Created By sethsneddon on Aug, 22 2018.
 */
@Entity
@Table(name = "LINKS")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "LINK_URL")
    private String url;
    @Column(name = "LINK_CONTEXT")
    private String linkText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COURSE_ID", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LRPS_ENDPOINT_ID")
    private EndPoint endPoint;

    public Link() {
    }

    public Link(Course course) {
        this.course = course;
    }

    public Link(String url, String linkText, Course course) {
        this.url = url;
        this.linkText = linkText;
        this.course = course;
    }

    @Override
    public String toString() {
        return "Link{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", linkText='" + linkText + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
