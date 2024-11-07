package web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import web.model.Distinctive;
import web.service.DistinctiveService;

import java.util.List;

@RestController
@RequestMapping("/api/distinctives")
public class DistinctiveController {

    @Autowired
    private DistinctiveService distinctiveService;

    @Operation(summary = "Get all distinctives")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved distinctives"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping
    public List<Distinctive> getAllDistinctives() {
        return distinctiveService.getAllDistinctives();
    }

    @Operation(summary = "Get distinctive by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved distinctive"),
        @ApiResponse(responseCode = "404", description = "Distinctive not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/{id}")
    public Distinctive getDistinctiveById(@PathVariable String id) {
        return distinctiveService.getDistinctiveById(id);
    }

    @Operation(summary = "Create a new distinctive")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Distinctive successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Distinctive createDistinctive(@RequestBody Distinctive distinctive) {
        return distinctiveService.saveDistinctive(distinctive);
    }

    @Operation(summary = "Update an existing distinctive")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Distinctive successfully updated"),
        @ApiResponse(responseCode = "404", description = "Distinctive not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PutMapping("/{id}")
    public Distinctive updateDistinctive(@PathVariable String id, @RequestBody Distinctive distinctive) {
        Distinctive existingDistinctive = distinctiveService.getDistinctiveById(id);
        if (existingDistinctive != null) {
            distinctive.setId(id);
            return distinctiveService.saveDistinctive(distinctive);
        } else {
            return null; // hoặc throw một ngoại lệ tùy vào cách bạn muốn xử lý
        }
    }

    @Operation(summary = "Delete a distinctive by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Distinctive successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Distinctive not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @DeleteMapping("/{id}")
    public void deleteDistinctive(@PathVariable String id) {
        distinctiveService.deleteDistinctive(id);
    }
}
