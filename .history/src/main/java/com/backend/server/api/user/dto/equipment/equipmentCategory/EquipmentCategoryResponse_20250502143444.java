import com.backend.server.model.entity.EquipmentCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EquipmentCategoryResponse {
    private Long id;
    private String name;
    private String englishCode;
    public EquipmentCategoryResponse(EquipmentCategory category) {
        this.id = category.getId();
        this.name = category.getName();
        this.englishCode = category.getEnglishCode();
    }
}