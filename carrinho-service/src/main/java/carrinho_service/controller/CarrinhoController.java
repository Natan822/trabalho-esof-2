package carrinho_service.controller;

import carrinho_service.model.ItemCarrinho;
import carrinho_service.model.Carrinho;
import carrinho_service.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @GetMapping("/itens")
    public List<Carrinho> listarItens() {
        return carrinhoService.listarItens();
    }

    @PostMapping("/itens")
    public ResponseEntity<String> adicionarItem(@RequestBody ItemCarrinho item) {
        carrinhoService.adicionarItem(item);
        return ResponseEntity.ok("Item adicionado ao carrinho");
    }

    @DeleteMapping("/itens/{produtoId}")
    public ResponseEntity<String> removerItem(@PathVariable Long produtoId) {
        carrinhoService.removerItem(produtoId);
        return ResponseEntity.ok("Item removido do carrinho");
    }

    @DeleteMapping("/limpar")
    public ResponseEntity<String> limparCarrinho() {
        carrinhoService.limparCarrinho();
        return ResponseEntity.ok("Carrinho limpo");
    }

    @GetMapping("/total")
    public BigDecimal totalCarrinho() {
        return carrinhoService.calcularTotal();
    }
}
