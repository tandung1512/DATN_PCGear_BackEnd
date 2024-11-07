package web.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    // You can define custom query methods here if needed
}
