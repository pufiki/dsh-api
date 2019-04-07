package ru.digitalsuperhero.dshapi.controllers;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.digitalsuperhero.dshapi.dao.DiscussionRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Discussion;

@RepositoryRestController
@RequestMapping(path = "/discussions")
@CrossOrigin(origins = "*")
public class DiscussionController {

    private DiscussionRepository discussionRepository;

    public DiscussionController(DiscussionRepository discussionRepository) {
        this.discussionRepository = discussionRepository;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Discussion> postWorkRequest(@RequestBody Discussion discussion) {
        Discussion foundDiscussion = discussionRepository.findById((discussion.getId())).get();
        if (foundDiscussion == null) {
            return new ResponseEntity<>(discussionRepository.save(discussion), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(foundDiscussion, HttpStatus.CONFLICT);
    }

    @GetMapping(path = "/{id}",produces = "application/json")
    public ResponseEntity<Discussion> getDiscussions(@PathVariable("id") Long id) {
        Discussion discussion = discussionRepository.findById(id).get();
        if(discussion != null){
            return  new ResponseEntity<Discussion>(discussion, HttpStatus.OK);
        }
        return new ResponseEntity<Discussion>(discussion, HttpStatus.NOT_FOUND);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Discussion> patchContractor(@PathVariable("id") Long id, @RequestBody Discussion discussion) {
        Discussion foundDiscussion = discussionRepository.findById(id).get();
        if (discussion.getId() != null) {
            foundDiscussion.setId(discussion.getId());
        }
        if (discussion.getMessages() != null) {
            foundDiscussion.setMessages(discussion.getMessages());
        }
        return new ResponseEntity<>(discussionRepository.save(foundDiscussion), HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable("id") Long id) {
        try {
            discussionRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
        }
    }
}
