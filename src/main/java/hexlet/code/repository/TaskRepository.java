package hexlet.code.repository;

import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.Predicate;
import hexlet.code.model.QTask;
import hexlet.code.model.Task;
import lombok.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Optional;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>,
                                            QuerydslPredicateExecutor<Task>,
                                            QuerydslBinderCustomizer<QTask> {
    Optional<Task> findFirstByOrderById();
    List<Task> findAllByOrderByIdAsc();
    @NonNull List<Task> findAll(@NonNull Predicate predicate, @NonNull Sort sort);
    @Override
    default void customize(QuerydslBindings bindings, QTask root) {
        bindings.bind(String.class).first(
            (StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.excluding(root.author);
    }

}
