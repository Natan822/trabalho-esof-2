package carrinho_service.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrinho")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemCarrinho> itens = new ArrayList<>();

    public Carrinho(Long usuarioId, String status) {
        this.usuarioId = usuarioId;
        this.status = status;
    }

    public BigDecimal getTotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (ItemCarrinho item : itens) {
            BigDecimal subtotal = item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
            sum = sum.add(subtotal);
        }
        return sum;
    }
}
