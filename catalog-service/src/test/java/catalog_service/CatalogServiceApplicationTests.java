package catalog_service;

import catalog_service.model.Product;
import catalog_service.repository.ProductRepository;
import catalog_service.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceApplicationTests {

	@Mock
	private ProductRepository repository;

	@InjectMocks
	private ProductService service;

	@Test
	void findAll_whenCalled_returnsListOfProducts() {
		Product p1 = new Product(1L, "Product A", 10.00, 101L, 5L);
		Product p2 = new Product(2L, "Product B", 20.00, 102L, 10L);
		when(repository.findAll()).thenReturn(Arrays.asList(p1, p2));

		List<Product> result = service.findAll();

		assertThat(result).containsExactly(p1, p2);
		verify(repository, times(1)).findAll();
	}

	@Test
	void save_whenProductProvided_returnsSavedProduct() {
		Product input = new Product(null, "New Product", 15.50, 103L, 7L);
		Product saved = new Product(3L, "New Product", 15.50, 103L, 7L);
		when(repository.save(input)).thenReturn(saved);

		Product result = service.save(input);

		assertThat(result).isEqualTo(saved);
		verify(repository, times(1)).save(input);
	}

	@Test
	void findById_whenProductExists_returnsProductOptional() {
		Product p = new Product(5L, "Existing", 30.00, 104L, 12L);
		when(repository.findById(5L)).thenReturn(Optional.of(p));

		Optional<Product> result = service.findById(5L);

		assertThat(result).isPresent().contains(p);
		verify(repository, times(1)).findById(5L);
	}

	@Test
	void findById_whenProductNotExists_returnsEmptyOptional() {
		when(repository.findById(99L)).thenReturn(Optional.empty());

		Optional<Product> result = service.findById(99L);

		assertThat(result).isNotPresent();
		verify(repository, times(1)).findById(99L);
	}

	@Test
	void delete_whenIdProvided_deletesProduct() {
		doNothing().when(repository).deleteById(7L);

		service.delete(7L);

		verify(repository, times(1)).deleteById(7L);
	}
}