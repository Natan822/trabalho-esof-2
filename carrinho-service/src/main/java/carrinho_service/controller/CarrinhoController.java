package carrinho_service.controller;

import carrinho_service.dto.CarrinhoDTO;
import carrinho_service.dto.ItemCarrinhoDTO;
import carrinho_service.model.Carrinho;
import carrinho_service.model.ItemCarrinho;
import carrinho_service.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @GetMapping()
    public List<Carrinho> listarCarrinhos() {
        return carrinhoService.listarCarrinhos();
    }

    @GetMapping("/{carrinhoId}/itens")
    public List<ItemCarrinho> listarItens(@PathVariable Long carrinhoId) {
        return carrinhoService.listarItens(carrinhoId);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCarrinho(CarrinhoDTO carrinhoDTO) {
        return carrinhoService.addCarrinho(carrinhoDTO);
    }

    @PostMapping("/itens/add")
    public ResponseEntity<?> adicionarItem(@ModelAttribute ItemCarrinhoDTO item) {
        return carrinhoService.adicionarItem(item);
    }

    @DeleteMapping("/{carrinhoId}/{produtoId}")
    public ResponseEntity<?> removerItem(@PathVariable Long carrinhoId,
                                         @PathVariable Long produtoId) {
        return carrinhoService.removerItem(carrinhoId, produtoId);
    }

    @DeleteMapping("{carrinhoId}/limpar")
    public ResponseEntity<?> limparCarrinho(@PathVariable Long carrinhoId) {
        return carrinhoService.limparCarrinho(carrinhoId);
    }

    @GetMapping("{carrinhoId}/total")
    public ResponseEntity<?> totalCarrinho(@PathVariable Long carrinhoId) {
        return carrinhoService.calcularTotal(carrinhoId);
    }
}
