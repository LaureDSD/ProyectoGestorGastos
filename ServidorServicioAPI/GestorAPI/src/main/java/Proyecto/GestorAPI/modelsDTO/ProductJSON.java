package Proyecto.GestorAPI.modelsDTO;

import Proyecto.GestorAPI.models.Category;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ProductJSON {
    private String name;
    private String quantity;
    private String price;
    private List<Category> category;
}
