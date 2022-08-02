package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.Todo;
import com.soongkordan.dothejib.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    @Transactional
    public Long save(Todo todo) {
        todoRepository.save(todo);
        return todo.getId();
    }

    @Transactional
    public void completeTodo(Long todoId, LocalDateTime completedAt){
        Optional<Todo> todo = todoRepository.findById(todoId);
        todo.get().completeTodo(completedAt);
    }

    @Transactional
    public void inCompleteTodo(Long todoId){
        Optional<Todo> todo = todoRepository.findById(todoId);
        todo.get().inCompleteTodo();
    }

    public Optional<Todo> findOne(Long todoId){
        Optional<Todo> todo = todoRepository.findById(todoId);
        return todo;
    }

    public List<Todo> findByFamilyId(Long familyId){
        return todoRepository.findByFamilyId(familyId);
    }

    public List<Todo> findByPublisherId(Long familyId){
        return todoRepository.findByPublisherId(familyId);
    }

    public Long deleteOne(Long todoId){
        todoRepository.deleteById(todoId);
        return todoId;
    }

}
