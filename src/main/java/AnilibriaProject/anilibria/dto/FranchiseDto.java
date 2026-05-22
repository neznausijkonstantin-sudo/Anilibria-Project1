package AnilibriaProject.anilibria.dto;

/*
 * DTO для одной франшизы из внешнего Anilibria/Aniliberty API.
 *
 * Jackson заполняет этот объект из JSON по именам полей и setter-методам.
 * Поэтому названия вроде first_year оставлены такими же, как приходят из API.
 */
public class FranchiseDto {

    private String id;
    private String name;
    private ImageDto image;
    private String thumbnail;
    private String rating;
    private String last_year;
    private String first_year;
    private String name_english;
    private String total_episodes;
    private String total_releases;
    private String total_duration;
    private String total_duration_in_seconds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLast_year() {
        return last_year;
    }

    public void setLast_year(String last_year) {
        this.last_year = last_year;
    }

    public String getFirst_year() {
        return first_year;
    }

    public void setFirst_year(String first_year) {
        this.first_year = first_year;
    }

    public String getName_english() {
        return name_english;
    }

    public void setName_english(String name_english) {
        this.name_english = name_english;
    }

    public String getTotal_episodes() {
        return total_episodes;
    }

    public void setTotal_episodes(String total_episodes) {
        this.total_episodes = total_episodes;
    }

    public String getTotal_releases() {
        return total_releases;
    }

    public void setTotal_releases(String total_releases) {
        this.total_releases = total_releases;
    }

    public String getTotal_duration() {
        return total_duration;
    }

    public void setTotal_duration(String total_duration) {
        this.total_duration = total_duration;
    }

    public String getTotal_duration_in_seconds() {
        return total_duration_in_seconds;
    }

    public void setTotal_duration_in_seconds(String total_duration_in_seconds) {
        this.total_duration_in_seconds = total_duration_in_seconds;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "franchiseDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", rating='" + rating + '\'' +
                ", last_year='" + last_year + '\'' +
                ", first_year='" + first_year + '\'' +
                ", name_english='" + name_english + '\'' +
                ", total_episodes='" + total_episodes + '\'' +
                ", total_releases='" + total_releases + '\'' +
                ", total_duration='" + total_duration + '\'' +
                ", total_duration_in_seconds='" + total_duration_in_seconds + '\'' +
                '}';
    }
}
