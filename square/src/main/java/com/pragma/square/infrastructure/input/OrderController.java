package com.pragma.square.infrastructure.input;

import com.pragma.square.application.handler.IOrderHandler;
import com.pragma.square.application.request.ClientRequest;
import com.pragma.square.application.response.OrderResponseDto;
import com.pragma.square.application.utils.PagesDto;
import com.pragma.square.infrastructure.output.repository.IOrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/square/order")
public class OrderController {

    private final IOrderRepository orderRepository;
    private final IOrderHandler orderHandler;

    @PostMapping
    public ResponseEntity<OrderResponseDto>create(@RequestBody @Valid List<ClientRequest> clientRequestList){
        OrderResponseDto response = orderHandler.create(clientRequestList);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping()
    public ResponseEntity<PagesDto<Page<OrderResponseDto>>> page(
            @RequestParam int page, @RequestParam int size,@RequestParam String sort,
            @RequestParam String status,@RequestParam String property
    ){
        Page<OrderResponseDto> pagination = orderHandler.findByStatus(page,size,sort,status,property);
        return new ResponseEntity<>(new PagesDto<>( pagination), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> update(@PathVariable Long orderId,@RequestParam String status){
        OrderResponseDto orderResponseDto = orderHandler.updateStatus(orderId,status);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/delivered/{orderId}")
    public ResponseEntity<OrderResponseDto> updateToDelivered(@PathVariable Long orderId){
        OrderResponseDto orderResponseDto = orderHandler.updateToDelivered(orderId);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.OK);
    }
    @DeleteMapping()
    public ResponseEntity<String> delete(){
        orderHandler.deleteOrder();
        return new ResponseEntity<>("your order has been deleted", HttpStatus.OK);
    }



//    @GetMapping("/all")
//    public ResponseEntity< List<OrderEntity>> getOrders() {
//        List<OrderEntity> order = orderRepository.findAll();
//        return ResponseEntity.ok(order);
//    }


}
