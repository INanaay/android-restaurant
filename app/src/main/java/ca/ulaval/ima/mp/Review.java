package ca.ulaval.ima.mp;

public class Review {
    private String first_name;
    private String last_name;
    private String nb_stars;
    private String img;
    private String comment;
    private String date;

    public Review(String first_name, String last_name, String nb_stars, String img, String comment, String date) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.nb_stars = nb_stars;
        this.img = img;
        this.comment = comment;
        this.date = date;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getNb_stars() {
        return nb_stars;
    }

    public void setNb_stars(String nb_stars) {
        this.nb_stars = nb_stars;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
