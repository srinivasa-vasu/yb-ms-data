package io.mservice.todo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.mservice.todo.HintStatementInspector.PlannerHint.NEST_OFF;

@Service
public class TodoService implements ITodoService {

	private final ITodoRepository todoRepository;
	private final EntityManager entityManager;

	TodoService(ITodoRepository todoRepository, EntityManager entityManager) {
		this.todoRepository = todoRepository;
		this.entityManager = entityManager;
	}

	@Transactional(readOnly = true)
	public List<Todo> findAllBySort(Sort sortOrder) {
		List<Todo> todoList = Collections.emptyList();
		Iterable<Todo> listIterable = todoRepository.findAll(sortOrder);
		if (listIterable.iterator().hasNext()) {
			todoList = new ArrayList<>();
			listIterable.forEach(todoList::add);
		}
		return todoList;
	}

	@Transactional(readOnly = true)
	public Page<Todo> findByLimit(int limit) {
		return todoRepository.findAll(PageRequest.ofSize(limit));
	}

	@Transactional(readOnly = true)
	public Optional<Todo> findById(UUID id) {
		return todoRepository.findById(id);
	}

	@Transactional
	public Todo save(Todo resource) {
		return todoRepository.save(resource);
	}

	@Transactional
	public void deleteById(UUID id) {
		todoRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Todo> getTodosByStatus(boolean status) {
		try {
			QueryHintContext.setPlannerHint(NEST_OFF);
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Todo> cq = cb.createQuery(Todo.class);
			Root<Todo> todo = cq.from(Todo.class);
			cq.select(todo).where(cb.equal(todo.get("status"), status));
			TypedQuery<Todo> query = entityManager.createQuery(cq);
//			query.setHint("org.hibernate.comment", "criteria:" + NEST_OFF);
			return query.getResultList();
		}
		finally {
			QueryHintContext.clear();
		}
	}

}
