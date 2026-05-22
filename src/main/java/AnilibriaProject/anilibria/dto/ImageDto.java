package AnilibriaProject.anilibria.dto;

/*
 * Часть JSON-структуры от внешнего API.
 * Описывает картинки франшизы: preview, thumbnail и optimized-версии.
 */
public class ImageDto {

    private String preview;
    private String thumbnail;
    private OptimizedDto optimized;

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

    public OptimizedDto getOptimized() {
        return optimized;
    }

    public void setOptimized(OptimizedDto optimized) {
        this.optimized = optimized;
    }
}
