package carrinho_service.service;

import carrinho_service.dto.CarrinhoDTO;
import carrinho_service.dto.ItemCarrinhoDTO;
import carrinho_service.repository.ItemCarrinhoRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import carrinho_service.model.ItemCarrinho;
import carrinho_service.model.Carrinho;
import carrinho_service.repository.CarrinhoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CarrinhoService {

    @Autowired
    private CarrinhoRepository carrinhoRepository;
    @Autowired
    private ItemCarrinhoRepository itemCarrinhoRepository;

    public List<Carrinho> listarCarrinhos() {
        return carrinhoRepository.findAll();
    }

    public ResponseEntity<?> addCarrinho(CarrinhoDTO carrinhoDTO) {
        // TODO: checar se usuario de CarrinhoDTO.userId existe

        if (carrinhoDTO.userId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "ID de usuário não informado."));
        }

        if (carrinhoDTO.status() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Status do carrinho não informado."));
        }

        Carrinho carrinho = new Carrinho(carrinhoDTO.userId(), carrinhoDTO.status());
        addCarrinho(carrinho);

        return ResponseEntity.ok(
                Map.of("message", "Carrinho criado com sucesso.",
                        "carrinho", carrinho));
    }

    private boolean carrinhoExists(Long id) {
        return carrinhoRepository.existsById(id);
    }

    public Carrinho findCarrinhoById(Long id) {
        return carrinhoRepository.findById(id).orElse(null);
    }

    private Carrinho addCarrinho(Carrinho carrinho) {
        return carrinhoRepository.save(carrinho);
    }

    private ItemCarrinho addItem(ItemCarrinho item) {
        return itemCarrinhoRepository.save(item);
    }

    public ResponseEntity<?> adicionarItem(ItemCarrinhoDTO item) {
        // TODO: checar se produto existe antes de adicionar

        if (!carrinhoExists(item.carrinhoId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Carrinho não existe."));
        }

        if (item.produtoId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "ID do produto não informado."));
        }

        if (item.carrinhoId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "ID do carrinho não informado."));
        }

        if (item.preco() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Preço do produto não informado."));
        }

        Carrinho carrinho = findCarrinhoById(item.carrinhoId());
        ItemCarrinho addedItem =
                addItem(new ItemCarrinho(item.produtoId(), item.quantidade(), item.preco(), carrinho));

        return ResponseEntity.ok(
                Map.of("message", "Item adicionado com sucesso.",
                        "item", addedItem));
    }

    public ResponseEntity<?> removerItem(Long carrinhoId, Long produtoId) {
        if (!carrinhoExists(carrinhoId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Carrinho não existe."));
        }

        itemCarrinhoRepository.removeItem(carrinhoId, produtoId);
        return ResponseEntity.ok(Map.of("message", "Produto removido com sucesso."));
    }

    private void clearCarrinho(Long carrinhoId) {
        itemCarrinhoRepository.clear(carrinhoId);
    }

    public ResponseEntity<?> limparCarrinho(Long carrinhoId) {
        if (!carrinhoExists(carrinhoId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Carrinho não existe."));
        }

        clearCarrinho(carrinhoId);
        return ResponseEntity.ok(Map.of("message", "Carrinho foi esvaziado com sucesso."));
    }

    public ResponseEntity<?> calcularTotal(Long carrinhoId) {
        if (!carrinhoExists(carrinhoId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Carrinho não existe."));
        }

        Carrinho carrinho = findCarrinhoById(carrinhoId);
        BigDecimal total = carrinho.getTotal();
        return ResponseEntity.ok(
                Map.of("message", "Total calculado com sucesso.",
                        "total", total));
    }

    public List<ItemCarrinho> listarItens(Long carrinhoId) {
        if (!carrinhoExists(carrinhoId)) {
            return new ArrayList<>();
        }

        Carrinho carrinho = findCarrinhoById(carrinhoId);
        return carrinho.getItens();
    }
}
