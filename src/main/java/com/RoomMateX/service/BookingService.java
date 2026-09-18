package com.RoomMateX.service;

import com.RoomMateX.dto.BookingResponse;
import com.RoomMateX.entity.BookingRequest;
import com.RoomMateX.entity.Room;
import com.RoomMateX.entity.User;
import com.RoomMateX.enums.BookingStatus;
import com.RoomMateX.repository.BookingRequestRepository;
import com.RoomMateX.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRequestRepository repo;
    private final RoomRepository roomRepo;
    private final PaymentService paymentService;

    @CacheEvict(value="myBookings", key="#requester.id")
    public BookingResponse request(Long roomId, User requester) {

        if(repo.existsByRoomIdAndRequesterId(roomId, requester.getId()))
            throw new RuntimeException("Already requested");

        Room room = roomRepo.findById(roomId).orElseThrow();

        BookingRequest br = BookingRequest.builder()
                .room(room)
                .requester(requester)
                .status(BookingStatus.PENDING)
                .build();

        return map(repo.save(br));
    }

    @Cacheable(value="myBookings", key="#user.id")
    public List<BookingResponse> myBookings(User user){
        return repo.findByRequester(user).stream().map(this::map).toList();
    }


    public List<BookingResponse> ownerBookings(User owner){
        return repo.findByRoomOwnerId(owner.getId()).stream().map(this::map).toList();
    }

    public BookingResponse approve(Long id){

        BookingRequest br = repo.findById(id).orElseThrow();
        br.setStatus(BookingStatus.APPROVED);
        br.getRoom().setAvailable(false);

        return map(repo.save(br));
    }

    public BookingResponse reject(Long id){

        BookingRequest br = repo.findById(id).orElseThrow();
        br.setStatus(BookingStatus.REJECTED);

        return map(repo.save(br));
    }

    private BookingResponse map(BookingRequest br){

        return BookingResponse.builder()
                .id(br.getId())
                .roomId(br.getRoom().getId())
                .roomTitle(br.getRoom().getTitle())
                .city(br.getRoom().getCity())
                .requesterName(br.getRequester().getName())
                .status(br.getStatus())
                .build();
    }
    public BookingResponse cancel(Long id, String paymentId) throws Exception {

        BookingRequest br = repo.findById(id).orElseThrow();

        if(br.getStatus() == BookingStatus.PAID){
            paymentService.refund(paymentId);
        }

        br.setStatus(BookingStatus.CANCELLED);
        br.getRoom().setAvailable(true);

        return map(repo.save(br));
    }


}
