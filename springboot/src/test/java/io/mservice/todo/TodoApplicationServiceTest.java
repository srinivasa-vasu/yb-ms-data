package io.mservice.todo;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.aot.DisabledInAotMode;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase(replace = NONE)
@Testcontainers
@ActiveProfiles("test")
@DisabledInAotMode
class TodoApplicationServiceTest extends AbstractTodoApplicationTest {

	@Autowired
	private ITodoService todoService;

	@MockBean
	private TodoRetryPolicy todoRetryPolicy;

	private Todo element;

	@BeforeEach
	void init() {
		element = Todo.builder().build();
	}

	@Test
	void shouldCreateOneRecord() {
		final var todo = todoService.save(element);
		assertThat(todoService.findById(todo.getId()).get()).isEqualTo(todo);
	}

	@Test
	void shouldUpdateOneRecord() {
		String task = "Using test-containers";
		final var todo = todoService.save(element.setTask(task));
		assertThat(todoService.findById(todo.getId()).get().getTask()).isEqualTo(task);
	}

	@Test
	void shouldDeleteOneRecord() throws SQLException {
		final var todo = todoService.save(element);
		todoService.deleteById(todo.getId());
		assertThat(todoService.findById(todo.getId()).isEmpty());
	}

}
