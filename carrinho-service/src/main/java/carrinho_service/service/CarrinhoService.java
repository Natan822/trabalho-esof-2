package carrinho_service.service;

import org.springframework.beans.factory.annotation.Autowired;

import carrinho_service.model.ItemCarrinho;
import carrinho_service.model.Carrinho;
import carrinho_service.repository.CarrinhoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CarrinhoService {

    private final Map<Long, ItemCarrinho> itens = new HashMap<>();
    @Autowired
    private CarrinhoRepository carrinhoRepository;

    public List<Carrinho> listarItens() {
        return carrinhoRepository.findAll();
        //return itens.values();
    }

    public void adicionarItem(ItemCarrinho item) {
        Long produtoId = item.getProdutoId();
        if (itens.containsKey(produtoId)) {
            ItemCarrinho existente = itens.get(produtoId);
            existente.setQuantidade(existente.getQuantidade() + item.getQuantidade());
        } else {
            itens.put(produtoId, item);
        }
    }

    public void removerItem(Long produtoId) {
        itens.remove(produtoId);
    }

    public void limparCarrinho() {
        itens.clear();
    }

    public BigDecimal calcularTotal() {
        return itens.values().stream()
                .map(i -> i.getPreco().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
