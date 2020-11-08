package pl.polsl.meetandride.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.polsl.meetandride.DTOs.ChatEntryDTO;
import pl.polsl.meetandride.entities.ChatEntry;
import pl.polsl.meetandride.exceptions.ResourceNotFoundException;
import pl.polsl.meetandride.repositories.ChatEntryRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatEntryService {
    private final ChatEntryRepository chatEntryRepository;
    private final UserService userService;
    private final TripService tripService;

    public ChatEntryDTO add(ChatEntryDTO chatEntryDTO) {
        return toDTO(chatEntryRepository.save(toEntity(chatEntryDTO)));
    }

    public ChatEntryDTO edit(ChatEntryDTO chatEntryDTO) {
        ChatEntry chatEntry = findById(chatEntryDTO.getId());
        fillEntityWithDtoData(chatEntry, chatEntryDTO);
        return toDTO(chatEntryRepository.save(chatEntry));
    }

    public ChatEntryDTO getById(Long id) {
        return toDTO(findById(id));
    }

    public void delete(Long id) {
        chatEntryRepository.deleteById(id);
    }

    public ChatEntry findById(Long id) {
        Optional<ChatEntry> optionalUser = chatEntryRepository.findById(id);
        if (optionalUser.isPresent())
            return optionalUser.get();
        else
            throw new ResourceNotFoundException("ChatEntry with id: " + id + " not exists in DB!");
    }

    private ChatEntryDTO toDTO(ChatEntry chatEntry) {
        ChatEntryDTO chatEntryDTO = new ChatEntryDTO();
        chatEntryDTO.setId(chatEntry.getId());
        chatEntryDTO.setContent(chatEntry.getContent());
        chatEntryDTO.setAuthorId(chatEntry.getAuthor().getId());
        chatEntryDTO.setTripId(chatEntry.getTrip().getId());
        return chatEntryDTO;
    }

    private ChatEntry toEntity(ChatEntryDTO chatEntryDTO) {
        ChatEntry chatEntry = new ChatEntry();
        fillEntityWithDtoData(chatEntry, chatEntryDTO);
        return chatEntry;
    }

    private void fillEntityWithDtoData(ChatEntry chatEntry, ChatEntryDTO chatEntryDTO) {
        chatEntry.setDate(LocalDateTime.now());
        chatEntry.setContent(chatEntryDTO.getContent());
        chatEntry.setAuthor(userService.findById(chatEntryDTO.getAuthorId()));
        chatEntry.setTrip(tripService.findById(chatEntryDTO.getTripId()));
    }
}
