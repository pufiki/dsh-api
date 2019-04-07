package ru.digitalsuperhero.dshapi.controllers;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.digitalsuperhero.dshapi.dao.MessageRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Message;

@RepositoryRestController
@RequestMapping(path = "/messages")
@CrossOrigin(origins = "*")
public class MessagesController {
    private MessageRepository messageRepository;

    public MessagesController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Message> postWorkRequest(@RequestBody Message message) {
        Message foundMessage = messageRepository.findById((message.getId())).get();
        if (foundMessage == null) {
            return new ResponseEntity<>(messageRepository.save(message), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(foundMessage, HttpStatus.CONFLICT);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Message> getMessages(@PathVariable("id") Long id) {
        Message foundMessage = messageRepository.findById(id).get();
        if (foundMessage != null) {
            return new ResponseEntity<Message>(foundMessage, HttpStatus.OK);
        }
        return new ResponseEntity<Message>(foundMessage, HttpStatus.NOT_FOUND);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Message> patchContractor(@PathVariable("id") Long id, @RequestBody Message message) {
        Message foundMessage = messageRepository.findById(id).get();
        if (message.getId() != null) {
            foundMessage.setId(message.getId());
        }
        if (message.getSentAt() != null) {
            message.setSentAt(foundMessage.getSentAt());
        }
        if (message.getUserId() != null) {
            message.setUserId(foundMessage.getUserId());
        }
        return new ResponseEntity<>(messageRepository.save(foundMessage), HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable("id") Long id) {
        try {
            messageRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
        }
    }
}
