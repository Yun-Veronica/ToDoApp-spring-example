package org.example.todo.repository;
import org.example.todo.model.Tasks;
import org.example.todo.model.Users;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long> {
    List<Tasks> findByUser(Users user);


}
