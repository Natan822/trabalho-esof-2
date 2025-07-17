package carrinho_service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "item_carrinho")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemCarrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false)
    private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "carrinho_id", nullable = false)
    @JsonBackReference
    private Carrinho carrinho;

    public ItemCarrinho(Long produtoId, int quantidade, BigDecimal preco, Carrinho carrinho) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.preco = preco;
        this.carrinho = carrinho;
    }
}
