package carrinho_service.dto;

import java.math.BigDecimal;

public record ItemCarrinhoDTO(
        Long produtoId,
        int quantidade,
        BigDecimal preco,
        Long carrinhoId
) {}
