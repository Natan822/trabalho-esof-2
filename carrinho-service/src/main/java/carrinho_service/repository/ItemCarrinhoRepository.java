package carrinho_service.repository;

import carrinho_service.model.ItemCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {

    @Modifying
    @Query(value = "DELETE FROM item_carrinho WHERE carrinho_id = :carrinhoId", nativeQuery = true)
    void clear(@Param("carrinhoId") Long carrinhoId);

    @Modifying
    @Query(value = "DELETE FROM item_carrinho WHERE carrinho_id = :carrinhoId AND produto_id = :productId",
            nativeQuery = true)
    void removeItem(@Param("carrinhoId") Long carrinhoId, @Param("productId") Long productId);



}