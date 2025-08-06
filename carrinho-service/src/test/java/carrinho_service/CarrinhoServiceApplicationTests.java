package carrinho_service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import carrinho_service.dto.CarrinhoDTO;
import carrinho_service.dto.ItemCarrinhoDTO;
import carrinho_service.model.Carrinho;
import carrinho_service.model.ItemCarrinho;
import carrinho_service.repository.CarrinhoRepository;
import carrinho_service.repository.ItemCarrinhoRepository;
import carrinho_service.service.CarrinhoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CarrinhoServiceApplicationTests {

	@Mock
	private CarrinhoRepository carrinhoRepository;

	@Mock
	private ItemCarrinhoRepository itemCarrinhoRepository;

	@InjectMocks
	private CarrinhoService carrinhoService;

	private Carrinho sampleCarrinho;
	private ItemCarrinho sampleItem;

	@BeforeEach
	void setUp() {
		sampleCarrinho = new Carrinho(5L, "OPEN");
		sampleCarrinho.setId(5L);
		sampleCarrinho.setItens(new ArrayList<>());
		sampleItem = new ItemCarrinho(10L, 2, BigDecimal.valueOf(15.5), sampleCarrinho);
		sampleItem.setId(100L);
	}

	@Test
	void listarCarrinhos_returnsAll() {
		List<Carrinho> list = List.of(sampleCarrinho);
		when(carrinhoRepository.findAll()).thenReturn(list);

		List<Carrinho> result = carrinhoService.listarCarrinhos();
		assertEquals(1, result.size());
		assertSame(sampleCarrinho, result.get(0));
	}

	@Test
	void addCarrinho_whenUserIdNull_returnsNotFound() {
		CarrinhoDTO dto = new CarrinhoDTO(null, "NEW");
		ResponseEntity<?> resp = carrinhoService.addCarrinho(dto);
		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
		assertEquals("ID de usuário não informado.", ((Map<?, ?>) resp.getBody()).get("error"));
	}

	@Test
	void addCarrinho_whenStatusNull_returnsNotFound() {
		CarrinhoDTO dto = new CarrinhoDTO(3L, null);
		ResponseEntity<?> resp = carrinhoService.addCarrinho(dto);
		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
		assertEquals("Status do carrinho não informado.", ((Map<?, ?>) resp.getBody()).get("error"));
	}

	@Test
	void addCarrinho_whenValid_savesAndReturns() {
		CarrinhoDTO dto = new CarrinhoDTO(7L, "ACTIVE");
		when(carrinhoRepository.save(any())).thenAnswer(inv -> {
			Carrinho c = inv.getArgument(0);
			c.setId(7L);
			return c;
		});

		ResponseEntity<?> resp = carrinhoService.addCarrinho(dto);
		assertEquals(HttpStatus.OK, resp.getStatusCode());
		@SuppressWarnings("unchecked")
		Map<String, Object> body = (Map<String, Object>) resp.getBody();
		assertEquals("Carrinho criado com sucesso.", body.get("message"));
		Carrinho created = (Carrinho) body.get("carrinho");
		assertEquals(7L, created.getId());
		assertEquals("ACTIVE", created.getStatus());
	}

	@Test
	void findCarrinhoById_whenExists_returnsCarrinho() {
		when(carrinhoRepository.findById(5L)).thenReturn(Optional.of(sampleCarrinho));
		Carrinho c = carrinhoService.findCarrinhoById(5L);
		assertSame(sampleCarrinho, c);
	}

	@Test
	void findCarrinhoById_whenNotExists_returnsNull() {
		when(carrinhoRepository.findById(6L)).thenReturn(Optional.empty());
		Carrinho c = carrinhoService.findCarrinhoById(6L);
		assertNull(c);
	}

	@Test
	void adicionarItem_whenCarrinhoNotExists_returnsNotFound() {
		ItemCarrinhoDTO dto = new ItemCarrinhoDTO(10L, 2, BigDecimal.ONE, 99L);
		when(carrinhoRepository.existsById(99L)).thenReturn(false);
		ResponseEntity<?> resp = carrinhoService.adicionarItem(dto);
		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
		assertEquals("Carrinho não existe.", ((Map<?, ?>) resp.getBody()).get("error"));
	}

	@Test
	void adicionarItem_whenProdutoIdNull_returnsNotFound() {
		ItemCarrinhoDTO dto = new ItemCarrinhoDTO(null, 1, BigDecimal.ONE, 5L);
		when(carrinhoRepository.existsById(5L)).thenReturn(true);
		ResponseEntity<?> resp = carrinhoService.adicionarItem(dto);
		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
		assertEquals("ID do produto não informado.", ((Map<?, ?>) resp.getBody()).get("error"));
	}

	@Test
	void adicionarItem_whenCarrinhoIdNull_returnsNotFound() {
		ItemCarrinhoDTO dto = new ItemCarrinhoDTO(10L, 1, BigDecimal.ONE, null);
		ResponseEntity<?> resp = carrinhoService.adicionarItem(dto);
		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
		assertEquals("Carrinho não existe.", ((Map<?, ?>) resp.getBody()).get("error"));
	}

	@Test
	void adicionarItem_whenPrecoNull_returnsNotFound() {
		ItemCarrinhoDTO dto = new ItemCarrinhoDTO(10L, 1, null, 5L);
		when(carrinhoRepository.existsById(5L)).thenReturn(true);
		ResponseEntity<?> resp = carrinhoService.adicionarItem(dto);
		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
		assertEquals("Preço do produto não informado.", ((Map<?, ?>) resp.getBody()).get("error"));
	}

	@Test
	void adicionarItem_whenValid_savesAndReturns() {
		ItemCarrinhoDTO dto = new ItemCarrinhoDTO(10L, 2, BigDecimal.valueOf(20), 5L);
		when(carrinhoRepository.existsById(5L)).thenReturn(true);
		when(carrinhoRepository.findById(5L)).thenReturn(Optional.of(sampleCarrinho));
		when(itemCarrinhoRepository.save(any())).thenAnswer(inv -> {
			ItemCarrinho it = inv.getArgument(0);
			it.setId(101L);
			return it;
		});

		ResponseEntity<?> resp = carrinhoService.adicionarItem(dto);
		assertEquals(HttpStatus.OK, resp.getStatusCode());
		@SuppressWarnings("unchecked")
		Map<String, Object> body = (Map<String, Object>) resp.getBody();
		assertEquals("Item adicionado com sucesso.", body.get("message"));
		ItemCarrinho it = (ItemCarrinho) body.get("item");
		assertEquals(10L, it.getProdutoId());
		assertEquals(2, it.getQuantidade());
	}

	@Test
	void removerItem_whenCarrinhoNotExists_returnsNotFound() {
		when(carrinhoRepository.existsById(8L)).thenReturn(false);
		ResponseEntity<?> resp = carrinhoService.removerItem(8L, 15L);
		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
		assertEquals("Carrinho não existe.", ((Map<?, ?>) resp.getBody()).get("error"));
	}

	@Test
	void removerItem_whenExists_removesAndReturns() {
		when(carrinhoRepository.existsById(5L)).thenReturn(true);
		ResponseEntity<?> resp = carrinhoService.removerItem(5L, 10L);
		assertEquals(HttpStatus.OK, resp.getStatusCode());
		assertEquals("Produto removido com sucesso.", ((Map<?, ?>) resp.getBody()).get("message"));
		verify(itemCarrinhoRepository).removeItem(5L, 10L);
	}

	@Test
	void limparCarrinho_whenNotExists_returnsNotFound() {
		when(carrinhoRepository.existsById(3L)).thenReturn(false);
		ResponseEntity<?> resp = carrinhoService.limparCarrinho(3L);
		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
		assertEquals("Carrinho não existe.", ((Map<?, ?>) resp.getBody()).get("error"));
	}

	@Test
	void limparCarrinho_whenExists_clearsAndReturns() {
		when(carrinhoRepository.existsById(5L)).thenReturn(true);
		ResponseEntity<?> resp = carrinhoService.limparCarrinho(5L);
		assertEquals(HttpStatus.OK, resp.getStatusCode());
		assertEquals("Carrinho foi esvaziado com sucesso.", ((Map<?, ?>) resp.getBody()).get("message"));
		verify(itemCarrinhoRepository).clear(5L);
	}

	@Test
	void calcularTotal_whenNotExists_returnsNotFound() {
		when(carrinhoRepository.existsById(4L)).thenReturn(false);
		ResponseEntity<?> resp = carrinhoService.calcularTotal(4L);
		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
		assertEquals("Carrinho não existe.", ((Map<?, ?>) resp.getBody()).get("error"));
	}

	@Test
	void listarItens_whenNotExists_returnsEmptyList() {
		when(carrinhoRepository.existsById(2L)).thenReturn(false);
		List<?> items = carrinhoService.listarItens(2L);
		assertTrue(items.isEmpty());
	}

	@Test
	void listarItens_whenExists_returnsItems() {
		sampleCarrinho.setItens(List.of(sampleItem));
		when(carrinhoRepository.existsById(5L)).thenReturn(true);
		when(carrinhoRepository.findById(5L)).thenReturn(Optional.of(sampleCarrinho));
		List<ItemCarrinho> items = carrinhoService.listarItens(5L);
		assertEquals(1, items.size());
		assertSame(sampleItem, items.get(0));
	}
}