package ru.chernov.prosto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.chernov.prosto.component.FileHandler;
import ru.chernov.prosto.domain.entity.Message;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.formatter.LastMessageInfoFormatter;
import ru.chernov.prosto.repository.MessageRepository;
import ru.chernov.prosto.utils.ImageUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Pavel Chernov
 */
@Service
public class MessageService {

    private final UserService userService;
    private final MessageRepository messageRepository;
    private final FileHandler fileHandler;
    private final LastMessageInfoFormatter lastMessageInfoFormatter;

    @Autowired
    public MessageService(UserService userService, MessageRepository messageRepository,
                          FileHandler fileHandler, LastMessageInfoFormatter lastMessageInfoFormatter) {
        this.userService = userService;
        this.messageRepository = messageRepository;
        this.fileHandler = fileHandler;
        this.lastMessageInfoFormatter = lastMessageInfoFormatter;
    }

    public Message findById(long id) {
        return messageRepository.findById(id).orElse(null);
    }

    public Set<User> getAllContacts(long userId) {
        Set<Long> contactsIds = messageRepository.findContactsIds(userId);
        contactsIds.remove(userId);

        Set<User> contacts = new TreeSet<>(
                Comparator.comparing(o -> getLastMessageInCorrespondence(o.getId(), userId).getCreationDateTime(),
                        Comparator.reverseOrder()));

        for (long contactId : contactsIds) {
            contacts.add(userService.findById(contactId));
        }

        return contacts;
    }

    public Set<Message> getCorrespondence(long user1Id, long user2Id) {
        Set<Message> messages = new TreeSet<>(Comparator.comparing(Message::getCreationDateTime).reversed());
        messages.addAll(messageRepository.findAllByAuthorIdAndTargetId(user1Id, user2Id));
        messages.addAll(messageRepository.findAllByAuthorIdAndTargetId(user2Id, user1Id));

        return messages;
    }

    public Message create(long authorId, long targetId, String text, MultipartFile[] images) throws IOException {
        User author = userService.findById(authorId);
        User target = userService.findById(targetId);

        Message message = new Message();
        message.setCreationDateTime(LocalDateTime.now());
        message.setAuthor(author);
        message.setTarget(target);
        message.setText(text);

        for(var image: images) {
            // если тип файла соответсвует изображению и кол-во файлов в сообщении меньше допустимого максимума
            if (ImageUtils.isImageTypeAllowed(image) && message.getImgFilenames().size() < Message.MAX_IMAGES) {
                // сохраняем изображение
                String filename = fileHandler.saveImage(image);
                message.getImgFilenames().add(filename);
            }
        }

        return messageRepository.save(message);
    }

    public void delete(long messageId) throws IOException {
        Message message = findById(messageId);
        for (var filename: message.getImgFilenames()) {
            fileHandler.deleteImage(filename);
        }

        messageRepository.deleteById(messageId);
    }

    public Message getLastMessageInCorrespondence(long userId, long targetId) {
        return new ArrayList<>(getCorrespondence(userId, targetId)).get(0);
    }

    public Map<String, String> getLastMessageInfo(long userId, long targetId) {

        Message lastMessage = getLastMessageInCorrespondence(userId, targetId);

        Map<String, String> map = new HashMap<>();
        map.put("author", lastMessageInfoFormatter.formatAuthor(lastMessage, userId));
        map.put("text", lastMessageInfoFormatter.formatLastMessageInfo(lastMessage));

        return map;
    }
}
