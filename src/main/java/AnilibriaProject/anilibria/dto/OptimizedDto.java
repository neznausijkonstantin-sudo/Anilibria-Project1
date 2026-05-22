package AnilibriaProject.anilibria.dto;
/*
 * Вложенный DTO для оптимизированных картинок из внешнего API.
 * Нужен Jackson, чтобы корректно разобрать вложенный JSON-объект.
 */

public class OptimizedDto {
    private String preview;
    private String thumbnail;

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
