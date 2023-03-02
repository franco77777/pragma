package com.pragma.square.infrastructure.input;

import com.pragma.square.application.handler.IPlateHandler;
import com.pragma.square.application.request.PlateRequestDto;
import com.pragma.square.application.request.PlateUpdateRequestDto;
import com.pragma.square.application.response.PlateResponseDto;
import com.pragma.square.application.response.PlatesPageResponseDto;
import com.pragma.square.application.response.RestaurantPageDto;
import com.pragma.square.application.utils.PlatesPagesDto;
import com.pragma.square.application.utils.RestaurantsPagesDto;
import com.pragma.square.infrastructure.output.entity.CategoryEntity;
import com.pragma.square.infrastructure.output.entity.PlateEntity;
import com.pragma.square.infrastructure.output.entity.RestaurantEntity;
import com.pragma.square.infrastructure.output.repository.ICategoryRepository;
import com.pragma.square.infrastructure.output.repository.IPlateRepository;
import com.pragma.square.infrastructure.output.repository.IRestaurantRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/square/plate")
@RequiredArgsConstructor

public class PlateController {
    private final IPlateRepository plateRepository;
    private final IRestaurantRepository restaurantRepository;
    private final IPlateHandler plateHandler;
    private  final ICategoryRepository categoryRepository;


    @GetMapping()
    public List<PlateEntity> existe (){
        List<PlateEntity> result = plateRepository.findAll();
        return result;
    }



    @GetMapping("/pagination/{categoryId}/{restaurantId}")
    public ResponseEntity< PlatesPagesDto<Page<PlateResponseDto>>> test(@PathVariable("categoryId") Long categoryId, @PathVariable("restaurantId") Long restaurantId,@RequestParam int page, @RequestParam int size,@RequestParam String sort,@RequestParam String property) {
        //Page<PlateEntity> result = plateRepository.findAllByIdCategory_IdAndIdRestaurant_Id( categoryId,restaurantId, PageRequest.of(page, size).withSort(Sort.by(Sort.Direction.ASC,property))).orElseThrow();
        Page<PlateResponseDto> response = plateHandler.getPlateResponseDtoByPage(categoryId,restaurantId,page,size,property,sort);
        return ResponseEntity.ok(new PlatesPagesDto<>(response.getSize(), response));
    }




    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/create/{categoryId}/{restaurantId}")
    public ResponseEntity<PlateResponseDto> create(@Valid @RequestBody  PlateRequestDto plate, @PathVariable("restaurantId") Long idRestaurant,@PathVariable("categoryId") Long categoryId){
        PlateResponseDto response = plateHandler.savePlate(plate,idRestaurant,categoryId);
        return  ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/update/{id}")
    public ResponseEntity<PlateResponseDto> update(@Valid @RequestBody PlateUpdateRequestDto update, @PathVariable("id") Long plateId){
        PlateResponseDto response = plateHandler.updatePlate(update,plateId);
        return  ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/deactivate/{id}")
    public ResponseEntity<PlateResponseDto> deactivatePlate(@PathVariable("id") Long plateId){
        PlateResponseDto response = plateHandler.deactivatePlate(plateId);
        return ResponseEntity.ok(response);
    }

}
