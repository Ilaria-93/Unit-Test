package com.example.unit_test.controllers;

import com.example.unit_test.entities.User;
import com.example.unit_test.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //crea nuovo user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    //restituisce la lista di tutti gli users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //restituisce un singolo user - se id non è presente in db (usa existsById()), restituisce user vuoto
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).orElse(null);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //aggiorna type dell'user specifico - se id non è presente in db (usa existsById()), restituisce user vuoto
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).orElseThrow();
            user.setEmail(userDetails.getEmail());
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            return ResponseEntity.ok(userRepository.save(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //cancella l'user specifico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
