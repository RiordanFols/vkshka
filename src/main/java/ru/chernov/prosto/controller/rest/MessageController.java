package ru.chernov.prosto.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.chernov.prosto.service.MessageService;

import java.io.IOException;

/**
 * @author Pavel Chernov
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable(name = "id") long messageId) throws IOException {
        messageService.delete(messageId);
    }
}
